package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
 * 文件操作
 * 
 * @author lwk
 * @version Mar 21, 2017 10:01:18 AM
 */
public class FileUtil {

	/**
	 * 从本地文件中读取数据到列表中， 列表中的一个元素表示一行数据
	 * 
	 * @param path
	 * @return list
	 */
	public static List<String> readFile2List(String path) {
		List<String> list = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(Paths.get(path));
			while (scanner.hasNextLine()) {
				list.add(scanner.nextLine());
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 从本地文件中读取数据到Hashset集合中
	 * 
	 * @param path
	 * @return
	 */
	public static HashSet<String> readFile2Set(String path) {
		HashSet<String> set = new HashSet<>();
		try {
			Scanner scanner = new Scanner(Paths.get(path));
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine().toUpperCase();
				boolean flag = set.add(str);
				if (!flag) {
					//System.out.println(str);
				}
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}

	public static HashMap<String, Double> readFile2Map(String path){
		HashMap<String, Double> map = new HashMap<>();

		try {
			Scanner scanner = new Scanner(Paths.get(path));
			while (scanner.hasNextLine()){
                String[] arr = scanner.nextLine().split("\t");
                double val = Double.parseDouble(arr[1]);
                map.put(arr[0],val);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 将列表中的数据写入到本地文件中
	 * 
	 * @param list
	 * @param path
	 */
	public static void write2File(List<String> list, String path) {

		try {
			PrintWriter writer = new PrintWriter(path);
			for (String str : list) {
				writer.println(str);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void printCalculationResult(List<Entry<String, Double>> list, HashSet<String> set, String path){
		List<String> resList = new ArrayList<>();
		for(Entry<String, Double> entry:list){

			int flag = 0;//0:nonessential,1:essential
			if (set.contains(entry.getKey())){
				flag = 1;
			}
			resList.add(entry.getKey()+"\t"+entry.getValue()+"\t"+flag);
		}
		FileUtil.write2File(resList, path);
	}
		
	
	public static void write2File(HashSet<String> set, String path) {
		try {
			PrintWriter writer = new PrintWriter(path);
			for (String str : set) {
				writer.println(str);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
