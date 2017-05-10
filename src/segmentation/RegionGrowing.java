/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.util.HashMap;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_NORMAL;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;

/**
 *
 * @author pierre.renard
 */
public class RegionGrowing {

    private HashMap<Pixel, Integer> blacklist = new HashMap(); //Blacklist for visited pixels
    private double gap; //Gap for color distance
    private int test = 0;

    /**
     *
     * @param image_path
     * @param x
     * @param y
     * @param gap
     */
    public RegionGrowing(String image_path, int x, int y, double gap) {

        this.gap = gap;
        this.algorithm(image_path, x, y);

    }

    /**
     *
     * @param image_path
     * @param x
     * @param y
     */
    public void algorithm(String image_path, int x, int y) {

        Mat image = imread(image_path); //Initialize image 
        IplImage ipl = new IplImage(image);

        Pixel pix = new Pixel(x, y);

        this.testNeighbours(pix, ipl, 1); //Every neighbours

        /*namedWindow("test", WINDOW_NORMAL);
        imshow("test", new Mat(ipl));
        waitKey(0);*/
        
        System.out.println("Programme terminé");
    }

    /**
     *
     * @param pixel
     * @param img
     * @param region
     */
    public void testNeighbours(Pixel pixel, IplImage img, int region) {

        System.out.println("ok" + test);
        test++;

        if (!blacklist.containsKey(pixel)) {
            blacklist.put(pixel, region);
        }

        int x = pixel.getX();
        int y = pixel.getY();
        CvScalar start_rgb = cvGet2D(img, y, x); //Initialize start pixel       

        for (int ix = x - 1; ix <= x + 1; ix++) {
            for (int iy = y - 1; iy <= y + 1; iy++) {
                if (ix > 0 && ix < img.width() && iy < img.height() && iy > 0) {
                    this.rec(start_rgb, img, ix, iy, region);
                }
            }
        }       

    }

    /**
     *
     * @param start_rgb
     * @param img
     * @param x
     * @param y
     * @param region
     */
    private void rec(CvScalar start_rgb, IplImage img, int x, int y, int region) {
        //Check if in blacklist
        if (!blacklist.containsKey(new Pixel(x, y))) {
            //Check if in the same region
            if (distRgb(start_rgb, cvGet2D(img, y, x)) <= this.gap) {
                blacklist.put(new Pixel(x, y), region);
                testNeighbours(new Pixel(x, y), img, region);               
            }
        }
    }

    /**
     * Give the distance between two RGB pixels
     *
     * @param first
     * @param second
     * @return
     */
    public double distRgb(CvScalar first, CvScalar second) {

        return Math.sqrt(Math.pow(first.red() - second.red(), 2) + Math.pow((first.green() - second.green()), 2) + Math.pow((first.blue() - second.blue()), 2));

    }

}
