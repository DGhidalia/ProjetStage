/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import org.bytedeco.javacpp.opencv_core;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pierre.renard
 */
public class RegionGrowingTest {
    
    public RegionGrowingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class RegionGrowing.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        RegionGrowing instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of show method, of class RegionGrowing.
     */
    @Test
    public void testShow() {
        System.out.println("show");
        RegionGrowing instance = null;
        instance.show();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of distRgb method, of class RegionGrowing.
     */
    @Test
    public void testDistRgb() {
        System.out.println("distRgb");
        opencv_core.CvScalar first = null;
        opencv_core.CvScalar second = null;
        RegionGrowing instance = null;
        double expResult = 0.0;
        double result = instance.distRgb(first, second);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class RegionGrowing.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        RegionGrowing.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
