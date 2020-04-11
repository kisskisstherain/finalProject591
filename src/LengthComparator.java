import java.util.ArrayList;
import java.util.Comparator;

public class LengthComparator implements Comparator<ArrayList<AdverseEvent>>{
	   public int compare(ArrayList<AdverseEvent> list1, ArrayList<AdverseEvent> list2) {
	       return list2.size() - list1.size(); // sort descending in length
	   }
	}

