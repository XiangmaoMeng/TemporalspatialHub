package analysis;

import utils.DataUtil;
import utils.FileUtil;

import java.util.HashSet;
import java.util.List;

public class Hub2NHub_2 {

	public static String dataset = "IntAct";//BioGRID,IntAct
	public static String dir = "D:/TemporalspatialHub/yeast/";
	
	public static final int TOP_NUM = 200;

	public static void main(String[] args) {

		HashSet<String> ess = FileUtil.readFile2Set(dir + "essentialProteins.txt");

		HashSet<String> sTop = getTopNodeSet(dir  + dataset+"/"+  dataset + "_DC.txt");
		HashSet<String> tsTop = getTopNodeSet(dir  + dataset+"/"+ dataset + "_TS_DC.txt");
		System.out.println("the origrinal TOP set：");
		DataUtil.compare(sTop, tsTop);
		HashSet<String> TemporospatialHub = new HashSet<>();
		HashSet<String> StaticHub = new HashSet<>();
		for (String hub : sTop) {
			if (tsTop.contains(hub)) {
				TemporospatialHub.add(hub);
			} else {
				StaticHub.add(hub);
			}
		}
		FileUtil.write2File(TemporospatialHub, dir+dataset+"/"+dataset+"_TemporospatialHub.txt");
		FileUtil.write2File(StaticHub, dir+dataset+"/"+dataset+"_StaticHub.txt");
		System.out.println("TemporospatialHub set with StaticHub set：");
		DataUtil.compare(TemporospatialHub, StaticHub);
		System.out.println("TemporospatialHub set with essential proteins set：");
		DataUtil.compare(ess, TemporospatialHub);
		System.out.println("StaticHub set with essential proteins set：");
		DataUtil.compare(ess, StaticHub);

		System.out.println("----END----");
	}

	public static HashSet<String> getTopNodeSet(String path) {

		HashSet<String> set = new HashSet<>();
		List<String> dcList = FileUtil.readFile2List(path);
		for (int i = 0; i < TOP_NUM; i++) {
			String[] arr = dcList.get(i).split("\t");
			set.add(arr[0]);
		}

		return set;
	}

}
