/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.timeseries.isax2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 *
 * @author djamel
 */
public class iSAXIndex {

    private RootNode root;

    private String IndexRootDir;

    public iSAXIndex(int timeSeriesLength, short saxWordLength, short saxMaxCard, short saxBaseCard, short threshold, String IndexRootDir) {

        this.IndexRootDir = IndexRootDir;
        this.root = new RootNode(new IndexHashParams(timeSeriesLength, saxWordLength, saxMaxCard, saxBaseCard, threshold, IndexRootDir));
    }

    public iSAXIndex(RootNode root, String IndexRootDir) {
        this.root = root;
        this.IndexRootDir = IndexRootDir;
    }

    public long ConstructionOfIndex(String filename, int nbrTS, int sizeTS, boolean b) throws FileNotFoundException, IOException, Exception {

        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
        String line = null;

        double[] values = new double[sizeTS];

        int i = 0;
        int k = 0;

        int compt = 0;
        long time = 0;
        while((line = br.readLine()) != null && i < nbrTS) {
            String[] temp = line.split(",");
            k = 0;
            for(int j = 1; j < temp.length && j < sizeTS+1; j++) {

                if(temp[j].length() > 0) {
                    values[k] = Double.valueOf(temp[j]);

                    k++;

                }

            }
            
            //System.out.println(k+" "+values.length);

            
            
            double start = System.currentTimeMillis();
            //compt += root.Insert(values);
            if(b) {
                compt += root.Insert(100000000*Integer.valueOf(temp[0]),Util.Z_Normalization(values));
            } else {
                compt += root.Insert(Integer.valueOf(temp[0]),Util.Z_Normalization(values));
            }
                
           
              //  compt += root.Insert(Integer.valueOf(temp[0]),Util.Z_Normalization(values));
            
            
            double end = System.currentTimeMillis();
            time += (end - start);
            //System.out.println(Arrays.toString(Util.Z_Normalization(values)));
            //System.out.println(i);
            i++;
        }
        br.close();
        System.out.println(compt);
        return time;
    }

    public int affichR(AbstractNode node) {

        if(node.getType() == NodeType.TERMINAL) {
            System.out.println(node);
            return 1;
        } else {
            int nbr =0;
            System.out.println(node);
            if(((InternalNode) node).node1.getType() == NodeType.TERMINAL) {
                System.out.println(((InternalNode) node).node1);
                if(((InternalNode) node).node1.getNbrTS() != 0) {
                   nbr++; 
                }
                
            } else {
                nbr += affichR(((InternalNode) node).node1);
            }

            if(((InternalNode) node).node2.getType() == NodeType.TERMINAL) {
                System.out.println(((InternalNode) node).node2);
                if(((InternalNode) node).node2.getNbrTS() != 0) {
                   nbr++; 
                }
            } else {
                nbr += affichR(((InternalNode) node).node2);
            }
            
            return nbr;
        }

    }

    public String affich() {

        System.out.println(root);
        
        int nbr = 0;

        for(Map.Entry<String, AbstractNode> entrySet : root.childs.entrySet()) {

            nbr += affichR(entrySet.getValue());

        }

        return "Fin\n number of node : "+nbr;
    }

    @Override
    public String toString() {
        return affich(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public String ApproximateSearch(double[] timeseries) throws Exception{
        return this.root.ApproximateSearch(timeseries);
    }
    
    

    /**
     *
     * @throws IOException
     */
    public void saveIndexInDisk() throws IOException {
        FileOutputStream fos = new FileOutputStream(this.IndexRootDir + "index.data");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.root);
        oos.close();
    }

    public static iSAXIndex serializeDataIn(String indexFile) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(indexFile);
        ObjectInputStream ois = new ObjectInputStream(fin);
        RootNode tmpRoot = (RootNode) ois.readObject();
        ois.close();
        return new iSAXIndex(tmpRoot, indexFile);
    }
    
    
    
    

}
