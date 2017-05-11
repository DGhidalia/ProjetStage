/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.indexer.FloatIndexer;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_calib3d.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

/**
 *
 * @author david.ghidalia
 */
public class Quadtree {

    private Node _root;
    
    public Quadtree(Node root){
        this._root = root;
    }
    
    //TODO 1.1 Retirer la récursivité
    public void Segmentate(){
        
        Node current = this._root;
        
        while(current.DetectionCouleur()){
            current.Découpage();
            
            current = current.getNorthEast();
        }
        /*this._root.Découpage();
        
        this._root.getNorthEast().Découpage();
        this._root.getNorthWest().Découpage();
        this._root.getSouthEast().Découpage();
        this._root.getSouthWest().Découpage();
        
        this._root.getNorthWest().getNorthWest().Découpage();
        this._root.getNorthWest().getNorthWest().getNorthWest().Découpage();*/
    }
}
