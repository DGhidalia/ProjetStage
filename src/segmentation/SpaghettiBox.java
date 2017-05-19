/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.util.HashMap;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;

/**
 *
 * @author pierre.renard
 */
public class SpaghettiBox {

    private HashMap<Pixel,Integer> blacklist;
    
    /**
     *
     * @param pixel
     * @param img
     * @param region
     */
    protected void testNeighbours(Pixel pixel, IplImage img, int region) {

        //System.out.println(test+"/ "+img.height()*img.width());
        if (!blacklist.containsKey(pixel)) {
            blacklist.put(pixel, region);

            int x = pixel.getX();
            int y = pixel.getY();
           // System.out.println(this.blacklist.values().size());
           CvScalar start_rgb = cvGet2D(img, y, x); //Initialize start pixel       

            for (int ix = x - 1; ix <= x + 1; ix++) {
                for (int iy = y - 1; iy <= y + 1; iy++) {
                    Pixel p = new Pixel(ix, iy);
                    //test pqs sortir image 
                    if (ix >= 0 && ix < img.width() && iy < img.height() && iy >= 0
                            && !pixel.equals(p)) {
                        boolean checkPixel = this.checkPixel(start_rgb, img, ix, iy);
                        if (checkPixel) {
                            //same region                        
                            //testNeighbours(p, img, region);
                        }
                    }
                }
            }

        }
    }

    private boolean checkPixel(CvScalar start_rgb, IplImage img, int ix, int iy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
