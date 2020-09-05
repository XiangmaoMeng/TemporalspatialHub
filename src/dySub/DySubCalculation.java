package dySub;

import algorithms.Centrality;

import utils.DataUtil;
import utils.FileUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;

/**
  * To evaluate the essentiality of proteins by integrating gene expression data and subcellular localization information
 * @author lwk
 * @version May 2, 2017 3:24:07 PM
 */
public class DySubCalculation {
	public static String[] locations = {"CYTOSKELETON","CYTOSOL","ENDOPLASMIC","ENDOSOME",
			"EXTRACELLULAR","GOLGI","LYSOSOME","MITOCHONDRION","NUCLEUS","PEROXISOME","PLASMA"};
	
	public static String dataset = "IntAct";//BioGRID,IntAct
	public static String dir = "D:/TemporalspatialHub/yeast/";
	
	
	public static String algo = "DC";
	public static String[] dynets = {"temporal"};// "NFAPIN"
	public static final int tNum = 12;
	
	public static void main(String[] args) throws Exception {

		System.out.println("----" + dataset + ";" + algo + "----");
		HashSet<String> essentials = FileUtil.readFile2Set(dir + "essentialProteins.txt");
		
		String desPath = dir + dataset+"/Temporal-spatial/";// save the temporal-spatial subnetworks
		File desFile = new File(desPath);
		if (!desFile.exists() && !desFile.isDirectory()) {
			desFile.mkdir();
		}
		
		//A set of proteins corresponding to the same subcellular location
		HashMap<String, HashSet<String>> subMap = new HashMap<>();
		List<String> allSubs = FileUtil.readFile2List(dir+"yeast_compartment_knowledge_full.txt");
		for (String location:locations) {
			HashSet<String> proteins = new HashSet<>();
			for (String str:allSubs) {
				String[] infos = str.toUpperCase().split("\t");
				if (infos[1].contains(location)) {
					proteins.add(infos[0]);
				}
			}
			subMap.put(location, proteins);
			//System.out.println(location+"\t"+proteins.size());
		}

		//String dynet = dynets[1];
		for(String dynet:dynets){
		System.out.println("----"+dynet+"----");
		//Save the centrality score for each node
		HashMap<String, Double> maxMap = new HashMap<>();
		for (int t = 0; t < tNum; t++) {
			//System.out.println("########");
			List<String> tNet = FileUtil.readFile2List(dir+dataset+"/"+dynet+"/"+ t + "_time_subnet.txt");
			//List<String> tNodes = getNodes(tNet);
			//List<String[]> tEdges = getEdges(tNet);
			//System.out.println(tNodes.size()+"\t"+tEdges.size());
			List<String> tNodes = getNodes(tNet);
			List<String[]> tEdges = getEdges(tNet);
			System.out.println(tNodes.size()+"\t"+tEdges.size());
			
			
			for (String location:locations) {
				HashSet<String> proteins = subMap.get(location);
				List<String> subEdges = new ArrayList<>();
				for(String str:tNet){
					String[] arr = str.toUpperCase().split("\t");
					if (proteins.contains(arr[0])&&proteins.contains(arr[1])) {
						subEdges.add(str);
					}
				}
				List<String> dsNodes = getNodes(subEdges);
				List<String[]> dsEdges = getEdges(subEdges);
				//System.out.println(location+"\t"+dsNodes.size()+"\t"+dsEdges.size());
				//printTSNetwork(dsEdges, "C:/Users/Dell/Desktop/tmp/"+t+"_"+location+".txt");
				System.out.println(location+"\t"+dsNodes.size()+"\t"+dsEdges.size());
				printTSNetwork(dsEdges, desPath+t+"_"+location+".txt");
				
				//Centrality calculation
				Class<?> c = Class.forName("algorithms."+algo);
				Constructor<?> con = c.getConstructor(List.class,List.class);
				Centrality cen = (Centrality) con.newInstance(dsNodes,dsEdges);
				
				HashMap<String, Double> dsMap = cen.process();
				//Select the maximum centrality value of a node at all times as its final centrality score
				for(Entry<String, Double> entry:dsMap.entrySet()){
					if (maxMap.containsKey(entry.getKey())) {
						double oldValue = maxMap.get(entry.getKey());
						if (oldValue<entry.getValue()) {
							maxMap.replace(entry.getKey(), entry.getValue());
						}
					}else {
						maxMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
		//The nodes were ranked in descending order according to their centrality scores
		List<Entry<String, Double>> list = new ArrayList<>(maxMap.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> arg0, Entry<String, Double> arg1) {
				return arg1.getValue().compareTo(arg0.getValue());
			}
		});
		//printCalculationResult(list, essentials, "C:/Users/Dell/Desktop/"+dynet+"_DC.txt");
		printCalculationResult(list, essentials, dir+dataset+"/"+dataset+"_TS_DC.txt");
		//output the Top result
		List<String> topNodes = new ArrayList<>();
		for(Entry<String, Double> entry:list){
			topNodes.add(entry.getKey());
		}
		//Determine if a protein is essential in the Temporal-spatial network
		utils.FileUtil.printCalculationResult(list, essentials, dir+dataset+"/"+dataset+"_TS_DC1.txt");
		//DataUtil.evaluate(topNodes, essentials);
		//DataUtil.jackknife(topNodes, essentials);
		DataUtil.calculateFraction(topNodes, essentials);
		DataUtil.evaluate(topNodes, essentials);
//		DataUtil.jackknife(topNodes, essentials);
		DataUtil.statistics(1285,topNodes, essentials);
		
		//FileUtil.write2File(topNodes, "C:/"+dataset+"_TS_ALL_DC.txt");
		FileUtil.write2File(topNodes, dir+dataset+"/"+dataset+"_TS_DC.txt");
		}
		System.out.println("----END---");
	}

	/**
	 * putput the temporal-spatial subnetworks
	 * @param list 
	 * @param path 
	 */
	public static void printTSNetwork(List<String[]> list, String path){
		List<String> tmpList = new ArrayList<>();
		for(String[] arr:list){
			tmpList.add(arr[0]+"\t"+arr[1]);
		}
		FileUtil.write2File(tmpList, path);
	}

	/**
	 * output the result of protein essential scores
	 * @param list
	 * @param set
	 * @param path
	 */
	public static void printCalculationResult(List<Entry<String, Double>> list, HashSet<String> set, String path){
		List<String> resList = new ArrayList<>();
		for(Entry<String, Double> entry:list){

			boolean flag = false;
			if (set.contains(entry.getKey())){
				flag = true;
			}
			resList.add(entry.getKey()+"\t"+entry.getValue()+"\t"+flag);
		}
		FileUtil.write2File(resList, path);
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
