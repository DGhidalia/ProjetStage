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
        
        String img_path = "C:\\Users\\pierre.renard\\Documents\\!TEST_TPS_EXEC\\600_600.jpg";       
        
        String outputImg = "C:\\Users\\pierre.renard\\Documents\\!TEST_TPS_EXEC\\Results\\600_600_R.jpg";
        
        
        
        RegionGrowing rg = new RegionGrowing(img_path, outputImg, 8 ,true);
        
        rg.run();
        
        
        
        
        
        //String outputFile = "fusion_2\\";
        
        /*Filters f = new Filters(img_path, outputFile);
        
        f.run();*/
        
        
        
        
        
    }
    
    
}
