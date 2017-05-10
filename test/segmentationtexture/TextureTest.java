/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentationtexture;

import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cameron.mourot
 */
public class TextureTest {
    
    public TextureTest() {
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
     * Test of run method, of class Texture.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Texture instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of SobelFilter method, of class Texture.
     */
    @Test
    public void testSobelFilter() {
        System.out.println("SobelFilter");
        String test = this.getClass().getResource("undistorted_perspective.jpg").getPath();
        opencv_core.Mat input = imread(test);
        Texture instance = new Texture(test);
        opencv_core.Mat expResult = null;
        opencv_core.Mat result = instance.SobelFilter(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
