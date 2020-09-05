package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import utils.FileUtil;

/**
 * In randomized interactome networks of the same topology, the average PCCs of hubs also show
 * a normal distribution centred on 0. This bimodal distribution suggests that hubs can be split
 * into two distinct populations: one with relatively high average PCCs (party hubs) and the
 * other with relatively low average PCCs (date hubs).
 * About 18% top hubs with relatively high average PCCs are  regarded as party hubs, which is the 
 * basis for selecting the average PCCs threshold.
 * reference:
 * Han J D J, Bertin N, Hao T, et al. Evidence for dynamically organized modularity
 *  in the yeast protein–protein interaction network[J]. Nature, 2004, 430(6995): 88-93.

 * @author mxm and lwk
 *
 * 2020年8月10日
 */
public class DateHub {
	public static String dataset = "IntAct";//BioGRID,IntAct
	public static String dir = "D:/TemporalspatialHub/yeast/";
	
    public static final int tNum = 12;
    public static final int TOP = 200;

    public static void main(String[] args) {
        System.out.println("----"+dataset+"----");

        HashSet<String> essentials = FileUtil.readFile2Set(dir+"essentialProteins.txt");
        List<String> nodes = FileUtil.readFile2List(dir+dataset+"/proteins.txt");
        List<String[]> edges = getNetProperties(FileUtil.readFile2List(dir+dataset+"/network.txt"));
        System.out.println("the numbers of nodes and edges of network："+nodes.size()+","+edges.size());

        HashMap<String, double[]> expMap = getGeneExpression(FileUtil.readFile2List(dir+"GSE3431_T12.txt"));

        HashMap<String, Double> map = new HashMap<>();
        for (int t = 0; t <= TOP; t = t + 50) {
            System.out.println("----"+t+"----");
            for (int i = 0; i < t; i++) {
                String node = nodes.get(i);
                double sum = 0;
                int dc = 0;
                for (String[] edge : edges) {
                    if (edge[0].equals(node) || edge[1].equals(node)) {
                        dc = dc + 1;
                        double pcc = 0;
                        if (expMap.keySet().contains(edge[0]) && expMap.keySet().contains(edge[1])) {
                            pcc = new PearsonsCorrelation().correlation(expMap.get(edge[0]), expMap.get(edge[1]));
                        }
                        sum = sum + pcc;
                    }
                }
                double ave = sum / dc;
                System.out.println(node+"\t"+dc+"\t"+ave);
                map.put(node, ave);
            }
            int parHub = 0;
            int datHub = 0;
            int parEss = 0;
            int datEss = 0;
    		HashSet<String> PartyHub = new HashSet<>();
    		HashSet<String> DateHub = new HashSet<>();
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                if (entry.getValue() >= 0.25) {
                	//IntAct:0.25,BioGRID:0.3 
                	//we choose the top 19% of top 200 ranked proteins with relatively high AvgPCC values as party hubs
                    parHub++;
                    PartyHub.add(entry.getKey());
                    if (essentials.contains(entry.getKey())) {
                        parEss++;
                    }
                } else {
                    datHub++;
                    DateHub.add(entry.getKey());
                    if (essentials.contains(entry.getKey())) {
                        datEss++;
                    }
                }
            }
            System.out.printf("PartyHub:\t%d\t%d\t%.3f\n", parHub, parEss, (1.0 * parEss / parHub));
            System.out.printf("DateHub:\t%d\t%d\t%.3f\n", datHub, datEss, (1.0 * datEss / datHub));
            
    		FileUtil.write2File(DateHub, dir+dataset+"/"+dataset+"_DateHub.txt");
    		FileUtil.write2File(PartyHub,dir+dataset+"/"+dataset+"_PartyHub.txt");
        }
        System.out.println("----END----");
    }

    /**
     * get the gene expression data
     * @param rows
     */
    public static HashMap<String, double[]> getGeneExpression(List<String> rows){

        HashMap<String, double[]> map = new HashMap<>();
        for (String row:rows){
            String[] dataStr = row.split("\t");
            double[] arr = new double[tNum];
            for (int i=0;i<arr.length;i++){
                arr[i] = Double.parseDouble(dataStr[i+1]);
            }
            map.put(dataStr[0], arr);
        }
        return map;
    }

    /**
     *get all the nodes and edges of network
     * @param rows
     */
    public static List<String[]> getNetProperties(List<String> rows){

        List<String[]> edges = new ArrayList<>();

        for (String row: rows){
            String[] edge = row.toUpperCase().split("\t");
            edges.add(edge);
        }
        return edges;
    }
}
