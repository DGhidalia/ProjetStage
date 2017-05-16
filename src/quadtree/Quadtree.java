/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.util.ArrayList;
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
    
    
    //         North
    //      .----.----.
    //      | NW | NE |
    // West '----'----' East
    //      | SW | SE |
    //      '----'----'
    //         South

    //TODO 1.1 Retirer la récursivité
    public void Segmentate(){
        
        Node current;

        ArrayList<Node> pile = new ArrayList<Node>();
        pile.add(_root);
                
        while(!pile.isEmpty()){
            current = pile.get(0);
            
            if(current.DetectionCouleur()){
                current.Découpage();
                pile.add(current.getNorthEast());
                pile.add(current.getNorthWest());
                pile.add(current.getSouthEast());
                pile.add(current.getSouthWest());
            }
            
            pile.remove(current);
        }
        
        
        
        waitKey(0);
        /*while(current.DetectionCouleur()){
            current.Découpage();
            
            current = current.getNorthEast();
        }*/
    }
}
