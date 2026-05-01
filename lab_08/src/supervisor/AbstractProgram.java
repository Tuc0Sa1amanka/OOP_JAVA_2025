package supervisor;

import java.lang.Thread;
import java.util.Random;

public final class AbstractProgram extends Thread {
    private State state;
    private boolean writable = true;
    public enum State {
        UNKNOWN,
        STOPPING,
        RUNNING,
        FATAL_ERROR
    }
    public AbstractProgram() {
        setAState(State.UNKNOWN);
    }
    public synchronized void setAState(State state) {
        while(!writable) {
            try {
                wait();
            } catch (InterruptedException e) {
                break;
            }
        }
        this.state = state;
        writable = false;
        notify();
    }
    public synchronized State getAState() {
        while (writable) {
            try {
                wait();
            } catch (InterruptedException e) {
                break;
            }
        }
        writable = true;
        notify();
        return state;
    }
    @Override
    public void run() {
        Thread daemon = new Thread(() -> {
            Random random = new Random();
            do {
                try {
                    setAState(State.values()[random.nextInt(1, State.values().length)]);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            } while (true);
        });
        daemon.setDaemon(true);
        daemon.start();
        while (!isInterrupted()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
