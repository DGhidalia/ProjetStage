/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import undistortion.Undistort;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import quadtree.Node;
import quadtree.Quadtree;

/**
 *
 * @author david.ghidalia
 */
public class main {
    
    public static void main(String[] args) throws IOException{        
        opencv_core.Mat img = imread("C:\\Users\\david.ghidalia\\Documents\\NetBeansProjects\\ProjetStage\\test\\ressource\\testDamier.png");
        
//        Node instance = new Node(new opencv_core.Rect(0,0,img.size().width(),img.size().height()),img,img.clone());
        
        Quadtree s = new Quadtree(img);
        
        s.run();
        
        //waitKey(0);
    }
    
    
}
