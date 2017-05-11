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

    private double gap; //Gap for color distance
    private int test = 0;
    private String image_path;
    private ArrayList<Pixel> pool; //List of pixels of the image
    private HashMap<Region,Integer> regions_list;
    private final IplImage ipl;

    /**
     *
     * @param image_path
     * @param x
     * @param y
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
        System.out.println("algorithm");

        //Create the pool of pixels from the image
        for (int x = 0; x < this.ipl.width(); x++) {
            for (int y = 0; y < this.ipl.height(); y++) {
                this.pool.add(new Pixel(x, y));
            }
        }
        
        int nbRegion = 0;

        while (!this.pool.isEmpty()) {
            
            nbRegion++;
            Pixel pix = this.pool.get((int) (Math.random() * this.pool.size())); //Get random Pixel in pool
            Region reg = new Region();
            reg.addMember(pix); //Add pixel to region
            int x = pix.getX();
            int y = pix.getY();

            //Create a region
            for (int i = 0; i <= reg.getMembers().size(); i++) {
                //Each neighbour of the pixel
                for (int ix = x - 1; ix <= x + 1; ix++) {
                    for (int iy = y - 1; iy <= y + 1; iy++) {
                        //Check distance in RGB
                        if(this.distRgb(cvGet2D(this.ipl, y, x), cvGet2D(this.ipl, iy, ix)) <= this.gap){
                            reg.addMember(new Pixel(ix, iy));
                        }
                    }
                }//End neighbours
                
            }//End create
            
            this.regions_list.put(reg, nbRegion);
            this.pool.remove(pix);

        }

    }

    /**
     * Show the image
     */
    public void show() {
        // this.color();TODO 1.1 colorer l imqge 
        namedWindow("test", WINDOW_NORMAL);
        imshow("test", new Mat(this.ipl));
        waitKey(0);
    }

    
    /**
     * Give the distance between two RGB pixels
     *
     * @param first
     * @param second
     * @return
     */
    protected double distRgb(CvScalar first, CvScalar second) {

        return Math.sqrt(Math.pow(first.red() - second.red(), 2) + Math.pow((first.green() - second.green()), 2) + Math.pow((first.blue() - second.blue()), 2));

    }
    

    public static void main(String[] args) {
        RegionGrowing rg = new RegionGrowing("C:\\Users\\pierre.renard\\Desktop\\ProjetStage\\ProjetStage\\undistorted_perspective.jpg", 1);
        rg.run();
    }

}
