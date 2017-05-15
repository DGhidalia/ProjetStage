/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undistortion;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import segmentation.RegionGrowing;

/**
 *
 * @author pierre.renard
 */
public class main {
    
    public static void main(String[] args) throws IOException{        
               
        /*String pathconf = "testCam1.txt";
        String pathImage = "test_algos.jpg";
        
        try {
            Undistort und = new Undistort(pathImage, pathconf);
            und.run();
        } catch (IOException ex) {
            Logger.getLogger(Undistort.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        RegionGrowing rg = new RegionGrowing("C:\\Users\\pierre.renard\\Desktop\\ProjetStage\\ProjetStage\\undistorted_perspective.jpg", 20);
        rg.run();
        
        
    }
    
    
}
