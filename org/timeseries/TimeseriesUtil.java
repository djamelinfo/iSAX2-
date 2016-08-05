/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.timeseries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author djamel
 */
public class TimeseriesUtil {

    public static double[][] readAllTSLine(String filename, int nbrTS, int sizeTS) throws NumberFormatException,
                                                                                          IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
        String line = null;

        double[][] values = new double[nbrTS][sizeTS];

        int i = 0;
        int k = 0;
        while((line = br.readLine()) != null && i < nbrTS) {
            String[] temp = line.split(",");
            k = 0;
            for(int j = 1; j < temp.length && j < sizeTS; j++) {

                if(temp[j].length() > 0) {
                    values[i][k] = Double.valueOf(temp[j]);

                    k++;

                }

            }

            i++;
        }
        br.close();

        return values;
    }

}
