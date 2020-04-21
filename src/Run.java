import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Run {
    public static void main(String[] args) throws IOException {
	FEARSAnalysis analysis = new FEARSAnalysis();

	DataReader dr = new DataReader(analysis);
	dr.reader();
	
	//Q1: print top adverse events for "LIPITOR"
	System.out.println();
	System.out.println("*Show me the top10 adverse events of LIPITOR (regardless of dosage)");
	analysis.calculateTopNAEforEachDrug(10, "LIPITOR");
	System.out.println();
	System.out.println("*Show me all dose options for LIPITOR");
	analysis.getDrugDoses("LIPITOR");
	System.out.println();
	System.out.println("*Show me the top10 adverse events of different doses of LIPITOR");
	analysis.calculateTopNAEforEachDrugDose(10, "LIPITOR", "10");
	System.out.println();
	analysis.calculateTopNAEforEachDrugDose(10, "LIPITOR", "20");
	System.out.println();
	analysis.calculateTopNAEforEachDrugDose(10, "LIPITOR", "40");
	System.out.println();
	analysis.calculateTopNAEforEachDrugDose(10, "LIPITOR", "80");
	System.out.println();

		
	//Question2 test case for 2 drugs
	System.out.println();
	System.out.println();
	System.out.println("*Show me the common adverse events and a comparison table of the Top 8 adverse events of TASIGNA and GLEEVEC");
	System.out.println();
	ArrayList<String> listDrugs2 = new ArrayList<String>();
	listDrugs2.add("TASIGNA");
	listDrugs2.add("GLEEVEC");
	//analysis.getMaps(listDrugs2);
	//analysis.printCommonEvents(10, listDrugs2);
	
	analysis.topEvents(8, analysis.printCommonEvents(10, listDrugs2));
	//System.out.println();
	//System.out.println("*Show me the top10 adverse events of TASIGNA itself");
	//analysis.calculateTopNAEforEachDrug(10, "TASIGNA");
	//System.out.println();
	//System.out.println("*Show me the top10 adverse events of GLEEVEC itself");
	//analysis.calculateTopNAEforEachDrug(10, "GLEEVEC");
	
	
	//Question3 test case for 2 drugs
	System.out.println();	
	System.out.println();
	System.out.println("*Show me the common adverse events and a comparison table of the Top 8 adverse events of HUMIRA and ENBREL");
	System.out.println();
	ArrayList<String> listDrugs1 = new ArrayList<String>();
	listDrugs1.add("HUMIRA");
	listDrugs1.add("ENBREL");
	//analysis.getMaps(listDrugs1);
	//analysis.printCommonEvents(10, listDrugs1);
	analysis.topEvents(8, analysis.printCommonEvents(10, listDrugs1));
		//analysis.topEvents(8);
		//System.out.println();
		//System.out.println("*Show me the top10 adverse events of HUMIRA itself");
		//analysis.calculateTopNAEforEachDrug(10, "HUMIRA");
		//System.out.println();
		//System.out.println("*Show me the top10 adverse events of ENBREL itself");
		//analysis.calculateTopNAEforEachDrug(10, "ENBREL");
		
		
		//Question4 test case for 3 drugs
	System.out.println();	
	System.out.println();
	System.out.println("*Show me the common adverse events and a comparison table of the Top 8 adverse events of OPDIVO and KEYTRUDA and IMFINZI");
	System.out.println();
	ArrayList<String> listDrugs3 = new ArrayList<String>();
	listDrugs3.add("OPDIVO");
	listDrugs3.add("KEYTRUDA");
	listDrugs3.add("IMFINZI");
	//analysis.getMaps(listDrugs1);
	analysis.topEvents(8, analysis.printCommonEvents(10, listDrugs3));
	//analysis.topEvents(8);
		//System.out.println();
		//System.out.println("*Show me the top10 adverse events of OPDIVO itself");
		//analysis.calculateTopNAEforEachDrug(10, "OPDIVO");
		//System.out.println();
		//System.out.println("*Show me the top10 adverse events of KEYTRUDA itself");
		//analysis.calculateTopNAEforEachDrug(10, "KEYTRUDA");
		//System.out.println();
		//System.out.println("*Show me the top10 adverse events of IMFINZI itself");
		//analysis.calculateTopNAEforEachDrug(10, "IMFINZI");
}
}
