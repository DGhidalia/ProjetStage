/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filtersPackage;

import org.bytedeco.javacpp.opencv_core.CvPoint;

/**
 *
 * @author pierre.renard
 * @version 1.0
 */
public class Line {
    
    private CvPoint pt1;
    private CvPoint pt2;
    
    /**
     * 
     * @param pt1
     * @param pt2 
     */
    public Line(CvPoint pt1, CvPoint pt2){
        this.pt1 = pt1;
        this.pt2 = pt2;
    }

    /**
     * 
     * @return 
     */
    public CvPoint getPt1() {
        return pt1;
    }

    /**
     * 
     * @return 
     */
    public CvPoint getPt2() {
        return pt2;
    }
    
}
 