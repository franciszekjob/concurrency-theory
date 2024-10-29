package org.example;

import java.util.concurrent.Semaphore;

public abstract class Philosopher extends Thread {
    protected final String name;
    protected final Semaphore leftFork;
    protected final Semaphore rightFork;

    public Philosopher(String name, Semaphore leftFork, Semaphore rightFork) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                acquireForks();
                eat();
                releaseForks();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void acquireForks() throws InterruptedException;

    protected void releaseForks() {
        leftFork.release();
        rightFork.release();
    }

    protected void think() {
        System.out.println(name + " is thinking");
        sleepRandom();
    }

    protected void eat() {
        System.out.println(name + " is eating");
        sleepRandom();
    }

    protected void sleepRandom() {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
