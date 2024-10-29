package org.example;

import java.util.concurrent.Semaphore;

public class Main {
    static final int philosophersCount = 5;
    static final Semaphore[] forks = new Semaphore[philosophersCount];

    public static void main(String[] args) {
        for(int i = 0; i < philosophersCount; i++){
            forks[i] = new Semaphore(1);
        }

        // 1. naive solution
//        runNaivePhilosophers();

        // 2. starving solution
//        runStarvingPhilosophers();

        // 3. asymmetric solution
        runAsymmetricPhilosophers();

        // 4. arbitrator solution
//        runArbitratorPhilosophers();
    }

    static void runNaivePhilosophers() {
        NaivePhilosopher[] philosophers = new NaivePhilosopher[philosophersCount];

        for(int i = 0; i < philosophersCount; i++){
            Semaphore leftFork = forks[i];
            Semaphore rightFork = forks[(i + 1) % philosophersCount];

            philosophers[i] = new NaivePhilosopher("Philosopher " + (i + 1), rightFork, leftFork);
            philosophers[i].start();
        }
    }

    static void runStarvingPhilosophers() {
        StarvingPhilosopher[] philosophers = new StarvingPhilosopher[philosophersCount];

        for(int i = 0; i < philosophersCount; i++){
            Semaphore leftFork = forks[i];
            Semaphore rightFork = forks[(i + 1) % philosophersCount];

            philosophers[i] = new StarvingPhilosopher("Philosopher " + (i + 1), rightFork, leftFork);
            philosophers[i].start();
        }
    }

    static void runAsymmetricPhilosophers() {
        AsymmetricPhilosopher[] philosophers = new AsymmetricPhilosopher[philosophersCount];

        for(int i = 0; i < philosophersCount; i++){
            Semaphore leftFork = forks[i];
            Semaphore rightFork = forks[(i + 1) % philosophersCount];

            philosophers[i] = new AsymmetricPhilosopher("Philosopher " + (i + 1), rightFork, leftFork, i);
            philosophers[i].start();
        }
    }

    static void runArbitratorPhilosophers() {
        Semaphore arbiter = new Semaphore(philosophersCount - 1);
        ArbitratorPhilosopher[] philosophers = new ArbitratorPhilosopher[philosophersCount];

        for(int i = 0; i < philosophersCount; i++){
            Semaphore leftFork = forks[i];
            Semaphore rightFork = forks[(i + 1) % philosophersCount];

            philosophers[i] = new ArbitratorPhilosopher("Philosopher " + (i + 1), rightFork, leftFork, arbiter);
            philosophers[i].start();
        }
    }
}
