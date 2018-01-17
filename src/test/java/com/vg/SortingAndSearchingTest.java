package com.vg;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;

public class SortingAndSearchingTest {

    @Test
    /**
     * Sorted Matrix Search: Given an M x N matrix in which each row and each column
     * is sorted in ascending order, write a method to find an element.
     */
    public void testSortedMatrixSearch() throws Exception {
        int[][] matrix = new int[][] { //
                { 15, 20, 40, 85 }, //
                { 20, 35, 80, 95 }, //
                { 30, 55, 95, 105 }, //
                { 40, 80, 100, 120 }//
        };
        assertEquals(new Point(1, 1), matrixSearch(matrix, 35));
        assertEquals(new Point(2, 1), matrixSearch(matrix, 80));
        assertEquals(new Point(3, 3), matrixSearch(matrix, 120));
    }

    private static Point matrixSearch(int[][] matrix, int key) {
        int binarySearch = Arrays.binarySearch(matrix[0], key);
        int col = -binarySearch - 2;
        int row = binarySearch(matrix, col, key);
        return new Point(col, row);
    }

    private static int binarySearch(int[][] matrix, int col, int key) {
        int low = 0;
        int high = matrix.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = matrix[mid][col];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1); // key not found.
    }

    @Test
    /**
     * Find Duplicates: You have an array with all the numbers from 1 to N, where N
     * is at most 32,000. The array may have duplicate entries and you do not know
     * what N is. With only 4 kilobytes of memory available, how would you print all
     * duplicate elements in the array?
     */
    public void testFindDups() throws Exception {
        BitSet bs = new BitSet(32000);
        IntStream.generate(() -> rnd.nextInt(32000)).limit(1000).forEach(x -> {
            boolean b = bs.get(x);
            if (!b) {
                bs.set(x);
            } else {
                System.out.println("dup: " + x);
            }
        });
    }

    @Test
    /**
     * Missing Int: Given an input file with four billion non-negative integers,
     * provide an algorithm to generate an integer that is not contained in the
     * file. Assume you have 1 GB of memory available for this task.
     * 
     * FOLLOW UP
     * 
     * What if you have only 1O MB of memory? Assume that all the values are
     * distinct and we now have no more than one billion non-negative integers.
     */
    public void testMissingInt() throws Exception {
        //4B uints == 2B uniq ints
        //bitset 2B/8 == 256MB
        //1B uniq uints 
        //bitset 1B/8 == 128MB

        //10MB case
        //blocks from 0-999 1000-1999

    }

    @Test
    /**
     * Sort Big File: Imagine you have a 20 GB file with one string per line.
     * Explain how you would sort the file.
     */
    public void testSortBigFile() throws Exception {
        //chunk file
        //sort chunks
        //merge chunks
    }

    @Test
    /**
     * Sparse Search: Given a sorted array of strings that is interspersed with
     * empty strings, write a method to nd the location of a given string.
     * 
     * EXAMPLE
     * 
     * Input: ball, {"at","", "", "", "ball", "", "", "car", "", "", "dad", "", ""}
     * 
     * Output: 4
     */
    public void testSparseSearch() throws Exception {
        String[] array = new String[] { "at", "", "", "", "ball", "", "", "car", "", "", "dad", "", "" };
        assertEquals(10, sparseSearch(array, "dad"));
    }

    private static int sparseSearch(String[] array, String key) {
        int low = 0;
        int high = array.length;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            String midVal = array[mid];

            while (midVal.equals("")) {
                midVal = array[mid--];
            }

            if (midVal.compareTo(key) < 0)
                low = mid + 1;
            else if (midVal.compareTo(key) > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1); // key not found.
    }

    static class Listy {
        int[] array;

        public Listy(int[] a) {
            this.array = a;
        }

        int elementAt(int idx) {
            return idx >= array.length ? -1 : array[idx];
        }
    }

    Random rnd = new Random(4243);

    @Test
    /**
     * Sorted Search, No Size: You are given an array like data structure Listy
     * which lacks a size method. It does, however, have an elementAt(i) method that
     * returns the element at index i in 0( 1) time. If i is beyond the bounds of
     * the data structure, it returns -1. (For this reason, the data structure only
     * supports positive integers.) Given a Listy which contains sorted, positive
     * integers, find the index at which an element x occurs. If x occurs multiple
     * times, you may return any index.
     */
    public void testSortedSearchNoSize() throws Exception {
        int[] array = IntStream.generate(() -> rnd.nextInt(100)).limit(10).sorted().toArray();
        System.out.println(Arrays.toString(array));
        Listy listy = new Listy(array);
        int findSize = findSize(listy, 0, Integer.MAX_VALUE);
        assertEquals(10, findSize);
        int idx = search(listy, 94);
        assertEquals(8, idx);

    }

    private static int binarySearch(Listy a, int fromIndex, int toIndex, int key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a.elementAt(mid);

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1); // key not found.
    }

    private static int search(Listy listy, int key) {
        int size = findSize(listy, 0, Integer.MAX_VALUE);
        return binarySearch(listy, 0, size, key);
    }

    private static int findSize(Listy listy, int start, int end) {
        int idx = 0;
        int i = 0;
        do {
            idx = idx == 0 ? 1 : idx << 1;
            i = listy.elementAt(start + idx);
        } while (i >= 0 && i < end);
        if (i == -1) {
            return findSize(listy, start + (idx >> 1), start + idx);
        }

        return start + idx + 1;
    }

    @Test
    /**
     * Search in Rotated Array: Given a sorted array of n integers that has been
     * rotated an unknown number of times, write code to find an element in the
     * array. You may assume that the array was originally sorted in increasing
     * order.
     * 
     * EXAMPLE
     * 
     * Input: find 5 in {15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14}
     * 
     * Output: 8 (the index of 5 in the array)
     */
    public void testSearchInRotated() throws Exception {
        int[] array = new int[] { 15, 16, 19, 20, 25, 1, 3, 4, 5, 7, 10, 14 };
        assertEquals(8, searchInRotated(array, 5));

    }

    static int searchInRotated(int[] array, int key) {
        int pivot = findPivot(array);
        int idx1 = Arrays.binarySearch(array, 0, pivot, key);
        int idx2 = Arrays.binarySearch(array, pivot, array.length, key);
        return idx1 >= 0 ? idx1 : idx2;
    }

    private static int findPivot(int[] array) {
        int prev = array[0];
        for (int i = 1; i < array.length; i++) {
            if (prev > array[i]) {
                return i;
            }
        }
        return 0;
    }

    @Test
    /**
     * Group Anagrams: Write a method to sort an array of strings so that all the
     * anagrams are next to each other.
     */
    public void testAnagrams() throws Exception {
        String[] array = { "ghi", "baa", "cba", "aab", "bca", "def" };
        String[] expected = { "baa", "aab", "cba", "bca", "def", "ghi" };
        assertArrayEquals(expected, sortAnagrams(array));
    }

    private static String[] sortAnagrams(String[] array) {
        Arrays.sort(array, (s1, s2) -> {
            char[] c1 = s1.toCharArray();
            char[] c2 = s2.toCharArray();
            Arrays.sort(c1);
            Arrays.sort(c2);
            boolean isAnagram = Arrays.equals(c1, c2);
            return isAnagram ? 0 : s1.compareTo(s2);
        });
        return array;
    }

    @Test
    /**
     * Sorted Merge: You are given two sorted arrays, A and B, where A has a large
     * enough buffer at the end to hold B. Write a method to merge B into A in
     * sorted order.
     */
    public void testSortedMerge() throws Exception {
        int[] a = new int[] { 2, 6, 8, 9, 0, 0, 0 };
        int[] b = new int[] { 1, 3, 7 };
        int[] c = mergeSorted(a, b);
        assertArrayEquals(new int[] { 1, 2, 3, 6, 7, 8, 9 }, c);

        a = new int[] { 2, 2, 8, 9, 0, 0, 0 };
        b = new int[] { 1, 3, 7 };
        c = mergeSorted(a, b);
        assertArrayEquals(new int[] { 1, 2, 2, 3, 7, 8, 9 }, c);

    }

    private static int[] mergeSorted(int[] a, int[] b) {
        int start = 0;
        int end = a.length - b.length;
        for (int i = 0; i < b.length; i++) {
            int key = b[i];
            int idx = Arrays.binarySearch(a, start, end, key);
            int ins = idx >= 0 ? idx : -idx - 1;
            System.arraycopy(a, ins, a, ins + 1, end - ins);
            end++;
            start = ins + 1;
            a[ins] = key;
        }
        return a;
    }

}
