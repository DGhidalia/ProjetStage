/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.util.ArrayList;
import java.util.HashMap;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_NORMAL;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

/**
 *
 * @author pierre.renard
 */
public class RegionGrowing implements Runnable {

    private final double gap; //Gap for color distance
    private final int test = 0; //Test counter
    private final String image_path;
    private final ArrayList<Pixel> pool; //List of pixels of the image
    private final HashMap<Region, Integer> regions_list; //List of regions of the image
    private final IplImage ipl; //Source image converted to IPL format
    /**
     * Initialize the properties using an image path
     *
     * @param image_path
     * @param gap distance between two colors to consider them as different
     * colors
     */
    public RegionGrowing(String image_path, double gap) {
        this.gap = gap;
        this.image_path = image_path;
        this.pool = new ArrayList<>();
        this.regions_list = new HashMap<>();
        Mat image = imread(image_path); //Initialize image 
        this.ipl = new IplImage(image);
    }    
    

    /**
     * Run the region growing algorithm on the given image
     */
    @Override
    public void run() {
        System.out.println("Traitement image");

        this.fillPool();

        System.out.println("Traitement image terminé");

        int nbRegion = 0; //Actual region number

        System.out.println("Traitement regions");

        while (!this.pool.isEmpty()) {
            nbRegion++;
            Pixel pix = this.pool.get((int) (Math.random() * this.pool.size())); //Get random Pixel in pool
            Region reg = new Region();
            reg.addMember(pix); //Add pixel to region

            //Create a region
            for (int i = 0; i < reg.size(); i++) {

                pix = reg.getMembers().get(i); //Get random Pixel in pool
                int x = pix.getX();
                int y = pix.getY(); //Each neighbour of the pixel
                for (int ix = x - 1; ix <= x + 1; ix++) {
                    for (int iy = y - 1; iy <= y + 1; iy++) {
                        Pixel p = new Pixel(ix, iy);
                        //Check if the pixel is ok to be visited
                        if (this.pool.contains(p)) {
                            //Check distance in RGB
                            if (this.distRgb(cvGet2D(this.ipl, y, x), cvGet2D(this.ipl, iy, ix)) <= this.gap) {
                                reg.addMember(p);//Add pixel to region
                                this.pool.remove(p);//Remove added pixel
                            }
                        }
                    }
                }//End neighbours

            }//End create

            this.regions_list.put(reg, nbRegion); //Add the region to the regions list
            this.pool.remove(pix); //Remove the pixel from the pixels list
        }

        System.out.println("Traitement regions terminé");
    }

    //--------------------------------------------------------------------------
    //METHODS
    //--------------------------------------------------------------------------
    /**
     * Show the final image
     */
    public void show() {
        // this.color();
        //TODO 1.1 colorer l imqge 
        namedWindow("test", WINDOW_NORMAL);
        imshow("test", new Mat(this.ipl));
        waitKey(0);
    }

    /**
     * Create the pool of pixels from the image
     */
    private void fillPool() {
        for (int x = 0; x < this.ipl.width(); x++) {
            for (int y = 0; y < this.ipl.height(); y++) {
                this.pool.add(new Pixel(x, y));
            }
        }
    }

    /**
     * Gives the color distance between two RGB pixels. Use the method cvGet2D()
     * to get a Scalar from a pixel.
     *
     * @param first
     * @param second
     * @return
     */
    protected double distRgb(CvScalar first, CvScalar second) {
        return Math.sqrt(Math.pow(first.red() - second.red(), 2) + Math.pow((first.green() - second.green()), 2) + Math.pow((first.blue() - second.blue()), 2));

    }
    
    /**
     * 
     */
    private void getPyramid(){
        
                
    }

    //--------------------------------------------------------------------------
    //GETTERS
    //--------------------------------------------------------------------------
    /**
     *
     * @return
     */
    public int getTest() {
        return test;
    }

    /**
     *
     * @return
     */
    public String getImage_path() {
        return image_path;
    }

    /**
     *
     * @return
     */
    public ArrayList<Pixel> getPool() {
        return pool;
    }

    /**
     *
     * @return
     */
    public HashMap<Region, Integer> getRegions_list() {
        return regions_list;
    }

    /**
     *
     * @return
     */
    public double getGap() {
        return gap;
    }

    /**
     *
     * @return
     */
    public IplImage getIpl() {
        return ipl;
    }
    
    

}
