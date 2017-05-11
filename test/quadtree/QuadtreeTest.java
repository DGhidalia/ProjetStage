/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.bytedeco.javacpp.opencv_core.CvRect;
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
public class QuadtreeTest {
    
    public QuadtreeTest() {
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
     * Test of Segmentate method, of class Quadtree.
     */
    @Test
    public void testSegmentate() {
        System.out.println("Segmentate");
        Node n = null;
        Quadtree instance = null;
        instance.Segmentate();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Découpage method, of class Quadtree.
     */
   // @Test
    /*public void testDécoupage() {
        System.out.println("D\u00e9coupage");
        Mat img = imread("C:\\Users\\david.ghidalia\\Documents\\NetBeansProjects\\ProjetStage\\test\\ressource\\testDamier.png");
        Node root = new Node(new Rect(0,0,img.size().width(),img.size().height()),img);
        Quadtree instance = new Quadtree(root);
        instance.Découpage();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of DetectionCouleur method, of class Quadtree.
     */
   // @Test
   /* public void testDetectionCouleur() {
        System.out.println("DetectionCouleur");
        //Path directory = Paths.get("../ressource/testDamier.png");
        Mat img = imread("C:\\Users\\david.ghidalia\\Documents\\NetBeansProjects\\ProjetStage\\test\\ressource\\testDamier.png");
        Node n = new Node(new Rect(0,0,img.size().width(),img.size().height()),img);
        Quadtree instance = new Quadtree(n);
        boolean expResult = true;
        boolean result = instance.DetectionCouleur();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of rgbToHsv method, of class Quadtree.
     */
    //@Test
   /* public void testRgbToHsv() {
        System.out.println("rgbToHsv");
        double[] rgb = new double[3];
        rgb[0] = 248;
        rgb[1] = 50;
        rgb[2] = 150;
        Quadtree instance = new Quadtree(null);
        double[] expResult = new double[3];
        expResult[0] = 330;
        expResult[1] = 0.798;
        expResult[2] = 0.973;
        double[] result = instance.rgbToHsv(rgb);
        assertEquals(expResult[0], result[0],0.5);
        assertEquals(expResult[1], result[1],0.05);
        assertEquals(expResult[2], result[2],0.05);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testdistColors(){
        System.out.println("DistDeuxCouleur");
        double[] a = new double[3];
        a[0] = 330;
        a[1] = 0.798;
        a[2] = 0.973;
        double[] b = new double[3];
        b[0] = 0;
        b[1] = 0;
        b[2] = 0.1;
        Quadtree instance = new Quadtree(null);
        double expResult = 0;
        double result = instance.distColors(a, b);
        assertEquals(expResult,result,0.5);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }*/
    
}
