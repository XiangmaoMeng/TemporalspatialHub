package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import subnet.Subcellular;

/**
 * 获取11种亚细胞子网
 * @author lwk
 * @version May 9, 2017 2:55:56 PM
 */
public class SubcellularUtil {
	
	/**
	 * 基于亚细胞定位信息和原始网络进行子网的划分，
	 * 并返回按子网可信度由大到小排序的子网集合。
	 * @param networkPath
	 * @param subcellularPath
	 * @param locations
	 * @return
	 */
	public static List<Subcellular> partitionSubcellular(String networkPath,String subcellularPath, String[] locations){
		
		List<Subcellular> resList = new ArrayList<>();
		//获取符合亚细胞条件的蛋白质节点
		List<String> allSubs = FileUtil.readFile2List(subcellularPath);
		List<HashSet<String>> subList = new ArrayList<>();
		for(String location:locations){
			HashSet<String> proteins = new HashSet<>();
			for(String str:allSubs){
				String[] infos = str.toUpperCase().split("\t");
				if (infos[1].contains(location)) {
					proteins.add(infos[0]);
				}
			}
			subList.add(proteins);
		}
		
		//获取每个亚细胞子网中的点和边
		List<String> oriNet = FileUtil.readFile2List(networkPath);
		//int oriNetSize = getNodes(oriNet).size();
		for (int i = 0; i < locations.length; i++) {
			HashSet<String> proteins = subList.get(i);
			List<String> interactions = new ArrayList<>();
			for(String str:oriNet){
				String[] arr = str.toUpperCase().split("\t");
				if (proteins.contains(arr[0])&&proteins.contains(arr[1])) {
					interactions.add(str);
				}
			}
			//初始化亚细胞对象
			Subcellular subCell = new Subcellular(locations[i]);
			List<String> teNodes = getNodes(interactions);
			subCell.setNodes(teNodes);
			List<String[]> teEdges = getEdges(interactions);
			subCell.setEdges(teEdges);
			double result = 1.0 * teNodes.size();
			subCell.setReliability(result);
			
			resList.add(subCell);
		}
		
		//根据亚细胞区间的可信度值由小到大进行排序
		Collections.sort(resList, new Comparator<Subcellular>() {
			@Override
			public int compare(Subcellular o1, Subcellular o2) {
				return Double.compare(o2.getReliability(), o1.getReliability());
			}
		});
		
		return resList;
	}
	
	/**
	 * 根据网络列表获取所有节点集合
	 * @param tList
	 * @return
	 */
	public static List<String> getNodes(List<String> tList) {
		HashSet<String> set = new HashSet<>();
		for (String str : tList) {
			String[] arr = str.toUpperCase().split("\t");
			set.add(arr[0]);
			set.add(arr[1]);
		}
		return new ArrayList<>(set);
	}
	
	/**
	 * 根据网络列表获取所有边集合
	 * @param tList
	 * @return
	 */
	public static List<String[]> getEdges(List<String> tList) {
		List<String[]> list = new ArrayList<>();
		for (String str : tList) {
			String[] arr = str.toUpperCase().split("\t");
			list.add(arr);
		}
		return list;
	}
}
