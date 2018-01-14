package com.vg;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

import org.junit.Test;

public class LinkedListsTest {

    static class Node<T> {
        Node<T> next;
        T value;

        public Node() {
        }

        public Node(Node<T> next, T value) {
            this.next = next;
            this.value = value;
        }

        public Node(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (this.value == null) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder();
            Node<T> node = this;
            sb.append('[').append(node.value);
            node = node.next;
            while (node != null && node.value != null) {
                sb.append(", ").append(node.value);
                node = node.next;
            }
            return sb.append(']').toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            Node<T> other = (Node) obj;
            Node<T> tmp = this;
            while (tmp != null && other != null) {
                if (!tmp.value.equals(other.value)) {
                    return false;
                }
                tmp = tmp.next;
                other = other.next;
            }
            return tmp == null && other == null;
        }

        public int size() {
            Node<T> tmp = this;
            int sz = 0;
            while (tmp != null) {
                sz++;
                tmp = tmp.next;
            }
            return sz;
        }

        public Node<T> get(int index) {
            Node<T> tmp = this;
            for (int i = 0; i < index && tmp != null; i++) {
                tmp = tmp.next;
            }
            return tmp;
        }
    }

    static <T> Node<T> removeDups(Node<T> list) {
        if (list == null)
            return null;
        Set<T> uniq = new HashSet<>();
        Node<T> result = new Node<>();
        Node<T> head = result;
        Node<T> node = list;
        while (node != null && node.value != null) {
            if (uniq.add(node.value)) {
                result.next = node;
                result = node;
            }
            node = node.next;
        }
        result.next = null;

        return head.next;
    }

    //How would you solve this problem if a temporary buffer is not allowed?
    //O(N^2)
    static <T> Node<T> removeDupsNoTmp(Node<T> list) {
        if (list == null)
            return null;

        Node<T> node = list;
        while (node != null && node.value != null) {
            Node<T> prev = node;
            Node<T> next = node.next;
            while (next != null && next.value != null) {
                if (node.value.equals(next.value)) {
                    prev.next = next.next;
                    next = next.next;
                } else {
                    prev = next;
                    next = next.next;
                }
            }
            node = node.next;
        }
        return list;
    }

    @Test
    //Remove Dups: Write code to remove duplicates from an unsorted linked list.
    //FOLLOW UP
    //How would you solve this problem if a temporary buffer is not allowed?
    public void testRemoveDups() throws Exception {
        Node<String> list = toNodes("b", "b", "c", "b", "a", "a");
        assertEquals("[b, c, a]", removeDups(list).toString());
        Node<String> list2 = toNodes("b", "b", "c", "b", "a", "a");
        assertEquals("[b, c, a]", removeDupsNoTmp(list2).toString());

    }

    static <T> Node<T> toNodes(T... values) {
        if (values.length == 0)
            return new Node<>();
        Node<T> node = new Node<>();
        Node<T> head = node;
        node.value = values[0];
        for (int i = 1; i < values.length; i++) {
            Node<T> n = new Node<>();
            n.value = values[i];
            node.next = n;
            node = n;
        }

        return head;
    }

    @Test
    //Return Kth to Last: Implement an algorithm to find the kth to last element of a singly linked list.
    public void testKLast() throws Exception {
        Node<String> list = toNodes("b", "d", "c", "e", "f", "a");
        assertEquals("e", last(list, 2).value);

        Node<String> list2 = toNodes("f", "a");
        assertEquals("f", last(list2, 2).value);
    }

    static <T> Node<T> last(Node<T> list, int k) {
        Node<T> klast = list;
        Node<T> last = list;
        for (int i = 0; i < k && last != null; i++) {
            last = last.next;
        }
        if (last == null || last.next == null) {
            return klast;
        }
        while (last.next != null) {
            last = last.next;
            klast = klast.next;
        }

        return klast;
    }

    @Test
    //Delete Middle Node: Implement an algorithm to delete a node in the middle 
    // (i.e., any node but the  first and last node, not necessarily the exact middle) of a singly linked list, 
    // given only access to that node.
    // EXAMPLE
    // Input: the node c from the linked list a->b->c->d->e->f
    // Result: nothing is returned, but the new linked list looks like a->b->d->e->f
    public void testDeleteMiddle() throws Exception {
        Node<String> list = toNodes("b", "d", "c", "e", "f", "a");
        Node<String> c = list.next.next;
        deleteNode(c);
        assertEquals("[b, d, e, f, a]", list.toString());
    }

    static void deleteNode(final Node<String> node) {
        node.value = node.next.value;
        node.next = node.next.next;
    }

