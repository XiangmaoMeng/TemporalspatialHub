package subnet;

import algorithms.Centrality;
import utils.DataUtil;
import utils.FileUtil;
import utils.SubcellularUtil;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;

/**
 * Amethod based on Localization Specificity for Essential protein Detection (LSED)
 * reference:
 * Peng X, Wang J, Wang J, et al. Rechecking the centrality-lethality rule in the scope of 
 * protein subcellular localization interaction networks[J]. PloS one, 2015, 10(6): e0130743.

 * @author mxm and lwk
 *
 * 2020年8月4日
 */
public class SubcellularLsed {

	public static String[] locations = { "CYTOSKELETON", "CYTOSOL", "ENDOPLASMIC", "ENDOSOME", "EXTRACELLULAR", "GOLGI",
			"LYSOSOME", "MITOCHONDRION", "NUCLEUS", "PEROXISOME", "PLASMA" };
	
	public static String dataset = "IntAct";//BioGRID,IntAct
	public static String dir = "D:/TemporalspatialHub/yeast/";
	
	public static String algo = "DC";

	public static void main(String[] args) throws Exception {
		System.out.println("----" + dataset + ";" + algo + "----");
		HashSet<String> essentials = FileUtil.readFile2Set(dir + "essentialProteins.txt");

		// The static PPI network are divided into 11 spatial subnetworks based on the subcellular location information
		String networkPath = dir + dataset + "/network.txt";
		String subcellularPath = dir + "yeast_compartment_knowledge_full.txt";
		List<Subcellular> subList = SubcellularUtil.partitionSubcellular(networkPath, subcellularPath, locations);
		for (Subcellular sub : subList) {
			sub.setReliability(1.0*sub.getNodes().size()/subList.get(0).getNodes().size());
		}
		for (Subcellular sub : subList) {
			System.out.printf("%-15s%6d%6d%12f\n", sub.getName(), sub.getNodes().size(), sub.getEdges().size(),
					sub.getReliability());
		}
		
		// store the score of each node
		HashMap<String, Double> scores = new HashMap<>();
		for (Subcellular sub : subList) {
			// calculate the degree centrality
			Class<?> c = Class.forName("algorithms." + algo);
			Constructor<?> con = c.getConstructor(List.class, List.class);
			Centrality cen = (Centrality) con.newInstance(sub.getNodes(), sub.getEdges());
			HashMap<String, Double> teMap = cen.process();
			
			for (Entry<String, Double> entry : teMap.entrySet()) {
				if (scores.containsKey(entry.getKey())) {
					double oldValue = scores.get(entry.getKey());
					if (oldValue < entry.getValue()) {
						double newValue = oldValue + (entry.getValue()-oldValue)*sub.getReliability();
						scores.replace(entry.getKey(), newValue);
					}
				} else {
					double score = entry.getValue()*sub.getReliability();
					scores.put(entry.getKey(), score);
				}
			}
		}

		// In descending order according to the score
		List<Entry<String, Double>> list = new ArrayList<>(scores.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		//Determine if a protein is essential in the spatial network
		utils.FileUtil.printCalculationResult(list, essentials, dir+dataset+"/"+dataset+"_S_DC1.txt");
		// top output
		List<String> topNodes = new ArrayList<>();
		for (Entry<String, Double> entry : list) {
			topNodes.add(entry.getKey());
		}
		DataUtil.calculateFraction(topNodes, essentials);
		DataUtil.evaluate(topNodes, essentials);
		//DataUtil.jackknife(topNodes, essentials);
//		DataUtil.jackknife(topNodes, essentials);
		DataUtil.statistics(800,topNodes, essentials);
		FileUtil.write2File(topNodes, dir+"/"+dataset+"/"+dataset+"_S_DC.txt");

		System.out.println("----END----");
	}
}
