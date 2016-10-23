/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeseries.isax2;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.TreeMap;

/**
 *
 * @author djamel
 */
public class RootNode implements Serializable {

    public IndexHashParams params;

    public AbstractMap<String, AbstractNode> childs
                                                     = new TreeMap<String, AbstractNode>();

    public RootNode(IndexHashParams params) {
        this.params = params;
    }

    public int Insert(int id, double[] ts) throws Exception {

        iSAXWord isaxword = new iSAXWord(SAXAlphabet.ConvertSAX(ts,
                                                                this.params.saxWordLength,
                                                                this.params.saxBaseCard),
                                         this.params.saxBaseCard);

        String key = isaxword.getIndexHash();

        if(childs.containsKey(key)) {

            AbstractNode node = childs.get(key);
//            if(id == 40071) {
//                System.out.println(Arrays.toString(ts));
//                System.out.println("ID = " + 40071 + " : " + node.isaxWord);
//            }
//            if(id == 85095) {
//                System.out.println(Arrays.toString(ts));
//                System.out.println("ID = " + 85095 + " : " + node.isaxWord);
//            }

            //System.out.println("++");
            //System.out.println(key);
            //System.out.println(node.isaxWord);
            //System.out.println("++");
            if(node.getType() == NodeType.TERMINAL) {
                if(node.IsOverThreshold()) {

                    double[][] listTS = node.getTimeseries();
                    int[] ids = node.getIds();

                    //System.out.println(listTS.length);
                    // for(double[] listTS1 : listTS) {
                    //    System.out.println(new iSAXWord(SAXAlphabet.ConvertSAXBaseOnCardArray(listTS1, this.params.saxWordLength, node.isaxWord.getCard()), node.isaxWord.getCard()));
                    //    System.out.println(new iSAXWord(SAXAlphabet.ConvertSAX(listTS1, this.params.saxWordLength, this.params.saxBaseCard), this.params.saxBaseCard));
                    //    System.out.println("");
                    // }
                    iSAXWord[] isaxwords = node.isaxWord.getSplitCardinality(
                            listTS);

                    //System.out.println(node.isaxWord);
                    AbstractNode tempNode = new InternalNode(new TerminalNode(
                            node.params, isaxwords[0]), new TerminalNode(
                                                                     node.params,
                                                                     isaxwords[1]),
                                                             node.params,
                                                             node.isaxWord);

                    //System.out.println("\t"+isaxwords[0]+"\n\t"+isaxwords[1]);
                    int i = 0;
                    for(int j = 0; j < listTS.length; j++) {

                        //System.out.println(new iSAXWord(SAXAlphabet.ConvertSAXBaseOnCardArray(listTS1, this.params.saxWordLength, isaxwords[0].getCard()), isaxwords[0].getCard()));
                        i += tempNode.Insert(ids[j], listTS[j]);

                    }
                    //System.out.println(new iSAXWord(SAXAlphabet.ConvertSAXBaseOnCardArray(ts, this.params.saxWordLength, ((InternalNode) tempNode).node1.isaxWord.getCard()), ((InternalNode) tempNode).node1.isaxWord.getCard()));
                    i += tempNode.Insert(id, ts);

                    childs.remove(key);
                    childs.put(key, tempNode);
                    return i;
                } else {

                    return node.Insert(id, ts);

//                    if(!Arrays.equals(node.isaxWord.getValues(), SAXAlphabet.ConvertSAXBaseOnCardArray(ts, node.params.saxWordLength, node.isaxWord.getCard()))) {
//                        System.out.println("\n"+Arrays.toString(node.isaxWord.getValues())+"\n"+Arrays.toString(SAXAlphabet.ConvertSAXBaseOnCardArray(ts, node.params.saxWordLength, node.isaxWord.getCard())));
//                    }
                }

            } else {
//                if(!Arrays.equals(node.isaxWord.getValues(), SAXAlphabet.ConvertSAXBaseOnCardArray(ts, node.params.saxWordLength, node.isaxWord.getCard()))) {
//                        System.out.println("\n"+Arrays.toString(node.isaxWord.getValues())+"\n"+Arrays.toString(SAXAlphabet.ConvertSAXBaseOnCardArray(ts, node.params.saxWordLength, node.isaxWord.getCard())));
//                    }
                return node.Insert(id, ts);
            }

        } else {
            // if it does not contain this node, create a new one
            // create a isaxword based on the base cardinality

            AbstractNode node = new TerminalNode(this.params, isaxword);
            childs.put(key, node);
//            if(id == 40071) {
//                System.out.println(Arrays.toString(ts));
//                System.out.println("ID = " + 40071 + " : " + node.isaxWord);
//            }
//            if(id == 85095) {
//                System.out.println(Arrays.toString(ts));
//                System.out.println("ID = " + 85095 + " : " + node.isaxWord);
//            }
            return node.Insert(id, ts);
        }

    }

    public String ApproximateSearch(double[] ts) throws Exception {

        iSAXWord isaxword = new iSAXWord(SAXAlphabet.ConvertSAX(ts,
                                                                this.params.saxWordLength,
                                                                this.params.saxBaseCard),
                                         this.params.saxBaseCard);
        String key = isaxword.getIndexHash();

        if(childs.containsKey(key)) {
            AbstractNode node = childs.get(key);

            if(node.getType() == NodeType.TERMINAL) {
                System.out.println(node.isaxWord);
                return node.isaxWord.getIndexHash() + ".id";
            } else {
                System.out.println(node.isaxWord);
                return node.ApproximateSearch(ts);
            }
        } else {
            return "not exist";
        }

    }

    @Override
    public String toString() {
        return "RootNode :\n\t Nbr of child =" + childs.size() + " "; //To change body of generated methods, choose Tools | Templates.
    }

}
