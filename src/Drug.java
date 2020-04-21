
public class Drug {

	private String drugSeq;
	private String role;
	private String drugName;
	private String activeIngredient;
	private String route;
	private String Dose;
	private String cumDoseUnit;
	private String form;
	private String freq;
	

	public Drug(String drugName, String Dose)
	{
	    this.drugName = drugName;
	    this.Dose = Dose;
	}
	
	public Drug(String drugSeq, String role, String drugName, String activeIngredient, String route,
			String Dose, String cumDoseUnit, String form, String freq) {
		super();

		this.drugSeq = drugSeq;
		this.role = role;
		this.drugName = drugName;
		this.activeIngredient = activeIngredient;
		this.route = route;
		this.Dose = Dose;
		this.cumDoseUnit = cumDoseUnit;
		this.form = form;
		this.freq = freq;
	}

	/*
	 * Depends only on drugName and Dose
	 * Return the same hashCode as long as two drug object has same drugName and Dose
	 */
	@Override
	public int hashCode()
	{
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((this.drugName == null) ? 0 : this.drugName.hashCode());
	    result = prime * result + ((this.Dose == null) ? 0 : this.Dose.hashCode());
	    return result;
	}
	/*
	 * Compare only drugName and Dose
	 * Take two drug as equal if drug.drugName and drug.dose
	 * are the same, ignoring all other instance variables.
	 * Other instance variables can also be added if it's requested in our program design.
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
	    Drug other = (Drug) obj;
	    return (this.drugName.equals(other.drugName) && this.Dose.equals(other.Dose));//(this.drugName == other.drugName);
	}
	
	

	public void setDose(String Dose) {
		this.Dose = Dose;
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

	public String getDose() {
		return Dose;
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
	
	