    @Test
    /**
     * Partition: Write code to partition a linked list around a value x, such that
     * all nodes less than x come before all nodes greater than or equal to x. If x
     * is contained within the list, the values of x only need to be after the
     * elements less than x (see below). The partition element x can appear anywhere
     * in the "right partition"; it does not need to appear between the left and
     * right partitions.<br>
     * 
     * EXAMPLE
     * 
     * <pre>
     * Input: 3 -> 5 -> 8 -> 5 -> 10 -> 2 -> 1[partition=5] 
     * Output: 3 -> 1 -> 2 -> 10 -> 5 -> 5 -> 8
     * </pre>
     */
    public void testPartition() throws Exception {
        Node<Integer> nodes = toNodes(3, 5, 8, 5, 10, 2, 1);
        assertEquals("[3, 2, 1, 5, 8, 5, 10]", partition(nodes, 5).toString());
    }

    private static Node<Integer> partition(Node<Integer> nodes, int p) {
        Node<Integer> smallerTail = new Node<>();
        Node<Integer> smallerHead = smallerTail;

        Node<Integer> largerTail = new Node<>();
        Node<Integer> largerHead = largerTail;

        Node<Integer> node = nodes;
        while (node != null) {
            if (node.value < p) {
                smallerTail.next = node;
                smallerTail = node;
            } else {
                largerTail.next = node;
                largerTail = node;
            }
            node = node.next;
        }
        largerTail.next = null;
        smallerTail.next = largerHead.next;
        return smallerHead.next;
    }

    @Test
    /**
     * Sum Lists: You have two numbers represented by a linked list, where each node
     * contains a single digit.
     * 
     * The digits are stored in reverse order, such that the 1's digit is at the
     * head of the list.
     * 
     * Write a function that adds the two numbers and returns the sum as a linked
     * list.
     * 
     * EXAMPLE
     * 
     * <pre>
     * Input:(7 -> 1 -> 6) + (5 -> 9 -> 2). That is, 617 + 295. 
     * Output:2 -> 1 -> 9. That is, 912.
     * </pre>
     * 
     * FOLLOW UP Suppose the digits are stored in forward order. Repeat the above
     * problem.
     * 
     * EXAMPLE
     * 
     * <pre>
     * Input:(6 -> 1 -> 7) + (2 -> 9 -> 5). That is,617 + 295. 
     * Output:9 -> 1 -> 2. That is,912.
     * </pre>
     */
    public void testName() throws Exception {
        Node<Integer> a = toNodes(7, 1, 6);
        Node<Integer> b = toNodes(5, 9, 2);

        assertEquals("[2, 1, 9]", sum(a, b).toString());

        Node<Integer> a2 = toNodes(6, 1, 7);
        Node<Integer> b2 = toNodes(2, 9, 5);

        assertEquals("[9, 1, 2]", sum2(a2, b2).toString());
    }

    @Test
    public void testReverse() throws Exception {
        Node<Integer> a2 = toNodes(6, 1, 7);
        assertEquals("[7, 1, 6]", reverse(a2).toString());
    }

    static Node<Integer> sum(Node<Integer> a, Node<Integer> b) {
        int rem = 0;
        Node<Integer> res = new Node<>();
        Node<Integer> head = res;
        while (a != null && b != null) {
            int A = a.value;
            int B = b.value;
            int sum = A + B + rem;
            int val = sum;
            if (sum >= 10) {
                val = sum % 10;
                rem = (sum - val) / 10;
            }
            res.next = new Node<>();
            res.next.value = val;
            res = res.next;
            a = a.next;
            b = b.next;
        }
        return head.next;
    }

    static <T> Node<T> reverse(Node<T> list) {
        Node<T> head = null;
        Node<T> tail = null;
        while (list != null) {
            if (head == null) {
                head = list;
                tail = head;
                list = list.next;
            } else {
                Node<T> tmp = head;
                head = list;
                list = list.next;
                head.next = tmp;
            }
        }
        if (tail != null) {
            tail.next = null;
        }
        return head;
    }

    static <T> Node<T> reverseClone(Node<T> list) {
        Node<T> head = null;
        while (list != null) {
            Node<T> n = new Node<>();
            n.value = list.value;
            n.next = head;
            head = n;
            list = list.next;
        }
        return head;
    }

    static Node<Integer> sum2(Node<Integer> a, Node<Integer> b) {
        return reverse(sum(reverse(a), reverse(b)));
    }

    @Test
    //Palindrome: Implement a function to check if a linked list is a palindrome.
    public void testPalindrome() throws Exception {
        assertTrue(isPalindromeRec(toNodes(1, 2, 3, 4, 3, 2, 1)));
        assertTrue(isPalindromeRec(toNodes(1, 2, 3, 3, 2, 1)));
        assertFalse(isPalindromeRec(toNodes(1, 2, 5, 3, 2, 1)));

        assertTrue(isPalindrome(toNodes(1, 2, 3, 4, 3, 2, 1)));
        assertTrue(isPalindrome(toNodes(1, 2, 3, 3, 2, 1)));
        assertFalse(isPalindrome(toNodes(1, 2, 5, 3, 2, 1)));
    }

    private static boolean isPalindrome(Node<Integer> nodes) {
        Node<Integer> reverseClone = reverseClone(nodes);
        return nodes.equals(reverseClone);
    }

