/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.timeseries.isax2;

/**
 *
 * @author djamel
 */
public class Util {

    public static double Mean(double[] data, int index1, int index2) throws Exception {

        if(index1 < 0 || index2 < 0 || index1 >= data.length
                || index2 >= data.length) {
            throw new Exception("Invalid index!");
        }

        if(index1 > index2) {
            int temp = index2;
            index2 = index1;
            index1 = temp;
        }

        double sum = 0;

        for(int i = index1; i <= index2; i++) {
            sum += data[i];
        }

        return sum / (index2 - index1);
    }

    public static double[] Mean(double[][] listTS) throws Exception {
        if(listTS.length < 1) {
            throw new Exception("no time series");
        }

        double[] mean = new double[listTS[0].length];

        double sum;

        for(int i = 0; i < listTS[0].length; i++) {
            sum = 0;
            for(int j = 0; j < listTS.length; j++) {
                sum += listTS[j][i];
            }

            mean[i] = sum / listTS[0].length;
        }

        return mean;
    }

    public static double StdDev(double[] timeSeries) throws Exception {
        double mean = Mean(timeSeries, 0, timeSeries.length - 1);
        double var = 0.0;

        for(int i = 1; i < timeSeries.length; i++) {
            var += (timeSeries[i] - mean) * (timeSeries[i] - mean);
        }
        var /= (timeSeries.length - 2);

        return Math.sqrt(var);
    }

    public static double[] StdDev(double[][] listTS, double[] mean) {

        double[] stdDev = new double[listTS[0].length];
        for(int i = 0; i < listTS[0].length; i++) {
            double var = 0.0;

            for(int j = 0; j < listTS.length; j++) {
                var += (listTS[j][i] - mean[i]) * (listTS[j][i] - mean[i]);
            }

            var /= (listTS[0].length - 1);

            stdDev[i] = Math.sqrt(var);
        }

        return stdDev;
    }

    public static double[] Z_Normalization(double[] timeSeries) throws Exception {
        double mean = Mean(timeSeries, 0, timeSeries.length - 1);
        double std = StdDev(timeSeries);

        double[] normalized = new double[timeSeries.length];

        if(std == 0) {
            std = 1;
        }

        for(int i = 0; i < timeSeries.length; i++) {
            normalized[i] = (timeSeries[i] - mean) / std;
        }

        return normalized;
    }

    public static double[] GetPAA(double[] data, int num_seg) throws Exception {
        if(Math.IEEEremainder(data.length, num_seg) != 0) {
            throw new Exception("Datalength not divisible by number of segments!");
        }

        // Determine the segment size
        int segment_size = data.length / num_seg;

        int offset = 0;

        double[] PAA = new double[num_seg];

        // if no dimensionality reduction, then just copy the data
        if(num_seg == data.length) {

            System.arraycopy(data, 0, PAA, 0, data.length); //PAA = data;
        }

        for(int i = 0; i < num_seg; i++) {
            PAA[i] = Util.Mean(data, offset, offset + (int) segment_size - 1);
            offset = offset + (int) segment_size;
        }

        return PAA;
    }

}
