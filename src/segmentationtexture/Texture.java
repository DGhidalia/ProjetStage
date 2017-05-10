/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentationtexture;


import org.bytedeco.javacpp.opencv_core;

import static org.bytedeco.javacpp.opencv_core.CV_16S;
import static org.bytedeco.javacpp.opencv_imgproc.Sobel;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_core.addWeighted;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;
import static org.bytedeco.javacpp.opencv_core.BORDER_DEFAULT;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.GaussianBlur;
import static org.bytedeco.javacpp.opencv_core.convertScaleAbs;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_NORMAL;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_TOZERO;


/**
 *
 * @author cameron.mourot
 * @version 1.1
 */
public class Texture {

    private final String pathimage;

    public Texture(String pathimage) {
        this.pathimage = pathimage;
    }

    /**
     * Run the algorithm to segment the image. Show the result in a window and
     * save the texture based segmented image
     */
    public void run() {

        opencv_core.Mat src1 = imread(this.pathimage);  // create an image object
        opencv_core.Mat textr, textr1;  // create a destination image
        
        //Call the method to apply Sobel on the image
        textr = this.SobelFilter(src1);
        
        //Call the method to apply Hough transform on the image
      //  textr1 = this.getHoughPTransform(textr,2,2,2);
        
      // Mat texture1 = textr1;
        
        //Display the source image  
        namedWindow("Undistorted image", WINDOW_NORMAL);
        imshow("Undistorted image", src1); 

        //Display the obtained image with the Sobel filter
        namedWindow("Sobel Filter - Simple Edge Detector", WINDOW_NORMAL);
        imshow("Sobel Filter - Simple Edge Detector", textr);
        
        //Display the obtained image
        namedWindow("Hough Transform", WINDOW_NORMAL);
   //   cvShowImage("Hough Transform", texture1);
   
        //Save the obtained image 
        imwrite("C:\\Users\\cameron.mourot\\Documents\\GitHub\\ProjetStage\\Sobel.jpg", textr);

        waitKey(0);
    }

    /**
     *
     * @param input
     * @return The image processed by the Sobel filter
     * @since 1.1 Apply the Sobel filter on the image to detect edges
     */
    public opencv_core.Mat SobelFilter(opencv_core.Mat input) {

        int scale = 1;
        int delta = 0;
        int ddepth = CV_16S;

        opencv_core.Mat src        = new opencv_core.Mat(input);
        opencv_core.Mat srcgray    = new opencv_core.Mat();
        opencv_core.Mat grad_x     = new opencv_core.Mat();
        opencv_core.Mat grad_y     = new opencv_core.Mat();
        opencv_core.Mat abs_grad_x = new opencv_core.Mat();
        opencv_core.Mat abs_grad_y = new opencv_core.Mat();
        opencv_core.Mat grad       = new opencv_core.Mat();
        opencv_core.Mat res        = new opencv_core.Mat();
        
        // Gaussian blur on the image
        GaussianBlur(src, src, new opencv_core.Size(3,3), 4 );
        
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
        threshold(grad, res, 20.0, 255.0, THRESH_TOZERO);

        return res;
    }


    /**
     *
     * @param image
     * @param rho
     * @param theta
     * @param threshold
     * @return 
     * @since 1.1 Apply Hough Transformation on the image to detect lines 
     */ 
  /*  public opencv_core.Mat getHoughPTransform(opencv_core.Mat image, double rho, double theta, int threshold) {
        opencv_core.Mat result = null;
        opencv_core.Mat lines = null;
        Imgproc.HoughLinesP(image, lines, rho, theta, threshold);

        for (int i = 0; lines.cols() >= i; i++) {
            int[] val = lines.get(0, i);
            Imgproc.line(result, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255, 5), 2);
        }
        return result;
    }*/
    
    
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
