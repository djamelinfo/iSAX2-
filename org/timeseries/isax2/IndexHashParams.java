/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.timeseries.isax2;

import java.io.Serializable;

/**
 *
 * @author djamel
 */
public class IndexHashParams implements Serializable {

    public int timeSeriesLength;
    public short saxWordLength;
    public short saxMaxCard;
    public short saxBaseCard;
    public short threshold;
    public String IndexRootDir;

    public IndexHashParams(int timeSeriesLength, short saxWordLength, short saxMaxCard, short saxBaseCard, short threshold, String IndexRootDir) {
        this.timeSeriesLength = timeSeriesLength;
        this.saxWordLength = saxWordLength;
        this.saxMaxCard = saxMaxCard;
        this.saxBaseCard = saxBaseCard;
        this.threshold = threshold;
        this.IndexRootDir = IndexRootDir;
    }

    public IndexHashParams() {

        this.timeSeriesLength = 256;
        this.saxWordLength = 8;
        this.saxMaxCard = 512;
        this.saxBaseCard = 2;
        this.threshold = 100;
        this.IndexRootDir = "/home/djamel/";
    }

}
