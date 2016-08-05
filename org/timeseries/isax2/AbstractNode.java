package org.timeseries.isax2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 * Base class for iSAX hash tree
 *
 *
 * @author DJAMEL
 *
 */
public abstract class AbstractNode implements Serializable ,Cloneable{

    private NodeType nt = NodeType.TERMINAL;

    public IndexHashParams params;

    public iSAXWord isaxWord;

    /**
     *
     * @param params
     * @param isaxWord
     */
    public AbstractNode(IndexHashParams params, iSAXWord isaxWord) {
        this.params = params;
        this.isaxWord = isaxWord;
    }

    public AbstractNode(IndexHashParams params, NodeType nt, iSAXWord isaxWord) {
        this.params = params;
        this.nt = nt;
        this.isaxWord = isaxWord;
    }

    /**
     * Get the node type (leaf or internal node).
     *
     * @return the Node type.
     */
    public NodeType getType() {
        return this.nt;
    }

    /**
     *
     * not sure what to do wiht this one
     *
     *
     * @param id
     * @param ts
     */
    public int Insert(int id ,double[] ts) throws Exception{
        return 0;
    }

    public boolean IsOverThreshold() {

        return false;
    }

    public void setType(NodeType t) {
        this.nt = t;
    }

    public double[][] getTimeseries() throws FileNotFoundException,IOException{
        return null;

    }
    
     public int[] getIds() throws FileNotFoundException, IOException{
        return null;
         
     }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public short getNbrTS() {
        return 0;
    }

    String ApproximateSearch(double[] ts) throws Exception{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    

}
