package Utils;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TooManyListenersException;

public class CL200A {
    // Reserved code
    static final int STX = 0x02;
    static final int ETX = 0x03;
    static final ArrayList<Integer> DELIMITER = new ArrayList<>(Arrays.asList(0x0d, 0x0a));
    // COM port name
    static final String COM = "COM5";

    // COM port
    private static CommPortIdentifier portId;
    private static SerialPort port;

    private static BufferedReader reader;

    // Received Data
    private static ArrayList<Integer> receivedData = new ArrayList<>();


    // Setting up com port
    public static void openPort() {
        // config serial and open port
        try {
            portId = CommPortIdentifier.getPortIdentifier(COM);
            port = (SerialPort) portId.open("serial", 2000);
            port.setSerialPortParams(
                    9600,
                    SerialPort.DATABITS_7,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_EVEN);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // add listener
        try {
            reader = new BufferedReader(
                    new InputStreamReader(port.getInputStream()));
            port.addEventListener(new MySerialPortEventListener());
            port.notifyOnDataAvailable(true);
        } catch (TooManyListenersException e) {
            System.out.println("Error: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Close com port
    public static void closePort() {
        try {
            port.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean setPcMode() {
        ArrayList<Integer> data = new ArrayList<>();

        data.add(STX);
        data.addAll(stringToData("00541   "));
        data.add(ETX);
        data.addAll(getBcc(data));
        data.addAll(DELIMITER);

        System.out.print("PUT: ");
        sendData(data);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) { e.printStackTrace(); }



        if (getCommand(receivedData).equals("54")) return true;
        else return false;
    }

    public static boolean setHoldMode() {
        ArrayList<Integer> data = new ArrayList<>();

        data.add(STX);
        data.addAll(stringToData("99551  0"));
        data.add(ETX);
        data.addAll(getBcc(data));
        data.addAll(DELIMITER);

        System.out.print("PUT: ");
        sendData(data);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) { e.printStackTrace(); }

        return true;
    }

    public static boolean setExtMode() {
        ArrayList<Integer> data = new ArrayList<>();

        data.add(STX);
        data.addAll(stringToData("004010  "));
        data.add(ETX);
        data.addAll(getBcc(data));
        data.addAll(DELIMITER);

        System.out.print("PUT: ");
        sendData(data);

        try {
            Thread.sleep(175);
        } catch (InterruptedException e) { e.printStackTrace(); }

        if (getCommand(receivedData).equals("40")) return true;
        else return false;
    }

    public static boolean startMeasurement() {
        boolean flag = false;
        while (!flag) {
            fire();
            getCctData("00");

            char checkData;
            int range = 0;

            // check fixed bit
            checkData = (char)(0x00 + receivedData.get(5));
            if (checkData == '1' || checkData == '5') {
                flag = true;
            }

            // check error bit
            checkData = (char)(0x00 + receivedData.get(6));
            if (checkData != ' ') {
                flag = false;
            }

            // check range bit
            checkData = (char)(0x00 + receivedData.get(7));
            range = Integer.valueOf(checkData);
            switch (range) {
                case 0:
                    flag = false;
                    break;
                case 6:
                    flag = false;
                    break;
            }
        }

        return true;
    }

    private static boolean fire() {
        ArrayList<Integer> data = new ArrayList<>();

        data.add(STX);
        data.addAll(stringToData("994021  "));
        data.add(ETX);
        data.addAll(getBcc(data));
        data.addAll(DELIMITER);

        System.out.print("PUT: ");
        sendData(data);

        try {
            Thread.sleep(600);
        } catch (InterruptedException e) { e.printStackTrace(); }

        try {
            port.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private static boolean getCctData(String section) {
        ArrayList<Integer> data = new ArrayList<>();

        data.add(STX);
        data.addAll(stringToData(section));
        data.addAll(stringToData("081200"));
        data.add(ETX);
        data.addAll(getBcc(data));
        data.addAll(DELIMITER);

        System.out.print("PUT: ");
        sendData(data);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) { e.printStackTrace(); }

        if (getCommand(receivedData).equals("08")) return true;
        else return false;
    }

    public static boolean setCommandByString(String section, String cmd, String status) {
        ArrayList<Integer> data = new ArrayList<>();

        data.add(STX);
        data.addAll(stringToData(section));
        data.addAll(stringToData(cmd));
        data.addAll(stringToData(status));
        data.add(ETX);
        data.addAll(getBcc(data));
        data.addAll(DELIMITER);

        //System.out.print("PUT: ");
        sendData(data);

        return true;
    }

    public static double getCct() {
        return getData(getCctData(receivedData));
    }

    public static double getIlluminance() {
        return getData(getIlluminanceData(receivedData));
    }

    // Send byte data into RS485 Serial COM port
    private static String sendData(ArrayList<Integer> data) {
        // Port variable
        OutputStream out;

        // Data sent from CL-200
        String result = "";

        // for debug
        // dump(data);

        // send data into RS485
        try{
            out = port.getOutputStream();
            out.flush();
            for(int i:data) {
                out.write(i);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return result;
    }

    // For debug, show all data bit into Hex.
    private static void dump(ArrayList<Integer> data) {
        for (int i : data) {
            String hex = String.format("%02x", i);
            System.out.print(hex + ", ");
        }
        System.out.println();
    }

    private static ArrayList<Integer> stringToData(String str) {
        byte[] bytes = str.getBytes();
        ArrayList<Integer> data = new ArrayList<>();
        for(byte b: bytes) {
            data.add((int) b);
        }
        return data;
    }

    private static ArrayList<Integer> getBcc(ArrayList<Integer> data) {
        int bcc = 0x00;

        for(int i: data) bcc ^= i;
        bcc ^= data.get(0);

        String hex = String.format("%02x", bcc);

        return stringToData(hex);
    }

    private static String getCommand(ArrayList<Integer> data) {
        String sdata = "";

        sdata += (char)(0x00 + data.get(3));
        sdata += (char)(0x00 + data.get(4));

        return sdata;
    }

    private static String getStatus(ArrayList<Integer> data) {
        String sdata = "";

        sdata += (char)(0x00 + data.get(5));
        sdata += (char)(0x00 + data.get(6));
        sdata += (char)(0x00 + data.get(7));
        sdata += (char)(0x00 + data.get(8));

        System.out.println(sdata);

        return sdata;
    }

    private static ArrayList<Integer> getIlluminanceData(ArrayList<Integer> extData) {
        ArrayList<Integer> iData = new ArrayList<>();

        for (int i=9; i<=14; i++) iData.add(extData.get(i));

        return iData;
    }

    private static ArrayList<Integer> getCctData(ArrayList<Integer> extData) {
        ArrayList<Integer> cData = new ArrayList<>();

        for (int i=15; i<=20; i++) cData.add(extData.get(i));

        return cData;
    }

    private static void dumpStatus(ArrayList<Integer> data) {
        ArrayList<Integer> cData = new ArrayList<>();
        String status = "Status: ";

        for (int i=5; i<=8; i++) {
            char tmp = (char)(0x00 + data.get(i));
            status += tmp + ", ";
        }

        System.out.println(status);
    }

    public static double getData(ArrayList<Integer> singleData) {
        double data;

        // read digit
        String tmp = "";
        for (int i=1; i<=4; i++) {
            tmp += (char)(0x00 + singleData.get(i));
        }
        data = Double.parseDouble(tmp);

        // read sign
        if ((char)(0x00 + singleData.get(0)) == '-') {
            data *= -1.0;
        }

        // read index
        int index = Integer.parseInt(String.valueOf((char)(0x00 + singleData.get(5)))) - 4;
        data *= Math.pow(1.0, index);

        return data;
    }

    static class MySerialPortEventListener implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                String buffer = null;
                try {
                    while (reader.ready()) {
                        buffer = reader.readLine();
                        ArrayList<Integer> bufferData = stringToData(buffer);
                        bufferData.addAll(DELIMITER);
                        System.out.print("GET: ");
                        dump(bufferData);
                        receivedData = bufferData;
                    }
                } catch (IOException ex){
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }
}
