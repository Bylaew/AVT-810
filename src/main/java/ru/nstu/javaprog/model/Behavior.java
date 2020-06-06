package ru.nstu.javaprog.model;

import ru.nstu.javaprog.view.ViewContainer;

import java.util.concurrent.TimeUnit;

public abstract class Behavior {
    private final Object mutex = new Object();
    private final Thread thread;
    protected Habitat habitat;
    protected ViewContainer view;
    private volatile boolean isSuspended = true;

    public Behavior(int period) {
        thread = new Thread(new Task(period));
    }

    final void prepare(Habitat habitat, ViewContainer view) {
        this.habitat = habitat;
        this.view = view;
        thread.start();
    }

    public abstract void act();

    final void activate() {
        isSuspended = false;
        synchronized (mutex) {
            mutex.notify();
        }
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
        final int period;

        Task(int period) {
            this.period = period;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (mutex) {
                    while (isSuspended) {
                        try {
                            mutex.wait();
                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                act();

                try {
                    TimeUnit.MILLISECONDS.sleep(period);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
