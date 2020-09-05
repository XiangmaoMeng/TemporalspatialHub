package analysis;

import utils.FileUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * calculate the fractions of essential proteins in TemporospatialHub and StaticHub
 */
public class Hub2NHub {

	public static String dataset = "IntAct";//BioGRID,IntAct,DIP
	public static String dir = "D:/TemporalspatialHub/yeast/";
	
	public static final int TOP_NUM = 600;

	public static void main(String[] args) {
		
		HashSet<String> ess = FileUtil.readFile2Set(dir + "essentialProteins.txt");
		
		List<String> sList = getNodeList(dir + dataset+"/"+ dataset + "_DC.txt");
		List<String> tsList = getNodeList(dir + dataset+"/"+ dataset + "_TS_DC.txt");
		
		System.out.printf("%s\t%s\t%s\n", "TOP","StaticHub","TemporospatialHub");//TemporospatialHub and StaticHub
		for (int top = 0; top <= TOP_NUM; top = top+50) {
			HashSet<String> sTop = getTopSet(sList, top);
			HashSet<String> tsTop = getTopSet(tsList, top);
			HashSet<String> TemporospatialHub = new HashSet<>();
			HashSet<String> StaticHub = new HashSet<>();
			for (String hub : sTop) {
				if (tsTop.contains(hub)) {
					TemporospatialHub.add(hub);
				} else {
					StaticHub.add(hub);
				}
			}
			double sFra = getFractionofEssential(StaticHub, ess);
			double tsFra = getFractionofEssential(TemporospatialHub, ess);
			System.out.printf("%d\t%.4f\t%.4f\n", top,sFra,tsFra);
		}

		System.out.println("----END----");
	}
	
	/**
	 * get the essential proteins from the set
	 * @param hubs
	 * @param ess
	 * @return
	 */
	public static double getFractionofEssential(HashSet<String> hubs,HashSet<String> ess){
		int sum = 0;
		for(String str:hubs){
			if (ess.contains(str)) {
				sum = sum + 1;
			}
		}
		return 1.0*sum / hubs.size();
	}
	
	/**
	 * get the ranked nodes from the file
	 * @param path
	 * @return
	 */
	public static List<String> getNodeList(String path) {

		List<String> list = new ArrayList<>();
		List<String> dcList = FileUtil.readFile2List(path);
		for (String str:dcList) {
			String[] arr = str.split("\t");
			list.add(arr[0]);
		}

		return list;
	}
	
	/**
	 * get the TOP set from the rankd list
	 * @param list
	 * @param topNum
	 * @return
	 */
	public static HashSet<String> getTopSet(List<String> list, int topNum){
		HashSet<String> set = new HashSet<>();
		for (int i = 0; i < topNum; i++) {
			set.add(list.get(i));
		}
		return set;
	}
 	
}
