package analysis;

public class DsNode {
	/**
	 * 蛋白质编号
	 */
	private int id;
	/**
	 * 蛋白质名称
	 */
	private String name;
	/**
	 * 蛋白质关键性
	 */
	private int essentiality;
	/**
	 * 表达时间集合
	 */
	private int[] expressions;
	/**
	 * 所在亚细胞集合
	 */
	private int[] subcellulars;
	/**
	 * 原始度值
	 */
	private double beforeDegree;
	/**
	 * 最终度值
	 */
	private double afterDegree;
	/**
	 * 最终度值对应的时刻及亚细胞位置
	 */
	private int[] locations;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEssentiality() {
		return essentiality;
	}

	public void setEssentiality(int essentiality) {
		this.essentiality = essentiality;
	}

	public int[] getExpressions() {
		return expressions;
	}

	public void setExpressions(int[] expressions) {
		this.expressions = expressions;
	}

	public int[] getSubcellulars() {
		return subcellulars;
	}

	public void setSubcellulars(int[] subcellulars) {
		this.subcellulars = subcellulars;
	}

	public double getBeforeDegree() {
		return beforeDegree;
	}

	public void setBeforeDegree(double beforeDegree) {
		this.beforeDegree = beforeDegree;
	}

	public double getAfterDegree() {
		return afterDegree;
	}

	public void setAfterDegree(double afterDegree) {
		this.afterDegree = afterDegree;
	}

	public int[] getLocations() {
		return locations;
	}

	public void setLocations(int[] locations) {
		this.locations = locations;
	}

	public DsNode() {
	}

	public DsNode(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "DsNode [id=" + id + ", name=" + name + ", essentiality=" + essentiality + ", beforeDegree="
				+ beforeDegree + ", afterDegree=" + afterDegree + ", locations:" + locations[0]+","+locations[1] + "]";
	}

}
