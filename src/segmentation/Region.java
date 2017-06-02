/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a region of pixels of an image. 
 *
 * @author pierre.renard
 * @version 1.0
 */
class Region {

    private final List<Pixel> members; //Pixels of the region

    /**
     * Initialize the properties
     */
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

    //--------------------------------------------------------------------------
    //GETTERS
    //--------------------------------------------------------------------------
    /**
     *
     * @return
     */
    public List<Pixel> getMembers() {
        return members;
    }

    /**
     * Get the size of the list of members
     *
     * @return
     */
    public int size() {
        return members.size();
    }

}
