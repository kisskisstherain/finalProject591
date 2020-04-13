
public class Drug {

	private String drugSeq;
	private String role;
	private String drugName;
	private String activeIngredient;
	private String route;
	private String cumDose;
	private String cumDoseUnit;
	private String form;
	private String freq;
	

	public Drug(String drugName, String cumDose)
	{
	    this.drugName = drugName;
	    this.cumDose = cumDose;
	}
	
	public Drug(String drugSeq, String role, String drugName, String activeIngredient, String route,
			String cumDose, String cumDoseUnit, String form, String freq) {
		super();

		this.drugSeq = drugSeq;
		this.role = role;
		this.drugName = drugName;
		this.activeIngredient = activeIngredient;
		this.route = route;
		this.cumDose = cumDose;
		this.cumDoseUnit = cumDoseUnit;
		this.form = form;
		this.freq = freq;
	}

	public int hashCode()
	{
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((this.drugName == null) ? 0 : this.drugName.hashCode());
	    result = prime * result + this.cumDose.hashCode();
	    return result;
	}
	
	public boolean equals(Object obj)
	{
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    Drug other = (Drug) obj;
	    return (this.drugName.equals(other.drugName) && this.cumDose.equals(other.cumDose));//(this.drugName == other.drugName);
	}
	
	

	public void setCumDose(String cumDose) {
		this.cumDose = cumDose;
	}

	public String getDrugSeq() {
		return drugSeq;
	}

	public String getRole() {
		return role;
	}

	public String getDrugName() {
		return drugName;
	}

	public String getActiveIngredient() {
		return activeIngredient;
	}

	public String getRoute() {
		return route;
	}

	public String getCumDose() {
		return cumDose;
	}

	public String getCumDoseUnit() {
		return cumDoseUnit;
	}

	public String getForm() {
		return form;
	}

	public String getFreq() {
		return freq;
	}
	
	
}
	
	