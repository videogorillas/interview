package com.vg;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import com.vg.TreesAndGraphsTest.GraphNode;

@SuppressWarnings("unchecked")
public class TreesAndGraphsTest {
    static class GraphNode<T> {
        boolean visited = false;
        T value;
        List<GraphNode<T>> children = new ArrayList<>(1);
        GraphNode<T> parent;

        public GraphNode(T value) {
            this.value = value;
        }

        public void addChild(GraphNode<T>... child) {
            children.addAll(Arrays.asList(child));
        }

        @Override
        public String toString() {
            return "" + value;
        }

        public GraphNode<T> getLeft() {
            return children.size() >= 1 ? children.get(0) : null;
        }

        public GraphNode<T> getRight() {
            return children.size() > 1 ? children.get(1) : null;
        }

    }

    static <T> GraphNode<T> node(T value) {
        return new GraphNode<>(value);
    }

    @Test
    //Validate BST: Implement a function to check if a binary tree is a binary search tree.
    public void testValidateBST() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        GraphNode<Integer> tree = binarySearchTree(array);
        assertTrue(isValidBST(tree));

    }

    private static boolean isValidBST(GraphNode<Integer> root) {
        if (root.children.isEmpty()) {
            return true;
        }
        boolean leftOk = true;
        if (root.getLeft() != null) {
            leftOk = root.getLeft().value <= root.value && isValidBST(root.getLeft());
        }
        boolean rightOk = true;
        if (leftOk && root.getRight() != null) {
            rightOk = root.value < root.getRight().value && isValidBST(root.getRight());
        }
        return leftOk && rightOk;
    }

    static <T> List<T> copyBST(GraphNode<T> root) {
        return copyBST(root, new ArrayList<>());
    }

    static <T> List<T> copyBST(GraphNode<T> root, List<T> list) {
        if (root == null)
            return list;
        copyBST(root.getLeft(), list);
        list.add(root.value);
        copyBST(root.getRight(), list);
        return list;
    }

    @Test
    public void testCopyBST() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        GraphNode<Integer> tree = binarySearchTree(array);
        List<Integer> copyBST = copyBST(tree);
        System.out.println(copyBST);
        assertArrayEquals(array, copyBST.stream().mapToInt(x -> x).toArray());
    }

    @Test
    /**
     * Successor: Write an algorithm to find the "next" node (i.e., in-order
     * successor) of a given node in a binary search tree. You may assume that each
     * node has a link to its parent.
     */
    public void testSuccessor() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        GraphNode<Integer> tree = binarySearchTree(array);
        populateParent(tree);
        printLayers(tree);
        assertEquals(21, (int) nextNode(find(tree, 19)).value);
        assertEquals(66, (int) nextNode(find(tree, 59)).value);
        assertEquals(59, (int) nextNode(find(tree, 47)).value);
        assertNull(nextNode(find(tree, 92)));

    }

    private static <T> void populateParent(GraphNode<T> node) {
        Queue<GraphNode<T>> q = new LinkedList<>();
        q.add(node);
        while (!q.isEmpty()) {
            GraphNode<T> pop = q.remove();
            for (GraphNode<T> c : pop.children) {
                c.parent = pop;
                q.add(c);
            }
        }
    }

    private static GraphNode<Integer> nextNode(GraphNode<Integer> node) {
        if (node.getRight() != null) {
            GraphNode<Integer> leftmost = node.getRight();
            while (leftmost.getLeft() != null) {
                leftmost = leftmost.getLeft();
            }
            return leftmost;
        }
        while (node.parent != null) {
            if (node.parent.getRight() == node) {
                node = node.parent;
            } else {
                break;
            }
        }
        return node.parent;
    }

    private static GraphNode<Integer> find(GraphNode<Integer> root, int needle) {
        if (needle == root.value) {
            return root;
        }
        GraphNode<Integer> left = root.getLeft();
        if (needle < root.value) {
            return left == null ? root : find(left, needle);
        }
        GraphNode<Integer> right = root.getRight();
        return right == null ? root : find(right, needle);
    }

    @Test
    /**
     * Build Order: You are given a list of projects and a list of dependencies
     * (which is a list of pairs of projects, where the second project is dependent
     * on the first project). All of a project's dependencies must be built before
     * the project is. Find a build order that will allow the projects to be built.
     * If there is no valid build order, return an error.
     * 
     * EXAMPLE
     * 
     * <pre>
     * Input: projects: a, b, c, d, e, f 
     * dependencies: (a, d), (f, b), (b, d), (f, a), (d, c) 
     * Output: f, e, a, b, d, c
     * </pre>
     */
    public void testBuildOrder() throws Exception {
        Map<String, GraphNode<String>> all = asList("a", "b", "c", "d", "e", "f").stream().map(x -> node(x)).collect(
                toMap(n -> n.value, n -> n));

        all.get("d").addChild(all.get("a"));
        all.get("b").addChild(all.get("f"));
        all.get("d").addChild(all.get("b"));
        all.get("a").addChild(all.get("f"));
        //        all.get("f").addChild(all.get("a"));
        all.get("c").addChild(all.get("d"));

        GraphNode<String> root = new GraphNode<>("PROJECT");
        all.forEach((x, n) -> root.addChild(n));

        List<String> dfs = dfs(root);

        assertEquals(Arrays.asList("f", "e", "b", "a", "d", "c", "PROJECT"), dfs);
    }

    static enum State {
        IN_PROGRESS, BUILT;
    }

    static List<String> dfs(GraphNode<String> root) {
        List<String> buildOrder = new ArrayList<>();
        LinkedList<GraphNode<String>> stack = new LinkedList<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            GraphNode<String> pop = stack.pop();
            System.out.println(": " + pop);
            if (pop.visited)
                continue;
            if (pop.children.isEmpty()) {
                buildOrder.add(pop.value);
                //                System.out.println("build " + pop);
                pop.visited = true;
            } else if (!pop.children.stream().filter(x -> !x.visited).findFirst().isPresent()) {
                buildOrder.add(pop.value);
                //                System.out.println("all children of " + pop + " built " + pop.children);
                pop.visited = true;
            } else {
                stack.push(pop);
                for (GraphNode<String> c : pop.children) {
                    if (!c.visited) {
                        System.out.println("push " + c);
                        stack.push(c);
                    }
                }
            }
        }
        return buildOrder;
    }

    static void dfsRec(GraphNode<String> root) {
        System.out.println(": " + root);
        if (root.children.isEmpty()) {
            System.out.println("build " + root);
            root.visited = true;
            return;
        }
        for (GraphNode<String> c : root.children) {
            if (!c.visited) {
                dfs(c);
            }
        }
        System.out.println("children of " + root + " built " + root.children);
        root.visited = true;
    }

    @Test
    /**
     * First Common Ancestor: Design an algorithm and write code to find the first
     * common ancestor of two nodes in a binary tree. Avoid storing additional nodes
     * in a data structure. NOTE: This is not necessarily a binary search tree.
     */
    public void testFirstCommonAncestor() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        GraphNode<Integer> tree = binarySearchTree(array);
        populateParent(tree);
        assertEquals(find(tree, 59), commonAncestor(tree, find(tree, 16), find(tree, 67)));
        System.out.println();
        assertEquals(find(tree, 84), commonAncestor(tree, find(tree, 92), find(tree, 66)));
        assertEquals(find(tree, 19), commonAncestor(tree, find(tree, 8), find(tree, 21)));
    }

    private static <T> GraphNode<T> commonAncestor(GraphNode<T> root, GraphNode<T> node1, GraphNode<T> node2) {
        if (root == null)
            return null;
        if (root == node1 && root == node2) {
            return root;
        }

        GraphNode<T> left = commonAncestor(root.getLeft(), node1, node2);
        if (left != null && left != node1 && left != node2) {
            return left;
        }
        GraphNode<T> right = commonAncestor(root.getRight(), node1, node2);
        if (right != null && right != node1 && right != node2) {
            return right;
        }

        if (left != null && right != null)
            return root;
        if (root == node1 || root == node2)
            return root;
        return left == null ? right : left;
    }

    @Test
    /**
     * BST Sequences: A binary search tree was created by traversing through an
     * array from left to right and inserting each element. Given a binary search
     * tree with distinct elements, print all possible arrays that could have led to
     * this tree.
     * 
     * EXAMPLE 1 <- 2 -> 3
     * 
     * Input:
     * 
     * Output: {2, 1, 3}, {2, 3, 1}
     */
    public void testBSTSequences() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        GraphNode<Integer> tree = binarySearchTree(array);
        List<List<Integer>> bfs = bfs(tree);
        System.out.println(bfs);

        Assert.fail("TODO");

    }

    private static List<List<Integer>> bfs(GraphNode<Integer> tree) {
        if (tree == null)
            return asList(emptyList());
        List<Integer> prefix = asList(tree.value);
        if (tree.children.isEmpty()) {
            return asList(prefix);
        }

        List<List<Integer>> left = bfs(tree.getLeft());
        List<List<Integer>> right = bfs(tree.getRight());

        List<List<Integer>> output = new ArrayList<>();
        output.add(prefix);

        return output;
    }

    @Test
    /**
     * Check Subtree: T1 and T2 are two very large binary trees, with T1 much bigger
     * than T2. Create an algorithm to determine if T2 is a subtree of T1. A tree T2
     * is a subtree of T1 if there exists a node n in T1 such that the subtree of n
     * is identical to T2. That is, if you cut off the tree at node n, the two trees
     * would be identical.
     */
    public void testCheckSubtree() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(20).sorted().toArray();
        GraphNode<Integer> tree1 = binarySearchTree(array);
        printLayers(tree1);
        GraphNode<Integer> tree2 = binarySearchTree(array);
        tree2 = find(tree2, 47);
        assertTrue(isSubtree(tree1, tree2));
    }

    @Test
    /**
     * Random Node: You are implementing a binary tree class from scratch which, in
     * addition to insert, find, and delete, has a method getRandomNode() which
     * returns a random node from the tree. All nodes should be equally likely to be
     * chosen. Design and implement an algorithm for getRandomNode, and explain how
     * you would implement the rest of the methods.
     */
    public void testRandomNode() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        GraphNode<Integer> tree1 = binarySearchTree(array);
        GraphNode<Integer> atIndex = getAtIndex(tree1, rnd.nextInt(10));
        System.out.println(atIndex);
        assertEquals(21, (int) atIndex.value);
    }

    static <T> GraphNode<T> getAtIndex(GraphNode<T> tree, int idx) {
        LinkedList<GraphNode<T>> q = new LinkedList<>();
        q.add(tree);
        int remaining = idx;
        while (!q.isEmpty()) {
            GraphNode<T> node = q.remove();
            remaining--;
            if (remaining < 0) {
                return node;
            }
            for (GraphNode<T> c : node.children) {
                q.add(c);
            }
        }

        return tree;
    }

    @Test
    /**
     * Paths with Sum: You are given a binary tree in which each node contains an
     * integer value (which might be positive or negative). Design an algorithm to
     * count the number of paths that sum to a given value. The path does not need
     * to start or end at the root or a leaf, but it must go downwards (traveling
     * only from parent nodes to child nodes).
     */
    public void testPathsWithSum() throws Exception {
        Assert.fail("TODO");
    }

    private static boolean isSubtree(GraphNode<Integer> tree1, GraphNode<Integer> tree2) {
        if (tree1 == null)
            return false;
        if (tree1.value.equals(tree2.value) && matchContent(tree1, tree2)) {
            return true;
        }
        return isSubtree(tree1.getLeft(), tree2) || isSubtree(tree1.getRight(), tree2);
    }

    private static boolean matchContent(GraphNode<Integer> tree1, GraphNode<Integer> tree2) {
        if (tree1 == null && tree2 != null)
            return false;
        if (tree2 == null && tree1 != null)
            return false;
        if (tree1 == null && tree2 == null)
            return true;

        if (!tree1.value.equals(tree2.value)) {
            return false;
        }
        return matchContent(tree1.getLeft(), tree2.getLeft()) && matchContent(tree1.getRight(), tree2.getRight());
    }

    @Test
    //Route Between Nodes: Given a directed graph, design an algorithm to  find out whether there is a route between two nodes.
    public void testSearch() throws Exception {
        Map<String, GraphNode<String>> all = range('a', 'l').mapToObj(x -> node("" + (char) x)).collect(
                toMap(n -> n.value, n -> n));
        GraphNode<String> a = all.get("a");
        GraphNode<String> b = all.get("b");
        GraphNode<String> c = all.get("c");
        GraphNode<String> d = all.get("d");
        GraphNode<String> e = all.get("e");
        GraphNode<String> f = all.get("f");
        GraphNode<String> g = all.get("g");
        GraphNode<String> h = all.get("h");
        GraphNode<String> i = all.get("i");
        GraphNode<String> j = all.get("j");
        GraphNode<String> k = all.get("k");

        c.addChild(a, b, d, e, f);
        e.addChild(g);
        g.addChild(h, k, j);
        k.addChild(i, g, h, j);

        assertTrue(hasPath(c, k));
        all.forEach((key, n) -> n.visited = false);

        assertFalse(hasPath(k, c));
    }

    private static <T> boolean hasPath(GraphNode<T> a, GraphNode<T> b) {
        Queue<GraphNode<T>> q = new LinkedList<>();
        q.add(a);
        while (!q.isEmpty()) {
            GraphNode<T> n = q.remove();
            n.visited = true;
            if (n == b) {
                return true;
            }
            for (GraphNode<T> c : n.children) {
                if (!c.visited) {
                    c.visited = true;
                    q.add(c);
                }
            }
        }
        return false;
    }

    private static GraphNode<Integer> binarySearchTree(int[] sortedArray) {
        return binarySearchTree(sortedArray, 0, sortedArray.length);
    }

    private static GraphNode<Integer> binarySearchTree(int[] sortedArray, int off, int len) {
        GraphNode<Integer> root = node(sortedArray[off + len / 2]);
        if (len == 1) {
            return root;
        } else if (len == 2) {
            root.addChild(node(sortedArray[off]));
            return root;
        }

        int even = (len ^ 1) & 0x1;
        GraphNode<Integer> smaller = binarySearchTree(sortedArray, off, len / 2);
        root.addChild(smaller);
        GraphNode<Integer> larger = binarySearchTree(sortedArray, off + len / 2 + 1, len / 2 - even);
        root.addChild(larger);

        return root;
    }

    static <T> void print(GraphNode<T> g) {
        bfs(g, n -> {
            System.out.println(n + " " + n.children);
            return false;
        });
    }

    static <T> GraphNode<T> bfs(GraphNode<T> g, Predicate<GraphNode<T>> func) {
        Set<GraphNode<T>> visited = new HashSet<>();
        Queue<GraphNode<T>> q = new LinkedList<>();
        q.add(g);
        while (!q.isEmpty()) {
            GraphNode<T> n = q.remove();
            visited.add(n);
            if (func.test(n)) {
                return n;
            }
            for (GraphNode<T> c : n.children) {
                if (!visited.contains(c)) {
                    visited.add(c);
                    q.add(c);
                }
            }
        }
        return null;
    }

    @Test
    //Minimal Tree: Given a sorted (increasing order) array with unique integer elements, write an algo­rithm to create a binary search tree with minimal height.
    public void testMinimalTree() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        GraphNode<Integer> tree = binarySearchTree(array);
        System.out.println(tree);
        printLayers(tree);
    }

    @Test
    /**
     * List of Depths: Given a binary tree, design an algorithm which creates a
     * linked list of all the nodes at each depth (e.g., if you have a tree with
     * depth D, you'll have D linked lists).
     */
    public void testListOfDepths() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        GraphNode<Integer> tree = binarySearchTree(array);
        List<int[]> layers = allLayers(tree).stream().map(x -> x.stream().mapToInt(n -> n.value).toArray()).collect(
                toList());
        assertArrayEquals(new int[] { 59 }, layers.get(0));
        assertArrayEquals(new int[] { 19, 84 }, layers.get(1));
        assertArrayEquals(new int[] { 16, 47, 67, 92 }, layers.get(2));
        assertArrayEquals(new int[] { 8, 21, 66 }, layers.get(3));
    }

    private static <T> void printLayers(GraphNode<T> tree) {
        printLayers(Arrays.asList(tree));
    }

    private static <T> void printLayers(List<GraphNode<T>> layer) {
        if (layer.isEmpty())
            return;
        System.out.println(layer);
        List<GraphNode<T>> list = new ArrayList<>();
        for (GraphNode<T> graphNode : layer) {
            list.addAll(graphNode.children);
        }
        if (list.isEmpty())
            return;
        printLayers(list);
    }

    private static <T> List<List<GraphNode<T>>> allLayers(GraphNode<T> tree) {
        List<List<GraphNode<T>>> layers = new ArrayList<>();
        allLayers(Arrays.asList(tree), layers);
        return layers;
    }

    private static <T> void allLayers(List<GraphNode<T>> layer, List<List<GraphNode<T>>> layers) {
        if (layer.isEmpty())
            return;
        layers.add(layer);
        List<GraphNode<T>> list = new ArrayList<>();
        for (GraphNode<T> graphNode : layer) {
            list.addAll(graphNode.children);
        }
        if (list.isEmpty())
            return;
        allLayers(list, layers);
    }

    @Test
    /**
     * Check Balanced: Implement a function to check if a binary tree is balanced.
     * For the purposes of this question, a balanced tree is defined to be a tree
     * such that the heights of the two subtrees of any node never differ by more
     * than one.
     */
    public void testCheckBalanced() throws Exception {
        Random rnd = new Random(4342);
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).distinct().limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        GraphNode<Integer> tree = binarySearchTree(array);
        int height = height(tree);
        assertEquals(4, height);
        assertTrue(isBalanced(tree));

        GraphNode<String> a = node("a");
        GraphNode<String> b = node("b");
        GraphNode<String> c = node("c");
        GraphNode<String> d = node("d");
        GraphNode<String> e = node("e");
        b.addChild(a, c);
        c.addChild(d);
        d.addChild(e);
        assertFalse(isBalanced(b));
    }

    static <T> boolean isBalanced(GraphNode<T> tree) {
        return Math.abs(height(tree.getLeft()) - height(tree.getRight())) <= 1;
    }

    static <T> int height(GraphNode<T> root) {
        if (root == null)
            return 0;
        if (root.children.isEmpty())
            return 1;
        int maxHeight = 0;
        for (GraphNode<T> c : root.children) {
            maxHeight = Math.max(maxHeight, height(c));
        }
        return 1 + maxHeight;
    }

}
