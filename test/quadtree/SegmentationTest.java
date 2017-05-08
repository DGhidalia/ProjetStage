/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import org.bytedeco.javacpp.opencv_core;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author david.ghidalia
 */
public class SegmentationTest {
    
    public SegmentationTest() {
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
     * Test of run method, of class Segmentation.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        opencv_core.Mat src = null;
        this.getClass().getResource("testDamier.png").getPath();
        Segmentation instance = new Segmentation();
        instance.run(src);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Break2 method, of class Segmentation.
     */
    @Test
    public void testBreak2() {
        System.out.println("Break2");
        Node n = null;
        Segmentation instance = new Segmentation();
        instance.Break2(n);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
