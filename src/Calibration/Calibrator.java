package Calibration;

import LightingClient.Light;
import LightingClient.SocketClient;
import Utils.CL200A;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Calibrator {
    public static void main(String[] args) {
        ArrayList<Light> lights;
        Logger logger = new Logger();

        // Setup Socket
        SocketClient.setEndpoint(new InetSocketAddress("172.20.11.58", 44344));
        lights = SocketClient.getLights();

        // Setup CL-200
        CL200A.openPort();
        CL200A.setPcMode();
        CL200A.setHoldMode();
        CL200A.setExtMode();

        // Dim out all lights
        for (Light l: lights) {
            l.setLumPct(0.0);
            l.setTemperature(2700);
        }
        SocketClient.dimByLights(lights);

        // Make log file
        logger.makeFile();

        // Dim and get data
        ArrayList<Light> dimLights = new ArrayList<>();
        dimLights.add(lights.get(21));

        // Calibration sequence
        /*
        for (int setCct = 2700; setCct<=6000; setCct += 1000) {
            for (double setLum = 0.0; setLum <= 100.0; setLum += 50.0) {
                // Dim
                for (Light l: dimLights) {
                    l.setLumPct(setLum);
                    l.setTemperature(setCct);
                }
                SocketClient.dimByLights(lights);

                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // DBG
                System.out.println("SetCCT: " + setCct + " SetLum: " + setLum );
                // Get
                CL200A.startMeasurement();

                System.out.println("ILL: " + CL200A.getIlluminance());
                System.out.println("CCT: " + CL200A.getCct());
                logger.appendData(setLum, setCct, CL200A.getIlluminance(), CL200A.getCct());

            }
        }
        */

        for (int setLum = 10; setLum <= 100; setLum += 5) {
            for (int setCct = 2700; setCct <= 5800; setCct += 100) {
                // Dim
                for (Light l : dimLights) {
                    l.setLumPct(setLum);
                    l.setTemperature(setCct);
                }
                SocketClient.dimByLights(lights);

                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // DBG
                System.out.println("SetCCT: " + setCct + " SetLum: " + setLum);
                // Get
                CL200A.startMeasurement();

                System.out.println("ILL: " + CL200A.getIlluminance());
                System.out.println("CCT: " + CL200A.getCct());
                logger.appendData(setLum, setCct, CL200A.getIlluminance(), CL200A.getCct());
            }
        }

        logger.closeFile();
        CL200A.closePort();
    }
}
