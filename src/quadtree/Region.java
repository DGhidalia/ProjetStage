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

    private List<Pixel> members; //Pixels of the region

    private int id;

    public int getId() {
        return id;
    }

    public Region(int id) {
        this.id = id;
        this.members = new ArrayList<>();
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
     * merge two Region
     */
    public void merge(Region r){
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
}
