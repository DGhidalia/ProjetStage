/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentationtexture;

/**
 *
 * @author cameron.mourot
 */
public class SegmentationTexture {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String pathimage = "C:\\Users\\cameron.mourot\\Documents\\GitHub\\ProjetStage\\undistorted_perspective.jpg";
        Texture tex = new Texture(pathimage);
        tex.run();  
       
    }
}
