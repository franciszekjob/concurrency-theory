package org.example;

import java.util.concurrent.Semaphore;

public class StarvingPhilosopher extends Philosopher {
    public StarvingPhilosopher(String name, Semaphore leftFork, Semaphore rightFork) {
        super(name, leftFork, rightFork);
    }

    @Override
    protected void acquireForks() throws InterruptedException {
        if (leftFork.tryAcquire() && rightFork.tryAcquire()) {
            Thread.sleep(2000);
        } else {
            if (leftFork.availablePermits() == 0) {
                leftFork.release();
            }
            if (rightFork.availablePermits() == 0) {
                rightFork.release();
            }
        }
    }
}
