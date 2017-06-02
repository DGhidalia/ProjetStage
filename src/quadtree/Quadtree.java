/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.util.ArrayList;
import java.util.Collection;
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

    public void Merge() {
        System.out.println("Début Merge");

        //this.getFinalNode();//noeud Pere des noeuds finaux
        for (Node n : this.getFinalNode()) {
            for (Node n2 : n.getFils()) {
                for (Node n3 : n.getFils()) {
                    //TODO Revenir ici
                    if (n2 != n3 && n2.canMergeNode(n3)) {

                    }
                }
            }
        }
//        ArrayList<Region> blacklist = new ArrayList<Region>();
//        
//        for(Region r : this.regions_list){
//            boolean find = false;
//            Region aTest = null;
//            
//            if(!blacklist.contains(r)){
//                for(Region reg : this.regions_list){
//                    if(!blacklist.contains(reg)){
//                        //TODO 1.1 Supprimer le true
//                        if(reg != r && this.canMerge(r, reg) || true){
//                            find = true;
//                            aTest = reg;
//                            break;
//                        }
//                    }
//                }
//                //int rand = (int) (Math.random() * this.regions_list.size());
//
//                //Region aTest = this.regions_list.get(rand);
//
//                //if(!blacklist.contains(r) || !blacklist.contains(aTest)){
//                    if(find){
//                        r.Merge(aTest);
//                        blacklist.add(aTest);
//                    }
//           }
//            //}
//        }
//        for(Region r : blacklist){
//            this.regions_list.remove(r);
//        }
    }

    public boolean canMergeRegion(Region a, Region b) {
        List<Pixel> PixelA = new ArrayList<>();
        List<Pixel> PixelB = new ArrayList<>();

        for (Node n : a.getNoeudRegion()) {
            for (Pixel p : n.getPixelNorth()) {
                PixelA.add(p);
            }
            for (Pixel p : n.getPixelEast()) {
                PixelA.add(p);
            }
            for (Pixel p : n.getPixelSouth()) {
                PixelA.add(p);
            }
            for (Pixel p : n.getPixelWest()) {
                PixelA.add(p);
            }
        }
        for (Node n : b.getNoeudRegion()) {
            for (Pixel p : n.getPixelNorth()) {
                PixelB.add(p);
            }
            for (Pixel p : n.getPixelEast()) {
                PixelB.add(p);
            }
            for (Pixel p : n.getPixelSouth()) {
                PixelB.add(p);
            }
            for (Pixel p : n.getPixelWest()) {
                PixelB.add(p);
            }
        }

        for (Pixel p : PixelA) {
            if (PixelB.contains(p)) {
                int randA = (int) (Math.random() * PixelA.size());
                int randB = (int) (Math.random() * PixelB.size());

                Pixel pA = PixelA.get(randA);
                Pixel pB = PixelB.get(randB);

                CvScalar _rgbA = cvGet2D(this.ipl, pA.getY(), pA.getX());
                CvScalar _rgbB = cvGet2D(this.ipl, pB.getY(), pB.getX());

                double[] rgbA = new double[3];
                rgbA[0] = _rgbA.red();
                rgbA[1] = _rgbA.green();
                rgbA[2] = _rgbA.blue();

                double[] rgbB = new double[3];
                rgbB[0] = _rgbB.red();
                rgbB[1] = _rgbB.green();
                rgbB[2] = _rgbB.blue();

                if (this.distColorsRGB(rgbA, rgbB) > this.GAP) {
                    return true;
                }
            }
        }

        return false;
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

        values.forEach((r) -> {
            //Total value of each color for each region
            int totRed = (int) (Math.random() * 255);
            int totGreen = (int) (Math.random() * 255);
            int totBlue = (int) (Math.random() * 255);
            //Calls a method to color a list of pixels
            List<Pixel> listPixel = r.getMembers();
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

    public void fusion(Node node) {
        //si  il a des fils et si ces fils ne sont pas fusioner.
        boolean son = node.hasSon();
        if (son) {
            ArrayList<Node> fils = node.getFils();
            //fusion des fils des fils
            for (Node fil : fils) {
                if (!fil.isMerged()) {
                    fusion(fil);
                }
            }
            //fusion des fils entre eux
            this.regionMerge(fils);
        }
        node.setMerged(true);
    }

    public ArrayList<Node> getFinalNode() {
        Node current;

        ArrayList<Node> pile = new ArrayList<Node>();
        ArrayList<Node> FinalNode = new ArrayList<Node>();
        ArrayList<Node> res = new ArrayList<Node>();
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
        }
        for (Node n : FinalNode) {
            Node f = n.getFather();
            if (!res.contains(f)) {
                res.add(f);
            }
        }
        return res;
    }

    //Passer une mat et les paramètres qu'on a besoin donc le gap
    //Sortie un arbre des régions
    @Override
    public void run() {

        this._root = new Node(new Rect(0, 0, this._src.size().width(), this._src.size().height()), this._src, this._src.clone(), this.GAP);

        this.Segmentate();

        this.Merge();

        Mat afficher = new Mat(this.color());

        System.out.println("colorisation");
        namedWindow("test", WINDOW_NORMAL);
        imshow("test", afficher);

        waitKey(0);
    }

    private void regionMerge(ArrayList<Node> fils) {
        int distanceE = Math.max(   this._root.getROI().height(),    this._root.getROI().width());
    ArrayList<Node>finale =new ArrayList<>();
        boolean[][] indexMerge = new boolean[fils.size()][fils.size()];
        for (int i = 0; i < fils.size(); i++) {
            for (int j = i + 1; j < fils.size(); j++) {
                Node ref = fils.get(i);
                Node test = fils.get(j);
               if(this.canMerge(ref,test)){
                   indexMerge[i][j]=true;
               }
            }
            //parcour du tableau pour la fusion
        }
    }

    //test la fusion de deux regions et creqtion d un nouvelle region dont tous les pixel ont lq couleur moyenne des deux regions
    private Node merge(Node ref, Node test) {
        // un pixel random de chaque regions
        //tester distqnce couleur
        /*
        si distqnce couleur ok, test distance euclidienne des coordonne dqns l imqge < somme tqille plus grqnd cote des deux regions;
        
        si fusion creqtuion new region qvec tout les pixel
        */
        return null;
    }

    private boolean canMerge(Node ref, Node test) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
