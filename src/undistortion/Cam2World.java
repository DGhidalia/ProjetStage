/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undistortion;

import java.io.IOException;
import static java.lang.Math.atan;
import static java.lang.Math.sqrt;

/**
 * Class that gives tools to convert a 2D image into a 3D Point Cloud
 * (cam2world) and to convert 3D Point Cloud into a 2D image(world2cam) It uses
 * an object containing the camera data to calculate with the right informations
 * Those data are given by a text file
 *
 * @author pierre.renard
 * @author cameron.mourot
 * @version 1.1
 * @since 1.0
 */
public class Cam2World {

    //The camera properties
    private CamModel model;

    /**
     * Initialize the camera data from a text file The informations about the
     * norm of the text file are given in a sample text file
     *
     * @param cameraDataPath Path to the txt file containing the camera data
     * @throws IOException
     */
    public Cam2World(String cameraDataPath) throws IOException {

        this.model = new CamModel(cameraDataPath);

    }    
    
    //2D POINT TO 3D POINT//
    //-------------------------------------------------------------------------

    /**
     * Method to convert a 2D point in a 3D point by giving a table with the x
     * and y coordinates of a 2D point.
     * <p>
     * It only converts one point using his coordinates, not a whole image. This
     * method is used to help the undistortion of an image by passing it in a 3D
     * PointCloud
     *
     *
     * @param point2D Table of double that contains x and y coordinates
     * @return A table of double that contains the 3D coordinates
     */
    public double[] cam2world(double[] point2D) {

        //Table that will contain the x,y and z coordinates of a 3D point
        double[] point3D = new double[3];

        //Using the camera's data
        double[] pol = model.getPol();
        double centerX = model.getCenterX();
        double centerY = model.getCenterY();
        double c = model.getC();
        double d = model.getD();
        double e = model.getE();
        int length_pol = model.getLength_pol();
        double invdet = 1 / (c - d * e); // 1/det(A), where A = [c,d;e,1] 

        double xp = invdet * ((point2D[0] - centerX) - d * (point2D[1] - centerY));
        double yp = invdet * (-e * (point2D[0] - centerX) + c * (point2D[1] - centerY));

        double r = sqrt(xp * xp + yp * yp); //distance [pixels] of  the point from the image center
        double zp = pol[0];
        double r_i = 1;
        int i;

        for (i = 1; i < length_pol; i++) {
            r_i *= r;
            zp += r_i * pol[i];
        }

        //normalize to unit norm
        double invnorm = 1 / sqrt(xp * xp + yp * yp + zp * zp);

        //Fill the table with the new coordinates
        point3D[0] = invnorm * xp;
        point3D[1] = invnorm * yp;
        point3D[2] = invnorm * zp;

        return point3D;
    }
    
    //3D POINT TO 2D POINT//
    //-------------------------------------------------------------------------

    /**
     * Method to convert a 3D point in 2D points by giving a table with the x, y
     * and z coordinates of a 3D point.
     * <p>
     * It only converts one point using his coordinates, and not a whole image.
     * This method is used to help the convetion of a 3D PointCloud into a 2D
     * image. For exemple, if you undistort an image by using 3D Point Cloud
     * convertion, you may use this method to pass it point by point in a 2D
     * udistorded image.
     *
     *
     * @param point3D Table of double that contains x, y and z coordinates
     * @return A table of double that contains de coordinates of a 2D points
     */
    public double[] world2cam(double[] point3D) {

        //Table that will contain the x and y coordinates of the 2D point
        double[] point2D = new double[2];

        //Using the camera's datas
        double[] invpol = model.getInvpol();
        double centerX = model.getCenterX();
        double centerY = model.getCenterY();
        double c = model.getC();
        double d = model.getD();
        double e = model.getE();
        int length_invpol = model.getLength_invpol();

        double norm = sqrt(point3D[0] * point3D[0] + point3D[1] * point3D[1]);  //Get the vector norm
        double theta = atan(point3D[2] / norm);     //Arctangent of the vector norm 
        double t, t_i;
        double rho, x, y;
        double invnorm;

        if (norm != 0) {
            invnorm = 1 / norm;
            t = theta;
            rho = invpol[0];
            t_i = 1;

            for (int i = 1; i < length_invpol; i++) {
                t_i *= t;
                rho += t_i * invpol[i];
            }

            x = point3D[0] * invnorm * rho;
            y = point3D[1] * invnorm * rho;

            //Fill the table with the calculated coordinates
            point2D[0] = x * c + y * d + centerX;
            point2D[1] = x * e + y + centerY;
        } else {
            point2D[0] = centerX;
            point2D[1] = centerY;
        }

        return point2D;
    }
}
