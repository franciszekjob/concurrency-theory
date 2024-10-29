package org.example;

import java.util.concurrent.Semaphore;

public class ArbitratorPhilosopher extends Philosopher {
    private final Semaphore arbiter;

    public ArbitratorPhilosopher(String name, Semaphore leftFork, Semaphore rightFork, Semaphore arbiter) {
        super(name, leftFork, rightFork);
        this.arbiter = arbiter;
    }

    @Override
    protected void acquireForks() throws InterruptedException {
        arbiter.acquire();
        leftFork.acquire();
        rightFork.acquire();
    }

    @Override
    protected void releaseForks() {
        super.releaseForks();
        arbiter.release();
    }
}