    static class Pair<K, V> {
        K key;
        V value;

        public Pair(K key, V value) {
            super();
            this.key = key;
            this.value = value;
        }

    }

    private static <T> Pair<Node<T>, Boolean> isPalindromeRec(Node<T> nodes, int maxlen) {
        if (maxlen <= 1)
            return new Pair<>(nodes.next, true);
        if (maxlen == 2)
            return new Pair<>(nodes.next.next, nodes.value.equals(nodes.next.value));

        Pair<Node<T>, Boolean> p = isPalindromeRec(nodes.next, maxlen - 2);
        if (!p.value) {
            return p;
        }
        return new Pair<>(p.key.next, nodes.value.equals(p.key.value));
    }

    private static boolean isPalindromeRec(Node<Integer> nodes) {
        int size = nodes.size();
        return isPalindromeRec(nodes, size).value;
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(toNodes(1, 2, 3).equals(toNodes(1, 2, 3)));
        assertFalse(toNodes(1, 2, 3, 4).equals(toNodes(1, 2, 3)));
        assertFalse(toNodes(1, 2, 3).equals(toNodes(1, 2, 3, 4)));
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(new Integer(2), toNodes(1, 2, 3).get(1).value);
        assertEquals(new Integer(1), toNodes(1, 2, 3).get(0).value);
    }

    @Test
    /**
     * Intersection: Given two (singly) linked lists, determine if the two lists
     * intersect. Return the inter­secting node. Note that the intersection is
     * defined based on reference, not value. That is, if the kth node of the first
     * linked list is the exact same node (by reference) as the jth node of the
     * second linked list, then they are intersecting.
     */
    public void testIntersection() throws Exception {
        Node<String> a = new Node<>("a");
        Node<String> b = new Node<>("b");
        Node<String> c = new Node<>("c");
        Node<String> d = new Node<>("d");
        Node<String> e = new Node<>("e");
        Node<String> f = new Node<>("f");
        Node<String> g = new Node<>("g");
        //a -> b -> d -> e -> f -> g
        //c -> e -> f -> g
        a.next = b;
        b.next = d;
        d.next = e;
        e.next = f;
        f.next = g;
        c.next = e;
        System.out.println(a);
        System.out.println(c);

        Node<String> intersect = findIntersectNode(a, c);
        assertTrue(intersect == e);
    }

    static <T> Node<T> findIntersectNode(Node<T> a, Node<T> b) {
        Node<T> atail = a;
        Node<T> btail = b;
        int alen = atail != null ? 1 : 0;
        int blen = btail != null ? 1 : 0;

        while (atail != null && atail.next != null) {
            alen++;
            atail = atail.next;
        }

        while (btail != null && btail.next != null) {
            blen++;
            btail = btail.next;
        }
        if (btail != atail) {
            return null;
        }

        //skip nodes in larger list
        Node<T> larger = b;
        Node<T> smaller = a;
        if (alen > blen) {
            larger = a;
            smaller = b;
        }
        for (int i = 0; i < Math.abs(alen - blen); i++) {
            larger = larger.next;
        }

        while (larger != null && smaller != null) {
            if (larger == smaller) {
                return larger;
            }
            larger = larger.next;
            smaller = smaller.next;
        }

        return null;
    }

    @Test
    /**
     * Loop Detection: Given a circular linked list, implement an algorithm that
     * returns the node at the beginning of the loop. DEFINITION Circular linked
     * list: A (corrupt) linked list in which a node's next pointer points to an
     * earlier node, so as to make a loop in the linked list.
     * 
     * EXAMPLE
     * 
     * <pre>
     * Input: A -> B -> C -> D -> E -> C [the same C as earlier] 
     * Output: C
     * </pre>
     */
    public void testLoop() throws Exception {
        Node<String> a = new Node<>("a");
        Node<String> b = new Node<>("b");
        Node<String> c = new Node<>("c");
        Node<String> d = new Node<>("d");
        Node<String> e = new Node<>("e");
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        e.next = c;

        Node<String> loop2 = findLoopPointers(a);
        assertTrue(loop2 == c);

        Node<String> loop = findLoop(a);
        assertTrue(loop == c);
    }

    static <T> Node<T> findLoop(Node<T> list) {
        IdentityHashMap<Node<T>, Boolean> map = new IdentityHashMap<>();
        while (list != null) {
            if (map.containsKey(list)) {
                return list;
            }
            map.put(list, true);
            list = list.next;
        }
        return null;
    }

    static <T> Node<T> findLoopPointers(Node<T> list) {
        Node<T> slow = list;
        Node<T> fast = list;
        while (slow != null && fast != null) {
            System.out.println("slow " + slow.value + " fast: " + fast.value);
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                System.out.println("collision at " + slow.value + " " + fast.value);
                break;
            }
        }
        if (slow == null) {
            return null;
        }
        Node<T> head = list;
        while (head != slow) {
            head = head.next;
            slow = slow.next;
        }
        return head;
    }
}
