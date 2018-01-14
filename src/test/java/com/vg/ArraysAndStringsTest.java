package com.vg;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class ArraysAndStringsTest {

    //O(N)
    static boolean isUnique(String str) {
        BitSet chars = new BitSet();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (chars.get(c)) {
                return false;
            }
            chars.set(c, true);
        }
        return true;
    }

    //O(N^2)
    static boolean isUnique2(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            for (int j = i + 1; j < str.length(); j++) {
                char c2 = str.charAt(j);
                if (c2 == c) {
                    return false;
                }
            }
        }
        return true;
    }

    //O(N * log(N) + N)
    static boolean isUnique3(String str) {
        char[] charArray = str.toCharArray();
        Arrays.sort(charArray);
        for (int i = 1; i < charArray.length; i++) {
            char prev = charArray[i - 1];
            char curr = charArray[i];
            if (prev == curr) {
                return false;
            }
        }
        return true;
    }

    @Test
    //Is Unique: Implement an algorithm to determine if a string has all unique characters. What if you cannot use additional data structures?
    public void testIsUnique() throws Exception {
        assertTrue(isUnique(""));
        assertTrue(isUnique("abc"));
        assertFalse(isUnique("abca"));

        assertTrue(isUnique2(""));
        assertTrue(isUnique2("abc"));
        assertFalse(isUnique2("abca"));

        assertTrue(isUnique3(""));
        assertTrue(isUnique3("abc"));
        assertFalse(isUnique3("abca"));
    }

    //O(N*log(N) + N * log(N) + N)
    static boolean isPermutation(String a, String b) {
        if (a.length() != b.length())
            return false;
        char[] _a = a.toCharArray();
        char[] _b = b.toCharArray();
        Arrays.sort(_a);
        Arrays.sort(_b);
        for (int i = 0; i < _a.length; i++) {
            if (_a[i] != _b[i])
                return false;
        }

        return true;
    }

    //O(A + B)
    static boolean isPermutation2(String a, String b) {
        if (a.length() != b.length())
            return false;
        int[] chars = new int[Character.MAX_VALUE];
        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            chars[c]++;
        }
        for (int i = 0; i < b.length(); i++) {
            char c = b.charAt(i);
            chars[c]--;
            if (chars[c] < 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    //Check Permutation: Given two strings,write a method to decide if one is a permutation of the other.
    public void testCheckPermutation() throws Exception {
        assertTrue(isPermutation("", ""));
        assertTrue(isPermutation("a", "a"));
        assertTrue(isPermutation("ab", "ba"));
        assertFalse(isPermutation("", "a"));
        assertFalse(isPermutation("ab", "bc"));

        assertTrue(isPermutation2("", ""));
        assertTrue(isPermutation2("a", "a"));
        assertTrue(isPermutation2("ab", "ba"));
        assertFalse(isPermutation2("", "a"));
        assertFalse(isPermutation2("ab", "bc"));
    }

    static char[] urlify(char[] input) {
        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            if (c == ' ') {
                System.arraycopy(input, i + 1, input, i + 3, input.length - i - 3);
                input[i] = '%';
                input[i + 1] = '2';
                input[i + 2] = '0';
            }
        }

        return input;
    }

    static char[] urlify2(char[] input, int trueLength) {
        int spaces = 0;
        for (int i = 0; i < trueLength; i++) {
            if (input[i] == ' ') {
                spaces++;
            }
        }

        int idx = trueLength + spaces * 2 - 1;

        for (int i = trueLength - 1; i >= 0; i--) {
            if (input[i] != ' ') {
                input[idx--] = input[i];
            } else {
                input[idx--] = '0';
                input[idx--] = '2';
                input[idx--] = '%';
            }
        }
        return input;
    }

    @Test
    //URLify : Write a method to replace all spaces in a string with '%20  You may assume that the string has suf cient space at the end to hold the additional characters,and that you are given the "true" length of the string. (Note: If implementing in Java,please use a character array so that you can perform this operation in place.)
    // EXAMPLE
    //Input:  "Mr John Smith    ", 13 
    //Output: "Mr%20John%20Smith"
    public void testURLify() throws Exception {
        String expected = "Mr%20John%20Smith";
        assertArrayEquals(expected.toCharArray(), urlify("Mr John Smith    ".toCharArray()));
        assertArrayEquals(expected.toCharArray(), urlify2("Mr John Smith    ".toCharArray(), 13));
    }

    @Test
    //Palindrome Permutation: Given a string, write a function to check if it is a permutation of a palin­ drome. A palindrome is a word or phrase that is the same  rwards and backwards. A permutation is a rearrangement of letters. The palindrome does not need to be limited to just dictionary words.
    // EXAMPLE
    // Input: Tact Coa
    //Output: True (permutations: "taco cat", "atco cta", etc.)
    public void testPaliPerm() throws Exception {
        assertTrue(isPaliPerm(""));
        assertTrue(isPaliPerm("a"));
        assertTrue(isPaliPerm("Tact Coa"));
        assertTrue(isPaliPerm("taco cat"));
        assertTrue(isPaliPerm("atco cta"));
        assertFalse(isPaliPerm("acot eta"));

        assertTrue(isPaliPerm2(""));
        assertTrue(isPaliPerm2("a"));
        assertTrue(isPaliPerm2("Tact Coa"));
        assertTrue(isPaliPerm2("taco cat"));
        assertTrue(isPaliPerm2("atco cta"));
        assertFalse(isPaliPerm2("acot eta"));
    }

    static boolean isPaliPerm(String a) {
        Map<Character, Integer> chars = new HashMap<>();
        for (int i = 0; i < a.length(); i++) {
            char ch = Character.toLowerCase(a.charAt(i));
            if (!Character.isWhitespace(ch)) {
                if (!chars.containsKey(ch)) {
                    chars.put(ch, 0);
                }
                chars.put(ch, chars.get(ch) + 1);
            }
        }
        int odd = 0;
        for (Entry<Character, Integer> entry : chars.entrySet()) {
            if ((entry.getValue() & 0x1) != 0) {
                odd++;
                if (odd > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean isPaliPerm2(String a) {
        BitSet chars = new BitSet();
        for (int i = 0; i < a.length(); i++) {
            char ch = Character.toLowerCase(a.charAt(i));
            if (!Character.isWhitespace(ch)) {
                chars.flip(ch);
            }
        }
        return chars.cardinality() <= 1;
    }

    static boolean isSingleEdit(String a, String b) {
        if (a.length() == b.length()) {
            return isOneReplace(a, b);
        } else if (a.length() + 1 == b.length()) {
            return isOneAdded(a, b);
        } else if (a.length() - 1 == b.length()) {
            return isOneDeleted(a, b);
        }
        return false;
    }

    private static boolean isOneDeleted(String a, String b) {
        String larger = a;
        String smaller = b;
        if (b.length() > a.length()) {
            larger = b;
            smaller = a;
        }
        boolean found = false;
        for (int i = 0, j = 0; i < larger.length(); i++, j++) {
            char c1 = larger.charAt(i);
            char c2 = smaller.charAt(j);
            if (c1 != c2) {
                if (found)
                    return false;
                found = true;
                i++;
            }
        }

        return true;
    }

    private static boolean isOneAdded(String a, String b) {
        String larger = a;
        String smaller = b;
        if (b.length() > a.length()) {
            larger = b;
            smaller = a;
        }
        boolean found = false;
        for (int i = 0, j = 0; i < smaller.length(); i++, j++) {
            char c1 = larger.charAt(i);
            char c2 = smaller.charAt(j);
            if (c1 != c2) {
                if (found)
                    return false;
                found = true;
                i++;
            }
        }

        return true;
    }

    private static boolean isOneReplace(String a, String b) {
        boolean found = false;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                if (found) {
                    return false;
                }
                found = true;
            }
        }
        return true;
    }

    @Test
    //One Away: There are three types of edits that can be performed on strings: 
    // insert a character, remove a character, or replace a character. 
    // Given two strings, write a function to check if they are one edit (or zero edits) away.
    //EXAMPLE
    //pale, ple -> true 
    //pales, pale -> true 
    //pale, bale -> true 
    //pale, bake -> false
    public void testOneWay() throws Exception {
        assertTrue(isSingleEdit("ple", "pale"));
        assertTrue(isSingleEdit("pale", "ple"));
        assertTrue(isSingleEdit("palse", "pale"));
        assertTrue(isSingleEdit("pale", "bale"));
        assertFalse(isSingleEdit("pale", "bake"));
    }

    @Test
    //String Compression: Implement a method to perform basic string compression using the counts of repeated characters. 
    //For example, the string aabcccccaaa would become a2b1c5a3. 
    //If the "compressed" string would not become smaller than the original string, your method should return
    //the original string. 
    //You can assume the string has only uppercase and lowercase letters (a - z).
    public void testCompress() throws Exception {
        assertEquals("a2b1c5a3", compress("aabcccccaaa"));
        assertEquals("abc", compress("abc"));

    }

    static String compress(String string) {
        if (string == null || string.isEmpty())
            return string;
        StringBuilder output = new StringBuilder();
        char prev = string.charAt(0);
        int count = 1;
        for (int i = 1; i < string.length(); i++) {
            char c = string.charAt(i);
            if (prev == c) {
                count++;
            } else {
                output.append(prev).append(count);
                count = 1;
            }
            prev = c;
        }
        output.append(prev).append(count);
        if (output.length() >= string.length()) {
            return string;
        }
        return output.toString();
    }

    static int[][] rotateRight90(int[][] matrix) {
        int cols = matrix[0].length;
        int[][] result = new int[matrix.length][cols];
        for (int r = 0; r < matrix.length; r++) {
            int[] row = matrix[r];
            for (int c = 0; c < row.length; c++) {
                result[c][cols - r - 1] = row[c];
            }
        }

        return result;
    }

    static int[][] rotateRight90i(int[][] matrix) {
        int layers = matrix.length / 2;
        for (int layer = 0; layer < layers; layer++) {
            int d = matrix.length - layer - 1;
            for (int i = layer, j = 0; i < d; i++, j++) {
                int tl = matrix[layer][i];
                int tr = matrix[i][d];
                int br = matrix[d][d - j];
                int bl = matrix[d - j][layer];
                int _tl = bl;
                int _tr = tl;
                int _br = tr;
                int _bl = br;

                matrix[layer][i] = _tl;
                matrix[i][d] = _tr;
                matrix[d][d - j] = _br;
                matrix[d - j][layer] = _bl;
            }
        }
        return matrix;
    }

    static int[][] rotateLeft90i(int[][] matrix) {
        for (int r = 0; r < matrix.length; r++) {
            int[] row = matrix[r];
            for (int c = r + 1; c < row.length; c++) {
                int a = matrix[r][c];
                int b = matrix[c][r];
                matrix[r][c] = b;
                matrix[c][r] = a;
            }
        }

        return matrix;
    }

    static int[][] copy(int[][] matrix) {
        int[][] copy = new int[matrix.length][matrix[0].length];
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                copy[r][c] = matrix[r][c];
            }
        }
        return copy;
    }

    //Rotate Matrix: Given an image represented by an NxN matrix, where each pixel in the image is 4 bytes, 
    //write a method to rotate the image by 90 degrees. Can you do this in place?
    @Test
    public void testRotateMatrix() throws Exception {
        int matrix[][] = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        int rotatedR90[][] = new int[][] { { 7, 4, 1 }, { 8, 5, 2 }, { 9, 6, 3 } };
        int rotatedL90[][] = new int[][] { { 1, 4, 7 }, { 2, 5, 8 }, { 3, 6, 9 } };

        assertArrayEquals(rotatedR90, rotateRight90i(copy(matrix)));
        assertArrayEquals(rotatedR90, rotateRight90(matrix));
        assertArrayEquals(rotatedL90, rotateLeft90i(matrix));

        int[][] m4x4 = new int[][] { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } };
        assertArrayEquals(rotateRight90(m4x4), rotateRight90i(m4x4));
    }

    @Test
    //Zero Matrix: Write an algorithm such that if an element in an MxN matrix is 0, its entire row and column are set to 0.
    public void testZeroMatrix() throws Exception {
        int[][] matrix = new int[4][5];
        fill(matrix, 1);
        matrix[2][3] = 0;

        zeroMatrix(matrix);
        assertArrayEquals(new int[5], matrix[2]);
        assertEquals(0, matrix[0][3]);
        assertEquals(0, matrix[1][3]);
        assertEquals(0, matrix[2][3]);
        assertEquals(0, matrix[3][3]);

        matrix = new int[4][5];
        fill(matrix, 1);
        matrix[2][3] = 0;

        zeroMatrix2(matrix);
        assertArrayEquals(new int[5], matrix[2]);
        assertEquals(0, matrix[0][3]);
        assertEquals(0, matrix[1][3]);
        assertEquals(0, matrix[2][3]);
        assertEquals(0, matrix[3][3]);

    }

    static void zeroMatrix(int[][] matrix) {
        Set<Integer> zeroColumns = new HashSet<>();
        Set<Integer> zeroRows = new HashSet<>();
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                if (zeroColumns.contains(c) && zeroRows.contains(r)) {
                    continue;
                }
                if (matrix[r][c] == 0) {
                    zeroColumns.add(c);
                    zeroRows.add(r);
                }
            }
        }
        for (int r : zeroRows) {
            Arrays.fill(matrix[r], 0);
        }
        for (int c : zeroColumns) {
            fillColumn(matrix, c, 0);
        }
    }

    static void zeroMatrix2(int[][] matrix) {
        boolean row0HasZeros = false;
        boolean col0HasZeros = false;
        for (int c = 0; c < matrix[0].length; c++) {
            if (matrix[0][c] == 0) {
                row0HasZeros = true;
                break;
            }
        }

        for (int r = 0; r < matrix.length; r++) {
            if (matrix[r][0] == 0) {
                col0HasZeros = true;
                break;
            }
        }

        for (int r = 1; r < matrix.length; r++) {
            for (int c = 1; c < matrix[r].length; c++) {
                if (matrix[r][c] == 0) {
                    matrix[r][0] = 0;
                    matrix[0][c] = 0;
                }
            }
        }

        for (int r = 0; r < matrix.length; r++) {
            if (matrix[r][0] == 0) {
                Arrays.fill(matrix[r], 0);
            }
        }

        for (int c = 0; c < matrix[0].length; c++) {
            if (matrix[0][c] == 0) {
                fillColumn(matrix, c, 0);
            }
        }

        if (row0HasZeros) {
            Arrays.fill(matrix[0], 0);
        }
        if (col0HasZeros) {
            fillColumn(matrix, 0, 0);
        }
    }

    private static void fillColumn(int[][] matrix, int c, int val) {
        for (int r = 0; r < matrix.length; r++) {
            matrix[r][c] = val;
        }
    }

    static void fill(int[][] matrix, int val) {
        for (int r = 0; r < matrix.length; r++) {
            Arrays.fill(matrix[r], val);
        }
    }
    
    static boolean isSubstring(String str, String substr) {
        return str.contains(substr);
    }

    @Test
    //String Rotation: Assume you have a method isSubstring which checks if one word is a substring of another. 
    // Given two strings, s1 and s2, write code to check if s2 is a rotation of s1 using only one call to isSubstring 
    // (e.g.,"waterbottle" is a rotation of "erbottlewat").
    public void testStringRotation() throws Exception {
        assertTrue(isRotation("waterbottle", "erbottlewat"));
        assertFalse(isRotation("1waterbottle", "erbottlewat"));

    }

    private static boolean isRotation(String string, String string2) {
        return isSubstring(string + string, string2);
    }

}
