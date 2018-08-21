import Utils.CL200A;

public class CL200A_Test {
    public static void main(String[] args) {
        CL200A.openPort();

        System.out.println(CL200A.setPcMode());
        System.out.println(CL200A.setHoldMode());
        System.out.println(CL200A.setExtMode());

        while (true) {
            System.out.println(CL200A.startMeasurement());
            System.out.println("ILL: " + CL200A.getIlluminance());
            System.out.println("CCT: " + CL200A.getCct());
        }

        // CL200A.closePort();
    }
}
