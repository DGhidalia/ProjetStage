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
     * Test of algorithm method, of class RegionGrowing.
     */
    @Test
    public void testAlgorithm() {
        System.out.println("algorithm");
        String image_path = "";
        int x = 0;
        int y = 0;
        RegionGrowing instance = null;
        instance.algorithm(image_path, x, y);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testNeighbours method, of class RegionGrowing.
     */
    @Test
    public void testTestNeighbours() {
        System.out.println("testNeighbours");
        Pixel pixel = null;
        opencv_core.IplImage img = null;
        int region = 0;
        RegionGrowing instance = null;
        instance.testNeighbours(pixel, img, region);
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
    
}
