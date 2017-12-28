package csc207b07.example.triage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter {
	
	private BufferedWriter bfw;
	
	/**
	 * Adds the specified record to the end of the file identified by healthCardNumber.
	 * @param healthCardNumber the identifier of the file to be written to.
	 * @param record the information to be added to the file.
	 */
	public void writeRecord(String healthCardNumber, Record record){
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(DefaultValues.file_path_folder +'/' + healthCardNumber, true));
			bfw.newLine();
			bfw.write(record.toString());
			bfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new visit in the file identified by healthCardNumber.
	 * @param healthCardNumber the identifier of the file to be written to.
	 * @param visitNumber the number of the visit, where the first time a patient visits a hospital is visit 1 and so on.
	 */
	public void writeVisit(String healthCardNumber, int visitNumber){
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(DefaultValues.file_path_folder + "/" + healthCardNumber, true));
			bfw.newLine();
			bfw.write("Visit " + visitNumber);
			bfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the date from the DefaultValues class to the file identified by healthCardNumber.
	 * @param healthCardNumber the identifier of the file to be written to.
	 */
	public void writeDate(String healthCardNumber){
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(DefaultValues.file_path_folder + "/" + healthCardNumber, true));
			bfw.newLine();
			bfw.write(DefaultValues.date_sep);
			bfw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a hyphen to the end of the file identified by heatlthCardNumber to signify a patient has checked-out.
	 * @param healthCardNumber the identifier of the file to be written to.
	 */
	public void checkout(String healthCardNumber){
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(DefaultValues.file_path_folder + "/" + healthCardNumber, true));
			bfw.newLine();
			bfw.write("-");
			bfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
