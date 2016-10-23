/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.isax2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author djamel
 */
public class TerminalNode extends AbstractNode {

    private short nbrTS = 0;

    public TerminalNode(IndexHashParams params, iSAXWord isaxWord) {
        super(params, NodeType.TERMINAL, isaxWord);
    }

    public int Insert(int id,double[] ts) throws IOException, Exception {

        iSAXWord isaxword = new iSAXWord(SAXAlphabet.ConvertSAXBaseOnCardArray(ts, this.params.saxWordLength, this.isaxWord.getCard()), this.isaxWord.getCard());

//        if(!isaxword.equals(this.isaxWord)) {
//            System.out.println("TerminalNode >> Should not have recv'd a ts at this InternalNode!!!\n\t[" + isaxword + "]\n node = \t[" + this.isaxWord + "]");
//        }

        //PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(this.params.IndexRootDir.concat(this.isaxWord.getIndexHash()+".data"), true)));
        File file = new File(this.params.IndexRootDir.concat(this.isaxWord.getIndexHash() + ".data"));
        File file2 = new File(this.params.IndexRootDir.concat(this.isaxWord.getIndexHash() + ".id"));

        //if file doesnt exists, then create it
        if(!file.exists()) {
            file.createNewFile();
        }
        if(!file2.exists()) {
            file2.createNewFile();
        }

        //PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file.getName(), true)));
        //PrintWriter out = new PrintWriter(file.getName(), "UTF-8");
        //true = append file
        FileWriter fileWritter = new FileWriter(file, true);
        BufferedWriter out = new BufferedWriter(fileWritter);
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2, true));

        if(out == null && out2 == null) {

            throw new Exception("file dan't exist");
        }

        String str = "" + ts[0];
        for(int i = 1; i < ts.length; i++) {
            str += "," + ts[i];
        }

        out.write(str + "\n");
        out2.write(id+"\n");
        nbrTS++;
        out.close();
        out2.close();

//        BufferedReader br = new BufferedReader(new FileReader(new File(this.params.IndexRootDir.concat(this.isaxWord.getIndexHash() + ".data"))));
//        String line = null;
//
//        double[][] values = new double[nbrTS][this.params.timeSeriesLength];
//
//        int i = 0;
//        int k = 0;
//        while((line = br.readLine()) != null && i < nbrTS) {
//            String[] temp = line.split(",");
//            k = 0;
//            for(int j = 0; j < temp.length && j < this.params.timeSeriesLength; j++) {
//
//                if(temp[j].length() > 0) {
//                    values[i][k] = Double.valueOf(temp[j]);
//                    k++;
//                }
//
//            }
//
//            i++;
//        }
//        br.close();

//        for(double[] listTS1 : values) {
//           if(!Arrays.equals(this.isaxWord.getValues(), SAXAlphabet.ConvertSAXBaseOnCardArray(listTS1, this.params.saxWordLength, this.isaxWord.getCard()))) {
//               System.out.println("\n" + Arrays.toString(this.isaxWord.getValues()) + "\n" + Arrays.toString(SAXAlphabet.ConvertSAXBaseOnCardArray(listTS1, this.params.saxWordLength, this.isaxWord.getCard())));
//            }
//
//        }
        
        return 0;

    }

    @Override
    public boolean IsOverThreshold() {
        return nbrTS >= params.threshold;
    }

    public double[][] getTimeseries() throws FileNotFoundException, IOException {
        
        File file = new File(this.params.IndexRootDir.concat(this.isaxWord.getIndexHash() + ".data"));

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;

        double[][] values = new double[nbrTS][this.params.timeSeriesLength];

        int i = 0;
        int k = 0;
        while((line = br.readLine()) != null && i < nbrTS) {
            String[] temp = line.split(",");
            k = 0;
            for(int j = 0; j < temp.length && j < this.params.timeSeriesLength; j++) {

                if(temp[j].length() > 0) {
                    values[i][k] = Double.valueOf(temp[j]);

                    k++;

                }

            }

            i++;
        }
        br.close();
        file.delete();

        return values;

    }
    
    public int[] getIds() throws FileNotFoundException, IOException{
        File file = new File(this.params.IndexRootDir.concat(this.isaxWord.getIndexHash() + ".id"));

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;

        int[] values = new int[nbrTS];

        int i = 0;
        int k = 0;
        while((line = br.readLine()) != null && i < nbrTS) {
            
                    values[i] = Integer.valueOf(line);

                 

            i++;
        }
        br.close();
        file.delete();

        return values;
    }

    @Override
    public String toString() {
        return "TerminalNode :\n\t [" + isaxWord + "] \n\t nbrOfTs = " + nbrTS; //To change body of generated methods, choose Tools | Templates.
    }

    public short getNbrTS() {
        return nbrTS;
    }
    
    
    

}
