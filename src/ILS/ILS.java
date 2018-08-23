package ILS;

import Device.Light;
import Device.PowerMeter;
import Device.Sensor;
import ILS.Algorithm.Algorithm;

import java.util.ArrayList;

public class ILS {
    // Devices
    private ArrayList<Light> lights;
    private ArrayList<Sensor> sensors;
    private PowerMeter powerMeter;

    // ILS parameters
    private Algorithm algorithm;
    private int step;

    public ILS() {

    }
}
