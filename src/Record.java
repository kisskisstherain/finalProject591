import java.util.ArrayList;

public class Record {
	private Patients patient;
	private Drug drug;
	private AdverseEvent adverseEvent;
	private Outcome outcome;
	
	public Record(Patients patient, Drug drug, AdverseEvent adverseEvent, Outcome outcome) {
		this.patient = patient;
		this.drug = drug;
		this.adverseEvent = adverseEvent;
		this.outcome = outcome;
	}

	public Patients getPatient() {
		return patient;
	}

	public Drug getDrug() {
		return drug;
	}

	public AdverseEvent getAdverseEvent() {
		return adverseEvent;
	}

	public Outcome getOutcome() {
		return outcome;
	}
	

}
