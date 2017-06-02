/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author pierre.renard
 */
class Region {
    
    private List<Node> _noeudRegion;

    private List<Pixel> members; //Pixels of the region
    
    private Pixel _pointCentre;

    private int id;

    public Region(int id) {
        this.id = id;
        this.members = new ArrayList<>();
        this._noeudRegion = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Region other = (Region) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    /**
     * Add a Pixel to the region
     *
     * @param member
     */
    public void addMember(Pixel member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
        }
    }

    /**
     * Merge two Region
     */
    public void Merge(Region r){
         for(Pixel p : r.getMembers())
             this.addMember(p);
    }
    
    /**
     *
     * @return
     */
    public List<Pixel> getMembers() {
        return members;
    }

    public int size() {
        return members.size();
    }

    /**
     * @return the _noeudRegion
     */
    public List<Node> getNoeudRegion() {
        return _noeudRegion;
    }

    /**
     * @param _noeudRegion the _noeudRegion to set
     */
    public void setNoeudRegion(List<Node> _noeudRegion) {
        this._noeudRegion = _noeudRegion;
    }

    /**
     * @return the _pointCentre
     */
    public Pixel getPointCentre() {
        return _pointCentre;
    }

    /**
     * @param _pointCentre the _pointCentre to set
     */
    public void setPointCentre(Pixel _pointCentre) {
        this._pointCentre = _pointCentre;
    }

}
