/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.freenect2.Frame;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.minMaxLoc;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_AUTOSIZE;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_NORMAL;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Rect;
import static org.bytedeco.javacpp.opencv_core.cvCopy;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvSetImageROI;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 *
 * @author david.ghidalia
 */
public class Quadtree {

    private Node _root;
    
    public Quadtree(Node root){
        this._root = root;
    }
    
    public void Segmentate(Node n){
        /*if(n.getHeight() > 1 && n.getWidth() > 1){
            int avancement = 0;
            boolean breakable = false;

            IplImage src = new IplImage(n.getImg()); 

            CvScalar rgb,rgb2;

            if(n.getWidth() > 10){
                avancement = n.getWidth() / 10;
            }
            else if(n.getHeight() > 10){
                avancement = n.getHeight() / 10;
            }

            for(int y = 0; y < n.getHeight();y++){
                for(int x = 0; x < n.getWidth();x++){
                    //si image a une largeur supérieur à 10 pixels
                    if(n.getWidth() > 10){
                        rgb = cvGet2D(src,y,x);
                        if(x <= 10){
                            rgb2 = cvGet2D(src,y,x*avancement);
                            if(distColors(rgb,rgb2) < 5 || distColors(rgb,rgb2) > 5){
                                breakable = true;
                                break;
                            }   
                        }
                        else{
                            break;
                        }
                    }
                    else{
                        rgb = cvGet2D(src,y,x);
                        if(x+1 < n.getWidth()){
                            rgb2 = cvGet2D(src,y,x+1);
                            if(distColors(rgb,rgb2) < 5 || distColors(rgb,rgb2) > 5){
                                breakable = true;
                                break;
                            }
                        }
                    }
                }
                if(breakable)
                    break;
            }

            //séparation en 4
            if(breakable){
                int medianeX = n.getWidth() / 2;
                int medianeY = n .getHeight() / 2;

                if(n.getWidth() % 2 == 1){
                    if(n.getHeight() % 2 == 1){
                        medianeX++;
                        medianeY++;

                        CvRect r1 = new CvRect(n.getX(),n.getY(),medianeX,medianeY);
                        cvSetImageROI(src,r1);
                        IplImage cropped1 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                        Node NW = new Node(medianeY,medianeX,n.getX(),n.getY(),new Mat(cropped1));
                        n.setNorthWest(NW);

                        CvRect r2 = new CvRect(n.getX()+medianeX-1,n.getY(),medianeX,medianeY);
                        cvSetImageROI(src,r2);
                        IplImage cropped2 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                        Node NE = new Node(medianeY,medianeX,n.getX()+medianeX-1,n.getY(),new Mat(cropped2));
                        n.setNorthEast(NE);

                        CvRect r3 = new CvRect(n.getX(),n.getY()+medianeY-1,medianeX,medianeY);
                        cvSetImageROI(src,r3);
                        IplImage cropped3 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                        Node SW = new Node(medianeY, medianeX, n.getX(), n.getY()+medianeY-1, new Mat(cropped3));
                        n.setSouthWest(SW);

                        CvRect r4 = new CvRect(n.getX()+medianeX-1, n.getY()+medianeY-1, medianeX, medianeY);
                        cvSetImageROI(src,r4);
                        IplImage cropped4 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                        Node SE = new Node(medianeY, medianeX, n.getX()+medianeX-1, n.getY()+medianeY-1, new Mat(cropped4));
                        n.setSouthEast(SE);

                        this.Break(NW);
                        this.Break(NE);
                        this.Break(SW);
                        this.Break(SE);
                    }
                    else{
                        medianeX++;

                        CvRect r1 = new CvRect(n.getX(),n.getY(),medianeX,medianeY);
                        cvSetImageROI(src,r1);
                        IplImage cropped1 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                        Node NW = new Node(medianeY,medianeX,n.getX(),n.getY(),new Mat(cropped1));
                        n.setNorthWest(NW);

                        CvRect r2 = new CvRect(n.getX()+medianeX-1,n.getY(),medianeX,medianeY);
                        cvSetImageROI(src,r2);
                        IplImage cropped2 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                        Node NE = new Node(medianeY,medianeX,n.getX()+medianeX-1,n.getY(),new Mat(cropped2));
                        n.setNorthEast(NE);

                        CvRect r3 = new CvRect(n.getX(),n.getY()+medianeY,medianeX,medianeY);
                        cvSetImageROI(src, r3);
                        IplImage cropped3 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                        Node SW = new Node(medianeY,medianeX,n.getX(),n.getY()+medianeY,new Mat(cropped3));
                        n.setSouthWest(SW);

                        CvRect r4 = new CvRect(n.getX()+medianeX-1,n.getY()+medianeY,medianeX,medianeY);
                        cvSetImageROI(src,r4);
                        IplImage cropped4 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                        Node SE = new Node(medianeY, medianeX, n.getX()+medianeX-1, n.getY()+medianeY,new Mat(cropped4));
                        n.setSouthEast(SE);

                        this.Break(NW);
                        this.Break(NE);
                        this.Break(SW);
                        this.Break(SE);
                    }
                }
                else if(n.getHeight() % 2 == 1){
                    medianeY++;

                    CvRect r1 = new CvRect(n.getX(),n.getY(),medianeX,medianeY);
                    cvSetImageROI(src,r1);
                    IplImage cropped1 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                    Node NW = new Node(medianeY,medianeX,n.getX(),n.getY(),new Mat(cropped1));
                    n.setNorthWest(NW);

                    CvRect r2 = new CvRect(n.getX() + medianeX,n.getY(),medianeX,medianeY);
                    cvSetImageROI(src,r2);
                    IplImage cropped2 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                    Node NE = new Node(medianeY,medianeX,n.getX()+medianeX,n.getY(),new Mat(cropped2));
                    n.setSouthEast(NE);

                    CvRect r3 = new CvRect(n.getX(),n.getY()+medianeY-1,medianeX,medianeY);
                    cvSetImageROI(src,r3);
                    IplImage cropped3 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                    Node SW = new Node(medianeY, medianeX, n.getX(), n.getY()+medianeY-1, new Mat(cropped3));
                    n.setSouthWest(SW);

                    CvRect r4 = new CvRect(n.getX()+medianeX,n.getY()+medianeY-1,medianeX,medianeY);
                    cvSetImageROI(src,r4);
                    IplImage cropped4 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                    Node SE = new Node(medianeY, medianeX, n.getX()+medianeX,n.getY()+medianeY-1, new Mat(cropped4));
                    n.setSouthEast(SE);

                    this.Break(NW);
                    this.Break(NE);
                    this.Break(SW);
                    this.Break(SE);
                }
                else{
                    CvRect r1 = new CvRect(n.getX(),n.getY(),medianeX,medianeY);
                    cvSetImageROI(src,r1);
                    IplImage cropped1 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                    Node NW = new Node(medianeY,medianeX,n.getX(),n.getY(),new Mat(cropped1));
                    n.setNorthWest(NW);

                    CvRect r2 = new CvRect(n.getX() + medianeX,n.getY(),medianeX,medianeY);
                    cvSetImageROI(src,r2);
                    IplImage cropped2 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                    Node NE = new Node(medianeY,medianeX,n.getX()+medianeX,n.getY(),new Mat(cropped2));
                    n.setSouthEast(NE);

                    CvRect r3 = new CvRect(n.getX(),n.getY()+medianeY,medianeX,medianeY);
                    cvSetImageROI(src, r3);
                    IplImage cropped3 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                    Node SW = new Node(medianeY,medianeX,n.getX(),n.getY()+medianeY,new Mat(cropped3));
                    n.setSouthWest(SW);

                    CvRect r4 = new CvRect(n.getX()+medianeX,n.getY()+medianeY,medianeX,medianeY);
                    cvSetImageROI(src,r4);
                    IplImage cropped4 = cvCreateImage(cvGetSize(src), src.depth(), src.nChannels());
                    Node SE = new Node(medianeY,medianeX,n.getX()+medianeX,n.getY()+medianeY,new Mat(cropped4));

                    this.Break(NW);
                    this.Break(NE);
                    this.Break(SW);
                    this.Break(SE);
                }
            }
        }*/
    }
    
    public void Découpage(Node n){
        
    }
    
    public boolean DetectionCouleur(Node n){
        return false;
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
}
