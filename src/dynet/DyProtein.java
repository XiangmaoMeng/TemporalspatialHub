package dynet;

/**
 * 蛋白质节点
 * @author lwk
 * @version Mar 27, 2017 3:36:34 PM
 */
public class DyProtein {

	/**
	 * 蛋白质名称
	 */
	private String name;
	/**
	 * 蛋白质在各个时间点的活性值
	 */
	private double[] exps;
	
	private double threshold;

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getExps() {
		return exps;
	}

	public void setExps(double[] exps) {
		this.exps = exps;
	}

	public DyProtein() {
	}

	public DyProtein(String name, double[] exps) {
		super();
		this.name = name;
		this.exps = exps;
	}

}
