/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

/**
 *
 * @author pierre.renard
 */
public class Pixel {
    
    
    private int X;
    private int Y;
    
    
    public Pixel(int x, int y){
        
        this.X = x;
        this.Y = y;
        
    }

    /**
     * @return the X
     */
    public int getX() {
        return X;
    }

    /**
     * @return the Y
     */
    public int getY() {
        return Y;
    }
    
    
    
}
