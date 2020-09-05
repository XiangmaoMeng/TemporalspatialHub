package subnet;

import java.util.List;

/**
 * 11 subcellular compartments：
 * "CYTOSKELETON","CYTOSOL","ENDOPLASMIC","ENDOSOME","EXTRACELLULAR","GOLGI",
 * "LYSOSOME","MITOCHONDRION","NUCLEUS","PEROXISOME","PLASMA";

 * @author mxm and lwk
 *
 * 2020年8月4日
 */
public class Subcellular {
	/**
	 * the name of subcellular compartments
	 */
	private String name;
	
	private List<String> nodes;
	
	private List<String[]> edges;
	/**
	 * the reliability of each subcellular compartments
	 */
	private double reliability;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

	public List<String[]> getEdges() {
		return edges;
	}

	public void setEdges(List<String[]> edges) {
		this.edges = edges;
	}

	public double getReliability() {
		return reliability;
	}

	public void setReliability(double reliability) {
		this.reliability = reliability;
	}

	public Subcellular() {
	}

	public Subcellular(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Subcellular [name=" + name + ", nodes=" + nodes.size() + ", edges=" + edges.size() + ", reliability="
				+ reliability + "]";
	}

}
