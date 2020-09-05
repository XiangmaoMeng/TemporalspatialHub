package dynet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import utils.FileUtil;
import utils.NetUtil;
import utils.SubcellularUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Time dependent and time independent models
 * reference:
 * Xiao Q, Wang J, Peng X, et al. Detecting protein complexes fromactive protein interaction networks
 *  constructed with dynamic gene expression profiles[J]. Proteome Science, 2013, 11(1):S20.

 * @author mxm and lwk
 *
 * 2020年8月4日
 */
public class DyXiao {

	public static String dataset = "IntAct";//BioGRID,IntAct
	public static String dir = "D:/TemporalspatialHub/yeast/";
	public static final int tNum = 12;

	public static void main(String[] args) {
		System.out.println("----" + dataset + "----");
		
		//read the static PPI network file
		List<String> netList = FileUtil.readFile2List(dir + dataset + "/network.txt");
		System.out.printf("the numbers of nodes and edges of the static PPI network：\n%d\t%d\n"
				,SubcellularUtil.getNodes(netList).size(),netList.size());
		
		List<DyProtein> proList = new ArrayList<>();
		
		// read the gene expression file
		List<String> expList = FileUtil.readFile2List(dir + "GSE3431_T12.txt");
		
		// read the  noise information of gene expression file,which is obtained by 
		//the Time dependent and time independent models
		HashSet<String> noise = FileUtil.readFile2Set(dir + "xiao_noise.txt");
		for (String exp : expList) {
			String[] infos = exp.split("\t");
			// filter the noise of the gene expression data
			if (!noise.contains(infos[0])) {
				double[] arr = new double[tNum];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = Double.parseDouble(infos[i + 1]);
				}
				proList.add(new DyProtein(infos[0], arr));
			}
		}
		System.out.println(proList.size());
		// calculate the active threshold
		for (DyProtein pro : proList) {
			double threshold = getThreshold(pro.getExps());
			pro.setThreshold(threshold);
		}
		//construct the temporal dynamic networks
		for (int i = 0; i < tNum; i++) {
			HashSet<String> set = new HashSet<>();
			for (DyProtein pro : proList) {
				if (pro.getExps()[i] > pro.getThreshold()) {
					set.add(pro.getName());
				}
			}
			List<String> edges = NetUtil.getTempralNet(set, netList);
			System.out.printf("the numbers of nodes and edges at %d timepoint：%d\t%d\n", i, SubcellularUtil.getNodes(edges).size(), edges.size());
			
			String desPath = dir + dataset+"/temporal/";// save the 12 temporal subnetworks
			File desFile = new File(desPath);
			if (!desFile.exists() && !desFile.isDirectory()) {
				desFile.mkdir();
			}
			
			
						FileUtil.write2File(edges, desPath+i+"_time_subnet.txt");
		}

		System.out.println("----END----");
	}

	/**
	 * Obtain the active threshold of the corresponding protein
	 * 
	 * @param arr
	 * @return
	 */
	public static double getThreshold(double[] arr) {
		DescriptiveStatistics ds = new DescriptiveStatistics(arr);
		double mu = ds.getMean();
		double sigma = ds.getStandardDeviation();
		double threshold = mu + 2.5 * sigma * (1 - 1.0 / (1 + sigma * sigma));
		// System.out.println(sigma + "\t" + threshold);
		return threshold;
	}
}
