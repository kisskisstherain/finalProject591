import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.*; 
public class FEARSAnalysis {
    /*
     * a set of Drug and a set of AdverseEvent
     * so that each drug and each AdverseEvent will only be constructed once
     */
    private HashMap<Drug, Drug> drugSet = new HashMap<Drug, Drug>();
    private HashMap<AdverseEvent, AdverseEvent> adverseEventSet = new HashMap<AdverseEvent, AdverseEvent>();
    /*
     * a mapping between ID to a list of drugs associated to the ID
     */
    private HashMap<String, ArrayList<Drug>> drugByID = new HashMap<String, ArrayList<Drug>>();	
    //private HashMap<String, ArrayList<Drug>> drugByName = new HashMap<String, ArrayList<Drug>>();	
    /*
     * a mapping between drug to a list of AEs associated to the drug
     * might need to override hashCode and equals methods
     */
    private HashMap<Drug, ArrayList<AdverseEvent>> adverseEventByDrug = new HashMap<Drug, ArrayList<AdverseEvent>>();
    private HashMap<Drug, ArrayList<AdverseEvent>> adverseEventByDrugDose = new HashMap<Drug, ArrayList<AdverseEvent>>();

    
    /**
     * This method is used to store drug info while reading in data from "Drug" csv file. Drug and ID information was stored in a
     * HashMap called drugByID. (key = primaryID, value = list of drug objects that are in this case)
     * In order to keep more drug informations(name,dose) and at the same time minimizing the number of drug objects
     * that we are creating so as to speed up the process, a HashMap called drugSet is used to replace drug objects
     * with the same drugName and Dose.
     * @param d
     * @param id
     */
    
	public void addDrug(Drug d, String id) {
		//While reading in a new Drug d, if drugSet already contain a drug object with same name and dose, d will not be added.
		if (drugSet.containsKey(d)) {
			d = drugSet.get(d);
		} else {
			drugSet.put(d, d);
		}
		//Create a HashMap linking the collection of drugs to each primaryID
		if (drugByID.containsKey(id)) {
			drugByID.get(id).add(d);
		} else {
			ArrayList<Drug> drugListForCurrentID = new ArrayList<Drug>();
			drugListForCurrentID.add(d);
			drugByID.put(id, drugListForCurrentID);
		}
	}
	
	/**
	 * This method is used to store adverseEvent info while reading in data from "AdverseEvent" csv file.
	 * Two HashMaps were generated:
	 * 1) adverseEventByDrug is used to link lists of AE objects to each drug, regardless of drug dose
	 * 2) adverseEventByDrugDose is used to link lists of AE objects to each drug with different doses
	 * In order to keep more AE informations(ID,PT) and at the same time minimizing the number of AE objects
     * that we are creating so as to speed up the process, a HashMap called adverseEventSet is used to replace 
     * AE objects with the same primaryID and PT.
	 * @param p
	 */
	public void addAdverseEvent(AdverseEvent p) {
		String key = p.getPrimaryID();
		//While reading in a new AE p, if adverseEventSet already contain an AE object with same PT and ID, p will not be added.
		if (adverseEventSet.containsKey(p)) {
			p = adverseEventSet.get(p);
		} else {
			adverseEventSet.put(p, p);
		}

		/*
		 * This new AE p can be linked to drug through looking for the primaryID from the drugByID HashMap created earlier 
		 * This generates a HashMap linking list of AE to drugs with different doses
		 */
		if (drugByID.containsKey(key)) {
			HashMap<Drug, Drug> filterOutSameDrugInOneCase = new HashMap<Drug, Drug>();
			//Some cases has the same drug same dose listed twice (patient took it twice), the duplicated drug in a same case will be removed here
			for (Drug drug : drugByID.get(key)) {
				if (!filterOutSameDrugInOneCase.containsKey(drug)) {
					filterOutSameDrugInOneCase.put(drug, drug);
				}
			}
			//add p into adverseEventByDrugDose
			for (Drug drug : filterOutSameDrugInOneCase.keySet()) {
				if (adverseEventByDrugDose.containsKey(drug)) {
					ArrayList<AdverseEvent> AElistByDrug = adverseEventByDrugDose.get(drug);
					AElistByDrug.add(p);
				} else {
					ArrayList<AdverseEvent> AElistByDrug = new ArrayList<AdverseEvent>();
					AElistByDrug.add(p);
					adverseEventByDrugDose.put(drug, AElistByDrug);
				}
			}
		} else {
			// exception
			System.out.println("error found");
		}

		// generate hashmap linking list of AE to drugs regardless of doses
		if (drugByID.containsKey(key)) {
			
			HashMap<Drug, Drug> filterOutSameDrugInOneCase = new HashMap<Drug, Drug>();
			/*This HashMap consider drugs with same name but different doses as a same drug object, 
			 * so we replace all drug doses to "0" and remove the duplicated drugs with same name in a same case
			 */
			for (Drug drug : drugByID.get(key)) {
				Drug drugWithoutDose = new Drug(drug.getDrugName(), "0");
				if (!filterOutSameDrugInOneCase.containsKey(drug)) {
					filterOutSameDrugInOneCase.put(drugWithoutDose, drugWithoutDose);
				}
			}
			
			//add p into adverseEventByDrug
			for (Drug drug : filterOutSameDrugInOneCase.keySet()) {
				if (adverseEventByDrug.containsKey(drug)) {
					ArrayList<AdverseEvent> AElistByDrug = adverseEventByDrug.get(drug);
					AElistByDrug.add(p);
				} else {
					ArrayList<AdverseEvent> AElistByDrug = new ArrayList<AdverseEvent>();
					AElistByDrug.add(p);
					adverseEventByDrug.put(drug, AElistByDrug);
				}
			}
		} else {
			// exception
			System.out.println("error found");
		}
	}
    
    
    
