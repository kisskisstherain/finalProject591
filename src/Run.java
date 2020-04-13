import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Run {
    public static void main(String[] args) throws IOException {
	FEARSAnalysis analysis = new FEARSAnalysis();

	DataReader dr = new DataReader(analysis);
	dr.reader();
	
    
	/**
	System.out.println("This is the list of drug names");
	System.out.println(map1.keySet());
	
	
	System.out.println("This is the list of primaryID for DHEA:");
	System.out.println(map1.get("DHEA"));

	HashMap<String, ArrayList<String>> map2 = analysis.eventIDMap("DHEA", dr.drugs, dr.events);
	System.out.println("This is the list of adverse events associated with DHEA:");
	System.out.println(map2.keySet());
	System.out.println("This is sets of primaryID list for all the adverse events: ");
	System.out.println(map2.values());
	
	*/
	//print top 5 adverse events for "DHEA"
	System.out.println("dose result");
	analysis.calculateTopNAEforEachDrugDose(10, "LIPITOR", "10");
	analysis.calculateTopNAEforEachDrugDose(10, "LIPITOR", "20");
	analysis.calculateTopNAEforEachDrugDose(10, "LIPITOR", "40");
	analysis.calculateTopNAEforEachDrugDose(10, "LIPITOR", "80");
	System.out.println("drug alone result");
	analysis.calculateTopNAEforEachDrug(10, "LIPITOR");
}
}
