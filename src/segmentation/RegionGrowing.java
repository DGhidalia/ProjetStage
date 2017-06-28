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
 * @version 1.3
 */
public class RegionGrowing implements Runnable {

    private final double gap; //Gap for color distance
    private final String image_path;
    private final String output_image;
    private final ArrayList<Pixel> pool; //List of pixels of the image
    private final HashMap<Integer, Region> regions_list; //List of regions of the image
    private final HashMap<Integer, Region> rejected_regions;//List of not taken regions
    private final IplImage ipl; //Source image converted to IPL format
    private boolean isRgb;
    int TAILLE_REG_MIN;

    /**
     * Initialize the properties using an image path. Adjust the gap depending
     * on the image. The higher it is, the less regions they are. Works with the
     * invert.
     *
     * @param image_path
     * @param output_image
     * @param gap distance between two colors to consider them as different
     * colors.
     * @param rgb true for RGB distance between pixels, false for HSV distance
     */
    public RegionGrowing(String image_path, String output_image, double gap, boolean rgb) {
        this.gap = gap;
        this.image_path = image_path;
        this.output_image = output_image;
        this.pool = new ArrayList<>();
        this.regions_list = new HashMap<>();
        this.rejected_regions = new HashMap<>();
        Mat image = imread(this.image_path); //Initialize image 
        this.ipl = new IplImage(image);
        this.isRgb = rgb;
        this.TAILLE_REG_MIN = (int) (this.ipl.width() * this.ipl.height() * 0.0005);
    }

    /**
     * Run the region growing algorithm on the given image
     */
    @Override
    public void run() {
        System.out.println("Traitement image");

        this.fillPool();

        System.out.println("Traitement image terminé");

        int nbRegion = 1; //Actual region number

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
                            } else {
                                distance = this.distHsv(cvGet2D(this.ipl, y, x), cvGet2D(this.ipl, iy, ix));
                            }
                            if (distance <= this.gap) {
                                reg.addMember(p);//Add pixel to region
                                this.pool.remove(p);//Remove added pixel
                            }
                        }
                    }
                }//End neighbours

            }//End create            

            this.addToRegionList(reg, nbRegion);

            this.pool.remove(pix); //Remove the pixel from the pixels list
        }

        System.out.println("Traitement regions terminé");

        this.show();
    }
    //--------------------------------------------------------------------------
    //METHODS
    //--------------------------------------------------------------------------

    /**
     * Show the final image and save it
     */
    public void show() {
        System.out.println("Colorisation en cours");
        //Create an image using the colorization algorithm
        IplImage color = this.color();
        Mat img = new Mat(color);
        //Display the created image in a window
        namedWindow("test", WINDOW_NORMAL);
        imshow("test", img);
        //Save the image file
        imwrite(this.output_image, img);
        System.out.println("Colorisation terminée");
        waitKey(0);
    }

    /**
     * Scan the image and add every pixel to the pool list
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

        double red = Math.pow(first.red() - second.red(), 2);
        double green = Math.pow((first.green() - second.green()), 2);
        double blue = Math.pow((first.blue() - second.blue()), 2);

        return Math.sqrt(red + green + blue);

    }

    /**
     * Gives the distance between two HSV pixels. Use the method cvGet2D() to
     * get a Scalar from a pixel.
     *
     * @param first
     * @param second
     * @return
     */
    private double distHsv(CvScalar first, CvScalar second) {
        double[] firstTab = {first.red(), first.green(), first.blue()};
        double[] secondTab = {second.red(), second.green(), second.blue()};
        double[] firstHsv = RgbToHsv.rgbToHsv(firstTab);
        double[] secondHsv = RgbToHsv.rgbToHsv(secondTab);

        return RgbToHsv.distColors(firstHsv, secondHsv);
    }

    /**
     * Color the regions of the source image. Give a random color to retained
     * regions and black color to rejected regions.
     *
     * @return
     */
    private IplImage color() {

        IplImage clone = this.ipl.clone();
        Collection<Region> regionValues = this.regions_list.values();
        Collection<Region> rejectedRegionsValues = this.rejected_regions.values();

        //Foreach loop 
        regionValues.forEach((region) -> {
            //Random values of each color for each region
            int redValue = (int) (Math.random() * 255);
            int greenValue = (int) (Math.random() * 255);
            int blueValue = (int) (Math.random() * 255);
            //Create a list of pixels (region)
            List<Pixel> listPixel = region.getMembers();
            //Calls a method to color a list of pixels (region)
            this.regionColor(redValue, greenValue, blueValue, listPixel, clone);
        });

        rejectedRegionsValues.forEach((region) -> {
            //Create a list of pixels (region)
            List<Pixel> listPixel = region.getMembers();
            //Calls a method to color a list of pixels (region)
            this.regionColor(0, 0, 0, listPixel, clone);
        });

        return clone;

    }

    /**
     * Color all the pixels of a region with the average color of all those
     * pixels. Sub function only used with the global coloring function.
     *
     * @param red Amount of red in the region
     * @param green Amount of green in the region
     * @param blue Amount of blue in the region
     * @param listPixel Represents the region
     * @param clone Clone of the basic image
     */
    private void regionColor(int red, int green, int blue, List<Pixel> listPixel, IplImage clone) {
        //Create an indexer for the image
        UByteIndexer index = clone.createIndexer();
        //Apply colorization on each pixel of the list
        for (int i = 0; i < listPixel.size(); i++) {
            Pixel pix = listPixel.get(i);

            index.put(pix.getY(), pix.getX(), 0, red);
            index.put(pix.getY(), pix.getX(), 1, green);
            index.put(pix.getY(), pix.getX(), 2, blue);
        }

        index.release(); //Memory release
    }

    /**
     * Test in wich list the region has to be added. If the size of the region
     * is too small, then it is added to a banlist. If the size is ok, it is
     * added to the normal regions list.
     *
     * @param reg
     * @param nbRegion
     */
    private void addToRegionList(Region reg, int nbRegion) {

        if (reg.size() >= TAILLE_REG_MIN) {
            this.regions_list.put(nbRegion, reg); //Add the region to the regions list                
        } else {
            this.rejected_regions.put(nbRegion, reg);
        }
    }

    //--------------------------------------------------------------------------
    //GETTERS
    //--------------------------------------------------------------------------
    /**
     * Get the path of the image
     *
     * @return
     */
    public String getImage_path() {
        return image_path;
    }

    /**
     * Get the list of pixels of the image
     *
     * @return
     */
    public ArrayList<Pixel> getPool() {
        return pool;
    }

    /**
     * Get the list of created regions
     *
     * @return
     */
    public HashMap<Integer, Region> getRegions_list() {
        return regions_list;
    }

    /**
     * Get the color gap used for distance calculation
     *
     * @return
     */
    public double getGap() {
        return gap;
    }

    /**
     * Get the treated image
     *
     * @return
     */
    public IplImage getIpl() {
        return ipl;
    }

}
