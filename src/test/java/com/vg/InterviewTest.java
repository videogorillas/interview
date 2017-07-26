package com.vg;

import static org.junit.Assert.*;

import org.junit.Test;

public class InterviewTest {
    @Test
    public void testName() throws Exception {
        Integer a = 100;
        Integer b = 100;
         
        System.out.println(a.equals(b));

        System.out.println(a == b);
        
        Integer a2 = 1000;
        Integer b2 = 1000;
         
        System.out.println(a2.equals(b2));

        System.out.println(a2 == b2);

    }

}
