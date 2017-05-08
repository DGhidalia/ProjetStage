/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

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
    
    private int _height;
    private int _width;
    private int _x;
    private int _y;
    
    public Node(int h, int w, int x, int y, Mat img){
        this._height = h;
        this._width = w;
        this._x = x;
        this._y = y;
      
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
     * @return the height
     */
    public int getHeight() {
        return _height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this._height = height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return _width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this._width = width;
    }

    /**
     * @return the x
     */
    public int getX() {
        return _x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this._x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return _y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this._y = y;
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
