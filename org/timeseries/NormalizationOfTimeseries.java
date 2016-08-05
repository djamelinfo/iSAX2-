/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.timeseries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.timeseries.isax2.Util;

/**
 *
 * @author djamel
 */
public class NormalizationOfTimeseries {

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File("/media/sda5/time series/seismic.data.csv")));
        File file = new File("/home/djamel/time series data/seismic.dataNormalize.csv");

        //if file doesnt exists, then create it
        if(!file.exists()) {
            file.createNewFile();
        }

        //PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getName(), true)));
        //PrintWriter out = new PrintWriter(file.getName(), "UTF-8");
        //true = append file
        FileWriter fileWritter = new FileWriter(file, true);
        BufferedWriter out = new BufferedWriter(fileWritter);

        String line = null;

        int i = 0;
        int k = 0;

        int compt = 0;
        while((line = br.readLine()) != null) {
            String[] temp = line.split(",");
            //System.out.println(temp.length);
            double[] values = new double[temp.length];
            k = 0;
            for(int j = 1; j < temp.length; j++) {

                if(temp[j].length() > 0) {
                    values[k] = Double.valueOf(temp[j]);

                    k++;

                }

            }

            double[] valuesNorm = Util.Z_Normalization(values);

            String str = "" + valuesNorm[0];
            for(int l = 1; l < valuesNorm.length; l++) {
                str += "," + valuesNorm[l];
            }

            out.write(str + "\n");

            i++;
        }
        out.close();
        br.close();
    }

}
