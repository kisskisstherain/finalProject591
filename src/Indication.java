
public class Indication {
	private String primaryID;
	private String drugSeq;
	private String indiPT;
	
	public Indication(String primaryID, String drugSeq, String indiPT) {
		super();
		this.primaryID = primaryID;
		this.drugSeq = drugSeq;
		this.indiPT = indiPT;
	}

	public String getPrimaryID() {
		return primaryID;
	}

	public String getDrugSeq() {
		return drugSeq;
	}

	public String getIndiPT() {
		return indiPT;
	}
	
	
	
}
