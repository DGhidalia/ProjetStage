/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undistortion;

import java.io.IOException;
import segmentation.RegionGrowing;

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
        
        new RegionGrowing("C:\\Users\\pierre.renard\\Desktop\\ProjetStage\\ProjetStage\\undistorted_perspective.jpg", 50);
        
        
    }
    
    
}
