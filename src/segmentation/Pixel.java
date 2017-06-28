/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segmentation;

/**
 * This class represents a pixel, by his coordinates, from an image.
 *
 * @author pierre.renard
 * @version 1.1
 */
public class Pixel {

    private int X; //Coordinate on X axis
    private int Y; //Coordinate on Y axis

    /**
     * Initialize coordinates of the pixel
     *
     * @param x
     * @param y
     */
    public Pixel(int x, int y) {

        this.X = x;
        this.Y = y;

    }

    /**
     * Get the X coordinate of the pixel
     *
     * @return
     */
    public int getX() {
        return X;
    }

    /**
     * Get the Y coordinate of the pixel
     *
     * @return
     */
    public int getY() {
        return Y;
    }

    /**
     * Get the hash code of the pixel
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.X;
        hash = 11 * hash + this.Y;
        return hash;
    }

    /**
     * Compare this object with another one
     *
     * @param obj
     * @return
     */
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

    /**
     * Give the informations about the pixel
     *
     * @return
     */
    @Override
    public String toString() {
        return "Pixel{" + "X=" + X + ", Y=" + Y + '}';
    }

}
