package algorithms;


import utils.DataUtil;
import utils.FileUtil;
import utils.SubcellularUtil;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;


/**
 * @author mxm and lwk
 *
 * 2020年8月4日
 */
public class Main {

	public static String algo = "DC";
	public static String dataset = "IntAct";//BioGRID,IntAct
	public static String dir = "D:/TemporalspatialHub/yeast/";

	public static void main(String[] args) throws Exception {
		System.out.println("----" + dataset + ";" + algo + "----");
		HashSet<String> essentials = FileUtil.readFile2Set(dir + "essentialProteins.txt");//read the known essential proteins file
		List<String> net = FileUtil.readFile2List(dir+dataset+"/network.txt");//read the static PPI network file
		
		System.out.print("nodes(proteins)\t");
		List<String> nodes = SubcellularUtil.getNodes(net);
		System.out.println(nodes.size());
		System.out.print("edges(PPIs)\t");
		List<String[]> edges = SubcellularUtil.getEdges(net);
		System.out.println(edges.size());
		DataUtil.compare(essentials, new HashSet<>(nodes));

		// Call the DC Centrality method
		Class<?> c = Class.forName("algorithms." + algo);
		Constructor<?> con = c.getConstructor(List.class, List.class);
		Centrality cen = (Centrality) con.newInstance(nodes, edges);
		HashMap<String, Double> map = cen.process();

		List<Entry<String, Double>> list = new ArrayList<>(map.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> arg0, Entry<String, Double> arg1) {
				return arg1.getValue().compareTo(arg0.getValue());
			}
		});
		calculateMean(list);
		
		//Determine if a protein is essential in the original static network
		utils.FileUtil.printCalculationResult(list, essentials, dir+dataset+"/"+dataset+"_DC1.txt");
		//top output
		List<String> topNodes = new ArrayList<>();
		for(Entry<String, Double> entry:list){
			topNodes.add(entry.getKey());
		}

		DataUtil.calculateFraction(topNodes, essentials);
		//DataUtil.evaluate(topNodes, essentials);
		//DataUtil.jackknife(topNodes, essentials);
		DataUtil.evaluate(topNodes, essentials);
//		DataUtil.jackknife(topNodes, essentials);
//		DataUtil.statistics(800,topNodes, essentials);
		//FileUtil.write2File(topNodes, "C:/Users/Dell/Desktop/"+dataset+"_ALL_DC.txt");
		FileUtil.write2File(topNodes, dir+"/"+dataset+"/"+dataset+"_DC.txt");

		System.out.println("----END----");
	}


	public static void calculateMean(List<Entry<String, Double>> list){
		double sum = 0;
		for (Entry<String,Double> entry:list){
			sum += entry.getValue();
		}
		double mean = sum / list.size();
		System.out.println("The average degree value：" + mean);
	}
}
