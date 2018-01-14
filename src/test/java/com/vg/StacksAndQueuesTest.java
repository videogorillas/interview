package com.vg;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Test;

import com.vg.StacksAndQueuesTest.Animal;
import com.vg.StacksAndQueuesTest.Dog;

public class StacksAndQueuesTest {
    interface Queue<T> {
        void add(T t);

        T remove();
    }

    interface Stack<T> {
        void push(T t);

        T pop();

        boolean isEmpty();

        T peek();

        boolean isFull();
    }

    static class ArrayStack<T> implements Stack<T> {
        private static final int DEFAULT_CAPACITY = 10;
        Object[] arr;
        int offset;
        int len;
        int head = -1;
        int[] minidx;
        int minhead = -1;

        public ArrayStack() {
            this(new Object[DEFAULT_CAPACITY]);
        }

        public ArrayStack(Object[] array) {
            this(array, 0, array.length);
        }

        public ArrayStack(Object[] array, int off, int len) {
            this.arr = array;
            this.offset = off;
            this.len = len;
            this.minidx = new int[len];
        }

        @Override
        public void push(T t) {
            if (isFull()) {
                throw new IllegalArgumentException("stack full");
            }
            head++;
            arr[offset + head] = t;
            if (minhead == -1) {
                minhead = 0;
                minidx[minhead] = 0;
            } else {
                T mint = (T) arr[minidx[minhead]];
                if (mint instanceof Comparable) {
                    if (((Comparable) mint).compareTo(t) > 0) {
                        minidx[++minhead] = head;
                    }

                }
            }
        }

        @Override
        public T pop() {
            if (isEmpty()) {
                throw new NoSuchElementException("empty stack");
            }
            T t = (T) arr[offset + head];
            if (head == minidx[minhead]) {
                minhead--;
            }
            head--;
            return t;
        }

        public T min() {
            if (isEmpty()) {
                throw new NoSuchElementException("empty stack");
            }
            return (T) arr[offset + minidx[minhead]];
        }

        @Override
        public boolean isEmpty() {
            return head < 0;
        }

        @Override
        public T peek() {
            if (isEmpty()) {
                throw new NoSuchElementException("empty stack");
            }
            return (T) arr[offset + head];
        }

        @Override
        public boolean isFull() {
            return head + 1 >= len;
        }

        @Override
        public String toString() {
            List<Object> subList = asList(arr).subList(offset, offset + head + 1);
            return subList.toString();
        }

    }

    @Test
    public void testArrayStack() throws Exception {
        testStack(new ArrayStack<>(new Object[3]));
    }

    private void testStack(Stack<String> s) {
        s.push("a");
        s.push("b");
        s.push("c");
        try {
            s.push("d");
            fail("exception expected");
        } catch (Exception e) {
        }
        assertEquals("c", s.pop());
        assertEquals("b", s.pop());
        assertEquals("a", s.pop());
        try {
            s.pop();
            fail("exception expected");
        } catch (Exception e) {
        }
    }

    @Test
    //Three in One: Describe how you could use a single array to implement three stacks.
    public void testThreeInOne() throws Exception {
        Object[] stackStorage = new Object[9];
        Stack<String> s1 = new ArrayStack<>(stackStorage, 0, 3);
        Stack<String> s2 = new ArrayStack<>(stackStorage, 3, 3);
        Stack<String> s3 = new ArrayStack<>(stackStorage, 6, 3);
        testStack(s1);
        testStack(s2);
        testStack(s3);
    }

    @Test
    /**
     * Stack Min: How would you design a stack which, in addition to push and pop,
     * has a function min which returns the minimum element? Push, pop and min
     * should all operate in 0(1) time.
     */
    public void testStackMin() throws Exception {
        ArrayStack<String> s = new ArrayStack<>();
        s.push("c");
        s.push("a");
        s.push("b");
        assertEquals("a", s.min());
        s.pop();
        assertEquals("a", s.min());
        s.pop();
        assertEquals("c", s.min());
    }

    static class SetOfStacks<T extends Comparable<T>> implements Stack<T> {
        private int maxsize;
        ArrayStack<ArrayStack<T>> stacks = new ArrayStack<>();

        public SetOfStacks(int maxsize) {
            this.maxsize = maxsize;
            stacks.push(new ArrayStack<>(new Object[maxsize]));
        }

        @Override
        public void push(T t) {
            ArrayStack<T> stack = stacks.peek();
            if (stack.isFull()) {
                stacks.push(newStack());
                stack = stacks.peek();
            }
            stack.push(t);
        }

        private ArrayStack<T> newStack() {
            return new ArrayStack<>(new Object[maxsize]);
        }

        @Override
        public T pop() {
            ArrayStack<T> stack = stacks.peek();
            if (stack.isEmpty()) {
                stacks.pop();
                stack = stacks.peek();
            }
            return stack.pop();
        }

        @Override
        public boolean isEmpty() {
            return stacks.isEmpty();
        }

        @Override
        public T peek() {
            return stacks.peek().peek();
        }

        @Override
        public boolean isFull() {
            return stacks.isFull() && stacks.peek().isFull();
        }

        public T popAt(int stackIndex) {
            ArrayStack<T> stack = (ArrayStack<T>) stacks.arr[stackIndex];
            return stack.pop();
        }
    }

