/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

import java.util.LinkedList;

/**
 *
 * @author pierre.renard
 */
class Region {

    private LinkedList<Pixel> members; //Pixels of the region

    public Region() {
        this.members = new LinkedList<>();
    }

    
    
    
    /**
     * Add a Pixel to the region
     *
     * @param member
     */
    public void addMember(Pixel member) {

        this.members.push(member);

    }

    /**
     *
     * @return
     */
    public LinkedList<Pixel> getMembers() {
        return members;
    }

}
