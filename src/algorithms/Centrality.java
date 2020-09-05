package algorithms;

import java.util.HashMap;
import java.util.List;

/**
 * method class
 * @author mxm and lwk
 *
 * 2020年8月4日
 */
public abstract class Centrality {
	
	public List<String> nodes;
	public List<String[]> edges;
	public int[][] adjMatrix;
	
	/**
	 * deteiled calculation process 
	 */
	public abstract HashMap<String, Double> process();
	
	/**
	 * Construct unweighted network
	 */
	public int[][] makeMatrix(List<String> nodes, List<String[]> edges) {

		int len = nodes.size();
		int eLen = edges.size();
		int[][] data = new int[len][len];
		String[] edge;
		int m, n;
		try {
			for (m = 0; m < len; m++) {
				for (n = 0; n < len; n++) {
					data[m][n] = 0;
				}
			}
			for (int i = 0; i < eLen; i++) {
				edge = edges.get(i);
				for (m = 0; m < len; m++) {
					if (nodes.get(m).equals(edge[0])) {
						break;
					}
				}
				for (n = 0; n < len; n++) {
					if (nodes.get(n).equals(edge[1])) {
						break;
					}
				}
				if (n < len) {
					data[n][m] = 1;
					data[m][n] = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}
	
	/**
	 * Construct weighted network
	 */
	public int[][] makeWeiMatrix(List<String> nodes, List<String[]> edges) {

		int len = nodes.size();
		int eLen = edges.size();
		int[][] data = new int[len][len];
		String[] edge;
		int m, n;
		for (m = 0; m < len; m++) {
			for (n = 0; n < len; n++) {
				data[m][n] = 0;
			}
		}
		for (int i = 0; i < eLen; i++) {
			edge = edges.get(i);
			for (m = 0; m < len; m++) {
				if (nodes.get(m).equals(edge[0])) {
					break;
				}
			}
			for (n = 0; n < len; n++) {
				if (nodes.get(n).equals(edge[1])) {
					break;
				}
			}
			if (n < len) {
				data[n][m] = Integer.parseInt(edge[2]);
				data[m][n] = Integer.parseInt(edge[2]);
			}
		}

		return data;
	}
}
