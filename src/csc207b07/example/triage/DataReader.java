package csc207b07.example.triage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class DataReader {
	/**
	 * Holds the path of the file with all patients and their personal
	 * information. The file itself is in the following format: Each patient's
	 * information is stored on a separate line.
	 */
	private String patients_file_path;

	/**
	 * Holds the path of the folder where all the files of individual patients
	 * are stored. The name of each file within this folder is the healthcard
	 * number of a patient. The format of a patient's file is the following:
	 * <p>
	 * VISITNUMBER
	 * <p>
	 * d yyyy-mm-dd
	 * <p>
	 * TimeOfRecord1:HR,BP,TEMP;DESCRIPTION
	 * <p>
	 * TimeOfRecord2:HR,BP,TEMP;DESCRIPTION
	 * <p>
	 * -
	 * <p>
	 * VISITNUMBER
	 * <p>
	 * d yyyy-mm-dd
	 * <p>
	 * TimeOfRecord1:HR,BP,TEMP;DESCRIPTION
	 * <p>
	 * -
	 * <p>
	 */
	private String file_path_folder;

	// We need two buffered readers because sometimes two methods will interact
	// with files at the same time,
	// so we need to avoid locking the reader being used by one of the
	// methods.
	/**
	 * 
	 */
	private BufferedReader bf1;
	private BufferedReader bf2;

	/**
	 * Creates a new DataReader object which stores the patients file path and
	 * the file path of the folder with all patients. There is only one of these
	 * objects created per session of the application.
	 */
	public DataReader() {

		this.patients_file_path = DefaultValues.patients_file_path;
		this.file_path_folder = DefaultValues.file_path_folder;

	}

	/**
	 * Checks that the given file exists, and if not, will create one if
	 * preferred.
	 * 
	 * @param file
	 *            name of the file we are looking for
	 * @param create
	 *            states whether a new file needs to be created
	 * @return -1 if a file wasn't found, 1 if a file exists, and 0 for error
	 */
	protected int verifyFile(String file, boolean create) {
		// If the file exists, 1, -1 if it wasn't found, 0 if error.
		// It will use the value of @create to create an empty file if required
		// (for stability).
		File patients_file = new File(file);

		if (!patients_file.exists()) {

			try {
				// Create an empty file if required.
				if (create)
					patients_file.createNewFile();
				return -1;

			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}

		}
		return 1;
	}

	/**
	 * Removes a file named name if it exists.
	 * 
	 * @param name
	 *            the name of the file we wish to remove.
	 */
	private void removeFile(String name) {

		File file = new File(DefaultValues.file_path_folder + name);
		if (file.exists())
			file.delete();

	}

	/**
	 * Throws the specified exception if there's an error in the format of the
	 * specified file.
	 * 
	 * @param e
	 *            the error to be thrown
	 * @param healthcard_num
	 *            the name of the file we're assessing
	 * @throws Exception
	 */
	private void throwFileFormatError(Exception e, String healthcard_num)
			throws Exception {

		// If the file exists, then there is an error in the formatting.
		if (verifyFile(healthcard_num, false) == 1) {

			if (!healthcard_num.equals(patients_file_path)) {

				throw new FileFormatError(
						"There is an error in the records file for the patient "
								+ healthcard_num + ".");

			} else {

				throw new FileFormatError(
						"There is an error in the format for the patients data file.");
			}

		} else {
			// Unknown error.
			throw e;
		}

	}

	/**
	 * Determines the data stored in line.
	 * 
	 * @param line
	 *            the string to be assessed
	 * @return A string array where the first element is the type of the line
	 *         and the second element is the line itself or a part of line
	 *         associated with the type found.
	 */
	private String[] getDataType(String line) {

		String[] datatype;

		// Determine the type of data stored in the line.
		// Return only what's relevant.

		if (line.contains(DefaultValues.param_sep)) {
			String type;
			String[] data = line.split(DefaultValues.param_sep, 5);
			if (data.length == 3) {
				type = "patient";
			} else {
				type = "record";
			}
			datatype = new String[] { type, line };

		} else if (line.startsWith(DefaultValues.c_out_sep)) {
			datatype = new String[] { "checked_out", null };
		} else if (line.startsWith(DefaultValues.date_sep)) {
			String date = line.split(" ")[1];
			datatype = new String[] { "date", date };
		} else if (line.equals(DefaultValues.line_sep)) {
			datatype = new String[] { "line_break", null };
		} else {
			datatype = new String[] { "visit_num", line };
		}

		return datatype;

	}

	/**
	 * Finds and returns all the records of a specified patient from a specified
	 * date.
	 * 
	 * @param healthcard_num
	 *            the identifier of a patient whose records are requested
	 * @param date
	 *            the date of the records requested
	 * @return a list of all records at the specified date for the patient with
	 *         healthcard number healthcard_num
	 * @throws Exception
	 *             if the file stored at healthcard_num doesn't exist or there
	 *             was an error accessing it.
	 */
	public SortedMap<String, SortedMap<String, List<Record>>> getRecordsByDate(
			String healthcard_num, String date) throws Exception {

		// Get all the visits that contain records of that date.
		SortedMap<String, SortedMap<String, List<Record>>> recordsofadate = getRecordsOfVisitByDate(
				healthcard_num, date);

		// From each visit, remove the records that don't contain the given
		// date.

		String[] visits = recordsofadate.keySet().toArray(
				new String[recordsofadate.keySet().size()]);

		int i = 0;

		while (i < recordsofadate.keySet().size()) {

			String[] dates_of_visit = recordsofadate
					.get(visits[i])
					.keySet()
					.toArray(
							new String[recordsofadate.get(visits[i]).keySet()
									.size()]);

			int j = 0;
			while (j < dates_of_visit.length) {

				if (!dates_of_visit[j].equals(date)) {
					recordsofadate.get(visits[i]).remove(dates_of_visit[j]);
				}
				j += 1;
			}

			i += 1;
		}

		return recordsofadate;

	}

	/**
	 * Finds all the records of a patient with healthcard number healthcard_num
	 * created during the visit at which the date date occurred.
	 * 
	 * @param healthcard_num
	 *            the identifier of a patient whose records are requested.
	 * @param date
	 *            the date whose records of its associated visit are requested.
	 * @return A map whose keys are the visit number and the values are all the
	 *         records which were created during that visit.
	 * @throws Exception
	 *             if the file stored at healthcard_num doesn't exist or there
	 *             was an error accessing it.
	 */
	public SortedMap<String, SortedMap<String, List<Record>>> getRecordsOfVisitByDate(
			String healthcard_num, String date) throws Exception {

		SortedMap<String, SortedMap<String, List<Record>>> allvisits = new TreeMap<String, SortedMap<String, List<Record>>>();

		try {

			bf2 = new BufferedReader(new FileReader(file_path_folder
					+ healthcard_num));

			boolean found_date_in_visit = false;
			SortedMap<String, List<Record>> recordsofvisit = new TreeMap<String, List<Record>>();
			String current_date = null;
			String current_visit = null;

			String line = null;

			while ((line = bf2.readLine()) != null) {

				String[] linetype = getDataType(line);

				if (!linetype[0].equals("line_break")) {

					if (linetype[0].equals("visit_num")) {

						if (found_date_in_visit) {

							allvisits.put(current_visit, recordsofvisit);
						}

						recordsofvisit = new TreeMap<String, List<Record>>();

						if (current_date != null) {
							if (DefaultValues.sdf.parse(current_date).after(
									DefaultValues.sdf.parse(date))) {
								break;
							}
						}

						found_date_in_visit = false;
						current_visit = linetype[1];

					} else if (linetype[0].equals("date")) {

						current_date = linetype[1];

						recordsofvisit.put(current_date,
								new ArrayList<Record>());

						if (current_date.equals(date)) {
							found_date_in_visit = true;
						}

					} else if (linetype[0].equals("record")) {

						recordsofvisit.get(current_date).add(
								generateRecord(current_date, linetype[1]));

					}

				}

			}

			if (found_date_in_visit) {

				allvisits.put(current_visit, recordsofvisit);
			}

			bf2.close();

		} catch (Exception e) {

			throwFileFormatError(e, healthcard_num);

		}

		// Sort the map.
		return allvisits;

	}

	/**
	 * Creates an object of Record at the date date and with information from
	 * recordstring.
	 * 
	 * @param date
	 *            the date at which the record occurred.
	 * @param recordstring
	 *            string to be sliced and added as parameters to the Record
	 *            constructor
	 * @return a Record object
	 */
	private Record generateRecord(String date, String recordstring) {

		String[] data = recordstring.toString().split(DefaultValues.param_sep,
				5);
		return new Record(date, data[0], Integer.parseInt(data[1]),
				Integer.parseInt(data[2]), Double.parseDouble(data[3]), data[4]);

	}

	/**
	 * Finds the last created record in the file named healthcard_num and
	 * produces a record object with the last record's specifications.
	 * 
	 * @param healthcard_num
	 * @return a Record object.
	 * @throws Exception
	 *             c
	 */
	public Record getMostRecentRecord(String healthcard_num) throws Exception {

		// Assume an empty record until the last one is found (if there is one).
		Record last_record = null;

		// If there is a file of records for the patient.
		try {

			bf1 = new BufferedReader(new FileReader(file_path_folder
					+ healthcard_num));
			String line = "null";
			String date = "null";
			// Holds the string containing the last record found in the
			// file.
			String lastrecordstring = null;

			while ((line = bf1.readLine()) != null) {

				String[] linetype = getDataType(line);

				if (linetype[0].equals("date")) {
					date = linetype[1];
				}

				else if (linetype[0].equals("record")) {
					lastrecordstring = linetype[1];
				}

			}

			bf1.close();
			last_record = generateRecord(date, lastrecordstring);

		} catch (Exception e) {

			throwFileFormatError(e, healthcard_num);

		}

		return last_record;
	}

	/**
	 * Finds the patient identified by healthcard_num and produces that
	 * patient's full name and birthdate.
	 * 
	 * @param healthcard_num
	 *            the identifier of the patient with healthcard number
	 *            healthcard_num.
	 * @return a string array where the first element is the name of the patient
	 *         with healthcard number healthcard_num and the second element is
	 *         their birth date.
	 * @throws Exception
	 *             if the file stored at healthcard_num doesn't exist or there
	 *             was an error accessing it.
	 */
	public String[] lookUpPatient(String healthcard_num) throws Exception {

		String[] data = null;

		try {

			bf1 = new BufferedReader(new FileReader(file_path_folder
					+ patients_file_path));
			String line = "null";

			while ((line = bf1.readLine()) != null) {

				String[] linetype = getDataType(line);

				if (!linetype[0].equals("line_break")) {
					String[] user_info = linetype[1]
							.split(DefaultValues.param_sep);
					if (user_info[0].equals(healthcard_num)) {
						data = new String[] { user_info[1], user_info[2] };
						break;
					}
				}

			}

			bf1.close();

		} catch (Exception e) {

			throwFileFormatError(e, patients_file_path);

		}

		return data;

	}

	/**
	 * Finds healthcard numbers of all patients.
	 * 
	 * @return a list where the elements are the healthcard numbers of all the
	 *         patients.
	 * @throws Exception
	 *             if the file stored at healthcard_num doesn't exist or there
	 *             was an error accessing it.
	 */
	public List<String> getHealthCardsofPatients() throws Exception {

		List<String> healthcards = new ArrayList<String>();

		try {

			bf1 = new BufferedReader(new FileReader(file_path_folder
					+ patients_file_path));
			String line = "null";

			while ((line = bf1.readLine()) != null) {

				String[] linetype = getDataType(line);

				if (!linetype[0].equals("line_break")) {

					String healthcard = linetype[1].substring(0,
							line.indexOf(DefaultValues.param_sep));

					boolean found = false;

					for (String s : healthcards) {
						if (s.equals(healthcard)) {
							found = true;
							break;
						}
					}

					if (!found) {
						healthcards.add(healthcard);
					}
				}
			}

			bf1.close();

		} catch (Exception e) {
			throwFileFormatError(e, patients_file_path);
		}

		return healthcards;
	}

	/**
	 * Determines whether a patient identified by healthcard_num has checked-in
	 * or not.
	 * 
	 * @param healthcard_num
	 *            the identifier of the patient with healthcard number
	 *            healthcard_num
	 * @return boolean indicating whether a patient has checked-in.
	 * @throws Exception
	 *             if the file stored at healthcard_num doesn't exist or there
	 *             was an error accessing it.
	 */
	public boolean isCheckedIn(String healthcard_num) throws Exception {

		boolean ischeckedin = false;

		try {

			bf1 = new BufferedReader(new FileReader(file_path_folder
					+ healthcard_num));

			int numvisits = 0;
			int numcheckouts = 0;

			String line = "null";

			while ((line = bf1.readLine()) != null) {

				String[] linetype = getDataType(line);

				if (linetype[0].equals("visit_num")) {
					numvisits += 1;
				} else if (linetype[0].equals("checked_out")) {
					numcheckouts += 1;
				}

			}

			bf1.close();

			ischeckedin = (numvisits > numcheckouts);

		} catch (Exception e) {

			throwFileFormatError(e, healthcard_num);

		}

		return ischeckedin;

	}

	/**
	 * Determines the number of times a patient has visited the ER.
	 * 
	 * @param healthcard_num
	 *            the identifier of the patient with healthcard number
	 *            healthcard_num
	 * @return an integer indicating the number of visits a patient has made to
	 *         the ER.
	 * @throws Exception
	 *             if the file stored at healthcard_num doesn't exist or there
	 *             was an error accessing it.
	 */
	public int find_last_visit_num(String healthcard_num) throws Exception {

		// By default -1 means there are no visit records for a patient.
		int last_visit = -1;

		try {

			bf1 = new BufferedReader(new FileReader(file_path_folder
					+ healthcard_num));

			String line = "null";

			while ((line = bf1.readLine()) != null) {

				String[] linetype = getDataType(line);

				if (linetype[0].equals("visit_num")) {
					// Get every visit number, replace the previous one,
					// until the program finishes reading the file.
					last_visit = Integer.parseInt(linetype[1]);
				}
			}

			bf1.close();

		} catch (Exception e) {

			throwFileFormatError(e, healthcard_num);

		}

		return last_visit;

	}

	/**
	 * Finds the date of the last recorded record of a patient identifiedby
	 * healthcard_num.
	 * 
	 * @param healthcard_num
	 *            the identifier of the patient with healthcard number
	 *            healthcard_num
	 * @return a string of a date.
	 * @throws Exception
	 *             if the file stored at healthcard_num doesn't exist or there
	 *             was an error accessing it.
	 */
	public String find_last_date(String healthcard_num) throws Exception {

		// This will be returned if there are no dates registered.
		String last_date = null;

		try {

			bf1 = new BufferedReader(new FileReader(file_path_folder
					+ healthcard_num));

			String line = "null";

			while ((line = bf1.readLine()) != null) {

				String[] linetype = getDataType(line);

				if (linetype[0].equals("date")) {
					// Get a date and replace the previous one until the
					// program finishes reading the file.
					last_date = linetype[1];
				}
			}

			bf1.close();

		} catch (Exception e) {

			throwFileFormatError(e, healthcard_num);

		}

		return last_date;

	}
}
