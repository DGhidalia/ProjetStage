/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.io.File;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
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
   
    private int w;
    private int h;
    int[][][] imqge=new int[w][h][3];
    public RegionGrowingTest() {
//          for (int x = 0; x < this.ipl.width(); x++) {
//            for (int y = 0; y < this.ipl.height(); y++) {
//                imqge[x][y][0]=new Pixel(x, y).getX());
//            }
//        }
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
        String absolutePath = new File(this.getClass().getResource("testRegion.png").getPath()).getAbsolutePath();
          RegionGrowing rg = new RegionGrowing(absolutePath, 0, true);
        rg.run();
       
        int size = rg.getRegions_list().values().size();
        System.out.println(size);
         assertEquals(size, 2);
    }

    /**
     * Test of show method, of class RegionGrowing.
     */
    @Test
    public void testShow() {
        System.out.println("show");
    }

    /**
     * Test of distRgb method, of class RegionGrowing.
     */
    @Test
    public void testDistRgb() {
        System.out.println("distRgb");
          String absolutePath = new File(this.getClass().getResource("testRegion.png").getPath()).getAbsolutePath();
           RegionGrowing rg = new RegionGrowing(absolutePath, 200000, true);
        //rg.run();
        opencv_core.IplImage im = rg.getIpl();
        opencv_core.CvScalar first = cvGet2D(im,0,0);
        opencv_core.CvScalar second = cvGet2D(im,20,130/5);
        System.out.println(first+""+second);
        double distRgb = rg.distRgb(first, second);
        System.out.println(distRgb);
    }
    
}
