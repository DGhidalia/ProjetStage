/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacpp.indexer.UByteIndexer;

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
public class Quadtree implements Runnable {

    private Mat _src;

    private Node _root;

    private ArrayList<Region> regions_list;

    private IplImage ipl;

    private final double GAP;

    public Quadtree(Mat src) {
        //this._root = root;
        this._src = src;

        this.ipl = new IplImage(src);

        this.regions_list = new ArrayList<Region>();

        GAP = 25;
    }

    //         North
    //      .----.----.
    //      | NW | NE |
    // West '----'----' East
    //      | SW | SE |
    //      '----'----'
    //         South
    public void Segmentate() {

        Node current;

        ArrayList<Node> pile = new ArrayList<Node>();
        pile.add(_root);

        while (!pile.isEmpty()) {
            current = pile.get(0);

            if (current.DetectionCouleur()) {
                current.Découpage();
                if (!current.isLeaf()) {
                    for (int i = 0; i < current.getFils().size(); i++) {
                        pile.add(current.getFils().get(i));
                    }
                }
            }
            pile.remove(current);
        }

//        ArrayList<Node> FinalNode = this.getFinalNode();
//        System.out.println("Début mise en place régions");
//        
//        int cpt = 0;
//        for(Node n : FinalNode){
//            Region r = new Region(cpt);
//            int _x = n.getROI().x();
//            int _y = n.getROI().y();
//            for(int x = n.getROI().x(); x< _x + n.getROI().width();x++){
//                for(int y = n.getROI().y(); y < _y + n.getROI().height();y++){
//                    r.addMember(new Pixel(x,y));
//                }
//            }
//            Pixel centre = new Pixel(n.getROI().x() + (n.getROI().width()/2),n.getROI().y() + (n.getROI().height()/2));
//            r.setPointCentre(centre);
//            r.getNoeudRegion().add(n);
//            this.regions_list.add(r);
//            cpt++;
//        }
    }

