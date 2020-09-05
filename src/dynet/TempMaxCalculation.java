package dynet;

import algorithms.Centrality;
import utils.DataUtil;
import utils.FileUtil;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;

/**
 * Temporal network 
 * The maximum value at all times is chosen as the final centrality score of a protein
 * @author mxm and lwk
 *
 * 2020年8月26日
 */
public class TempMaxCalculation {

	public static String algo = "DC";
	public static String dataset = "IntAct";//BioGRID,IntAct,DIP
	public static String dir = "D:/TemporalspatialHub/yeast/";
	
	
	public static String[] dynets = {"temporal"};// "NFAPIN"
	public static final int tNum = 12;

	public static void main(String[] args) throws Exception {
		System.out.println("----" + dataset + "----");
		HashSet<String> essentials = FileUtil.readFile2Set(dir + "essentialProteins.txt");

		//String dynet = dynets[0];
		for(String dynet:dynets){
			System.out.println("======"+dynet+"======");
			HashMap<String, Double> maxMap = new HashMap<>();
			
			for (int t = 0; t < tNum; t++) {
				// get the subnetwork
				List<String> edges = FileUtil.readFile2List(dir + dataset + "/" + dynet + "/" + t + "_time_subnet.txt");
				List<String> tNodes = getNodes(edges);
				List<String[]> tEdges = getEdges(edges);
				//System.out.printf("%d时刻点和边的数目：%d,%d\n", t, tNodes.size(), tEdges.size());
				//DataUtil.compare(new HashSet<>(tNodes), essentials);

				// Centrality calculation
				Class<?> c = Class.forName("algorithms." + algo);
				Constructor<?> con = c.getConstructor(List.class, List.class);
				Centrality cen = (Centrality) con.newInstance(tNodes, tEdges);
				HashMap<String, Double> tMap = cen.process();
				//save the maximum value at each timepoint
				for(Entry<String, Double> entry:tMap.entrySet()){
					if (maxMap.containsKey(entry.getKey())) {
						double temp = maxMap.get(entry.getKey());
						if (temp<entry.getValue()) {
							maxMap.replace(entry.getKey(), entry.getValue());
						}
					}else {
						maxMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
			//In descending order according to the centrality score
			List<Entry<String, Double>> list = new ArrayList<>(maxMap.entrySet());
			Collections.sort(list, new Comparator<Entry<String, Double>>() {
				@Override
				public int compare(Entry<String, Double> arg0, Entry<String, Double> arg1) {
					return arg1.getValue().compareTo(arg0.getValue());
				}
			});
			// output the TOP result
			List<String> topNodes = new ArrayList<>();
			for (Entry<String, Double> entry : list) {
				topNodes.add(entry.getKey());
			}
			//Determine if a protein is essential in the temporal network
			utils.FileUtil.printCalculationResult(list, essentials, dir+dataset+"/"+dataset+"_T_DC1.txt");
			
			DataUtil.calculateFraction(topNodes, essentials);
			DataUtil.evaluate(topNodes, essentials);
			//DataUtil.jackknife(topNodes, essentials);
//			DataUtil.jackknife(topNodes, essentials);
			DataUtil.statistics(1285,topNodes, essentials);
			FileUtil.write2File(topNodes, dir+"/"+dataset+"/"+dataset+"_T_DC.txt");
		}

		System.out.println("----END----");
	}

	public static List<String> getNodes(List<String> tList) {
		HashSet<String> set = new HashSet<>();
		for (String str : tList) {
			String[] arr = str.toUpperCase().split("\t");
			set.add(arr[0]);
			set.add(arr[1]);
		}
		return new ArrayList<>(set);
	}

	public static List<String[]> getEdges(List<String> tList) {
		List<String[]> list = new ArrayList<>();
		for (String str : tList) {
			String[] arr = str.toUpperCase().split("\t");
			list.add(arr);
		}
		return list;
	}
}
