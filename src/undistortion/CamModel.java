/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undistortion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class corresponding to the camera parameters at the moment when the picture
 * has been taken. It contains two main methods based on the reading of a text
 * file which contains the camera parameters and the fill of the different
 * properties of the class. The goal is to simplify the transmission of the
 * data. You will find an example of the data file with the sources. The
 * different properties are explained below
 *
 * @author pierre.renard
 * @author cameron.mourot
 * @version 1.1
 * @since 1.0
 */
public final class CamModel {

    private final int MAX_POL_LENGTH = 64;                  //Max size of the polynomials table

    private double pol[] = new double[MAX_POL_LENGTH];      // the polynomial coefficients: pol[0] + x"pol[1] + x^2*pol[2] + ... + x^(N-1)*pol[N-1]
    private int length_pol;                                 // length of polynomial
    private double invpol[] = new double[MAX_POL_LENGTH];   // the coefficients of the inverse polynomial
    private int length_invpol;                              // length of inverse polynomial
    private double centerX;                                 // row coordinate of the center
    private double centerY;                                 // column coordinate of the center
    private double c;                                       // affine parameter
    private double d;                                       // affine parameter
    private double e;                                       // affine parameter
    private int width;                                      // image width
    private int height;                                     // image height

    /**
     * Set up the properties with a text file containing the camera's data.
     *
     * @param path The location of the file
     * @throws IOException
     */
    public CamModel(String path) throws IOException {

        this.length_pol = this.pol.length;
        this.length_invpol = this.invpol.length;

        this.parseDataFile(path);   //Call the parser   

    }

    //PARSER//
    //-------------------------------------------------------------------------
    /**
     * Read a text file correctly filled with the camera data.
     * <p>
     * This method is a parser that recognize the # comments in the file and
     * fill each property in order. It's important to fill the file in the right
     * order, or the results could be false.
     *
     * @param path The location of the file
     * @throws IOException
     */
    protected void parseDataFile(String path) throws IOException {

        String line = "";           //Initialize a line
        String lineStart = "#";     //Start of a section
        String[] table = null;      //Array of the data from a line
        int index = 0;              //Index of the data line, used to fill the properties
        BufferedReader txt;     //Buffer of the text file
        try {
            txt = new BufferedReader(new FileReader(new java.io.File(path)));

            if (txt == null) {
                throw new FileNotFoundException("File not found : " + path);
            }

            try {
                line = txt.readLine();

                while (line != null) {

                    /*Detect if the line is a section title or a data line.
                      Go to the next line if that's a section title.*/
                    if (line.indexOf(lineStart) == 0) {

                        line = txt.readLine();
                        index++;
                        line = line.replace(",", ".");    //Replace comas by dots
                        table = line.split("\\s+");       //Split the line                    

                        //Call the method to fill the properties using the actual line
                        this.setProperties(table, index);
                    }

                    line = txt.readLine();
                }

                txt.close();

            } catch (IOException ex) {
                System.out.println("Error ! " + ex.getMessage());
            }

            txt.close();

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    //SETTING THE PROPERTIES UP//
    //-------------------------------------------------------------------------
    /**
     * Set the value of each property considering the line that is read in the
     * file Following the value of the index, it fills :
     * <ul>
     * <li>pol : The polynomial coefficients
     * <li>invpol : The coefficients of the inverse polynomial
     * <li>centerX
     * <li>centerY
     * <li>c : First affine parameter
     * <li>d : Second affine parameter
     * <li>e : Third affine parameter
     * <li>Height of the image
     * <li>Width of the image
     * </ul>
     *
     * @param table Contains a splitted line
     * @param index Index of the data line
     */
    protected void setProperties(String[] table, int index) {

        switch (index) {

            //This case fill the "pol" table 
            case 1:
                for (int i = 0; i < table.length; i++) {
                    this.pol[i] = Double.parseDouble(table[i]);
                }
                break;

            //This case fill the "invpol" table
            case 2:
                for (int i = 0; i < table.length; i++) {
                    this.invpol[i] = Double.parseDouble(table[i]);
                }
                break;

            //This case fill the "centerX" property, center of the X axis, equals to the columns
            case 3:
                this.centerY = Double.parseDouble(table[0]);
                break;

            //This case fill the "centerY" property, center of the Y axis, equals to the rows
            case 4:
                this.centerX = Double.parseDouble(table[0]);
                break;

            //This case fill the first affine parameter of the camera
            case 5:
                this.c = Double.parseDouble(table[0]);
                break;

            //This case fill the second affine parameter of the camera
            case 6:
                this.d = Double.parseDouble(table[0]);
                break;

            //This case fill the third affine parameter of the camera
            case 7:
                this.e = Double.parseDouble(table[0]);
                break;

            //This case fill the "height" property of the image
            case 8:
                this.height = Integer.parseInt(table[0]);
                break;

            //This case fill the "width" property of the image
            case 9:
                this.width = Integer.parseInt(table[0]);
                break;

            //If there is a problem with the reading of a data
            default:
                System.out.println("Problem with the table");
                break;
        }
    }

    //GETTERS AND SETTERS SECTION//
    //-------------------------------------------------------------------------
    /**
     * @return the MAX_POL_LENGTH
     */
    public int getMAX_POL_LENGTH() {
        return MAX_POL_LENGTH;
    }

    /**
     * @return the pol
     */
    public double[] getPol() {
        return pol;
    }

    /**
     * @param pol the pol to set
     */
    public void setPol(double[] pol) {
        this.pol = pol;
    }

    /**
     * @return the length_pol
     */
    public int getLength_pol() {
        return length_pol;
    }

    /**
     * @param length_pol the length_pol to set
     */
    public void setLength_pol(int length_pol) {
        this.length_pol = length_pol;
    }

    /**
     * @return the invpol
     */
    public double[] getInvpol() {
        return invpol;
    }

    /**
     * @param invpol the invpol to set
     */
    public void setInvpol(double[] invpol) {
        this.invpol = invpol;
    }

    /**
     * @return the length_invpol
     */
    public int getLength_invpol() {
        return length_invpol;
    }

    /**
     * @param length_invpol the length_invpol to set
     */
    public void setLength_invpol(int length_invpol) {
        this.length_invpol = length_invpol;
    }

    /**
     * @return the centerX
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * @param centerX the centerX to set
     */
    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    /**
     * @return the centerY
     */
    public double getCenterY() {
        return centerY;
    }

    /**
     * @param centerY the centerY to set
     */
    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    /**
     * @return the c
     */
    public double getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(double c) {
        this.c = c;
    }

    /**
     * @return the d
     */
    public double getD() {
        return d;
    }

    /**
     * @param d the d to set
     */
    public void setD(double d) {
        this.d = d;
    }

    /**
     * @return the e
     */
    public double getE() {
        return e;
    }

    /**
     * @param e the e to set
     */
    public void setE(double e) {
        this.e = e;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

}
