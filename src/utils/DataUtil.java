package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * data  handling

 * @author mxm and lwk
 *
 * 2020年8月4日
 */
public class DataUtil {
	
	public static void compare(HashSet<String> xSet, HashSet<String> ySet){
		int sameNum = 0;
		for (String str : xSet) {
			if (ySet.contains(str)) {
				sameNum++;
			}
		}
		System.out.printf("The number of nodes in the two sets：%d,%d\n", xSet.size(), ySet.size());
		System.out.printf("The number of shared nodes between the two sets：%d\n", sameNum);
	}
	
	public static void compare(HashSet<String> xSet, HashSet<String> ySet, HashSet<String> zSet){
		int sameNum = 0;
		for (String str : xSet) {
			if (ySet.contains(str) && zSet.contains(str)) {
				sameNum++;
			}
		}
		System.out.printf("The number of proteins in the three sets：%d,%d,%d\n", xSet.size(), ySet.size(), zSet.size());
		System.out.printf("The number of shared proteins among the three sets：%d\n", sameNum);
	}
	
	public static void evaluate(List<String> nodes, HashSet<String> essentials){
		
		System.out.printf("The number of proteins in the PPI network and the number of known essential protein：%d, %d\n",nodes.size(),essentials.size());
		int[] topNums = { 100, 200, 300, 400, 500, 600, 700, 800};
		for (int topNum : topNums) {
			System.out.print(topNum + "\t");
		}
		System.out.println();

		for (int topNum : topNums) {
			if (nodes.size()>=topNum) {
				int tpNum = 0;
				for (int i = 0; i < topNum; i++) {
					String node = nodes.get(i);
					if (essentials.contains(node)) {
						tpNum = tpNum + 1;
					}
				}
				System.out.print(tpNum + "\t");
			}
		}
		System.out.println();
	}
	
	public static void jackknife(List<String> topList, HashSet<String> essentials){
		int tp = 0;
		for (int i = 0; i < 800; i++) {
			if (essentials.contains(topList.get(i))) {
				tp = tp + 1;
			}
			if ((i+1)%10==0) {
				//System.out.println((double)tp / (i+1));
				System.out.println(tp);
			}
		}
	}

	public static void statistics(int top, List<String> nodes, HashSet<String> essentials){

		//The total number of essential proteins in the PPI network
		int essNum = 0;
		for (String node:nodes){
			if (essentials.contains(node)){
				essNum = essNum + 1;
			}
		}
		System.out.println("The total number of essential proteins in the PPI network ："+essNum);

		top = (top==0)?essNum:top;
		int tp=0,fp=0;
		for (int i = 0; i < top; i++) {
			if (essentials.contains(nodes.get(i))){
				tp = tp + 1;
			}else {
				fp = fp + 1;
			}
		}
		int fn=0,tn=0;
		for (int i = top; i < nodes.size(); i++) {
			if (essentials.contains(nodes.get(i))){
				fn = fn + 1;
			}else {
				tn = tn + 1;
			}
		}

		double sn = 1.0 * tp / (tp+fn);
		double sp = 1.0 * tn / (tn+fp);
		double ppv = 1.0 * tp / (tp+fp);
		double npv = 1.0 * tn / (tn+fn);
		double fScore = 2.0 * sn * ppv / (sn+ppv);
		double acc = 1.0 * (tp+tn) / (tp+tn+fp+fn);

		DecimalFormat df = new DecimalFormat("0.000");
//		System.out.printf("sn,sp,ppv,npv,fScore,acc,tp:\n");
//		System.out.printf("%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%d\n",sn,sp,ppv,npv,fScore,acc,tp);

	}

	public static void differentiation(int top, List<String> ourNodes, List<String> othNodes, HashSet<String> essentials){
		List<String> ourTop = ourNodes.subList(0,top);
		List<String> othTop = othNodes.subList(0,top);

		List<String> difList = new ArrayList<>();
		int comNum = 0;
		for (String node:ourTop){
			if (!othTop.contains(node)){
				difList.add(node);
			}else {
				othTop.remove(node);
				comNum = comNum + 1;
			}
		}
		System.out.println("the number of different proteins:"+difList.size()+";the number of shared proteins:"+comNum);

		int ourEssNum = 0;
		for (String node:difList){
			if (essentials.contains(node)){
				ourEssNum = ourEssNum + 1;
			}
		}
		System.out.printf("the number of essential protein identified by our method：%d\t%.3f\n",ourEssNum,(double)ourEssNum/difList.size());
		int othEssNum = 0;
		for (String node:othTop){
			if (essentials.contains(node)){
				othEssNum = othEssNum + 1;
			}
		}
		System.out.printf("the number of essential protein identified by other method：%d\t%.3f\n",othEssNum,(double)othEssNum/othTop.size());
	}

	/**
	 * The proportion of essential proteins in different TOP sets
	 * @param topList
	 * @param essetnials
	 */
	public static void calculateFraction(List<String> topList, HashSet<String> essetnials){
		int essNum = 0;
		System.out.println("TOP\tFRACTION");
		for (int topLoc=0; topLoc<=800; topLoc++){
			if (essetnials.contains(topList.get(topLoc))){
				essNum = essNum + 1;
			}
			if (topLoc%20 == 0){
				double fra = 1.0 * essNum / topLoc;
				System.out.println(topLoc+"\t"+fra);
			}
		}
	}
}
