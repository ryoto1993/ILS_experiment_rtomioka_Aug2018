package Calibration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    static final String fileName = "calibration_ID22_20180821.csv";

    private int sequence = 0;
    BufferedWriter bw;

    public void makeFile() {
        try {
            bw = new BufferedWriter(new FileWriter(fileName, true));
            bw.write("Sequence" + "," + "SET_LUM" + "," + "SET_CCT" + "," + "RAW_LUM" + "," + "RAW_CCT");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFile() {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendData(double set_lum, int set_cct, double raw_lum, double raw_cct) {
        try {
            bw.write(sequence+ "," + set_lum + "," + set_cct + "," + raw_lum + "," + raw_cct);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sequence++;
    }
}
