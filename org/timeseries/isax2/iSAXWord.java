/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.timeseries.isax2;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author djamel
 */
public class iSAXWord implements Serializable, Comparable<Object> {

    private short[] values;
    private short[] card;

    public iSAXWord(short isax_word_length) {
        this.values = new short[isax_word_length];
        this.card = new short[isax_word_length];
    }

    public iSAXWord(short isax_word_length, short cardBase) {
        this.values = new short[isax_word_length];
        this.card = new short[isax_word_length];

        for(int i = 0; i < isax_word_length; i++) {
            this.card[i] = cardBase;
        }
    }

    public iSAXWord(short[] values, short[] card) {
        //this.values = values;
        //this.card = card;

        this.values = values.clone();
        this.card = card.clone();

    }

    public iSAXWord(short[] values, short cardBase) {
        this.values = values.clone();
        this.card = new short[values.length];

        for(int i = 0; i < card.length; i++) {

            this.card[i] = cardBase;
        }
    }

    /**
     * @return the values
     */
    public short[] getValues() {
        return values;
    }

    /**
     * @return the card
     */
    public short[] getCard() {
        return card;
    }

    @Override
    public int compareTo(Object o) {
        return 0;

    }

    public String getIndexHash() {
        String str = "";

        for(int i = 0; i < values.length; i++) {
            str += values[i] + "_" + card[i] + "|";
        }

        return str;

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof iSAXWord) {
            iSAXWord word = (iSAXWord) obj;

            return Arrays.equals(this.values, word.values) && Arrays.equals(this.values, word.values);

        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Arrays.hashCode(this.values);
        return hash;
    }

    public int[] findSegmentToSplit(short[] card, double[] Means, double[] StdDev) throws Exception {

        int segmentToSplit = 0;
        double smalDist = Double.MAX_VALUE;

        for(int i = 0; i < values.length; i++) {

            double breakPoint = getBreakPoint(card[i], Means[i]);

            if(breakPoint < (Means[i] + (3 * StdDev[i])) && breakPoint > (Means[i] - (3 * StdDev[i]))) {
                if(smalDist > Math.abs(Means[i] - breakPoint)) {
                    segmentToSplit = i;
                    smalDist = Math.abs(Means[i] - breakPoint);
                    //System.out.println("segmentToSplit = "+(segmentToSplit+1));
                }

            }

        }

        if(segmentToSplit != 0) {
            int[] temp = new int[2];

            temp[0] = segmentToSplit;
            temp[1] = card[segmentToSplit] << 1;
            return temp;
        } else {

            for(int i = 0; i < card.length; i++) {
                card[i] = (short) (card[i] << 1);
            }
            return findSegmentToSplit(card, Means, StdDev);
        }
    }

    public iSAXWord[] getSplitCardinality(double[][] listTS) throws Exception {

        if(listTS.length < 1) {

            throw new Exception("no time series");

        }

        iSAXWord[] isaxwords = new iSAXWord[2];

        double[] Means = Util.Mean(listTS);
        double[] StdDev = Util.StdDev(listTS, Means);

        short[] tempCards = card.clone();

        int segmentToSplit = 0;
        double smalDist = Double.MAX_VALUE;

        for(int i = 0; i < values.length; i++) {

            if(card[i] <= 256) {

                double breakPoint = getBreakPoint(card[i], Means[i]);

                if(breakPoint < (Means[i] + (3 * StdDev[i])) && breakPoint > (Means[i] - (3 * StdDev[i]))) {
                    if(smalDist > Math.abs(Means[i] - breakPoint)) {
                        segmentToSplit = i;
                        smalDist = Math.abs(Means[i] - breakPoint);
                        //System.out.println("cc");
                        //System.out.println("segmentToSplit = "+(segmentToSplit+1));
                    }

                }
            }

        }

        if(segmentToSplit == 0) {

            int iLSF = this.card[0];
            int index = 0;

            for(int i = 0; i < this.card.length; i++) {
                if(iLSF > this.card[i] && this.card[i] <= 256) {

                    iLSF = this.card[i];
                    index = i;
                }
            }

            segmentToSplit = index;

            //throw new Exception("can't find segment to split");
        }
        
        short[] values1 = this.values.clone();
            short[] values2 = this.values.clone();
            short[] cards = this.card.clone();

        if((cards[segmentToSplit] << 1) <= 512) {

            

            //for(int i = 0; i < this.values.length; i++) {
            //    values1[i] = this.values[i];
            //    values2[i] = this.values[i];
            //  cards[i] = this.card[i];
            //}
            //int i = cards[segmentToSplitCard[0]];
            //System.out.println("base = "+cards[segmentToSplitCard[0]]+" to = "+segmentToSplitCard[1]);
            //while(i < segmentToSplitCard[1]) {
            //    i = i << 1;
            //System.out.println("i = "+i );
            //System.out.println("-values1[segmentToSplitCard[0]] = "+values1[segmentToSplitCard[0]]+"\n-values2[segmentToSplitCard[0]] = "+values2[segmentToSplitCard[0]]);
            values1[segmentToSplit] = (short) (values1[segmentToSplit] * 2);
            values2[segmentToSplit] = (short) ((values2[segmentToSplit] * 2) + 1);
              //  System.out.println("+values1[segmentToSplitCard[0]] = "+values1[segmentToSplitCard[0]]+"\n+values2[segmentToSplitCard[0]] = "+values2[segmentToSplitCard[0]]);

            //}
            cards[segmentToSplit] = (short) (cards[segmentToSplit] << 1);

            isaxwords[0] = new iSAXWord(values1, cards);
            isaxwords[1] = new iSAXWord(values2, cards);

            return isaxwords;
        } else {
            return null;
        }

    }

    private double getBreakPoint(short card, double mean) throws Exception {

        double[] Cutes = SAXAlphabet.getCuts((short) (card << 1));

        if(mean < Cutes[0]) {
            //System.err.println("return "+Cutes[0]);
            return Cutes[0];
        } else {
            if(mean > Cutes[Cutes.length - 1]) {
                //System.err.println("return "+Cutes[Cutes.length-1]);
                return Cutes[Cutes.length - 1];
            } else {
                for(int i = 1; i < Cutes.length; i++) {
                    if(mean >= Cutes[i - 1] && mean < Cutes[i]) {
                        //System.err.println("WOOOOOW");
                        double dist1 = Math.abs(Cutes[i] - mean);
                        double dist2 = Math.abs(Cutes[i - 1] - mean);
                        if(dist1 > dist2) {
                            //System.err.println("return "+Cutes[i - 1]);
                            return Cutes[i - 1];
                        } else {
                            //System.err.println("return "+Cutes[i]);
                            return Cutes[i];
                        }
                    }
                }
            }
        }
        // System.err.println("return 0");
        //System.exit(0);
        return 0;
    }

    @Override
    public String toString() {
        return getIndexHash();
    }

}
