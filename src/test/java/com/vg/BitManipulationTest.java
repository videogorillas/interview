package com.vg;

import static java.lang.Integer.toBinaryString;
import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Random;

import org.junit.Test;

public class BitManipulationTest {

    /**
     * Next Number: Given a positive integer, print the next smallest and the next
     * largest number that have the same number of 1 bits in their binary
     * representation.
     */
    @Test
    public void testNextNumber() throws Exception {
        assertEquals(9, nextLargest(6));
        assertEquals(5, nextSmallest(6));
        Random rnd = new Random(4243);
        for (int i = 0; i < 10; i++) {
            int i1 = rnd.nextInt(255);
            System.out.printf("%3d\t%8.8s %8.8s %8.8s\n", i1, toBinaryString(i1), toBinaryString(nextSmallest(i1)),
                    toBinaryString(nextLargest(i1)));
        }
    }

    static int nextLargest(int n) {
        int expected = Integer.bitCount(n);

        for (int i = n + 1; i < Integer.MAX_VALUE; i++) {
            int bitCount = Integer.bitCount(i);
            if (bitCount == expected) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    static int nextSmallest(int n) {
        int expected = Integer.bitCount(n);

        for (int i = n - 1; i >= 0; i--) {
            int bitCount = Integer.bitCount(i);
            if (bitCount == expected) {
                return i;
            }
        }
        return 0;
    }

    @Test
    //Debugger: Explain what the following code does: ((n & (n-1)) == 0).
    public void testDebugger() throws Exception {
        assertTrue(isSingleBitSet(2));
        assertTrue(isSingleBitSet(32));
        assertFalse(isSingleBitSet(31));
        assertFalse(isSingleBitSet(5));
    }

    static boolean isSingleBitSet(int n) {
        return (n & (n - 1)) == 0;
    }

    @Test
    /**
     * Conversion: Write a function to determine the number of bits you would need
     * to flip to convert integer A to integer B.
     * 
     * EXAMPLE
     * 
     * <pre>
     * Input: 29 (or: 11101), 15 (or: 01111) 
     * Output: 2
     * </pre>
     */
    public void testConversion() throws Exception {
        assertEquals(2, flipCount(29, 15));
        assertEquals(4, flipCount(14, 1));
    }

    static int flipCount(int a, int b) {
        return Integer.bitCount(a ^ b);
    }

    @Test
    /**
     * Pairwise Swap: Write a program to swap odd and even bits in an integer with
     * as few instructions as possible (e.g., bit 0 and bit 1 are swapped, bit 2 and
     * bit 3 are swapped, and so on).
     */
    public void testPairwiseSwap() throws Exception {
        assertEquals(b("1001"), pairwiseSwap(b("0110")));
        assertEquals(b("1100"), pairwiseSwap(b("1100")));

    }

    private static int b(String string) {
        return Integer.parseInt(string, 2);
    }

    static int pairwiseSwap(int n) {
        int a = (n << 1) & 0xaaaaaaaa;
        int b = (n >>> 1) & 0x55555555;
        return a | b;
    }

    private static String b(int a) {
        return toBinaryString(a);
    }

    @Test
    /**
     * Draw Line: A monochrome screen is stored as a single array of bytes, allowing
     * eight consecutive pixels to be stored in one byte. The screen has width w,
     * where w is divisible by 8 (that is, no byte will be split across rows). The
     * height of the screen, of course, can be derived from the length of the array
     * and the width. Implement a function that draws a horizontal line from (x1, y)
     * to (x2, y).
     * 
     * The method signature should look something like:
     * 
     * drawline(byte[] screen, int width, int x1, int x2, int y)
     * 
     * @throws Exception
     */
    public void testDrawLine() throws Exception {
        System.out.println(b((1 << 5) - 1));
        byte[] screen = new byte[9 * 9];
        drawline(screen, 9 * 8, 3, 17, 1);
        assertEquals((1 << 5) - 1, screen[9] & 0xff);
        assertEquals(1 << 7, screen[11] & 0xff);
        
        screen = new byte[9 * 9];
        drawline(screen, 9 * 8, 3, 16, 1);
        assertEquals((1 << 5) - 1, screen[9] & 0xff);
        assertEquals(-1, screen[10]);
        assertEquals(0, screen[11]);

    }

    static void drawline(byte[] screen, int width, int x1, int x2, int y) {
        int start = y * width + x1;
        int end = y * width + x2;
        int prefix = 8 - start % 8;
        int suffix = end % 8;

        if (prefix != 0) {
            int val = (1 << prefix) - 1;
            screen[start / 8] = (byte) val;
        }

        int _start = start + prefix;
        int _end = end - suffix;
        for (int i = _start; i < _end; i += 8) {
            screen[i >> 3] = -1;
        }

        if (suffix != 0) {
            int val = ~((1 << (8 - suffix)) - 1);
            screen[end / 8] = (byte) (val & 0xff);
        }
    }

    @Test
    /**
     * Insertion: You are given two 32-bit numbers, N and M, and two bit positions,
     * i and j. Write a method to insert M into N such that M starts at bit j and
     * ends at bit i. You can assume that the bits j through i have enough space to
     * fit all of M. That is, if M = 10011, you can assume that there are at least 5
     * bits between j and i. You would not, r example, have j = 3 and i = 2, because
     * M could not fully fit between bit 3 and bit 2.
     * 
     * EXAMPLE
     * 
     * <pre>
     * Input:  N = 10000000000, M = 10011, i = 2, j = 6 
     * Output: N = 10001001100
     * </pre>
     * 
     * @throws Exception
     */
    public void testInsertion() throws Exception {
        int n = Integer.parseInt("10000000000", 2);
        int m = Integer.parseInt("10011", 2);
        int expected = Integer.parseInt("10001001100", 2);
        int insert = insert(n, m, 2, 6);
        assertEquals(expected, insert);

    }

    static int insert(int n, int m, int i, int j) {
        int ones = -1;
        int left = ones << (i + j);
        int right = ones >>> (32 - i);
        int mask = left | right;
        int cleared = n & mask;
        return cleared | (m << i);
    }

    @Test
    /**
     * Binary to String: Given a real number between O and 1 (e.g., 0.72) that is
     * passed in as a double, print the binary representation. If the number cannot
     * be represented accurately in binary with at most 32 characters, print "ERROR"
     */
    public void testDoubleToBinaryString() throws Exception {
        assertEquals("0.1", toBinary(0.5));
        assertEquals("0.101", toBinary(0.625));
        assertEquals("ERROR!", toBinary(0.1));
    }

    static String toBinary(double d) {
        if (d < 0 || d > 1) {
            return "ERROR";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0.");
        while (d > 0) {
            if (sb.length() > 32) {
                return "ERROR!";
            }

            double n = d * 2;
            if (n >= 1) {
                d = n - 1;
                sb.append(1);
            } else {
                sb.append(0);
                d = n;
            }
        }
        return sb.toString();
    }

    @Test
    /**
     * Flip Bit to Win: You have an integer and you can flip exactly one bit from a
     * 0 to a 1. Write code to nd the length of the longest sequence of ls you could
     * create.
     * 
     * EXAMPLE
     * 
     * <pre>
     * Input: 1775 (or: 11011101111) Output: 8 
     * Input: 1767 (or: 11011100111) Output: 6
     * Input: 341 (or: 1010101) Output: 3
     * </pre>
     */
    public void testFlipBit() throws Exception {
        assertEquals(8, flipBitToWin(1775));
        assertEquals(6, flipBitToWin(1767));
        assertEquals(3, flipBitToWin(341));
    }

    private static int flipBitToWin(int n) {
        if (~n == 0)
            return Integer.SIZE;
        int prevLen = 0;
        int curLen = 0;
        int maxLen = 0;
        while (n > 0) {
            if ((n & 1) == 1) {
                curLen++;
            } else {
                if ((n & 2) == 0) {
                    prevLen = 0;
                } else {
                    prevLen = curLen;
                }
                curLen = 0;
            }
            maxLen = Math.max(maxLen, curLen + prevLen + 1);
            n = n >>> 1;
        }
        return maxLen;
    }
}
