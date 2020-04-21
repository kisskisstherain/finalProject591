import java.io.IOException;
import java.util.*; 
public class FEARSAnalysis {
   
	/*
     * Two HashMaps: a set of Drug and a set of AdverseEvent, are created to be used
     * while reading in data from drug and AE csv files respectively.
     * A newly added drug is replaced with a previously added drug if they have the
     * same name and dose. Similarly, a newly added AE is replaced by a previously added AE if they have the
     * same PT and primaryID. The purpose is to reduce the amount of drug/AE object being created.
     * so as to reduce the HashMap size. These two HashMaps were used in addDrug and addAdverseEvent methods, respectively.
     * 
     * This is achieved by override hashCode and equals methods in Drug class and AdverseEvent class
     */
    private HashMap<Drug, Drug> drugSet = new HashMap<Drug, Drug>();
    private HashMap<AdverseEvent, AdverseEvent> adverseEventSet = new HashMap<AdverseEvent, AdverseEvent>();
    /*
     * A mapping between ID to a list of drugs objects associated to the ID.
     * This HashMap is used in the addDrug method. A newly added drug is first "screened" using drugSet,
     * and is subsequently added into drugByID
     */
    private HashMap<String, ArrayList<Drug>> drugByID = new HashMap<String, ArrayList<Drug>>();	

    /*
     * A mapping between drug to a list of AEs associated to the drug
     * This HashMap is used in the addAdverseEvent method. A newly added AE is first "screened" using adverseEventSet,
     * and is subsequently added into adverseEventByDrug
     */
    private HashMap<Drug, ArrayList<AdverseEvent>> adverseEventByDrug = new HashMap<Drug, ArrayList<AdverseEvent>>();
    
    /*
     * A mapping between drug with a certain dose to a list of AEs associated to the drug at this dose
     * This HashMap is used in the addAdverseEvent method. A newly added AE is first "screened" using adverseEventSet,
     * and is subsequently added into adverseEventByDrugDose
     */
    private HashMap<Drug, ArrayList<AdverseEvent>> adverseEventByDrugDose = new HashMap<Drug, ArrayList<AdverseEvent>>();
    
    private HashMap<String, ArrayList<Drug>> drugDose = new HashMap<String, ArrayList<Drug>>();

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
		/*While reading in a new Drug d, if drugSet already contain a drug object with same name and dose, d will not be added.
		"asking if it containsKey(d)" is asking to take them as equal if drug.Name and drug.dose
		are the same, ignoring all other instance variables.This is achieved by the overridden hashcode and equal method
		in drug class
		*/
		if (drugSet.containsKey(d)) {
			d = drugSet.get(d);//if contain, d is replaced by a pre-existing d with same name and dose
		} else {
			drugSet.put(d, d);//if not contain, put d into the hashmap
			
			/*
			 * Below a drugDose HashMap was created with drugName being the key, an arraylist of drug objects being the values,
			 * Drug objects were used as values because it contains both dose and dose unit information. This HashMap is used in
			 * getDrugDose method to give users a list of dose options of a given drug name.
			 */
			if( Double.parseDouble(d.getDose()) >= 0) {
				if (drugDose.containsKey(d.getDrugName())) {
					drugDose.get(d.getDrugName()).add(d);
				} else {
					ArrayList<Drug> doseOptionForCurrentDrug = new ArrayList<Drug>();
					doseOptionForCurrentDrug.add(d);
					drugDose.put(d.getDrugName(), doseOptionForCurrentDrug);
				}
			}
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
    /**
     * This method is used to show all dose options for a given drug
     * A HashMap drugDose was created previously in addDrug method. Here, user input a drugName, which was searched
     * from the drugDose HashMap to identify all the dose options
     * @param drugName
     */
	public void getDrugDoses(String drugName) {
		ArrayList<Drug> doseOptions = new ArrayList<Drug>();
		doseOptions = drugDose.get(drugName);
		Collections.sort(doseOptions, new LengthComparator4());
		System.out.println("Dose options for " + drugName + " are: ");
		for(int i = 0; i < doseOptions.size(); i++) {
			System.out.print(doseOptions.get(i).getDose() + " " + doseOptions.get(i).getCumDoseUnit().toLowerCase() + "  ");
		}
		System.out.println();

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
	public void calculateTopNAEforEachDrug(int N, String drugName) {

		Drug d = new Drug(drugName, "0");

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
			//System.out.println("sorted");
			// printArrayLists(resultList);
			System.out.println("Top AE for " + drugName + " is:");
			for (int i = 0; i < N && (i < resultList.size()); ++i) {
				System.out.println(i + 1 + ": " + resultList.get(i).get(0).preferredTerm + ", "
						+ resultList.get(i).size() + " cases");
			}
		}

		//return topAEList;
	}
    
    /**
     * This method calculate the Top N adverseEvents when user input a drugName, a drug dose and a number N
     * @param N
     * @param drugName
     * @param dose
     * @return
     */
    public void calculateTopNAEforEachDrugDose(int N, String drugName, String dose){

	Drug d = new Drug(drugName, dose);
	//Drug d2 = new Drug("4", "SS",drugName, "ATORVASTATIN CALCIUM", "Oral", "-1", "-1", "FILM-COATED TABLET", "-1");

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
		//System.out.println("sorted");
		//printArrayLists(resultList);

		System.out.println("Top AE for " + drugName + " " + dose + " mg:");
		for (int i = 0; i < N && (i < resultList.size()); ++i) {
			System.out.println(i + 1 + ": " + resultList.get(i).get(0).preferredTerm + ", "
					+ resultList.get(i).size() + " cases");
		}
	}
	//return topAEList;
    }
    
