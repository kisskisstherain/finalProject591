
public class Patients {
	private String primaryID;
	private String gender;
	private int age;
	private String ageGroup;
	private double weight;
	private String occupation;
	
	public Patients(String primaryID, String gender, int age, String ageGroup, double weight,
			String occupation) {
		this.primaryID = primaryID;
		this.gender = gender;
		this.age = age;
		this.ageGroup = ageGroup;
		this.weight = weight;
		this.occupation = occupation;
	}

	public String getPrimaryID() {
		return primaryID;
	}


	public String getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public double getWeight() {
		return weight;
	}

	public String getOccupation() {
		return occupation;
	}
	
	
	
}
