/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import undistortion.Undistort;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import quadtree.Segmentation;

/**
 *
 * @author pierre.renard
 */
public class main {
    
    public static void main(String[] args) throws IOException{        
               
        /*String pathconf = "test.txt";
        String pathImage = "test_img.jpg";
        
        try {
            Undistort und = new Undistort(pathImage, pathconf);
            und.run();
        } catch (IOException ex) {
            Logger.getLogger(Undistort.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        Segmentation s = new Segmentation();
        
        s.run(imread("C:\\Users\\david.ghidalia\\Documents\\PROJETJAVA\\result\\undistorted_perspective.jpg"));
        
        waitKey(0);
    }
    
    
}
