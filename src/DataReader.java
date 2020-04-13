import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class DataReader {
	ArrayList<Patients> patients = new ArrayList<Patients>();
	ArrayList<Outcome> outcomes = new ArrayList<Outcome>();
	ArrayList<Indication> indications = new ArrayList<Indication>();
	// ArrayList<Record> records;

	//added by WC
	FEARSAnalysis myAnalysis = new FEARSAnalysis();
	

	public DataReader(FEARSAnalysis analysis)
	{
	    myAnalysis = analysis;
	}
	
	public ArrayList<Patients> getPatients() {
		return patients;
	}


	public ArrayList<Outcome> getOutcomes() {
		return outcomes;
	}

	public ArrayList<Indication> getIndications() {
		return indications;
	}

	/**
	 * This method process a String by assigning -1 to an empty or null String
	 * 
	 * @param s
	 * @return
	 */

	public String processEmptyString(String s) {
		if (s.equals("") || s.equals(null)) {
			return "-1";
		} else {
			return s;
		}
	}

	/**
	 * This method reads in data from DEMOyyQy.txt, DRUGyyQy.txt, OUTCyyQY.txt,
	 * REACyyQy.txt and INDIyyQy.txt and fills ArrayLists.
	 * 
	 * @throws IOException
	 */
	public void reader() throws IOException {
		Path patientFile = Paths.get("DEMO19Q4.txt");
		ArrayList<String> demoData = new ArrayList<String>(Files.readAllLines(patientFile));
		demoData.remove(0);

		Path outcomeFile = Paths.get("OUTC19Q4.txt");
		ArrayList<String> outcomeData = new ArrayList<String>(Files.readAllLines(outcomeFile));
		outcomeData.remove(0);

		Path drugFile = Paths.get("DRUG19Q4.txt");
		ArrayList<String> drugData = new ArrayList<String>(Files.readAllLines(drugFile));
		drugData.remove(0);

		Path eventsFile = Paths.get("REAC19Q4.txt");
		ArrayList<String> reacData = new ArrayList<String>(Files.readAllLines(eventsFile));
		reacData.remove(0);

		Path indicationFile = Paths.get("INDI19Q4.txt");
		ArrayList<String> indicationData = new ArrayList<String>(Files.readAllLines(indicationFile));
		indicationData.remove(0);
		
		
		
		for (String line : drugData) {
			String[] lineComp = line.split("\\$", -1);
			String id = lineComp[0];
			//System.out.println(id);
			String seq = lineComp[2];
			String role = processEmptyString(lineComp[3]);				
			String name = processEmptyString(lineComp[4]);
			String ai = processEmptyString(lineComp[5]);
			String route = processEmptyString(lineComp[7]);
			String dose = processEmptyString(lineComp[16]);
			String unit = processEmptyString(lineComp[17]);
			String form = processEmptyString(lineComp[18]);
			String freq = processEmptyString(lineComp[19]);
		//added by WC
			
			Drug d = new Drug(seq, role, name, ai, route, dose, unit, form, freq);
			myAnalysis.addDrug(d, id);
			//System.out.println(d.getDrugName()+ id);
			//myAnalysis.addDrugName(d);
			//System.out.println("addDrug " + d.getDrugName() + " " + d.hashCode());
			
		}
		System.out.println("drugs added");
			


		for (String line : reacData) {
				String[] lineComp = line.split("\\$", -1);
				String id = lineComp[0];
				//System.out.println(id);
				String pt = processEmptyString(lineComp[2]);
				String event = processEmptyString(lineComp[3]);
				AdverseEvent ae = new AdverseEvent(id, pt, event);
				
			//added by WC	
				myAnalysis.addAdverseEvent(ae);
				
				//myAnalysis.addAdverseEventToDrugName(ae);
				//System.out.println("addAe " + ae.primaryID);
				
			}	
		System.out.println("addAdverseEvent added");	
		for (String line : demoData) {
			if (!(line.equals(null) || line.equals(""))) {
				String[] lineComp = line.split("\\$");
				String id = processEmptyString(lineComp[0]);
				String gender = processEmptyString(lineComp[16]);
				int age = Integer.parseInt(processEmptyString(lineComp[13]));
				String ageGroup = processEmptyString(lineComp[15]);
				double weight = Double.parseDouble(processEmptyString(lineComp[18]));
				String occupation = processEmptyString(lineComp[22]);

				Patients p = new Patients(id, gender, age, ageGroup, weight, occupation);
				this.patients.add(p);
				//System.out.println("addPatient " + p.getPrimaryID());
			}
		}

		for (String line : outcomeData) {
			String[] lineComp = line.split("\\$");
			String id = lineComp[0];
			String outcome = lineComp[2];

			Outcome o = new Outcome(id, outcome);
			this.outcomes.add(o);
		}


		for (String line : indicationData) {
			String[] lineComp = line.split("\\$", -1);
			String id = lineComp[0];
			String seq = lineComp[2];
			String pt = lineComp[3];
			Indication indi = new Indication(id, seq, pt);
			this.indications.add(indi);
		}

	}

	

}
