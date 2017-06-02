/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filtersPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.opencv_core.CV_16S;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

/**
 * This class permit to execute different algorithms on a given image. There is
 * a Sobel filter algorithm, Hough Transform algorithm and an algorithm to
 * reduce the size of the image to apply the region growing easily.
 *
 * @author cameron.mourot
 * @version 1.2
 */
public class Filters implements Runnable {

    private final String pathimage;
    private final List<Line> line_list;
    private String outputFile;

    public Filters(String pathimage) {
        this.pathimage = pathimage;
        this.line_list = new ArrayList<>();
        this.outputFile = "";
    }

    public Filters(String pathimage, String outputFile) {
        this.pathimage = pathimage;
        this.line_list = new ArrayList<>();
        this.outputFile = outputFile;
    }

    /**
     * Run the algorithms to segment the image. Show the result in a window and
     * save each treatment
     *
     * @since 1.0
     */
    @Override
    public void run() {

        Mat src = imread(this.pathimage); // Retrieves the image from the given path
        Mat sobelFltr, houghTrs, imgPyramid, imgPyramidAdd; // Create matrix
        Mat addition = new Mat();
        double alpha = 0.5, beta;

        //Display the source image  
        namedWindow("Undistorted image", WINDOW_NORMAL);
        imshow("Undistorted image", src);

        //Call the method to apply Sobel on the image on get the result in sobelFltr variable
        sobelFltr = this.SobelFilter(src);

        //Display the obtained image with the Sobel filter
        namedWindow("Sobel Filter - Simple Edge Detector", WINDOW_NORMAL);
        imshow("Sobel Filter - Simple Edge Detector", sobelFltr);
        imwrite(this.outputFile + "\\Sobel.jpg", sobelFltr);

        //Call the method to apply Hough transform on the image given by the result of the Sobel filter
        houghTrs = this.getHoughPTransform(sobelFltr);

        //Display the obtained image with the hough transform
        namedWindow("Hough Transform", WINDOW_NORMAL);
        imshow("Hough Transform", houghTrs);
        imwrite(this.outputFile + "\\HoughTrs.jpg", houghTrs);

        imgPyramid = this.imagePyramid(src);

        //Display the obtained image with the pyramidal transformation to zoom down
        namedWindow("Pyramid Zoom Down", WINDOW_NORMAL);
        imshow("Pyramid Zoom Down", imgPyramid);
        imwrite(this.outputFile + "\\PyramidDown.jpg", imgPyramid);

        //Get the Hough Image
        Mat sobelRes = imread("fusion\\Sobel.jpg");

        //Realize the add of the source and hough image
        beta = (1.0 - alpha);
        addWeighted(src, alpha, sobelRes, beta, 0.0, addition);

        //Display the obtained image of the add of Source and Hough image
        namedWindow("Add Source and Hough", WINDOW_NORMAL);
        imshow("Add Source and Hough", addition);
        imwrite(this.outputFile + "\\AddImage.jpg", addition);

        imgPyramidAdd = this.imagePyramid(addition);

        //Display the obtained image with the pyramidal transformation to zoom down
        namedWindow("Pyramid Zoom Down Add", WINDOW_NORMAL);
        imshow("Pyramid Zoom Down Add", imgPyramidAdd);
        imwrite(this.outputFile + "\\PyramidDownAdd.jpg", imgPyramidAdd);

        waitKey(0);
    }

    /**
     *
     * @param input
     * @return The image processed by the Sobel filter
     * @since 1.0 Apply the Sobel filter on the image to detect edges
     */
    public Mat SobelFilter(Mat input) {

        int scale = 1;
        int delta = 0;
        int ddepth = CV_16S;

        //Creation of Matrix
        Mat srcgray = new Mat();
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();
        Mat grad = new Mat();
        Mat result = new Mat();

        // Gaussian blur on the image
        GaussianBlur(input, input, new opencv_core.Size(3, 3), 4);

        /// Convert it to gray
        cvtColor(input, srcgray, CV_BGR2GRAY);

        /// Gradient X - Detect vertical edges
        Sobel(srcgray, grad_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT);
        convertScaleAbs(grad_x, abs_grad_x);

        /// Gradient Y - Detect horizontal edges
        Sobel(srcgray, grad_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT);
        convertScaleAbs(grad_y, abs_grad_y);

        /// Total Gradient (approximate)
        addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);

        // Convert to threshold zero image
        // threshold(grad, result, 40.0, 255.0, THRESH_TOZERO);
        threshold(grad, result, 40.0, 255.0, THRESH_TOZERO);

