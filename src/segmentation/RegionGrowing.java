/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import org.bytedeco.javacpp.opencv_core;
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

    private double gap; //Gap for color distance
    private int test = 0;
    private String image_path;
    private ArrayList<Pixel> pool; //List of pixels of the image
    private HashMap<Region, Integer> regions_list;
    private final IplImage ipl;

    /**
     *
     * @param image_path
     * @param gap
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
     * Run the algorithm
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
                                // System.out.println(this.pool.size()+" / "+reg.getMembers().size());
                            }
                            //}
                        }
                    }
                }//End neighbours

            }//End create

            this.regions_list.put(reg, nbRegion); //Add the region to the regions list
            this.pool.remove(pix); //Remove the pixel from the pixels list
        }

        System.out.println("Traitement regions terminé");
    }


    /**
     * Show the image
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
     * Gives the color distance between two RGB pixels
     *
     * @param first
     * @param second
     * @return
     */
    protected double distRgb(CvScalar first, CvScalar second) {
        return Math.sqrt(Math.pow(first.red() - second.red(), 2) + Math.pow((first.green() - second.green()), 2) + Math.pow((first.blue() - second.blue()), 2));

    }
    
    
    public int getTest() {
        return test;
    }

    public String getImage_path() {
        return image_path;
    }

    public ArrayList<Pixel> getPool() {
        return pool;
    }

    public HashMap<Region, Integer> getRegions_list() {
        return regions_list;
    }

    public double getGap() {
        return gap;
    }
    
    public IplImage getIpl() {
        return ipl;
    }

}
