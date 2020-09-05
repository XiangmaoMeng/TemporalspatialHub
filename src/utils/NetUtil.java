package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 网络操作
 * @author lwk
 * @version Mar 27, 2017 4:48:54 PM
 */
public class NetUtil {
	
	/**
	 * 对静态网络筛选活性蛋白质间的相互作用网络
	 * @param actives 活性蛋白质集合
	 * @param net 原始蛋白质相互作用网络
	 * @return
	 */
	public static List<String> getTempralNet(HashSet<String> actives, List<String> net){
		List<String> list = new ArrayList<>();
		for (String edge : net) {
			String[] arr = edge.split("\t");
			if (actives.contains(arr[0]) && actives.contains(arr[1])) {
				list.add(edge);
			}
		}
		return list;
	}
	
	/**
	 * 从网络文件中获取所有节点
	 * @param net
	 * @return
	 */
	public static HashSet<String> getNodes4Net(List<String> net){
		HashSet<String> set = new HashSet<>();
		for(String str:net){
			String[] arr = str.split("\t");
			set.add(arr[0]);
			set.add(arr[1]);
		}
		return set;
	}
}
