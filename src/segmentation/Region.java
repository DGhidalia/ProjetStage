/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author pierre.renard
 */
class Region {

    private List<Pixel> members; //Pixels of the region

    public Region() {
        this.members = new ArrayList<>();
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
