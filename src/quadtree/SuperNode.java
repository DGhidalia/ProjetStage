/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.util.ArrayList;
import org.bytedeco.javacpp.opencv_core;

/**
 *
 * @author david.ghidalia
 */
public class SuperNode extends Node {

    private ArrayList<Node> _members;

    public SuperNode(opencv_core.Rect ROI, opencv_core.Mat img, opencv_core.Mat clone, double gap) {
        super(ROI, img, clone, gap);
    }

    public SuperNode(Node fils1, Node fils2) {
        super(null, null, null, 0);
        ArrayList<Node> list = new ArrayList<>();
        list.add(fils1);
        list.add(fils2);
        this._members = list;
    }

 

    void addNode(Node fils2) {
        if (!fils2.equals(this)) {
            if (fils2.getClass() == SuperNode.class) {
                SuperNode sn = (SuperNode) fils2;
                ArrayList<Node> members = sn.getMembers();
                for (Node member : members) {
                    this._members.add(member);
                }
            }
            this._members.add(fils2);
        }
    }

    /**
     * @return the _members
     */
    public ArrayList<Node> getMembers() {
        return _members;
    }

    /**
     * @param _members the _members to set
     */
    public void setMembers(ArrayList<Node> _members) {
        this._members = _members;
    }

    private void todo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
