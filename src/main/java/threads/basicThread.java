package threads;

public class basicThread extends Thread {

    private boolean runnin = true;

    public void kill() {
        runnin = false;
    }

    public boolean getRunnin() {
        return runnin;
    }
}
