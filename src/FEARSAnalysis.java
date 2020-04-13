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
    //private HashMap<String, ArrayList<AdverseEvent>> adverseEventByDrugName = new HashMap<String, ArrayList<AdverseEvent>>();

    public void addDrug(Drug d, String id)
    {
	if(drugSet.containsKey(d))
	{
	    //System.out.println("replace");
	    d = drugSet.get(d);
	}
	else
	{
	    //System.out.println("new");
	    drugSet.put(d, d);
	}
	
	if(drugByID.containsKey(id)) {
	    drugByID.get(id).add(d);
	    //exception
	  //System.out.println("error found1"+id + " " + d.getDrugName());
	}else {
	    ArrayList<Drug> drugListForCurrentID = new ArrayList<Drug>();
	    drugListForCurrentID.add(d);
	    drugByID.put(id, drugListForCurrentID);
	  //System.out.println(id + d.getDrugName());
	}
	
    }
    /*
    public void addDrugName(Drug d)
    {
	if(drugByName.containsKey(d.getDrugName())) {
	    drugByName.get(d.getDrugName()).add(d);
	    //System.out.println(d.getDrugName());
	}else {
	    ArrayList<Drug> drugListForCurrentDrugName = new ArrayList<Drug>();
	    drugListForCurrentDrugName.add(d);
	    drugByName.put(d.getDrugName(), drugListForCurrentDrugName);
	    //System.out.println(d.getDrugName());
	}
    }
    */
    
    
    public void addAdverseEvent(AdverseEvent p) 
    {
	String key = p.getPrimaryID();
	
	if(adverseEventSet.containsKey(p))
	{
	    p = adverseEventSet.get(p);
	}
	else
	{
	    adverseEventSet.put(p, p);
	}
	
	if(drugByID.containsKey(key)) 
	{
	    HashMap<Drug, Drug> filterOutSameDrugInOneCase = new HashMap<Drug, Drug>();
	    for(Drug drug : drugByID.get(key)) 
	    {
		if(!filterOutSameDrugInOneCase.containsKey(drug)) 
		{
		    filterOutSameDrugInOneCase.put(drug, drug);
		}
	    }
	    for(Drug drug : filterOutSameDrugInOneCase.keySet()) 
	    {
		if(adverseEventByDrug.containsKey(drug))
		{
			ArrayList<AdverseEvent> AElistByDrug = adverseEventByDrug.get(drug);//or get key?
			//insert adverseEvent
			AElistByDrug.add(p);
		}
		else 
		{
			ArrayList<AdverseEvent> AElistByDrug = new ArrayList<AdverseEvent>();
			AElistByDrug.add(p);
			adverseEventByDrug.put(drug, AElistByDrug);
			//System.out.println(drug.getDrugName() + " " +p.getPreferredTerm());
		}
	    }
	       
	}
	else 
	{
		  //exception
		  System.out.println("error found");
        }
		
    }
    /*
    public void addAdverseEventToDrugName(AdverseEvent p) {
	String key = p.getPrimaryID();
	if(drugByID.containsKey(key)) {
	    for(Drug drug : drugByID.get(key)) {
		if(adverseEventByDrugName.containsKey(drug.getDrugName())){
			ArrayList<AdverseEvent> AElistByDrugName = adverseEventByDrugName.get(drug.getDrugName());//or get key?
			//insert adverseEvent
			AElistByDrugName.add(p);
		 }else {
			ArrayList<AdverseEvent> AElistByDrugName = new ArrayList<AdverseEvent>();
			AElistByDrugName.add(p);
			adverseEventByDrugName.put(drug.getDrugName(), AElistByDrugName);
			//System.out.println(drug.getDrugName() + " " +p.getPreferredTerm());
		    }
		}
	}else {
		  //exception
		  System.out.println("error found");
		}
		
	    }
	    */
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
    
    public ArrayList<String> calculateTopNAEforEachDrug(int N, String drugName, String dose){

	Drug d = new Drug(drugName, dose);
	//Drug d2 = new Drug("4", "SS",drugName, "ATORVASTATIN CALCIUM", "Oral", "-1", "-1", "FILM-COATED TABLET", "-1");
	ArrayList<String> topAEList = new ArrayList<String>();
	HashMap<String, Integer> ha = new HashMap<String, Integer>();
	ArrayList<Integer> numberList = new ArrayList<Integer>();
	HashMap<String, ArrayList<AdverseEvent>> AEListByPT = new HashMap<String, ArrayList<AdverseEvent>>();
	//System.out.println(adverseEventByDrugName.get(drugName));
	if(adverseEventByDrug.containsKey(d)) {
	    for(AdverseEvent ae: adverseEventByDrug.get(d)) {
		if(AEListByPT.containsKey(ae.getPreferredTerm())) {
		    ArrayList<AdverseEvent> AEListForCurrentPT = AEListByPT.get(ae.getPreferredTerm());
		    AEListForCurrentPT.add(ae);
		}else {
		    ArrayList<AdverseEvent> AEListForCurrentPT = new ArrayList<AdverseEvent>();
		    AEListForCurrentPT.add(ae);
		    AEListByPT.put(ae.getPreferredTerm(), AEListForCurrentPT);
		}
	    }
	    
	    ArrayList<ArrayList<AdverseEvent>> resultList = new ArrayList<ArrayList<AdverseEvent>>();
	    for(String PT :AEListByPT.keySet()) {
	        resultList.add(AEListByPT.get(PT));
	    }
	    
	    printArrayLists(resultList);
	    Collections.sort(resultList, new LengthComparator());
	    System.out.println("sorted");
	    printArrayLists(resultList);
	    
	    for(int i=0; i<N &&(i<resultList.size()); ++i)
	    {
		System.out.println(i+1 + ": " + resultList.get(i).get(0).preferredTerm + ", " + resultList.get(i).size() + " cases");
	    }
	    /*
	    for(String PT :AEListByPT.keySet()) {
		int numberOfCasesforCurrentPT = AEListByPT.get(PT).size();
		ha.put(PT, numberOfCasesforCurrentPT);
		numberList.add(numberOfCasesforCurrentPT);
	    }
	    //System.out.println(ha.keySet());
	    //Collections.sort(numberList, new LengthComparator());

	    for (int i = 0; i < N; i++) {
		int TopNumber = numberList.get(numberList.size()-1-i);
		for(String PT: ha.keySet()) {
		    int count = 0;
		    while(count <= N) {
			if(ha.get(PT) == TopNumber) {
			    if(!topAEList.contains(PT)) {
				System.out.println(PT + "," + TopNumber + "," + i );
				topAEList.add(PT);
			    }
			}
			count++;
		    }
		}
	    }*/
	}

	return topAEList;
    }
}
	  
    
    
    
    