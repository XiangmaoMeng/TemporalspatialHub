package dynet;
/**
 * 描述网络中一条边的信息
 * 
 * @author lwk
 * @version Apr 3, 2017 1:23:15 PM
 */
class DyEdge {
	private String fName;
	private String tName;
	private double wVal;

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public double getwVal() {
		return wVal;
	}

	public void setwVal(double wVal) {
		this.wVal = wVal;
	}

	public DyEdge() {
		super();
	}

	public DyEdge(String fName, String tName, double wVal) {
		super();
		this.fName = fName;
		this.tName = tName;
		this.wVal = wVal;
	}

}
