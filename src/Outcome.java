
public class Outcome {

	private String primaryID;
	String outcome;
	//String preferredTerm;
	
	public Outcome(String primaryID, String outcome) {
		this.primaryID = primaryID;
		this.outcome = outcome;
		//this.preferredTerm = preferredTerm;
	}

	public String getPrimaryID() {
		return primaryID;
	}

	public String getOutcome() {
		return outcome;
	}

	//public String getPreferredTerm() {
	//	return preferredTerm;
	//}
	
	
	
}
