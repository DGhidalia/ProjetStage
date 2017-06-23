/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undistortion;

import java.io.IOException;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import org.bytedeco.javacpp.opencv_core.CvMat;
import static org.bytedeco.javacpp.opencv_core.cvCreateMat;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvScalarAll;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_NORMAL;
import static org.bytedeco.javacpp.opencv_highgui.cvNamedWindow;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import org.bytedeco.javacpp.opencv_imgcodecs;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static org.bytedeco.javacpp.opencv_imgproc.cvRemap;

/**
 * Class that gives tools to convert a 2D image into a 3D Point Cloud
 * (cam2world) and to convert 3D Point Cloud into a 2D image(world2cam) It uses
 * an object containing the camera data to calculate with the right informations
 * Those data are given by a text file
 *
 * @author jean-jacques
 */
public class Undistort implements Runnable{

    private final String pathimage;
    private final Cam2World model;
    private final float SF = 3;     // Zoom factor 

    //-------------------------------------------------------------------------
    /**
     * Initialize the attributes of the class
     *
     * @param pathimage the path to the image you want to undistort
     * @param pathConfig the path to the camara data config file
     * @throws IOException
     */
    public Undistort(String pathimage, String pathConfig) throws IOException {
        this.pathimage = pathimage;                 // initialize the path of the image
        this.model = new Cam2World(pathConfig);     // a structure containing the data of the camera
    }

    //-------------------------------------------------------------------------
    /**
     * Run the different algorithms to undistord the image.
     * Show the result in a window and save the undistorded 
     * 
     */
    @Override
    public void run() {

        opencv_core.IplImage src1 = opencv_imgcodecs.cvLoadImage(this.pathimage);      // create an image object
        opencv_core.IplImage dst_persp = opencv_core.cvCreateImage(cvGetSize(src1), 8, 3);   // create a destination image

        // Matrix of the source image
        CvMat mapx_persp = cvCreateMat(src1.height(), src1.width(), CV_32FC1);
        CvMat mapy_persp = cvCreateMat(src1.height(), src1.width(), CV_32FC1);

        //Call the method to undistort the image
        this.create_perspecive_undistortion_LUT(mapx_persp, mapy_persp);

        /*
        An OpenCV method, used to help the image undistortion.
        See the OpenCV Doc for more informations about this method
         */
        cvRemap(src1, dst_persp, mapx_persp, mapy_persp, CV_INTER_LINEAR + CV_WARP_FILL_OUTLIERS, cvScalarAll(0));

        //Display the source image and the obtained image
        cvNamedWindow("Original fisheye camera image", WINDOW_NORMAL);
        cvShowImage("Original fisheye camera image", src1);

        cvNamedWindow("Undistorted Perspective Image", WINDOW_NORMAL);
        cvShowImage("Undistorted Perspective Image", dst_persp);

        //Save the obtained image 
        cvSaveImage("undistorted_perspective.jpg", dst_persp);

        waitKey(0);
    }

    //-------------------------------------------------------------------------
    /**
     * Undistort a perspective image using matrix. Create Look-Up-Table for
     * perspective undistortion The undistortion is done on a plane
     * perpendicular to the camera axis
     *
     * @param mapx matrix based on the source image
     * @param mapy matrix based on the source image
     */
    private void create_perspecive_undistortion_LUT(CvMat mapx, CvMat mapy) {
        int i, j;
        int width = mapx.cols(); //New width
        int height = mapx.rows();//New height     
        FloatPointer data_mapx = mapx.data_fl();
        FloatPointer data_mapy = mapy.data_fl();
        float Nxc = (float) (height / 2.0);
        float Nyc = (float) (width / 2.0);
        float Nz = -width / this.SF;
        double[] M = new double[3];

        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                M[0] = (i - Nxc);
                M[1] = (j - Nyc);
                M[2] = Nz;
                double[] m = this.model.world2cam(M);
                data_mapx.position(i * width + j);
                data_mapx.put((float) m[1]);
                data_mapy.position(i * width + j);
                data_mapy.put((float) m[0]);
            }
        }
    }

}
