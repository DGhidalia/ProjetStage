/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_NORMAL;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

/**
 *
 * @author pierre.renard
 */
public class RegionGrowing implements Runnable {

    private final double gap; //Gap for color distance
    private final String image_path;
    private final ArrayList<Pixel> pool; //List of pixels of the image
    private final HashMap<Integer, Region> regions_list; //List of regions of the image
    private final IplImage ipl; //Source image converted to IPL format
    private boolean isRgb;

    /**
     * Initialize the properties using an image path
     *
     * @param image_path
     * @param gap distance between two colors to consider them as different
     * colors
     */
    public RegionGrowing(String image_path, double gap, boolean rgb) {
        this.gap = gap;
        this.image_path = image_path;
        this.pool = new ArrayList<>();
        this.regions_list = new HashMap<>();
        Mat image = imread(this.image_path); //Initialize image 
        this.ipl = new IplImage(image);
        this.isRgb = rgb;
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
                            double distance;
                            if (this.isRgb) {
                                distance = this.distRgb(cvGet2D(this.ipl, y, x), cvGet2D(this.ipl, iy, ix));
                            }
                            else{
                                distance = this.distHsv(cvGet2D(this.ipl, y, x), cvGet2D(this.ipl, iy, ix));
                            }
                                if ( distance <= this.gap) {
                                    reg.addMember(p);//Add pixel to region
                                    this.pool.remove(p);//Remove added pixel
                                }
                            }
                        }
                    }//End neighbours

                }//End create

                this.regions_list.put(nbRegion, reg); //Add the region to the regions list
                this.pool.remove(pix); //Remove the pixel from the pixels list
            }

            System.out.println("Traitement regions terminé");

            this.show();
        }
        //--------------------------------------------------------------------------
        //METHODS
        //--------------------------------------------------------------------------
        /**
         * Show the final image
         */
    public void show() {
        IplImage color = this.color();
        Mat img = new Mat(color);
        namedWindow("test", WINDOW_NORMAL);
        imshow("test", img);
        imwrite("regionsResult.jpg", img);
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
     * @param first
     * @param second
     * @return
     */
    private double distHsv(CvScalar first, CvScalar second) {
        double[] firstTab = {first.red(), first.green(), first.blue()};
        double[] secondTab = {second.red(), second.green(), second.blue()};
        double[] rgbToHsv1 = RgbToHsv.rgbToHsv(firstTab);
        double[] rgbToHsv2 = RgbToHsv.rgbToHsv(secondTab);

        
        return RgbToHsv.distColors(rgbToHsv1, rgbToHsv2);
    }

    /**
     * Color the image depending on the regions
     *
     * @return
     */
    private IplImage color() {

        IplImage clone = this.ipl.clone();
        Collection<Region> values = this.regions_list.values();

        values.forEach((r) -> {
            //Total value of each color for each region
            int totRed = (int) (Math.random() * 255);
            int totGreen = (int) (Math.random() * 255);
            int totBlue = (int) (Math.random() * 255);
            //Counter
            List<Pixel> listPixel = r.getMembers();
            this.regionColor(totRed, totGreen, totBlue, listPixel, clone);
        });

        return clone;

    }

    /**
     * Color all the pixels of a region with the average color of all those
     * pixels. Sub function only used with the global coloring function.
     *
     * @param red
     * @param green
     * @param blue
     * @param index
     */
    private void regionColor(int red, int green, int blue, List<Pixel> listPixel, IplImage clone) {
        UByteIndexer index = clone.createIndexer();
        for (int i = 0; i < listPixel.size(); i++) {
            Pixel pix = listPixel.get(i);

            index.put(pix.getY(), pix.getX(), 0, red);
            index.put(pix.getY(), pix.getX(), 1, green);
            index.put(pix.getY(), pix.getX(), 2, blue);
        }
        index.release();
    }

    //--------------------------------------------------------------------------
    //GETTERS
    //--------------------------------------------------------------------------
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
    public HashMap<Integer, Region> getRegions_list() {
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
