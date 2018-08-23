package Manager;

import ILS.ILS;

public class Manager {
    private static ILS ils = new ILS();

    public static ILS getIls() {
        return ils;
    }
}
