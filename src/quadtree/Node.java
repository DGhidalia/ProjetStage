/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.util.ArrayList;
import java.util.List;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
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

    //TODO Liste de node pour les "super Node"
    //Rcupérer le coté le plus long pour gérer si deux noeuds sont adjacents
    //gérer dans la fusion, il faut fusionner uniquement les noeuds qui vienneent d'etre créé au préalable avec le tableau.
    private boolean merged;

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }
    
    private ArrayList<Node> fils;
    
    private double _gap;
    
    private  Mat clone;
    
    private Mat _img;

    private Rect _ROI;
    
    private Node _father;

    public Node(Rect ROI, Mat img, Mat clone,double gap) {
        this.merged = false;
        this.clone =clone;
        
        this._gap = gap;
        
        this._ROI = ROI;

        this._img = img;

        this.fils = new ArrayList<Node>();
        
        this._father = null;
    }
    
    public Node(Rect ROI, Mat img, Node father, Mat clone,double gap) {
        this.merged = false;
        this.clone =clone;
        
        this._gap = gap;
        
        this._ROI = ROI;

        this._img = img;

        this.fils = new ArrayList<Node>();
        
        this._father = father;
    }

    /**
     * Separate the Node in 4 parts
     */
    public void Découpage() {

        int x = this.getROI().x();
        int y = this.getROI().y();

        int medianeX = this.getROI().width() / 2;
        int medianeY = this.getROI().height() / 2;

        if (this.getROI().width() % 2 == 1) {
            medianeX++;
        }
        if (this.getROI().height() % 2 == 1) {
            medianeY++;
        }
      
        //bon
        Rect r1 = new Rect(x, y, medianeX, medianeY);
        Node NW = new Node(r1, this.getImg(),this, getClone(),0.1);
        this.getFils().add(NW);
        //rectangle(getClone(), r1, new opencv_core.Scalar(255, 0, 0, 0), 0, 0, 0);

        //reculer X
        Rect r2 = new Rect();
        if (this.getROI().width() % 2 == 1) {
            r2 = new Rect(x + medianeX - 1, y, medianeX, medianeY);
        } else {
            r2 = new Rect(x + medianeX, y, medianeX, medianeY);
        }
        Node NE = new Node(r2, this.getImg(),this, getClone(),0.1);
        this.getFils().add(NE);
        //rectangle(getClone(), r2, new opencv_core.Scalar(255, 0, 0, 0), 0, 0, 0);

        //reculer Y
        Rect r3 = new Rect();
        if (this.getROI().height() % 2 == 1) {
            r3 = new Rect(x, y + medianeY - 1, medianeX, medianeY);
        } else {
            r3 = new Rect(x, y + medianeY, medianeX, medianeY);
        }
        Node SW = new Node(r3, this.getImg(),this, getClone(),0.1);
        this.getFils().add(SW);
        //rectangle(getClone(), r3, new opencv_core.Scalar(255, 0, 0, 0), 0, 0, 0);

        //reculer X et Y
        Rect r4 = new Rect();
        if (this.getROI().width() % 2 == 1) {
            x--;
        }
        if (this.getROI().height() % 2 == 1) {
            y--;
        }
        r4 = new Rect(x + medianeX, y + medianeY, medianeX, medianeY);
        Node SE = new Node(r4, this.getImg(),this, getClone(),0.1);
        this.getFils().add(SE);
        //rectangle(getClone(), r4, new opencv_core.Scalar(255, 0, 0, 0), 0, 0, 0);

        /*namedWindow("test", WINDOW_NORMAL);
        imshow("test", clone);

        waitKey(0);*/
    }

    /**
     * 
     * @return true if there is different color in Node
     */
    public boolean DetectionCouleur() {

        int x = 0;
        int y = 0;

        ArrayList<double[]> list = new ArrayList<double[]>();

        opencv_core.IplImage src = new opencv_core.IplImage(new Mat(this.getImg(), this.getROI()));

        int rx=(int) (Math.random() * this.getROI().width());
        int ry =(int) (Math.random() * this.getROI().height());

        CvScalar color = cvGet2D(src, ry, rx);

        double[] ref = new double[3];
        ref[0] = color.red();
        ref[1] = color.green();
        ref[2] = color.blue();
        
//        if (this.getROI().width() > 20) {
//            int avancement = this.getROI().width() / 10;
//
//            while (x <= this.getROI().width() && y <= this.getROI().height()) {
//                if (x * avancement >= this.getROI().width()) {
//                    x = 0;
//                    y++;
//                }
//
//                CvScalar _rgb = cvGet2D(src, y, x * avancement);
//
//                double[] rgb = new double[3];
//                rgb[0] = _rgb.val(3);
//                rgb[1] = _rgb.val(2);
//                rgb[2] = _rgb.val(1);
//
//                //rgb = rgbToHsv(rgb);
//                for (double[] ds : list) {
//                    if (this.distColorsRGB(ds, rgb) != gap) {
//                        return true;
//                    }
//                }
//
//                list.add(rgb);
//                x++;
//            }
//        } else if (this.getROI().height() > 20) {
//            int avancement = this.getROI().width();
//
//            while (x <= this.getROI().width() && y <= this.getROI().height()) {
//                if (y * avancement >= this.getROI().height()) {
//                    y = 0;
//                    x++;
//                }
//
//                CvScalar _rgb = cvGet2D(src, y * avancement, x);
//
//                double[] rgb = new double[3];
//                rgb[0] = _rgb.val(3);
//                rgb[1] = _rgb.val(2);
//                rgb[2] = _rgb.val(1);
//
//                //rgb = rgbToHsv(rgb);
//                for (double[] ds : list) {
//                    if (this.distColorsRGB(ds, rgb) != gap) {
//                        return true;
//                    }
//                }
//
//                list.add(rgb);
//                y++;
//            }
//        } else {
            
            for (x = 0; x < this.getROI().width(); x++) {
                for (y = 0; y < this.getROI().height(); y++) {
                    CvScalar _rgb = cvGet2D(src, y, x);

                    double[] rgb = new double[3];
                    rgb[0] = _rgb.red();
                    rgb[1] = _rgb.green();
                    rgb[2] = _rgb.blue();

                    if (this.distColorsRGB(ref, rgb) > this._gap) {
                        return true;
                    }
                }
        }
        return false;
    }

    public boolean canMergeNode(Node n){
        //TODO merge deux nodes puis gérer un premier étage!!!
        List<Pixel> PixelA = new ArrayList<>();
        List<Pixel> PixelB = new ArrayList<>();
        
        for(Pixel p : this.getPixelNorth()){
            PixelA.add(p);
        }
        for(Pixel p : this.getPixelSouth()){
            PixelA.add(p);
        }
        for(Pixel p : this.getPixelEast()){
            PixelA.add(p);
        }
        for(Pixel p : this.getPixelWest()){
            PixelA.add(p);
        }
        for(Pixel p : this.getPixelNorth()){
            PixelB.add(p);
        }
        for(Pixel p : this.getPixelSouth()){
            PixelB.add(p);
        }
        for(Pixel p : this.getPixelEast()){
            PixelB.add(p);
        }
        for(Pixel p : this.getPixelWest()){
            PixelB.add(p);
        }
        
        for(Pixel p : PixelA){
            for(Pixel p2 : PixelB){
                if(p.getX() == p2.getX() && p.getY() == p2.getY()){
                    int randX1 = (int)(Math.random() * this.getROI().width()) + this.getROI().x();
                    int randY1 = (int)(Math.random() * this.getROI().height()) + this.getROI().y();
                    
                    int randX2 = (int)(Math.random() * n.getROI().width()) + n.getROI().x();
                    int randY2 = (int)(Math.random() * n.getROI().height() + n.getROI().y());
                    
                    CvScalar _rgbA = cvGet2D(new IplImage(this._img),randY1,randX1);
                    double[] rgbA = new double[3];
                    rgbA[0] = _rgbA.red();
                    rgbA[1] = _rgbA.green();
                    rgbA[2] = _rgbA.blue();
                    
                    CvScalar _rgbB = cvGet2D(new IplImage(n._img),randY2,randX2);
                    double[] rgbB = new double[3];
                    rgbB[0] = _rgbB.red();
                    rgbB[1] = _rgbB.green();
                    rgbB[2] = _rgbB.blue();
                    
                    if(this.distColorsRGB(rgbA, rgbB) > this._gap){
                        return true;
                    }
                }
            }
        }        
        return false;
    }
    
    public boolean SonsAreLeaf(){
        int cpt = 0;
        for(Node n : this.fils){
            if(n.isLeaf())
                cpt++;
        }
        if(cpt >= 4)
            return true;
        return false;
    }
    
    public double[] rgbToHsv(double[] rgb) {
        if (rgb.length == 3 && rgb[1] <= 255 && 0 <= rgb[1] && 0 <= rgb[2] && rgb[2] <= 255 && 0 <= rgb[0] && rgb[0] <= 255) {
            double[] res = new double[3];
            double R = rgb[0] / 255;
            double G = rgb[1] / 255;
            double B = rgb[2] / 255;

            double Cmax = Math.max(R, Math.max(G, B));
            double Cmin = Math.min(R, Math.min(G, B));

            double delta = Cmax - Cmin;

            double H = 0, S, V;

            if (delta == 0) {
                H = 0;
            } else if (Cmax == R) {
                H = ((G - B) / delta);
            } else if (Cmax == G) {
                H = ((B - R) / delta) + 2;
            } else if (Cmax == B) {
                H = ((R - G) / delta) + 4;
            }

            H *= 60;
            if (H < 0) {
                H += 360;
            }

            if (Cmax == 0) {
                S = 0;
            } else {
                S = (delta / Cmax);
            }

            V = Cmax;

            res[0] = H;
            res[1] = S;
            res[2] = V;

            return res;
        } else {
            return null;
        }
    }

    public double distColors(double[] a, double[] b) {
        return Math.pow(Math.sin((a[0] * Math.PI) / 180) * a[1] * a[2] - Math.sin((b[0] * Math.PI) / 180) * b[1] * b[2], 2)
                + Math.pow(Math.cos((a[0] * Math.PI) / 180) * a[1] * a[2] - Math.cos((b[0] * Math.PI) / 180) * b[1] * b[2], 2)
                + Math.pow(a[2] - b[2], 2);
    }

    public double distColorsRGB(double[] a, double[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2) + Math.pow(a[2] - b[2], 2));
    }

    /**
     * 
     * @return List of pixel located on the North Side of ROI
     */
    public List<Pixel> getPixelNorth(){
        List<Pixel> res = new ArrayList<>();
        for(int i = this.getROI().x();i<this.getROI().width();i++){
            res.add(new Pixel(i,this.getROI().y()));
        }
        return res;
    }
    
    /**
     * 
     * @return List of pixel located on the West Side of ROI
     */
    public List<Pixel> getPixelWest(){
        List<Pixel> res = new ArrayList<>();
        for(int i = this.getROI().y();i < this.getROI().height();i++){
            res.add(new Pixel(this.getROI().x(),i));
        }
        return res;
    }
    /**
     * 
     * @return List of pixel located on the South Side of ROI
     */
    public List<Pixel> getPixelSouth(){
        List<Pixel> res = new ArrayList<>();
        for(int i = this.getROI().x();i<this.getROI().width();i++)
            res.add(new Pixel(i,this.getROI().y() + this.getROI().height()));
        return res;
    }
    
    /**
     * 
     * @return List of pixel located on the East Side of ROI
     */
    public List<Pixel> getPixelEast(){
        List<Pixel> res = new ArrayList<>();
        for(int i = this.getROI().y(); i < this.getROI().height();i++)
            res.add(new Pixel(this.getROI().x() + this.getROI().width(),i));
        return res;
    }
    
    /**
     * 
     * @return boolean if true then node is leaf else it is not
     */
    public boolean isLeaf() {
        if (this.getFils().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the clone
     */
    public Mat getClone() {
        return clone;
    }

    /**
     * @param clone the clone to set
     */
    public void setClone(Mat clone) {
        this.clone = clone;
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

    /**
     * @return the fils
     */
    public ArrayList<Node> getFils() {
        return fils;
    }

    /**
     * @param fils the fils to set
     */
    public void setFils(ArrayList<Node> fils) {
        this.fils = fils;
    }

    /**
     * @return the _father
     */
    public Node getFather() {
        return _father;
    }

    /**
     * @param _father the _father to set
     */
    public void setFather(Node _father) {
        this._father = _father;
    }

    boolean hasSon() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
