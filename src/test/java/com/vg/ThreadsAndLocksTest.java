package com.vg;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.stream.IntStream.range;
import static rx.schedulers.Schedulers.newThread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import org.junit.Test;

import rx.Observable;

public class ThreadsAndLocksTest {
    static void print(String s) {
        System.out.println(currentThread().getName() + ": " + s);
    }

    @Test
    /**
     * FizzBuzz: In the classic problem FizzBuzz, you are told to print the numbers
     * from 1 to n. However, when the number is divisible by 3, print "Fizz" When it
     * is divisible by 5, print "Buzz" When it is divisible by 3 and 5, print
     * "FizzBuzz" In this problem, you are asked to do this in a multithreaded way.
     * Implement a multithreaded version of FizzBuzz with four threads. One thread
     * checks for divisibility of 3 and prints "Fizz" Another thread is responsible
     * for divisibility of 5 and prints "Buzz" A third thread is responsible for
     * divisibility of 3 and 5 and prints "FizzBuzz" A fourth thread does the
     * numbers.
     */
    public void testFizzBuzz() throws Exception {
        Observable<Integer> range = Observable.range(0, 100).observeOn(newThread());
        range.filter(x -> (x % 3) == 0).subscribe(x -> print("Fizz " + x));
        range.filter(x -> (x % 5) == 0).subscribe(x -> print("Buzz " + x));
        range.filter(x -> (x % 3) == 0 && (x % 5) == 0).subscribe(x -> print("FizzBuzz " + x));
        range.filter(x -> (x % 3) != 0 && (x % 5) != 0).subscribe(x -> print("" + x));
        range.toBlocking().subscribe();
    }

    static class Foo {
        Semaphore s = new Semaphore(3);

        public Foo() throws InterruptedException {
            s.acquire(2);
        }

        void first() {
            while (!s.tryAcquire())
                ;
            System.out.println("first " + Thread.currentThread().getName());
            s.release(2);
        }

        void second() {
            while (!s.tryAcquire(2))
                ;
            System.out.println("second " + Thread.currentThread().getName());
            s.release(3);
        }

        void third() {
            while (!s.tryAcquire(3))
                ;
            System.out.println("third " + Thread.currentThread().getName());
            s.release(3);
        }
    }

    @Test
    /**
     * Call In Order: Suppose we have the following code:
     * 
     * <pre>
     * public class Foo { 
     * public Foo() { ... } 
     * public void first() { ... } 
     * public void second() { ... } 
     * public void third() { ... } 
     * }
     * </pre>
     * 
     * The same instance of Foo will be passed to three different threads. ThreadA
     * will call first, threads will call second, and thread( will call third.
     * Design a mechanism to ensure that first is called be re second and second is
     * called before third.
     * 
     * @throws Exception
     */
    public void testCallInOrder() throws Exception {
        Foo f = new Foo();
        Thread a = new Thread(() -> f.first(), "a");
        Thread b = new Thread(() -> f.second(), "b");
        Thread c = new Thread(() -> f.third(), "c");
        b.start();
        c.start();
        a.start();
        c.join();
        b.join();
        a.join();

    }

    /**
     * Dining Philosophers: In the famous dining philosophers problem, a bunch of
     * philosophers are sitting around a circular table with one chopstick between
     * each of them. A philosopher needs both chopsticks to eat, and always picks up
     * the left chopstick before the right one. A deadlock could potentially occur
     * if all the philosophers reached for the left chopstick at the same time.
     * Using threads and locks, implement a simulation of the dining philosophers
     * problem that prevents dead­ locks.
     */
    static int CPUs = Runtime.getRuntime().availableProcessors();

    @Test
    public void testPhilosophers() throws Exception {
        Lock[] locks = range(0, CPUs).mapToObj(x -> new ReentrantLock()).toArray(Lock[]::new);
        Thread[] threads = range(0, CPUs)
                .mapToObj(x -> new Thread(new Philosopher(locks[x], locks[(x + 1) & (CPUs - 1)], x + 1 > CPUs)))
                .toArray(Thread[]::new);

        Stream.of(threads).forEach(t -> {
            t.setDaemon(true);
            t.start();
        });
        Stream.of(threads).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    static class Philosopher implements Runnable {
        final static AtomicInteger ID = new AtomicInteger();
        final Lock left;
        final Lock right;
        final boolean putBackRightFirst;
        final int id;
        final AtomicInteger counter = new AtomicInteger();

        public Philosopher(Lock left, Lock right) {
            this(left, right, false);
        }

        public Philosopher(Lock left, Lock right, boolean putBackRightFirst) {
            this.left = left;
            this.right = right;
            this.putBackRightFirst = putBackRightFirst;
            this.id = ID.incrementAndGet();
        }

        boolean pickUp() {
            if (left.tryLock()) {
                if (!right.tryLock()) {
                    left.unlock();
                    return false;
                }
                return true;
            }
            return false;
        }

        boolean putDown() {
            right.unlock();
            left.unlock();
            return false;
        }

        long prevTime = System.currentTimeMillis();

        @Override
        public void run() {
            while (true) {
                if (pickUp()) {
                    //eat
                    try {
                        System.out.println(id + " eat");
                        Thread.sleep(500);
                        long now = System.currentTimeMillis();
                        long lastTimeIAte = now - prevTime;
                        System.out
                                .println(id + " done eating " + counter.incrementAndGet() + " " + lastTimeIAte + "ms");
                        prevTime = now;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    putDown();
                }
            }
        }
    }

}
