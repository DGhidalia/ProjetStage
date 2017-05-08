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
public class Segmentation {

    
    
    public void run(Mat src){
        Node root = new Node(src.size().height(),src.size().width(),0,0,src);
        
        Break2(root);
        
        namedWindow("Résultat",WINDOW_NORMAL);
        imshow("Résultat",src);
    }
    
    public void Break2(Node n){
        if(n.getHeight() > 1 && n.getWidth() > 1){
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
                            if(distDeuxCouleur(rgb,rgb2) < 5 || distDeuxCouleur(rgb,rgb2) > 5){
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
                            if(distDeuxCouleur(rgb,rgb2) < 5 || distDeuxCouleur(rgb,rgb2) > 5){
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

                        this.Break2(NW);
                        this.Break2(NE);
                        this.Break2(SW);
                        this.Break2(SE);
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

                        this.Break2(NW);
                        this.Break2(NE);
                        this.Break2(SW);
                        this.Break2(SE);
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

                    this.Break2(NW);
                    this.Break2(NE);
                    this.Break2(SW);
                    this.Break2(SE);
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

                    this.Break2(NW);
                    this.Break2(NE);
                    this.Break2(SW);
                    this.Break2(SE);
                }
            }
        }
    }
    
    
    /*public void Break(Mat src,Node n){
        boolean end = false;
        
        int x = 1;
        int y = 0;
        int h = n.getHeight()/2;
        int w = n.getWidth()/2;
        CvScalar a;
        
        if(n.getHeight() % 2 == 1){
            h++;
        }
        if(n.getWidth() % 2 == 1){
            w++;
        }
        
        IplImage Iplsrc = new IplImage(src);
        
        if(src.size().width() < 10){
            int avancement = src.size().width() / 10;
            while(!end || (x == src.size().width() && y == src.size().height())){
                if(x-1 < 0){
                    
                }
            }
        }
        else if(src.size().height() < 10){
            
        }
        //Faire une comparaison entre les pixels 10%
        if(src.size().width() > 1 && src.size().height() > 1){
            while(!end || (x == src.size().width() && y == src.size().height())){
                if(x-1 < 0){
                    a = cvGet2D(Iplsrc,y-1,src.size().width());
                }
                else{
                    a = cvGet2D(Iplsrc,y,x-1);
                }
                CvScalar b = cvGet2D(Iplsrc,y,x);

                if(this.distDeuxCouleur(a,b) > 2){
                    end = true;
                }

                x++;
                if(x > src.size().width()){
                    x = 0;
                    y++;
                }
            }
        }
        
        if(end == true){
            if(n.getHeight()>= 2 && n.getWidth() >= 2){
                Node NW = new Node(h,w,n.getX(),n.getY());
                n.setNorthWest(NW);
                CvRect r1 = new CvRect();
                r1.x(NW.getX());
                r1.y(NW.getY());
                r1.width(NW.getWidth());
                r1.height(NW.getHeight());
                cvSetImageROI(Iplsrc,r1);
                IplImage cropped1 = cvCreateImage(cvGetSize(Iplsrc), Iplsrc.depth(), Iplsrc.nChannels());
                cvCopy(Iplsrc,cropped1);
                //cvSaveImage("test1.png",cropped1);
                this.Break(new Mat(cropped1), NW);

                Node NE = new Node(h,w,n.getX()+w,n.getY());
                n.setNorthEast(NE);
                CvRect r2 = new CvRect();
                r2.x(NE.getX());
                r2.y(NE.getY());
                r2.width(NE.getWidth());
                r2.height(NE.getHeight());
                cvSetImageROI(Iplsrc,r2);
                IplImage cropped2 = cvCreateImage(cvGetSize(Iplsrc), Iplsrc.depth(), Iplsrc.nChannels());
                cvCopy(Iplsrc,cropped2);
                //cvSaveImage("test2.png",cropped2);
                this.Break(new Mat(cropped2), NE);

                Node SW = new Node(h,w,n.getX(),n.getY()+h);
                n.setSouthWest(SW);
                CvRect r3 = new CvRect();
                r3.x(SW.getX());
                r3.y(SW.getY());
                r3.width(SW.getWidth());
                r3.height(SW.getHeight());
                cvSetImageROI(Iplsrc,r3);
                IplImage cropped3 = cvCreateImage(cvGetSize(Iplsrc), Iplsrc.depth(), Iplsrc.nChannels());
                cvCopy(Iplsrc,cropped3);
                //cvSaveImage("test3.png",cropped3);
                this.Break(new Mat(cropped3), SW);

                Node SE = new Node(h,w,n.getX()+w,n.getY()+h);
                n.setSouthEast(SE);
                CvRect r4 = new CvRect();
                r4.x(SE.getX());
                r4.y(SE.getY());
                r4.width(SE.getWidth());
                r4.height(SE.getHeight());
                cvSetImageROI(Iplsrc,r4);
                IplImage cropped4 = cvCreateImage(cvGetSize(Iplsrc), Iplsrc.depth(), Iplsrc.nChannels());
                cvCopy(Iplsrc,cropped4);
                //cvSaveImage("test4.png",cropped4);
                this.Break(new Mat(cropped4), SE);
            }
        }
    }*/
    
    private static double distDeuxCouleur(CvScalar a, CvScalar b){
        return Math.sqrt(Math.pow(a.val(0)-b.val(0), 2) + Math.pow(a.val(1)-b.val(1), 2) + Math.pow(a.val(2)-b.val(2), 2));
    }
}
