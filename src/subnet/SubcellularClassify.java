package subnet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Process the subcellular location information data and divid it into 11 spatial subnetworks according to 11 subcellular locations

 * @author mxm and lwk
 *
 * 2020年8月4日
 */
public class SubcellularClassify {
	
	public static String[] locations = {"CYTOSKELETON","CYTOSOL","ENDOPLASMIC","ENDOSOME",
			"EXTRACELLULAR","GOLGI","LYSOSOME","MITOCHONDRION","NUCLEUS","PEROXISOME","PLASMA"};
	
	public static String dir = "D:/TemporalspatialHub/yeast/";
	public static String dataset = "IntAct";//BioGRID,IntAct
	
	public static void main(String[] args) throws IOException {
		
		List<String[]> subloc = fiel2list(dir+"yeast_compartment_knowledge_full.txt");
		List<String[]> network = fiel2list(dir+dataset+"/network.txt");
		String desPath = dir + dataset+"/subcellular/";// save the 11 spatial subnetworks
		File desFile = new File(desPath);
		if (!desFile.exists() && !desFile.isDirectory()) {
			desFile.mkdir();
		}
		
		for (String location : locations) {			
			//save the number of proteins in the same subcellular location
			HashSet<String> proteins = new HashSet<>();
			for (String[] infos : subloc) {
				if (infos[1].contains(location)) {
					proteins.add(infos[0]);
				}
			}
			System.out.println(location+"：\n\tall-"+proteins.size());
			
			//save the PPIs in the same subcellular location
			HashSet<String> resSet = new HashSet<>();
			List<String> resList = new ArrayList<>();
			for (String[] interaction : network) {
				if (proteins.contains(interaction[0]) && proteins.contains(interaction[1])) {
					resSet.add(interaction[0]);
					resSet.add(interaction[1]);
					resList.add(interaction[0]+"\t"+interaction[1]);
				}
			}
			System.out.println("\tnode:"+resSet.size());
			System.out.println("\tedge:"+resList.size());
			
			//save the spatial subnetworks
			PrintWriter writer = new PrintWriter(desPath+location.toLowerCase()+"_subnetwork.txt");
			for (String str : resList) {
				writer.println(str);
			}
			writer.close();
		}
		
		System.out.println("----END----");
	}
	
	/**
	 * save the file to list
	 * @param filepath
	 * @return
	 */
	public static List<String[]> fiel2list(String filepath){
		
		List<String[]> list = new ArrayList<>();
		
		try {
			Scanner scanner = new Scanner(Paths.get(filepath));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().toUpperCase();
				String[] arr = line.split("\t");
				list.add(arr);
			}
			scanner.close();
			System.out.println("the size of the file(lines number)："+list.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
