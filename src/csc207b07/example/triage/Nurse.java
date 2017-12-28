package csc207b07.example.triage;

import java.util.ArrayList;

public class Nurse {
	
	public ArrayList<String> allPatients = new ArrayList<String>();
	DataReader reader = new DataReader();
	DataWriter writer = new DataWriter();
	//writer.writeVisit();
	//writer.writeDate();
	
	public loadingstatus = 0
			
	
	/*THIS IS HOW VERIFY FILE WORKS:
	 * If the file you sent as parameter exists: returns 0.
	 * If the file you sent as parameter doesn't exist it returns 1
	 * If there is an error while verifying the file (usually will never happen) it returns -1
	 * 
	 * If create is true, and the file doesn't exist then it creates an empty file.
	 * However, whether you create a file or not it will always return -1 if a file 
	 * was originally not found. 
	 * 
	 * So for load patients you should call verify file on the patients_file_path, I think you
	 * should call it with create=true... to create consistency after the intialization.
	 * */
		public void loadPatients() throws Exception {
		if (reader.verifyFile(DefaultValues.patients_file_path, false) == 1) {
			if (reader.getHealthCardsofPatients() != null) {
				for (String heathcardNumber: reader.getHealthCardsofPatients()){
					this.allPatients.add(heathcardNumber);
				}
		} else if (reader.verifyFile(DefaultValues.patients_file_path, false) == 0){
				//Do nothing since there is no patient file
		} else if (reader.verifyFile(DefaultValues.patients_file_path, true) == -1){
				//Do nothing since there are no patients in a new patient file
			}
		} 
	}
	
	/*If the patient is checked in you are checking him in again, I think you forgot to add the !
	 * */
	public void addRecord(String healthcardNumber, Record record) throws Exception{
		if (this.allPatients.contains(healthcardNumber)){
			if (reader.isCheckedIn(healthcardNumber)) {
				checkInPatient(healthcardNumber, record);
				writer.writeDate(healthcardNumber);
				writer.writeRecord(healthcardNumber, record);
				if (record.getHr() == 0){
					checkOutPatient(healthcardNumber);
				}else if (record.getBp() == 0){
					checkOutPatient(healthcardNumber);
				}else if (record.getTemp() == 0){
					checkOutPatient(healthcardNumber);
				}
			}
		}
	}

	public String lookUpPatient(String healthcardNumber) throws Exception {
		String result = "Sorry, there is no patient with that healthcard Number.";
		if (this.allPatients.contains(healthcardNumber)) {
			if (reader.lookUpPatient(healthcardNumber) != null){
				result = "Name: " + reader.lookUpPatient(healthcardNumber)[0] + 
						"\nBirthdate: " + reader.lookUpPatient(healthcardNumber)[1];
			} 
		}
		return result;
	}

	/*Consider the new format of the getRecordsByDate*/
	public String lookUpPatientRecord(String healthcardNumber, String date) throws Exception {
		if (this.allPatients.contains(healthcardNumber)) {
			if (reader.getRecordsByDate(healthcardNumber, date).isEmpty()){
				return "This patient has no records on that date.";
			} else{
				String result = "Records on " + date + ":\n";
				
				for (Record records: reader.getRecordsByDate(healthcardNumber, date)){
					String[] vitals = records.toString().split(",");
					result += "\nTime Recorded: " + vitals[0] +"\nHeart Rate: " + vitals[1] + 
							"\nBlood Pressure: " + vitals[2] + "\nTemperature: " + vitals[3] + 
							" degrees Celsius\nDescription: " + vitals[4] + "\n";
				}
				return result;
			}
		} else {
			return "Sorry, there is no patient with that healthcard Number.";
		}
	}
	
	public void checkInPatient(String healthcardNumber, Record record) throws Exception{
		if (this.allPatients.contains(healthcardNumber)) {
			int currentVisitNumber = reader.find_last_visit_num(healthcardNumber) + 1;
			writer.writeVisit(healthcardNumber, currentVisitNumber);
		}
	}
	
	public void checkOutPatient(String healthcardNumber) throws Exception{
		if (this.allPatients.contains(healthcardNumber)) {
			if (reader.isCheckedIn(healthcardNumber)){
				writer.checkout(healthcardNumber);
			}
		}
	}
	
}