    /**
	 * This method is used to make analysis for drug comparison analysis.
	 * 1)This first part of the method gets the eventIDmap for each drug that user will want to compare.
	 * The method uses the adversEventByDrug HashMap generated from above methods which stores
	 * drug and events information from the DataReader class. Each eventIDMap is then put into a HashMap (called mapEachDrug) with a drugName
	 * being the key. This HashMap will then be used by the second part of the method or other methods to display the
	 * top or most common adverse events for multiple drugs.
	 * 
	 * @param drugsToCompare
	 * @param k
	 */
	public HashMap<String, HashMap<String, ArrayList<AdverseEvent>>> printCommonEvents(int k, ArrayList<String> drugsToCompare) {
		
		/*
		 * commonEvents and mapEachDrug are populated with data using this method
		 * Set is used to retain only the adverse events that are common for the
		 * drugs within drugsToCompare. mapEachDrug is a HashMap with drug name as the
		 * key, each key has a list of the drug's adverse events objects.
		 */
		Set<String> commonEvents = new HashSet<String>();
		HashMap<String, HashMap<String, ArrayList<AdverseEvent>>> mapEachDrug = new HashMap<String, HashMap<String, ArrayList<AdverseEvent>>>();	
		
		int drugNumber = 1;
		for(String drugName : drugsToCompare){
			HashMap<String, ArrayList<AdverseEvent>> eventIDmap = new HashMap<String, ArrayList<AdverseEvent>>();
			Drug d = new Drug(drugName, "0");
			
			if (adverseEventByDrug.containsKey(d)) {
				for (AdverseEvent ae : adverseEventByDrug.get(d)) {
					if (eventIDmap.containsKey(ae.getPreferredTerm())) {
						ArrayList<AdverseEvent> AEListForCurrentPT = eventIDmap.get(ae.getPreferredTerm());
						AEListForCurrentPT.add(ae);
					} else {
						ArrayList<AdverseEvent> AEListForCurrentPT = new ArrayList<AdverseEvent>();
						AEListForCurrentPT.add(ae);
						eventIDmap.put(ae.getPreferredTerm(), AEListForCurrentPT);
					}
				}
			}
			mapEachDrug.put(drugName, eventIDmap);
			
			if (drugNumber == 1) {// gets adverse events of first drug
				commonEvents = eventIDmap.keySet();
			} else {// checks which adverse events are common for subsequent drugs using retainAll
				commonEvents.retainAll(eventIDmap.keySet());
			}
			
			drugNumber++;// increments the HashMap
		}

	/*
	 * The purpose of the second part of the method is to print out the most common adverse events
	 * between the drugs of interest. The method makes use of the OrderedEvents
	 * class, which contains two instance variables: the adverse event name and its
	 * number of events An array of OrderedEvents is used with each element being an
	 * adverse event and its total number of events for all drugs. A sort method is
	 * used to get the adverse events with the most reports. The most common k
	 * adverse events are then printed.
	 * 
	 */

		// Adds the adverse events for the different drugs together using the
		// OrderedEvents class to hold the data
		OrderedEvents[] sumOfEvents = new OrderedEvents[commonEvents.size()];

		int eventNumber = 0;
		for (String events : commonEvents) {
			int tempTotal = 0;
			// for loop totals the adverse events cases per drug
			for (String drug : drugsToCompare) {
				tempTotal = tempTotal + mapEachDrug.get(drug).get(events).size();
			}
			OrderedEvents temp = new OrderedEvents(events, tempTotal);
			sumOfEvents[eventNumber] = temp;
			eventNumber++;
		}

		// Sorting from greatest to least events using comparator
		Arrays.sort(sumOfEvents, new LengthComparator2());

		// -----------------------------------------------------------------
		// This segment of code prints out the adverse events in a grid format
		// prints header
		System.out.format("%30s", "Adverse Event");
		for (String drugs : mapEachDrug.keySet()) {
			System.out.format("%10s", drugs);
		}
		System.out.println("");// new line

		// Prints all common adverse reactions if less than k, or maximum of k
		int numToDisplay = 0;
		if (commonEvents.size() <= k) {
			numToDisplay = commonEvents.size();
		} else {
			numToDisplay = k;
		}
		// prints reactions and number cases reported
		for (int i = 0; i < numToDisplay; i++) {
			if (sumOfEvents[i].getEvent().length() > 25) {// curtails the longer words so table looks neat
				String shortened = sumOfEvents[i].getEvent().substring(0, 25);
				System.out.format("%30s", shortened);
			} else {
				System.out.format("%30s", sumOfEvents[i].getEvent());
			}
			for (String drugs : mapEachDrug.keySet()) {
				System.out.format("%10s", mapEachDrug.get(drugs).get(sumOfEvents[i].getEvent()).size());// counts
																												// the
																												// number
																												// of
																												// primaryIDs
																												// per
																												// adverse
																												// event
																												// per
																												// drug
			}
			System.out.println("");// new line
		}
		return mapEachDrug;
	}
	
	
	/**
	 * This method prints out the top adverse events for each drug, independent of
	 * other drugs. A HashMap with the keys as each drug and an ArrayList of its top
	 * adverse events is generated
	 * 
	 * @param mapEachDrug
	 * 
	 */
	public void topEvents(int k, HashMap<String, HashMap<String, ArrayList<AdverseEvent>>> mapEachDrug) {

		// Drug name is used as key, ArrayList of the top adverse events is found for
		// each key
		HashMap<String, ArrayList<String>> topEventsPerDrug = new HashMap<String, ArrayList<String>>();

		for (String drug : mapEachDrug.keySet()) {// loops through each drug

			ArrayList<String> aeNames = new ArrayList<>();

			// Use the Utility class to find the adverse event with the most events
			while (aeNames.size() <= k && aeNames.size() <= mapEachDrug.get(drug).size()) {
				ArrayList<String> keyForMax = Utility.findKeyForMax(mapEachDrug.get(drug));
				aeNames.addAll(keyForMax);
				for (String key : keyForMax) {
					mapEachDrug.get(drug).remove(key);
				}
			}
			// System.out.println(drug + " top AE is " + aeNames.get(0));
			topEventsPerDrug.put(drug, aeNames);// adds ArrayList of top adverse events for each drug
		}

		// -----------------------------------------------------------------
		// This segment of code prints out the adverse events in a grid format
		// prints header
		System.out.println();
		System.out.println();
		System.out.format("%5s", "Ranking");
		for (String drugs : mapEachDrug.keySet()) {
			System.out.format("%30s", drugs);
		}
		System.out.println("");// new line

		// loops through each drug in the topEventsPerDrug HashMap to get the top k
		// adverse events
		for (int i = 0; i < k; i++) {
			System.out.format("%5s", i + 1);

			// for loop gets the i'th event per drug
			for (String drugs : topEventsPerDrug.keySet()) {

				if (i < topEventsPerDrug.get(drugs).size()) {// if the ArrayList of top events per drug still has
																// events, then prints adverse event
					ArrayList<String> tempAE = topEventsPerDrug.get(drugs);
					if (tempAE.get(i).length() > 25) {// curtails the longer words so table looks neat
						String shortened = tempAE.get(i).substring(0, 25);
						System.out.format("%30s", shortened);
					} else {
						System.out.format("%30s", tempAE.get(i));
					}
				} else {// if there are no more events, then print a empty cell
					System.out.format("%30s", "");
				}
			}
			System.out.println("");// new line
		}

	}


    
}
	  
    
    
    
    