/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.X;
        hash = 11 * hash + this.Y;
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
        final Pixel other = (Pixel) obj;
        if (this.X != other.X) {
            return false;
        }
        if (this.Y != other.Y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pixel{" + "X=" + X + ", Y=" + Y + '}';
    }
    
    
    
}
