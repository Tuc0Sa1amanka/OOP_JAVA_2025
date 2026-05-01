package supervisor;

public final class Supervisor extends Thread {
    private final AbstractProgram program;
    Supervisor() {
        program = new AbstractProgram();
    }
    public void startApp() {
        program.setAState(AbstractProgram.State.RUNNING);
    }
    public void stopApp() {
        System.out.println("Stop app is called");
        program.interrupt();
    }
    @Override
    public void run() {
        do {
            try {
                synchronized (program) {
                    AbstractProgram.State current = program.getAState();
                    System.out.println("Current state is " + current.name());
                    if (current == AbstractProgram.State.UNKNOWN) {
                        program.start();
                        startApp();
                    } else if (current == AbstractProgram.State.STOPPING) {
                        startApp();
                    } else if (current == AbstractProgram.State.FATAL_ERROR) {
                        stopApp();
                        break;
                    }
                }
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    break;
                }
        } while (true);
    }
    public static void main(String[] args) {
        Supervisor sup = new Supervisor();
        sup.start();
    }
}
