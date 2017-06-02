/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undistortion;

import filtersPackage.Filters;
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
        
        String img_path = "testSobelMulti.jpg";       
        
        String outputImg = "test_rg\\multi_result8.jpg";
        
        String outputFile = "\\fusion";
                        
        //RegionGrowing rg = new RegionGrowing(img_path,outputImg,6.5 ,true);
        
//        RegionGrowing rg = new RegionGrowing(img_path, outputImg,7 ,true);
//        
//        rg.run();
        
        
        Filters f = new Filters(outputImg, outputFile);
        
        f.run();
        
        
        
        
        
    }
    
    
}