    public double distColorsRGB(double[] a, double[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2) + Math.pow(a[2] - b[2], 2));
    }

    /**
     * Color the image depending on the regions
     *
     * @return
     */
    private IplImage color() {

        IplImage clone = this.ipl.clone();
        Collection<Region> values = this.regions_list;
        //Collection<Region> dark = this.rejected_regions.values();

        values.forEach((region) -> {
            //Total value of each color for each region
            int totRed = (int) (Math.random() * 255);
            int totGreen = (int) (Math.random() * 255);
            int totBlue = (int) (Math.random() * 255);
            //Calls a method to color a list of pixels
            List<Pixel> listPixel = region.getMembers();
            this.regionColor(totRed, totGreen, totBlue, listPixel, clone);
        });

        /*dark.forEach((r) -> {
            List<Pixel> listPixel = r.getMembers();
            this.regionColor(0, 0, 0, listPixel, clone);
        });*/
        return clone;
    }

    /**
     * Color all the pixels of a region with the average color of all those
     * pixels. Sub function only used with the global coloring function.
     *
     * @param red
     * @param green
     * @param blue
     * @param index
     */
    private void regionColor(int red, int green, int blue, List<Pixel> listPixel, IplImage clone) {
        UByteIndexer index = clone.createIndexer();
        for (int i = 0; i < listPixel.size(); i++) {
            Pixel pix = listPixel.get(i);

            index.put(pix.getY(), pix.getX(), 0, red);
            index.put(pix.getY(), pix.getX(), 1, green);
            index.put(pix.getY(), pix.getX(), 2, blue);
        }
        index.release();
    }

    /*public ArrayList<Node> fusion(Node node) {
        //si  il a des fils et si ces fils ne sont pas fusioner.
        boolean son = node.hasSon();
        ArrayList<Node> supernodes = new ArrayList<>();
        if (son) {
            ArrayList<Node> fils = node.getFils();
            //fusion des fils des fils
            for (Node fil : fils) {
                if (!fil.isMerged()) {
                    supernodes.addAll(fusion(fil));
                }
            }
//            node.clearSon();
//            for (SuperNode re : supernodes) {
//                node.getFils().add(re);
//            }
            //fusion des fils entre eux
            for (SuperNode sn : this.regionMerge(supernodes)) {
                supernodes.add(sn);
            }

        }else supernodes.add(node);
        return supernodes;
    }*/
    
    public void fusion(Node node){
        //si node a des fils on jump vers les autres fils
        boolean son = node.hasSon();
        
        if(son){
            ArrayList<Node> fils = node.getFils();
            for(Node fil: fils){
                fusion(fil);
            }
        }
        this.Merge(node);
    }

    public ArrayList<Node> getFinalNode() {
        Node current;

        ArrayList<Node> pile = new ArrayList<Node>();
        ArrayList<Node> FinalNode = new ArrayList<Node>();
        //ArrayList<Node> res = new ArrayList<Node>();
        pile.add(_root);

        while (!pile.isEmpty()) {
            current = pile.get(0);

            if (current.isLeaf()) {
                FinalNode.add(current);
            } else {
                for (Node n : current.getFils()) {
                    pile.add(n);
                }
            }
            pile.remove(0);
        }
        /*for (Node n : FinalNode) {
            Node f = n.getFather();
            if (!res.contains(f)) {
                res.add(f);
            }
        }*/
        return FinalNode;
    }

    //Passer une mat et les paramètres qu'on a besoin donc le gap
    //Sortie un arbre des régions
    @Override
    public void run() {

        this._root = new Node(new Rect(0, 0, this._src.size().width(), this._src.size().height()), this._src, this._src.clone(), this.GAP);

        System.out.println("Segmentation");
        this.Segmentate();

        System.out.println("Traitement region");
        this.Node2Region(this.getFinalNode());

        System.out.println("Coloration");
        Mat afficher = new Mat(this.color());

        namedWindow("test", WINDOW_NORMAL);
        imshow("test", afficher);

        waitKey(0);
    }

    private ArrayList<SuperNode> regionMerge(ArrayList<Node> fils) {
        HashMap<Node, SuperNode> superNodes = new HashMap<>();

        for (Node fils2 : fils) {
            for (Node fils1 : fils) {
                if (!fils1.equals(fils2) && this.needMerge(fils1, fils2)) {
                    //if at least one node at merge the both merge together
                    if (fils1.isMerged() == true || fils2.isMerged() == true) {
                        this.mergeIfOneHasMerge(fils1, fils2, superNodes);
                    } else {
                        //if not one are merged
                        SuperNode superNode = new SuperNode(fils1, fils2);
                        superNodes.put(fils1, superNode);
                        superNodes.put(fils2, superNode);
                        fils1.setMerged(true);
                        fils2.setMerged(true);
                    }
                }
            }
        }
        Collection<SuperNode> values = superNodes.values();
        ArrayList<SuperNode> finaleNode = new ArrayList<>();
        values.stream().filter((value) -> (!finaleNode.contains(value))).forEachOrdered((value) -> {
            finaleNode.add(value);
        });
        return finaleNode;
    }

    private boolean needMerge(Node fils1, Node fils2) {
        return true;/*
        Pixel pix1 = new Pixel((int) Math.random() * fils1.getROI().width(), (int) Math.random() * fils1.getROI().height());
        Pixel pix2 = new Pixel((int) Math.random() * fils2.getROI().width(), (int) Math.random() * fils2.getROI().height());

        //Récupération colors
        CvScalar color1 = cvGet2D(ipl, pix1.getY(), pix1.getX());
        CvScalar color2 = cvGet2D(ipl, pix2.getY(), pix2.getX());

        //Passage tab double
        double[] rgb1 = new double[3];
        rgb1[0] = color1.red();
        rgb1[1] = color1.green();
        rgb1[2] = color1.blue();

        double[] rgb2 = new double[3];
        rgb2[0] = color2.red();
        rgb2[1] = color2.green();
        rgb2[2] = color2.blue();

        if (this.distColorsRGB(rgb2, rgb1) == 0) {
            return true;
        }
        return false;*/
    }

    private void Node2Region(ArrayList<Node> fusion) {
        int cpt = 0;
        for (Node n : fusion) {
            Region nouv = new Region(cpt);
            /*SuperNode sn=(SuperNode) snu;
            for (Node n : sn.getMembers()) {
                if (n.getClass() == SuperNode.class) {
                    System.out.println("ragte");
                }
                for (int i = n.getROI().x(); i < n.getROI().width()+n.getROI().x(); i++) {
                    for (int j = n.getROI().y(); j < n.getROI().height()+n.getROI().y(); j++) {
                        Pixel pix = new Pixel(i, j);
                        nouv.addMember(pix);
                    }
                }
            }
            cpt++;
            this.regions_list.add(nouv);*/
            for(int i = n.getROI().x();i<n.getROI().width()+n.getROI().x();i++){
                for(int j = n.getROI().y();j<n.getROI().height()+n.getROI().y();j++){
                    Pixel pix = new Pixel(i,j);
                    nouv.addMember(pix);
                }
            }
            this.regions_list.add(cpt, nouv);
            cpt++;
        }
    }

    private void mergeBothUNDIST(Node fils1, Node fils2, HashMap<Node, SuperNode> superNodes) {
        //if one of the both nodes has already merged 
        if (fils1.isMerged()) {
            //get the super not of the son1
            SuperNode get = superNodes.get(fils1);
            if (fils2.isMerged()) {
                //merge the supernode
                //add the son2
                get.addNode(superNodes.get(fils2));//TODO gerer les supernode
            } else { //add the son2
                get.addNode(fils2);
                //flag the son2 is merged
                fils2.setMerged(true);
                superNodes.put(fils2, get);
            }
        }
    }

    private void mergeIfOneHasMerge(Node fils1, Node fils2, HashMap<Node, SuperNode> superNodes) {
        if (!fils1.equals(fils2)) {
            this.mergeBothUNDIST(fils1, fils2, superNodes);
            this.mergeBothUNDIST(fils2, fils1, superNodes);
        }
    }
}
