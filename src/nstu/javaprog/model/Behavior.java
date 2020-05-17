package nstu.javaprog.model;

import java.util.concurrent.TimeUnit;

public abstract class Behavior {
    private final Thread thread;
    protected Habitat habitat = null;
    private volatile boolean isSuspended = true;

    public Behavior(int period) {
        thread = new Thread(new Task(period));
    }

    final void prepare(Habitat habitat) {
        this.habitat = habitat;
        thread.start();
    }

    public abstract void act();

    final synchronized void activate() {
        isSuspended = false;
        notify();
    }

    final void deactivate() {
        isSuspended = true;
    }

    final void changePriority(int newPriority) {
        thread.setPriority(newPriority);
    }

    final boolean isActivated() {
        return !isSuspended;
    }

    private class Task implements Runnable {
        private final int period;

        Task(int period) {
            this.period = period;
        }

        @Override
        public void run() {
            while (!thread.isInterrupted()) {
                synchronized (Behavior.this) {
                    while (isSuspended) {
                        try {
                            Behavior.this.wait();
                        } catch (InterruptedException exception) {
                            thread.interrupt();
                        }
                    }
                }

                act();

                try {
                    TimeUnit.MILLISECONDS.sleep(period);
                } catch (InterruptedException exception) {
                    thread.interrupt();
                }
            }
        }
    }
}
