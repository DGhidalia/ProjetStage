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
public class RgbToHsv {

    public double[] rgbToHsv(double[] rgb) {
        if (rgb.length == 3 && rgb[1] <= 255 && 0 <= rgb[1] && 0 <= rgb[2] && rgb[2] <= 255 && 0 <= rgb[0] && rgb[0] <= 255) {
            double[] res = new double[3];
            double R = rgb[0] / 255;
            double G = rgb[1] / 255;
            double B = rgb[2] / 255;

            double Cmax = Math.max(R, Math.max(G, B));
            double Cmin = Math.min(R, Math.min(G, B));

            double delta = Cmax - Cmin;

            double H = 0, S, V;

            if (delta == 0) {
                H = 0;
            } else if (Cmax == R) {
                H = ((G - B) / delta);
            } else if (Cmax == G) {
                H = ((B - R) / delta) + 2;
            } else if (Cmax == B) {
                H = ((R - G) / delta) + 4;
            }

            H *= 60;
            if (H < 0) {
                H += 360;
            }

            if (Cmax == 0) {
                S = 0;
            } else {
                S = (delta / Cmax);
            }

            V = Cmax;

            res[0] = H;
            res[1] = S;
            res[2] = V;

            return res;
        } else {
            return null;
        }
    }

    public double distColors(double[] a, double[] b) {
        return Math.pow(Math.sin((a[0] * Math.PI) / 180) * a[1] * a[2] - Math.sin((b[0] * Math.PI) / 180) * b[1] * b[2], 2)
                + Math.pow(Math.cos((a[0] * Math.PI) / 180) * a[1] * a[2] - Math.cos((b[0] * Math.PI) / 180) * b[1] * b[2], 2)
                + Math.pow(a[2] - b[2], 2);
    }

}