  /*
    public void printArrayLists(ArrayList<ArrayList<AdverseEvent>> listOfAEList)
	{
	    for(int i =0; i< listOfAEList.size(); ++i)
	    {
		for(int j=0; j< listOfAEList.get(i).size(); ++j)
		    System.out.println(listOfAEList.get(i).get(j).preferredTerm);
		    System.out.println(" , ");
		    System.out.println();
	    }

	}
    */
    /**
     * This method calculate the Top N adverseEvents when user input a drugName and a number N
     * @param N
     * @param drugName
     * @return
     */
	public ArrayList<String> calculateTopNAEforEachDrug(int N, String drugName) {

		Drug d = new Drug(drugName, "0");

		ArrayList<String> topAEList = new ArrayList<String>();
		HashMap<String, Integer> ha = new HashMap<String, Integer>();
		ArrayList<Integer> numberList = new ArrayList<Integer>();
		HashMap<String, ArrayList<AdverseEvent>> AEListByPT = new HashMap<String, ArrayList<AdverseEvent>>();

		if (adverseEventByDrug.containsKey(d)) {
			for (AdverseEvent ae : adverseEventByDrug.get(d)) {
				if (AEListByPT.containsKey(ae.getPreferredTerm())) {
					ArrayList<AdverseEvent> AEListForCurrentPT = AEListByPT.get(ae.getPreferredTerm());
					AEListForCurrentPT.add(ae);
				} else {
					ArrayList<AdverseEvent> AEListForCurrentPT = new ArrayList<AdverseEvent>();
					AEListForCurrentPT.add(ae);
					AEListByPT.put(ae.getPreferredTerm(), AEListForCurrentPT);
				}
			}

			ArrayList<ArrayList<AdverseEvent>> resultList = new ArrayList<ArrayList<AdverseEvent>>();
			for (String PT : AEListByPT.keySet()) {
				resultList.add(AEListByPT.get(PT));
			}

			// printArrayLists(resultList);
			Collections.sort(resultList, new LengthComparator());
			System.out.println("sorted");
			// printArrayLists(resultList);
			System.out.println("Top AE for " + drugName + " is:");
			for (int i = 0; i < N && (i < resultList.size()); ++i) {
				System.out.println(i + 1 + ": " + resultList.get(i).get(0).preferredTerm + ", "
						+ resultList.get(i).size() + " cases");
			}
		}

		return topAEList;
	}
    
    /**
     * This method calculate the Top N adverseEvents when user input a drugName, a drug dose and a number N
     * @param N
     * @param drugName
     * @param dose
     * @return
     */
    public ArrayList<String> calculateTopNAEforEachDrugDose(int N, String drugName, String dose){

	Drug d = new Drug(drugName, dose);
	//Drug d2 = new Drug("4", "SS",drugName, "ATORVASTATIN CALCIUM", "Oral", "-1", "-1", "FILM-COATED TABLET", "-1");

	ArrayList<String> topAEList = new ArrayList<String>();
	HashMap<String, Integer> ha = new HashMap<String, Integer>();
	ArrayList<Integer> numberList = new ArrayList<Integer>();
	HashMap<String, ArrayList<AdverseEvent>> AEListByPT = new HashMap<String, ArrayList<AdverseEvent>>();
	

	if(adverseEventByDrugDose.containsKey(d)) {
		for (AdverseEvent ae : adverseEventByDrugDose.get(d)) {
			if (AEListByPT.containsKey(ae.getPreferredTerm())) {
				ArrayList<AdverseEvent> AEListForCurrentPT = AEListByPT.get(ae.getPreferredTerm());
				AEListForCurrentPT.add(ae);
			} else {
				ArrayList<AdverseEvent> AEListForCurrentPT = new ArrayList<AdverseEvent>();
				AEListForCurrentPT.add(ae);
				AEListByPT.put(ae.getPreferredTerm(), AEListForCurrentPT);
			}
		}

		ArrayList<ArrayList<AdverseEvent>> resultList = new ArrayList<ArrayList<AdverseEvent>>();
		for(String PT :AEListByPT.keySet()) {
			resultList.add(AEListByPT.get(PT));
		}

		//printArrayLists(resultList);
		Collections.sort(resultList, new LengthComparator());
		System.out.println("sorted");
		//printArrayLists(resultList);

		System.out.println("Top AE for " + drugName + " " + dose + " is:");
		for (int i = 0; i < N && (i < resultList.size()); ++i) {
			System.out.println(i + 1 + ": " + resultList.get(i).get(0).preferredTerm + ", "
					+ resultList.get(i).size() + " cases");
		}
	}
	return topAEList;
    }
}
	  
    
    
    
    