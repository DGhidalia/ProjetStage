/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
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
public class NodeTest {
    
    public NodeTest() {
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
     * Test of Découpage method, of class Node.
     */
    @Test
    public void testDécoupage() {
        System.out.println("D\u00e9coupage");
        Mat img = imread("C:\\Users\\david.ghidalia\\Documents\\NetBeansProjects\\ProjetStage\\test\\ressource\\testDamier.png");
        Node instance = new Node(new Rect(0,0,img.size().width(),img.size().height()),img);
        instance.Découpage();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of DetectionCouleur method, of class Node.
     */
    @Test
    public void testDetectionCouleur() {
        System.out.println("DetectionCouleur");
        Node instance = null;
        boolean expResult = false;
        boolean result = instance.DetectionCouleur();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rgbToHsv method, of class Node.
     */
    @Test
    public void testRgbToHsv() {
        System.out.println("rgbToHsv");
        double[] rgb = null;
        Node instance = null;
        double[] expResult = null;
        double[] result = instance.rgbToHsv(rgb);
        //assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of distColors method, of class Node.
     */
    @Test
    public void testDistColors() {
        System.out.println("distColors");
        double[] a = new double[3];
        double[] b = new double[3];
        for(int i = 0;i<3;i++){
            a[i] = 255;
            b[i] = 255;
        }
        Node instance = new Node(null,null);
        double expResult = 0.0;
        double result = instance.distColors(a, b);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isLeaf method, of class Node.
     */
    @Test
    public void testIsLeaf() {
        System.out.println("isLeaf");
        Node instance = null;
        boolean expResult = false;
        boolean result = instance.isLeaf();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNorthEast method, of class Node.
     */
    @Test
    public void testGetNorthEast() {
        System.out.println("getNorthEast");
        Node instance = null;
        Node expResult = null;
        Node result = instance.getNorthEast();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNorthEast method, of class Node.
     */
    @Test
    public void testSetNorthEast() {
        System.out.println("setNorthEast");
        Node _NorthEast = null;
        Node instance = null;
        instance.setNorthEast(_NorthEast);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNorthWest method, of class Node.
     */
    @Test
    public void testGetNorthWest() {
        System.out.println("getNorthWest");
        Node instance = null;
        Node expResult = null;
        Node result = instance.getNorthWest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNorthWest method, of class Node.
     */
    @Test
    public void testSetNorthWest() {
        System.out.println("setNorthWest");
        Node _NorthWest = null;
        Node instance = null;
        instance.setNorthWest(_NorthWest);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSouthEast method, of class Node.
     */
    @Test
    public void testGetSouthEast() {
        System.out.println("getSouthEast");
        Node instance = null;
        Node expResult = null;
        Node result = instance.getSouthEast();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSouthEast method, of class Node.
     */
    @Test
    public void testSetSouthEast() {
        System.out.println("setSouthEast");
        Node _SouthEast = null;
        Node instance = null;
        instance.setSouthEast(_SouthEast);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSouthWest method, of class Node.
     */
    @Test
    public void testGetSouthWest() {
        System.out.println("getSouthWest");
        Node instance = null;
        Node expResult = null;
        Node result = instance.getSouthWest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSouthWest method, of class Node.
     */
    @Test
    public void testSetSouthWest() {
        System.out.println("setSouthWest");
        Node _SouthWest = null;
        Node instance = null;
        instance.setSouthWest(_SouthWest);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getImg method, of class Node.
     */
    @Test
    public void testGetImg() {
        System.out.println("getImg");
        Node instance = null;
        opencv_core.Mat expResult = null;
        opencv_core.Mat result = instance.getImg();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setImg method, of class Node.
     */
    @Test
    public void testSetImg() {
        System.out.println("setImg");
        opencv_core.Mat _img = null;
        Node instance = null;
        instance.setImg(_img);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getROI method, of class Node.
     */
    @Test
    public void testGetROI() {
        System.out.println("getROI");
        Node instance = null;
        opencv_core.Rect expResult = null;
        opencv_core.Rect result = instance.getROI();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setROI method, of class Node.
     */
    @Test
    public void testSetROI() {
        System.out.println("setROI");
        opencv_core.Rect _ROI = null;
        Node instance = null;
        instance.setROI(_ROI);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
