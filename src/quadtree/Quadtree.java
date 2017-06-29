/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bytedeco.javacpp.indexer.UByteIndexer;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

/**
 *
 * @author david.ghidalia
 */
public class Quadtree implements Runnable {

    private Mat _src;

    private Node _root;

    private List<Region> regions_list;

    private IplImage ipl;

    private final double GAP;

    public Quadtree(Mat src) {
        //this._root = root;
        this._src = src;

        this.ipl = new IplImage(src);

        this.regions_list = new ArrayList<>();

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

        ArrayList<Node> pile = new ArrayList<>();
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
    
    /**
     * 
     * @param node
     * @return 
     */
    public List<Region> fusion(Node node) {
        //si node a des fils on jump vers les autres fils
        boolean son = node.hasSon();
        List<Region> regs = new ArrayList<>();
        if (son) {
            ArrayList<Node> fils = node.getFils();
            for (Node fil : fils) {
                regs.addAll(fusion(fil));
            }
        }
        //ajoute les regions du noeud
        if (node.getRegion() != null) {
            regs.add(node.getRegion());
        }
        //fusionner la list
        this.Merge(regs);
        return regs;
    }
    
    /**
     * 
     * @return list of the Node which have no son
     */
    public ArrayList<Node> getFinalNode() {
        Node current;

        ArrayList<Node> pile = new ArrayList<>();
        ArrayList<Node> FinalNode = new ArrayList<>();
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
        this.regions_list = this.fusion(_root);

        System.out.println("Coloration");
        Mat afficher = new Mat(this.color());

        namedWindow("test", WINDOW_NORMAL);
        imshow("test", afficher);

        waitKey(0);
    }

    /**
     * Associate every node to a region 
     * @param fusion 
     */
    private void Node2Region(ArrayList<Node> fusion) {
        int cpt = 0;
        for (Node n : fusion) {
            Region nouv = new Region(cpt);
            for (int i = n.getROI().x(); i < n.getROI().width() + n.getROI().x(); i++) {
                for (int j = n.getROI().y(); j < n.getROI().height() + n.getROI().y(); j++) {
                    Pixel pix = new Pixel(i, j);
                    nouv.addMember(pix);
                }
            }
            n.setRegion(nouv);
            cpt++;
        }
    }

    /**
     * 
     * @param regs 
     * @return list of the merged region
     */
    private List<Region> Merge(List<Region> regs) {
        List<List<Integer>> mergedLink = new ArrayList<>();
        for (int i = 0; i < regs.size(); i++) {
            //regions that need to merge with the ith region
            List<Integer> mergedRegI = new ArrayList<>();
            //get all regions that merge with the ith region
            for (int j = i + 1; j < regs.size(); j++) {
                boolean needmerge = this.needMerge(regs.get(i), regs.get(j));
                if (needmerge) {
                    mergedRegI.add(j);
                }
            }
            mergedLink.add(mergedRegI);
        }
        //merge arrays
        Quadtree.mergeArray(mergedLink);
        
          List<Region> result = new ArrayList<>();
        for (int i = 0; i < mergedLink.size(); i++) {
            Region merged = new Region(i);
            //merge all region of the current list
            for (Integer ind : mergedLink.get(i)) {
                merged.merge(regs.get(ind));
            }
            result.add(merged);
        }
        return result;

        //get all merged regions
    }

    /**
     * Set if two regeion needs to be merge
     * @param reg1
     * @param reg2
     * @return boolean  
     */
    private boolean needMerge(Region reg1, Region reg2) {
        //return true;//todo a completer
        Pixel pix1 = reg1.getMembers().get((int) (Math.random() * reg1.getMembers().size()));
        Pixel pix2 = reg2.getMembers().get((int) (Math.random() * reg2.getMembers().size()));
        
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

        return this.distColorsRGB(rgb2, rgb1) == 0;
    }

    /**
     * Merge list in order to get merged regions
     * @param mergedLink 
     */
    public static void mergeArray(List<List<Integer>> mergedLink) {
//ici on a liste contenant pour chaque region; la liste des region avec lesquelles elle doit merger
        boolean change = true;
        int i = 0;
        int j = i + 1;
        while (change && i < mergedLink.size() - 1) {
            //test if the array i and j have a common index
            boolean merge = false;
            int k = 0;
            while (!merge && k < mergedLink.get(j).size()) {
                Integer index = mergedLink.get(j).get(k);
                //test if the index is know
                if (mergedLink.get(i).contains(index)) {
                    merge = true;

                } else {
                    k++;
                }
            }
            if (merge) {
                //merge the j region
                mergedLink.get(i).addAll(mergedLink.get(j));
                //remove the jth region
                mergedLink.remove(j);
                change = true;
                i = 0;
                j = i + 1;
            } else {
                change = false;
                j++;
                if (j == mergedLink.size()) {
                    i++;
                    j = i + 1;
                }
            }
        }
    }
    
    public double distColorsRGB(double[] a, double[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2) + Math.pow(a[2] - b[2], 2));
    }

}
