package org.example;

import java.util.concurrent.Semaphore;

public class NaivePhilosopher extends Philosopher {
    public NaivePhilosopher(String name, Semaphore leftFork, Semaphore rightFork) {
        super(name, leftFork, rightFork);
    }

    @Override
    protected void acquireForks() throws InterruptedException {
        leftFork.acquire();
        rightFork.acquire();
    }
}
