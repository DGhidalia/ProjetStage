/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.Mat;

/**
 *
 * @author david.ghidalia
 */
public class Node {
    
    private Node _NorthEast;
    private Node _NorthWest;
    private Node _SouthEast;
    private Node _SouthWest;
    
    private Mat _img;
    
    private CvRect _ROI;
    
    public Node(CvRect ROI, Mat img){
        this._ROI = ROI;
        
        this._img = img;
        
        this._NorthEast = null;
        this._NorthWest = null;
        this._SouthEast = null;
        this._SouthWest = null;
    }
    
    public boolean isLeaf(){
        if(this._NorthEast != null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * @return the _NorthEast
     */
    public Node getNorthEast() {
        return _NorthEast;
    }

    /**
     * @param _NorthEast the _NorthEast to set
     */
    public void setNorthEast(Node _NorthEast) {
        this._NorthEast = _NorthEast;
    }

    /**
     * @return the _NorthWest
     */
    public Node getNorthWest() {
        return _NorthWest;
    }

    /**
     * @param _NorthWest the _NorthWest to set
     */
    public void setNorthWest(Node _NorthWest) {
        this._NorthWest = _NorthWest;
    }

    /**
     * @return the _SouthEast
     */
    public Node getSouthEast() {
        return _SouthEast;
    }

    /**
     * @param _SouthEast the _SouthEast to set
     */
    public void setSouthEast(Node _SouthEast) {
        this._SouthEast = _SouthEast;
    }

    /**
     * @return the _SouthWest
     */
    public Node getSouthWest() {
        return _SouthWest;
    }

    /**
     * @param _SouthWest the _SouthWest to set
     */
    public void setSouthWest(Node _SouthWest) {
        this._SouthWest = _SouthWest;
    }

    /**
     * @return the _img
     */
    public Mat getImg() {
        return _img;
    }

    /**
     * @param _img the _img to set
     */
    public void setImg(Mat _img) {
        this._img = _img;
    }
}
