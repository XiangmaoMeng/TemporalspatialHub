package algorithms;

import java.util.HashMap;
import java.util.List;

/**
 * degree centrality
 * @author mxm and lwk
 *
 * 2020年8月4日
 */
public class DC extends Centrality{
	
	public DC(List<String> nodes, List<String[]> edges) {
		this.nodes = nodes;
		this.edges = edges;
		
		//Construct the adjacency matrix
		adjMatrix = makeMatrix(nodes, edges);
	}

	@Override
	public HashMap<String, Double> process() {
		HashMap<String, Double> map = new HashMap<>();
		
		for (int i = 0; i < nodes.size(); i++) {
			int neiNum = 0;
			for (int j = 0; j < nodes.size(); j++) {
				if (adjMatrix[i][j] != 0) {
					neiNum = neiNum + 1;
				}
			}
			map.put(nodes.get(i), (double)neiNum);
		}
		
		return map;
	}
	
}
