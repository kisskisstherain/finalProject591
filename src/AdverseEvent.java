import java.util.ArrayList;

public class AdverseEvent {
	String primaryID;
	String preferredTerm;
	String reaction;
//	String indication;

	public AdverseEvent(String primaryID, String preferredTerm, String reaction) {
		this.primaryID = primaryID;
		this.preferredTerm = preferredTerm;
		this.reaction = reaction;
	}
	/*
	 * Depends only on primaryID and preferredTerm
	 * Return the same hashCode as long as two AEs has same primaryID and preferredTerm
	 */
	@Override
	public int hashCode()
	{
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((this.primaryID == null) ? 0 : this.primaryID.hashCode());
	    result = prime * result + ((this.preferredTerm == null) ? 0 : this.preferredTerm.hashCode());
	    return result;
	}
	/*
	 * Compare only primaryID and preferredTerm
	 * Return the same hashCode as long as two AEs has same primaryID and preferredTerm
	 */
	@Override
	public boolean equals(Object obj)
	{
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    AdverseEvent other = (AdverseEvent) obj;
	    return ((this.primaryID.contentEquals(other.primaryID))&&(this.getPreferredTerm().equals(other.getPreferredTerm())));
	}

	public String getPrimaryID() {
		return primaryID;
	}

	public String getPreferredTerm() {
		return preferredTerm;
	}

	public String getReaction() {
		return reaction;
	}
	
	
	
	
	
	
}