        // Return the image processed by the Sobel Filter
        return result;
    }

    /**
     *
     * @param input
     * @return The image processed by the Hough Transform
     * @since 1.1 Apply Hough Transformation on the image to detect lines
     */
    public Mat getHoughPTransform(Mat input) {

        IplImage image = new IplImage(input); // Convert to IplImage to be used in cvHoughLines2
        CvMemStorage storage = cvCreateMemStorage(0); //A storage for various OpenCV dynamic data structures
        CvSeq lines; //Dynamic data structures

        int thickness = 1; // Thickness of the circle outline, if positive. Negative thickness means that a filled circle is to be drawn.
        int shift = 0; // Number of fractional bits in the coordinates of the center and in the radius value.
        double rho = 1; // Distance resolution in pixel-related units
        double theta = Math.PI / 180; // Angle resolution measured in radians
        int threshold = 60; // Threshold parameter. A line is returned by the function if the corresponding accumulator value is greater than threshold
        ArrayList<Line> tempList = new ArrayList<>();

        //Probabilistic Hough transform returns line segments rather than the whole lines.
        //Every segment is represented by starting and ending points 
        lines = cvHoughLines2(image, storage, CV_HOUGH_PROBABILISTIC, rho, theta, threshold, 50, 10, 0, CV_PI);

        //Color image in black to only have the lines visible
        cvSet(image, CV_RGB(0, 0, 0));

        //For each line, get the coordonnates of the point 1 and 2
        for (int i = 0; i < lines.total(); i++) {
            Pointer line = cvGetSeqElem(lines, i);
            CvPoint pt1 = new CvPoint(line).position(0); //2D point with integer coordinates
            CvPoint pt2 = new CvPoint(line).position(1);

            //Display the coordonnates and the number of the line
            //System.out.println("- Line n° " + i);
            //System.out.println("\tp1: " + pt1);
            //System.out.println("\tp2: " + pt2 + "\n");
            Line lineObject = new Line(pt1, pt2);
            tempList.add(i, lineObject);
        }

        //Test the lines neighbours
        this.testNeighbours(tempList);

        //Draw the lines on the image
        this.drawLines(image, thickness, shift);

        //Convert IplImage to Mat
        Mat hough = new Mat(image);

        //Image with lines draw on it
        return hough;
    }

    /**
     *
     * @param input
     * @return The image divided by the pyrDown method
     * @since 1.2 Divide the image thanks to the pyrDown method
     */
    public Mat imagePyramid(Mat input) {

        Mat temp = new Mat(); // Matrix initialisation
        Mat output = new Mat();
        Mat temp2 = new Mat();
        Mat output2 = new Mat();
        Mat temp3 = new Mat();
        Mat output3 = new Mat();

        // First use of pyrDown, it will divide by two the size of the input image
        pyrDown(input, temp, new opencv_core.Size(input.cols() / 2, input.rows() / 2), BORDER_DEFAULT);

        // Second use of pyrDown, it will divide by two the size of the temporary image
        //   pyrDown(temp, output, new opencv_core.Size(temp.cols() / 2, temp.rows() / 2), BORDER_DEFAULT);
        //   pyrDown(output, temp2, new opencv_core.Size(output.cols() / 2, output.rows() / 2), BORDER_DEFAULT);
        //   pyrDown(temp2, output2, new opencv_core.Size(temp2.cols() / 2, temp2.rows() / 2), BORDER_DEFAULT);
        //   pyrDown(output2, temp3, new opencv_core.Size(output2.cols() / 2, output2.rows() / 2), BORDER_DEFAULT);
        // Return the image divided by four thanks to the pyrDown method
        return temp;
    }

    /**
     * Test distance between every line of the list of lines to reduce their
     * number if they are too close to each other
     *
     * @param list
     */
    private void testNeighbours(ArrayList<Line> list) {

        //Test a principal line's neighbours 
        for (int i = 0; i < list.size(); i++) {

            //Add the line to the final list
            Line line1 = list.get(i);
            this.line_list.add(line1);

            //Test every neighbours
            for (int j = 0; j < list.size(); j++) {

                //Test if the tested line is not the current main line
                if (!list.get(j).equals(list.get(i))) {
                    Line line2 = list.get(j);
                    //Distance gap
                    int gap = 0;
                    //Test of the distance between the two lines
                    if (distancePoints(line1.getPt1(), line2.getPt1()) >= gap && distancePoints(line1.getPt2(), line2.getPt2()) >= gap) {
                        //If its far, add it to the final list
                        this.line_list.add(line2);

                    } else {
                        list.remove(j);
                    }
                }
            }
        }

    }

    /**
     * Gives the distance between two points of the image. Used to calculate
     * distance between two lines
     *
     * @param pt1
     * @param pt2
     * @return
     */
    private double distancePoints(CvPoint pt1, CvPoint pt2) {

        double x = Math.pow(pt1.x() - pt2.x(), 2);
        double y = Math.pow(pt1.y() - pt2.y(), 2);

        return Math.sqrt(x + y);
    }

    /**
     * Draw the lines for the Hough Transform using the lines list
     *
     * @param image
     * @param thickness
     * @param shift
     */
    private void drawLines(IplImage image, int thickness, int shift) {

        for (int i = 0; i < this.line_list.size(); i++) {

            CvPoint pt1 = this.line_list.get(i).getPt1();
            CvPoint pt2 = this.line_list.get(i).getPt2();

            //Line method, draw a line between point 1 and 2 
            cvLine(image, pt1, pt2, cvScalar(255, 255, 255, 255), thickness, CV_AA, shift); // Draws a line segment connecting two points
        }

    }
}
