/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.util.ArrayList;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_NORMAL;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;

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
    
    private Rect _ROI;
    
    public Node(Rect ROI, Mat img){
        this._ROI = ROI;
        
        this._img = img;
        
        this._NorthEast = null;
        this._NorthWest = null;
        this._SouthEast = null;
        this._SouthWest = null;
    }
    
    public void Découpage(){
        
        int x = this.getROI().x();
        int y = this.getROI().y();

        int medianeX = this.getROI().width()/2;
        int medianeY = this.getROI().height()/2;
        
        if(this.getROI().width() % 2 == 1)
            medianeX++;
        if(this.getROI().height() % 2 == 1) 
            medianeY++;

        //bon
        Rect r1 = new Rect(x,y,medianeX,medianeY);
        Node NW = new Node(r1,this.getImg());
        this.setNorthWest(NW);
        rectangle(this.getImg(),r1,new opencv_core.Scalar(255,0,0,0),1,0,0);

        //reculer X
        Rect r2 = new Rect();
        if(this.getROI().width() % 2 == 1){
            r2 = new Rect(x+medianeX - 1,y,medianeX,medianeY);
        }
        else{
            r2 = new Rect(x+medianeX,y,medianeX,medianeY);
        }
        Node NE = new Node(r2,this.getImg());
        this.setNorthEast(NE);
        rectangle(this.getImg(),r2,new opencv_core.Scalar(255,0,0,0),1,0,0);

        //reculer Y
        Rect r3 = new Rect();
        if(this.getROI().height() % 2 == 1){
            r3 = new Rect(x,y+medianeY-1,medianeX,medianeY);
        }
        else{
            r3 = new Rect(x,y+medianeY,medianeX,medianeY);
        }
        Node SW = new Node(r3,this.getImg());
        this.setSouthWest(SW);
        rectangle(this.getImg(),r3,new opencv_core.Scalar(255,0,0,0),1,0,0);

        //reculer X et Y
        Rect r4 = new Rect();
        if(this.getROI().width() % 2 == 1)
            x--;
        if(this.getROI().height() % 2 == 1) {
            y--;
        }
        r4 = new Rect(x+medianeX,y+medianeY,medianeX,medianeY);
        Node SE = new Node(r4,this.getImg());
        this.setSouthEast(SE);
        rectangle(this.getImg(),r4,new opencv_core.Scalar(255,0,0,0),1,0,0);

        namedWindow("test",WINDOW_NORMAL);
        imshow("test",this.getImg());
 
        waitKey(0);
    }
    
    
    public boolean DetectionCouleur(){
        boolean res = false;
        
        int x = 0;
        int y = 0;
        
        ArrayList<double[]> list = new ArrayList<double[]>();
        
        opencv_core.IplImage src = new opencv_core.IplImage(new Mat(this.getImg(),this.getROI()));
        
        int avancement = 0;
        
        if(this.getROI().width() > 20){
            avancement = this.getROI().width()/10;
        }
        else{
            avancement = 1;
        }
            
        while((res == false) || (x <= this.getROI().width() && y <= this.getROI().height())){
            if(x*avancement >= this.getROI().width()){
                x = 0;
                y++;
            }

            opencv_core.CvScalar _rgb = cvGet2D(src,y, x * avancement);

            double[] rgb = new double[3];
            rgb[0] = _rgb.val(3);
            rgb[1] = _rgb.val(2);
            rgb[2] = _rgb.val(1);

            for (double[] ds : list) {
                if(this.distColors(ds, rgb) != 0){
                    res = true;
                }
            }
            
            list.add(rgb);
            x++;
        }     
        return res;
    }
    
    public double[] rgbToHsv(double[] rgb){
        if(rgb.length == 3 && rgb[1]<= 255 && 0 <= rgb[1] && 0 <= rgb[2] && rgb[2] <= 255 && 0 <= rgb[0] && rgb[0] <= 255){
            double[] res = new double[3];
            double R = rgb[0]/255;
            double G = rgb[1]/255;
            double B = rgb[2]/255;
            
            double Cmax = Math.max(R, Math.max(G, B));
            double Cmin = Math.min(R, Math.min(G, B));
            
            double delta = Cmax - Cmin;
            
            double H = 0,S,V;
            
            if(delta == 0){
                H = 0;
            }
            else if(Cmax == R){
                H = ((G-B)/delta);
            }
            else if(Cmax == G){
                H = ((B-R)/delta)+2;
            }
            else if(Cmax == B){
                H = ((R-G)/delta)+4;
            }
            
            H *= 60;
            if(H < 0)
                H+=360;
            
            if(Cmax == 0){
                S = 0;
            }
            else{
                S = (delta / Cmax);
            }
            
            V = Cmax;
            
            res[0] = H;
            res[1] = S;
            res[2] = V;
            
            return res;
        }
        else{
            return null;
        }
    }
    
    
    public double distColors(double[] a, double[] b){
        return Math.pow(Math.sin((a[0]*Math.PI)/180)*a[1]*a[2] - Math.sin((b[0]*Math.PI)/180)*b[1]*b[2],2)
                + Math.pow(Math.cos((a[0]*Math.PI)/180)*a[1]*a[2] - Math.cos((b[0]*Math.PI)/180)*b[1]*b[2],2)
                + Math.pow(a[2]-b[2],2);
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

    /**
     * @return the _ROI
     */
    public Rect getROI() {
        return _ROI;
    }

    /**
     * @param _ROI the _ROI to set
     */
    public void setROI(Rect _ROI) {
        this._ROI = _ROI;
    }
}
