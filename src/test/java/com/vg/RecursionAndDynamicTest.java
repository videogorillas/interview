package com.vg;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class RecursionAndDynamicTest {

    @Test
    /**
     * Boolean Evaluation: Given a boolean expression consisting of the symbols 0
     * (false), 1 (true), & (AND), | (OR), and ^ (XOR), and a desired boolean result
     * value result, implement a function to count the number of ways of
     * parenthesizing the expression such that it evaluates to result. The
     * expression should be fully parenthesized (e.g.,(0)^(1)) but not
     * extraneously(e.g.,(((0))^(1))).
     * 
     * EXAMPLE
     * 
     * countEval("1^0|0|1", false) -> 2
     * 
     * countEval("0&0&0&1^1|0", true) -> 10
     */
    public void testBooleanEval() throws Exception {
        Assert.fail("TODO");
    }

    @Test
    /**
     * Stack of Boxes: You have a stack of n boxes, with widths wi, heights hi, and
     * depths di. The boxes cannot be rotated and can only be stacked on top of one
     * another if each box in the stack is strictly larger than the box above it in
     * width, height, and depth. Implement a method to compute the height of the
     * tallest possible stack. The height of a stack is the sum of the heights of
     * each box.
     */
    public void testStackOfBoxes() throws Exception {
        Assert.fail("TODO");
    }

    private static final int GRID_SIZE = 8;

    @Test
    /**
     * Eight Queens: Write an algorithm to print all ways of arranging eight queens
     * on an 8x8 chess board so that none of them share the same row, column, or
     * diagonal. In this case, "diagonal" means all diagonals, not just the two that
     * bisect the board.
     */
    public void test8Queens() throws Exception {
        ArrayList<int[]> results = new ArrayList<>();
        placeQueens(0, new int[8], results);
        for (int[] is : results) {
            System.out.println(Arrays.toString(is));
        }

    }

    static void placeQueens(int row, int[] columns, ArrayList<int[]> results) {
        if (row == GRID_SIZE) {//Found valid placement
            results.add(Arrays.copyOf(columns, columns.length));
        } else {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (checkValid(columns, row, col)) {
                    columns[row] = col; // Place queen
                    placeQueens(row + 1, columns, results);
                }
            }
        }
    }

    private static boolean checkValid(int[] columns, int row1, int column1) {
        for (int row2 = 0; row2 < row1; row2++) {
            int column2 = columns[row2];
            if (column1 == column2) {
                return false;
            }
            int columnDistance = Math.abs(column2 - column1);
            int rowDistance = row1 - row2;
            if (columnDistance == rowDistance) {
                return false;
            }
        }
        return true;
    }

    @Test
    /**
     * Coins: Given an infinite number of quarters (25 cents), dimes (10 cents),
     * nickels (5 cents), and pennies (1 cent), write code to calculate the number
     * of ways of representing n cents.
     */
    public void testCoins() throws Exception {
        assertEquals(13, coins(5, new int[] { 1, 2, 3 }));
    }

    static int coins(int remaining, int[] denom) {
        int count = 0;
        for (int coin : denom) {
            count += coins(coin, remaining, denom);
        }
        return count;
    }

    static int coins(int jump, int remaining, int[] denom) {
        if (jump == remaining) {
            return 1;
        } else if (remaining - jump < 0) {
            return 0;
        }
        remaining -= jump;
        int count = 0;
        for (int coin : denom) {
            count += coins(coin, remaining, denom);
        }
        return count;
    }

    @Test
    /**
     * Paint Fill: Implement the "paint fill" function that one might see on many
     * image editing programs. That is, given a screen (represented by a
     * two-dimensional array of colors), a point, and a new color, fill in the
     * surrounding area until the color changes from the original color.
     */
    public void testPaintFill() throws Exception {
        int[][] image = new int[][] { //
                { 1, 2, 1, 2, 3 }, //
                { 2, 1, 1, 2, 3 }, //
                { 2, 1, 1, 1, 3 }, //
                { 1, 2, 1, 2, 3 }, //
                { 1, 1, 1, 2, 3 }, };
        int[][] expected = new int[][] { //
                { 1, 2, 0, 2, 3 }, //
                { 2, 0, 0, 2, 3 }, //
                { 2, 0, 0, 0, 3 }, //
                { 0, 2, 0, 2, 3 }, //
                { 0, 0, 0, 2, 3 }, };
        paintFill(image, 1, 2, 1, 0);
        assertArrayEquals(expected, image);

    }

    private static void paintFill(int[][] image, int r, int c, int fromColor, int color) {
        if (r < 0 || c < 0 || r >= image.length || c >= image[0].length || image[r][c] == color
                || image[r][c] != fromColor) {
            return;
        }
        image[r][c] = color;
        paintFill(image, r - 1, c, fromColor, color);
        paintFill(image, r + 1, c, fromColor, color);
        paintFill(image, r, c - 1, fromColor, color);
        paintFill(image, r, c + 1, fromColor, color);
    }

    @Test
    /**
     * Parens: Implement an algorithm to print all valid (i.e., properly opened and
     * closed) combinations of n pairs of parentheses.
     * 
     * EXAMPLE
     * 
     * Input: 3 Output: ((())), (()()), (())(), ()(()), ()()()
     */
    public void testParens() throws Exception {
        List<String> expected = asList("((()))", "(()())", "(())()", "()(())", "()()()");
        Collection<String> parens = parens(3);
        System.out.println(parens);
        assertEquals(expected.size(), parens.size());
        assertTrue(expected.containsAll(parens));
        assertTrue(parens.containsAll(expected));
    }

    private static Collection<String> parens(int n) {
        if (n == 1) {
            return asList("()");
        }
        if (n == 2) {
            return asList("(())", "()()");
        }
        LinkedHashSet<String> list = new LinkedHashSet<>();
        Collection<String> parens = parens(n - 1);
        for (String child : parens) {
            list.add("(" + child + ")");
            list.add(child + "()");
            list.add("()" + child);
        }
        return list;
    }

    @Test
    /**
     * Permutations with Dups: Write a method to compute all permutations of a
     * string whose charac­ ters are not necessarily unique. The list of
     * permutations should not have duplicates.
     */
    public void testPermWithDups() throws Exception {
        assertEquals("[a]", permsDups("a").toString());
        assertEquals("[a]", permsDups("aa").toString());
        assertEquals("[abc, acb, bac, bca, cab, cba]", permsDups("abc").toString());
        assertEquals("[ac, ca]", permsDups("aac").toString());
    }

    private static List<String> permsDups(String p) {
        LinkedHashSet<Character> chars = new LinkedHashSet<>();
        for (int i = 0; i < p.length(); i++) {
            chars.add(p.charAt(i));
        }
        String collect = chars.stream().map(x -> "" + x).collect(joining());
        return permsNoDups(collect);
    }

    @Test
    /**
     * Permutations without Dups: Write a method to compute all permutations of a
     * string of unique characters.
     */
    public void testPermutations() throws Exception {
        assertEquals("[a]", permsNoDups("a").toString());
        assertEquals("[ab, ba]", permsNoDups("ab").toString());
        assertEquals("[abc, acb, bac, bca, cab, cba]", permsNoDups("abc").toString());
        System.out.println(permsNoDups("abcd"));
    }

    static List<String> permsNoDups(String p) {
        if (p.length() == 1)
            return asList(p);
        if (p.length() == 2) {
            return asList(p.charAt(0) + "" + p.charAt(1), p.charAt(1) + "" + p.charAt(0));
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < p.length(); i++) {
            String head = p.substring(i, i + 1);
            String tail = new StringBuilder(p).deleteCharAt(i).toString();
            List<String> permsNoDups = permsNoDups(tail);
            for (String perm : permsNoDups) {
                list.add(head + perm);
            }
        }

        return list;
    }

    @Test
    /**
     * Towers of Hanoi: In the classic problem of the Towers of Hanoi, you have 3
     * towers and N disks of different sizes which can slide onto any tower. The
     * puzzle starts with disks sorted in ascending order of size from top to bottom
     * (i.e., each disk sits on top of an even larger one).You have the following
     * constraints:
     * 
     * (1) Only one disk can be moved at a time.
     * 
     * (2) A disk is slid off the top of one tower onto another tower.
     * 
     * (3) A disk cannot be placed on top of a smaller disk.
     * 
     * Write a program to move the disks from the first tower to the last using
     * stacks.
     */
    public void testTowersOfHanoi() throws Exception {
        LinkedList<Integer> src = new LinkedList<>(asList(1, 2));
        LinkedList<Integer> tmp = new LinkedList<>();
        LinkedList<Integer> dst = new LinkedList<>();
        Assert.fail("TODO");
    }

    private static void movePiece(LinkedList<Integer> src, LinkedList<Integer> dst) {
        Integer pop = src.pop();
        if (!dst.isEmpty()) {
            if (dst.peek() < pop) {
                throw new IllegalStateException("BUG " + dst.peek() + " < " + pop);
            }
        }
        dst.push(pop);
    }

    @Test
    /**
     * Recursive Multiply: Write a recursive function to multiply two positive
     * integers without using the * operator. You can use addition, subtraction, and
     * bit shifting, but you should minimize the number of those operations.
     */
    public void testRecursiveMultiply() throws Exception {
        assertEquals(6, mulRec(2, 3));
        assertEquals(55, mulRec(11, 5));
        assertEquals(16, mulRec(4, 4));
    }

    static boolean isPowerOfTwo(int n) {
        return (n & (n - 1)) == 0;
    }

    private static int mulRec(int a, int b) {
        int smaller = a;
        int larger = b;
        if (a > b) {
            larger = a;
            smaller = b;
        }
        return _mulRec(smaller, larger);
    }

    private static int _mulRec(int smaller, int larger) {
        if (smaller == 0 || larger == 0)
            return 0;
        if (smaller < 0 || larger < 0)
            return -1;
        if (smaller == 1)
            return larger;
        if (larger == 1)
            return smaller;

        //if power of two?

        int s = smaller >> 1;
        int mul = _mulRec(s, larger);
        int extra = (smaller & 1) == 0 ? 0 : larger;
        return mul + mul + extra;
    }

    @Test
    /**
     * Triple Step: A child is running up a staircase with n steps and can hop
     * either 1 step, 2 steps, or 3 steps at a time. Implement a method to count how
     * many possible ways the child can run up the stairs.
     */
    public void testTripleStep() throws Exception {
        assertEquals(13, countWays(5));
    }

    static int countWays(int remaining) {
        return countWays(1, remaining) + countWays(2, remaining) + countWays(3, remaining);
    }

    static int countWays(int jump, int remaining) {
        if (jump == remaining) {
            return 1;
        } else if (remaining - jump < 0) {
            return 0;
        }
        remaining -= jump;
        return countWays(1, remaining) + countWays(2, remaining) + countWays(3, remaining);
    }

    @Test
    /**
     * Robot in a Grid: Imagine a robot sitting on the upper left corner of grid
     * with r rows and c columns. The robot can only move in two directions, right
     * and down, but certain cells are "off limits" such that the robot cannot step
     * on them. Design an algorithm to find a path for the robot from the top left
     * to the bottom right.
     */
    public void testRobot() throws Exception {
        Assert.fail("TODO");
    }

    @Test
    /**
     * 
     * Magic Index: A magic index in an array A[0...n -1] is defined to be an index
     * such that A[i] = i. Given a sorted array of distinct integers, write a method
     * to find a magic index, if one exists, in array A.
     * 
     * FOLLOW UP
     * 
     * What if the values are not distinct?
     */
    public void testMagicIndex() throws Exception {
        assertEquals(3, magicIndex(new int[] { -1, 0, 1, 3, 5, 7, 8, 10, 12, 15 }));
        assertEquals(7, magicIndex(new int[] { -3, -2, -1, 1, 2, 3, 5, 7, 12, 15 }));

    }

    private static int magicIndex(int[] array) {
        return magicIndex(array.length - 1, array);
    }

    private static int magicIndex(int idx, int[] array) {
        System.out.println(idx + " " + array[idx]);
        if (array[idx] == idx)
            return idx;

        return magicIndex(idx - (array[idx] - idx), array);
    }

    @Test
    //Power Set: Write a method to return all subsets of a set.
    public void testPowerSet() throws Exception {
        List<List<Integer>> powerset = powerSet(Arrays.asList(1, 2, 3));
        assertEquals("[[1, 2, 3], [1, 2], [1, 3], [1], [2, 3], [2], [3], []]", powerset.toString());
    }

    private static List<List<Integer>> powerSet(List<Integer> list) {
        return powerSet(list, 0);
    }

    private static List<List<Integer>> powerSet(List<Integer> list, int start) {
        if (start == list.size()) {
            return asList(emptyList());
        }
        List<List<Integer>> powerset = new ArrayList<>();
        Integer head = list.get(start);
        List<List<Integer>> smaller = powerSet(list, start + 1);
        for (List<Integer> small : smaller) {
            List<Integer> x = new ArrayList<>();
            x.add(head);
            x.addAll(small);
            powerset.add(x);
        }
        powerset.addAll(smaller);
        return powerset;
    }

}
