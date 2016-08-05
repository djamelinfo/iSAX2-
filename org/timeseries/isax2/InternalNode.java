/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.timeseries.isax2;

import java.util.Arrays;

/**
 *
 * @author djamel
 */
public class InternalNode extends AbstractNode {

    public AbstractNode node1;
    public AbstractNode node2;

    public InternalNode(IndexHashParams params, iSAXWord isaxWord) {
        super(params, NodeType.INTERNAL, isaxWord);
    }

    public InternalNode(AbstractNode node1, AbstractNode node2, IndexHashParams params, iSAXWord isaxWord) {
        super(params, NodeType.INTERNAL, isaxWord);
        this.node1 = node1;
        this.node2 = node2;

    }

    @Override
    public int Insert(int id, double[] ts) throws Exception {

        iSAXWord isaxword = new iSAXWord(SAXAlphabet.ConvertSAXBaseOnCardArray(ts, this.params.saxWordLength, node1.isaxWord.getCard()), node1.isaxWord.getCard());

        String key = isaxword.getIndexHash();

        if(node1.isaxWord.equals(isaxword)) {
//            if(id == 40071) {
//                System.out.println("ID = " + 40071 + " : " + node1.isaxWord);
//            }
//            if(id == 85095) {
//                System.out.println("ID = " + 85095 + " : " + node1.isaxWord);
//            }

            if(node1.getType() == NodeType.TERMINAL) {
                if(node1.IsOverThreshold()) {

                    double[][] listTS = node1.getTimeseries();

                    //System.out.println(listTS.length);
                    iSAXWord[] isaxwords = isaxword.getSplitCardinality(listTS);
                    //System.out.println(Arrays.toString(isaxwords));
                    int i = 0;
                    if(isaxwords != null) {
                        int[] ids = node1.getIds();
                        //System.out.println(Arrays.toString(isaxwords));
                        node1 = new InternalNode(new TerminalNode(node1.params, isaxwords[0]), new TerminalNode(node1.params, isaxwords[1]), node1.params, node1.isaxWord);

                        for(int j = 0; j < listTS.length; j++) {

                            i += node1.Insert(ids[j], listTS[j]);
                        }
                    }
                    return i + node1.Insert(id, ts);

                } else {

                    return node1.Insert(id, ts);
                }

            } else {
                return node1.Insert(id, ts);
            }

        } else {
            if(node2.isaxWord.equals(isaxword)) {
//                if(id == 40071) {
//                System.out.println("ID = " + 40071 + " : " + node1.isaxWord);
//            }
//            if(id == 85095) {
//                System.out.println("ID = " + 85095 + " : " + node1.isaxWord);
//            }

                if(node2.getType() == NodeType.TERMINAL) {
                    if(node2.IsOverThreshold()) {

                        double[][] listTS = node2.getTimeseries();

                        int i = 0;
                        //System.out.println(listTS.length);
                        iSAXWord[] isaxwords = isaxword.getSplitCardinality(listTS);
                        //System.out.println(Arrays.toString(isaxwords));
                        if(isaxwords != null) {
                            //System.out.println(Arrays.toString(isaxwords));
                            int[] ids = node2.getIds();
                            node2 = new InternalNode(new TerminalNode(node2.params, isaxwords[0]), new TerminalNode(node2.params, isaxwords[1]), node2.params, node2.isaxWord);

                            for(int j = 0; j < listTS.length; j++) {

                                i += node2.Insert(ids[j], listTS[j]);
                            }
                        }
                        return i + node2.Insert(id, ts);

                    } else {

                        return node2.Insert(id, ts);
                    }

                } else {
                    return node2.Insert(id, ts);
                }

            } else {
                // ok, how did we get here?
                //return 1;
                throw new Exception("\nInternalNode >> Should not have recv'd a ts "+id+" of size "+ts.length+" at this InternalNode!!!\n\t[" + isaxword + "]\n node1 =[" + node1.isaxWord + "]\n node2 =[" + node2.isaxWord + "]");
                //System.err.println("InternalNode >> Should not have recv'd a ts at this InternalNode!!!\n\t["+isaxword+"]\n node1 = \t["+node1.isaxWord+"]\n node2 = \t["+node2.isaxWord+"]");
            }
        }

    }

    public String ApproximateSearch(double[] ts) throws Exception {
        iSAXWord isaxword = new iSAXWord(SAXAlphabet.ConvertSAXBaseOnCardArray(ts, this.params.saxWordLength, node1.isaxWord.getCard()), node1.isaxWord.getCard());

        String key = isaxword.getIndexHash();

        if(node1.isaxWord.equals(isaxword)) {

            if(node1.getType() == NodeType.TERMINAL) {
                return node1.isaxWord.getIndexHash() + ".id";
            } else {
                System.out.println( node1.isaxWord);
                return node1.ApproximateSearch(ts);
            }
        } else {
            if(node2.isaxWord.equals(isaxword)) {

                if(node2.getType() == NodeType.TERMINAL) {
                    return node2.isaxWord.getIndexHash() + ".id";
                } else {
                    System.out.println( node2.isaxWord);
                    return node2.ApproximateSearch(ts);
                }
            } else {
                // ok, how did we get here?
                //return 1;
                throw new Exception("\nInternalNode >> Should not have recv'd a ts at this InternalNode!!!\n\t[" + isaxword + "]\n node1 = \t[" + node1.isaxWord + "]\n node2 = \t[" + node2.isaxWord + "]");
                //System.err.println("InternalNode >> Should not have recv'd a ts at this InternalNode!!!\n\t["+isaxword+"]\n node1 = \t["+node1.isaxWord+"]\n node2 = \t["+node2.isaxWord+"]");
            }
        }

    }

    @Override
    public String toString() {
        return "InternalNode : \n\t [" + isaxWord + "] "; //To change body of generated methods, choose Tools | Templates.
    }

}
