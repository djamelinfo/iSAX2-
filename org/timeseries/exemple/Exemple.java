/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.exemple;

import java.io.File;
import java.io.IOException;
import timeseries.isax2.iSAXIndex;

/**
 *
 * @author djamel
 */
public class Exemple {

    public static void main(String[] args) throws NumberFormatException,
                                                  IOException, Exception {

        if(args.length < 4) {
            System.err.println(
                    "Usage: iSAXindex <numberOfTs> <sizeOfTs> <indexHome> <DataSet1> <DataSet2>");
            System.exit(1);
        }

        iSAXIndex index = new iSAXIndex(Integer.parseInt(args[1]), (short) 8,
                                        (short) 512, (short) 2, (short) 100,
                                        args[2]);

        File f = new File(args[2]);
        if(f.isDirectory()) {
            File[] list = f.listFiles();
            for(File list1 : list) {
                list1.delete();
            }
        }
        boolean b = true;

        double start = System.currentTimeMillis();

        //System.out.println(index.ConstructionOfIndex("/media/sda5/time series/Seismic_data.dataNormalize.csv", Integer.parseInt(args[0]), 256));
        System.out.println(index.ConstructionOfIndex(args[3], Integer.parseInt(
                                                     args[0]), Integer.parseInt(
                                                             args[1]), false));
        if(args.length == 5) {
            System.out.println(index.ConstructionOfIndex(args[4], Integer.
                                                         parseInt(args[0]),
                                                         Integer.parseInt(
                                                                 args[1]), true));
        }

        double end = System.currentTimeMillis();
        System.out.println(index);
        System.out.println("time to builde index = " + (end - start) + "ms");


        index.saveIndexInDisk();
    }

}
