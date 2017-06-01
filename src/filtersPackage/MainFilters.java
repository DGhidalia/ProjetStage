/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filtersPackage;

import segmentation.RegionGrowing;

/**
 *
 * @author cameron.mourot
 */
public class MainFilters {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String pathimage = "C:\\Users\\cameron.mourot\\Documents\\GitHub\\ProjetStage\\multiprise2.jpg";
      //  String pathimage = "C:\\Users\\cameron.mourot\\Documents\\GitHub\\ProjetStage\\results\\AddImage.jpg";
      //  double gap = 150;

        /*    RegionGrowing region = new RegionGrowing(pathimage,gap);
        region.run(); */
        
        Filters filt = new Filters(pathimage);
        filt.run(); 
        
    }
}
