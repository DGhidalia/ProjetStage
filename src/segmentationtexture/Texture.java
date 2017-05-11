/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentationtexture;

import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.opencv_core.CV_16S;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

/**
 *
 * @author cameron.mourot
 * @version 1.1
 */
public class Texture implements Runnable {

    private final String pathimage;

    public Texture(String pathimage) {
        this.pathimage = pathimage;
    }

    /**
     * Run the algorithm to segment the image. Show the result in a window and
     * save the texture based segmented image
     *
     * @since 1.0
     */
    @Override
    public void run() {

        Mat src = imread(this.pathimage);  // Retrieves the image from the given path
        Mat sobelFltr;
        IplImage houghTrs;

        //Display the source image  
        namedWindow("Undistorted image", WINDOW_NORMAL);
        imshow("Undistorted image", src);

        //Call the method to apply Sobel on the image on get the result in textr variable
        sobelFltr = this.SobelFilter(src);

        //Display the obtained image with the Sobel filter
        namedWindow("Sobel Filter - Simple Edge Detector", WINDOW_NORMAL);
        imshow("Sobel Filter - Simple Edge Detector", sobelFltr);

        //Call the method to apply Hough transform on the image given by the result of the Sobel filter
        houghTrs = this.getHoughPTransform(sobelFltr);
        Mat houghtest = new Mat(houghTrs);

        //Display the obtained image
        namedWindow("Hough Transform", WINDOW_NORMAL);

        if (!houghtest.empty()) {
            imshow("Hough Transform", houghtest);
        }

        //Save the obtained image at the path
        imwrite("C:\\Users\\cameron.mourot\\Documents\\GitHub\\ProjetStage\\Sobel.jpg", sobelFltr);
        imwrite("C:\\Users\\cameron.mourot\\Documents\\GitHub\\ProjetStage\\HoughTrs.jpg", houghtest);
        
        waitKey(0);
    }

    /**
     *
     * @param input
     * @return The image processed by the Sobel filter
     * @since 1.1 Apply the Sobel filter on the image to detect edges
     */
    public Mat SobelFilter(Mat input) {

        int scale = 1;
        int delta = 0;
        int ddepth = CV_16S;

        Mat src = new Mat(input);
        Mat srcgray = new Mat();
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();
        Mat grad = new Mat();
        Mat res = new Mat();

        // Gaussian blur on the image
        GaussianBlur(src, src, new opencv_core.Size(3, 3), 4);

        /// Convert it to gray
        cvtColor(src, srcgray, CV_BGR2GRAY);

        /// Gradient X - Detect vertical edges
        Sobel(srcgray, grad_x, ddepth, 1, 0, 3, scale, delta, BORDER_DEFAULT);
        convertScaleAbs(grad_x, abs_grad_x);

        /// Gradient Y - Detect horizontal edges
        Sobel(srcgray, grad_y, ddepth, 0, 1, 3, scale, delta, BORDER_DEFAULT);
        convertScaleAbs(grad_y, abs_grad_y);

        /// Total Gradient (approximate)
        addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad);

        // Convert to threshold zero image
        threshold(grad, res, 40.0, 255.0, THRESH_TOZERO);

        return res;
    }

    /**
     *
     * @param image
     * @return
     * @since 1.1 Apply Hough Transformation on the image to detect lines
     */
    public IplImage getHoughPTransform(Mat image) {
        IplImage dst = new IplImage(image);
        CvScalar white = new CvScalar(255,255,255,255);
        CvMemStorage storage = cvCreateMemStorage(0); //A storage for various OpenCV dynamic data structures
        CvSeq lines = new CvSeq(); //Dynamic data structures

        lines = cvHoughLines2(dst, storage, CV_HOUGH_PROBABILISTIC, 1, Math.PI / 180, 40, 50, 10, 0, CV_PI);
        for (int i = 0; i < lines.total(); i++) {
            Pointer line = cvGetSeqElem(lines, i);
            CvPoint pt1 = new CvPoint(line).position(0); //2D point with integer coordinates
            CvPoint pt2 = new CvPoint(line).position(1);

            System.out.println("Line spotted: ");
            System.out.println("\t pt1: " + pt1);
            System.out.println("\t pt2: " + pt2);
            cvLine(dst, pt1, pt2, white, 3, CV_AA, 0); // Draws a line segment connecting two points
        }
        return dst;
    }

    /**
     *
     * @param inputImg
     * @return The segmented image
     * @since 1.0 Realize the texture based segmentation of an input image
     */
    /* public IplImage TextureSegmentation(IplImage inputImg) {

        int width = inputImg.width();
        int height = inputImg.height();
        int i, j;

        for (i = 0; i < width; i++) {
            for (j = 0; j < height; j++) {

            }
        }

        System.out.println("Largeur : " + width + "\nHauteur : " + height);
        return null;
    }*/
}
