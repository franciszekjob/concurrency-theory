package org.example;

import java.util.concurrent.Semaphore;

public class AsymmetricPhilosopher extends Philosopher {
    private final int philosopherNumber;

    public AsymmetricPhilosopher(String name, Semaphore leftFork, Semaphore rightFork, int philosopherNumber) {
        super(name, leftFork, rightFork);
        this.philosopherNumber = philosopherNumber;
    }

    @Override
    protected void acquireForks() throws InterruptedException {
        if (philosopherNumber % 2 == 0) {
            rightFork.acquire();
            leftFork.acquire();
            Thread.sleep(1000);
        } else {
            leftFork.acquire();
            rightFork.acquire();
        }
    }
}