    /**
     * Stack of Plates: Imagine a (literal) stack of plates. If the stack gets too
     * high, it might topple. Therefore, in real life, we would likely start a new
     * stack when the previous stack exceeds some threshold. Implement a data
     * structure SetOfStacks that mimics this. SetOfStacks should be composed of
     * several stacks and should create a new stack once the previous one exceeds
     * capacity. SetOfStacks.push() and SetOfStacks.pop() should behave identically
     * to a single stack (that is, pop() should return the same values as it would
     * if there were just a single stack).
     * 
     * FOLLOW UP Implement a function popAt(int index) which performs a pop
     * operation on a specific sub-stack.
     */
    @Test
    public void testStackOfPlates() throws Exception {
        SetOfStacks<String> stacks = new SetOfStacks<>(2);
        stacks.push("a");
        stacks.push("b");
        stacks.push("c");
        assertEquals("c", stacks.pop());
        assertEquals("b", stacks.pop());
        assertEquals("a", stacks.pop());

        stacks.push("d");
        stacks.push("e");
        stacks.push("f");
        assertEquals("e", stacks.popAt(0));
    }

    static class MyQueue<T> implements Queue<T> {

        Stack<T> s1 = new ArrayStack<>();
        Stack<T> s2 = new ArrayStack<>();

        @Override
        public void add(T t) {
            s1.push(t);
        }

        @Override
        public T remove() {
            while (!s1.isEmpty()) {
                s2.push(s1.pop());
            }
            T pop = s2.pop();
            while (!s2.isEmpty()) {
                s1.push(s2.pop());
            }
            return pop;
        }
    }

    @Test
    //Queue via Stacks: Implement a MyQueue class which implements a queue using two stacks.
    public void testQueueViaStacks() throws Exception {
        Queue<String> q = new MyQueue<>();
        q.add("a");
        q.add("b");
        assertEquals("a", q.remove());
        q.add("c");

        assertEquals("b", q.remove());
        assertEquals("c", q.remove());
    }

    static <T> void moveAll(Stack<T> src, Stack<T> dst) {
        while (!src.isEmpty()) {
            dst.push(src.pop());
        }
    }

    static <T> void moveAll(Stack<T> src, Stack<T> dst, int maxCount) {
        for (int i = 0; i < maxCount && !src.isEmpty(); i++) {
            dst.push(src.pop());
        }
    }

    @Test
    /**
     * Sort Stack: Write a program to sort a stack such that the smallest items are
     * on the top. You can use an additional temporary stack, but you may not copy
     * the elements into any other data structure (such as an array). The stack
     * supports the following operations: push, pop, peek, and isEmpty.
     */
    public void testSortStack() throws Exception {
        Random rnd = new Random(4243);
        ArrayStack<Integer> stack = new ArrayStack<>();
        int[] expected = new int[10];
        for (int i = 0; i < 10; i++) {
            int nextInt = rnd.nextInt(100);
            stack.push(nextInt);
            expected[i] = nextInt;
        }
        Arrays.sort(expected);
        reverseInplace(expected);

        System.out.println("random:   " + stack);
        System.out.println("expected: " + Arrays.toString(expected));
        ArrayStack<Integer> sorted = new ArrayStack<>();
        while (!stack.isEmpty()) {
            Integer pop = stack.pop();
            if (sorted.isEmpty() || sorted.peek() > pop) {
                sorted.push(pop);
            } else {
                Integer pop2 = sorted.peek();
                int count = 0;
                while (pop2 < pop) {
                    stack.push(sorted.pop());
                    count++;
                    if (sorted.isEmpty()) {
                        break;
                    }
                    pop2 = sorted.peek();
                }
                sorted.push(pop);
                moveAll(stack, sorted, count);
            }
        }
        assertEquals(Arrays.toString(expected), sorted.toString());
    }

    static int[] reverseInplace(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int tmp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = tmp;
        }
        return arr;
    }

    static class Animal {
        String type;

        @Override
        public String toString() {
            return type;
        }
    }

    static class Dog extends Animal {
        public Dog() {
            this.type = "dog";
        }
    }

    static class Cat extends Animal {
        public Cat() {
            this.type = "cat";
        }
    }

    static class AnimalShelter {
        LinkedList<Animal> all = new LinkedList<>();

        public void enqueue(Animal animal) {
            all.add(animal);
        }

        public Animal dequeueAny() {
            Animal animal = all.remove();
            if (animal == null)
                return null;
            return animal;
        }

        public Dog dequeueDog() {
            ListIterator<Animal> it = all.listIterator();
            while (it.hasNext()) {
                Animal next = it.next();
                if (next instanceof Dog) {
                    it.remove();
                    return (Dog) next;
                }
            }
            return null;
        }

        public Cat dequeueCat() {
            ListIterator<Animal> it = all.listIterator();
            while (it.hasNext()) {
                Animal next = it.next();
                if (next instanceof Dog) {
                    it.remove();
                    return (Cat) next;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return all.toString();
        }

    }

    /**
     * Animal Shelter: An animal shelter, which holds only dogs and cats, operates
     * on a strictly first in, first out" basis. People must adopt either the
     * "oldest" (based on arrival time) of all animals at the shelter, or they can
     * select whether they would prefer a dog or a cat (and will receive the oldest
     * animal of that type). They cannot select which specific animal they would
     * like. Create the data structures to maintain this system and implement
     * operations such as enqueue, dequeueAny, dequeueDog, and dequeueCat. You may
     * use the built-in LinkedList data structure.
     */
    @Test
    public void testAnimalShelter() throws Exception {
        AnimalShelter shelter = new AnimalShelter();
        shelter.enqueue(new Cat());
        shelter.enqueue(new Cat());
        shelter.enqueue(new Dog());
        shelter.enqueue(new Cat());
        shelter.enqueue(new Dog());
        assertEquals("cat", shelter.dequeueAny().type);
        assertEquals("dog", shelter.dequeueDog().type);
        assertEquals("[cat, cat, dog]", shelter.toString());
    }

}
