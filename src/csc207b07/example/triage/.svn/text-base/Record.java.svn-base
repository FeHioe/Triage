package csc207b07.example.triage;

public class Record {
	/**
	 * holds the date the Record was created.
	 */
	private String date;
	/**
	 * holds the time the record was created.
	 */
	private String time;
	/**
	 * holds the heart rate and blood pressure.
	 */
	private int hr, bp;
	/**
	 * holds the temperature.
	 */
	private double temp;
	/**
	 * holds the description of symptoms and any additional comments made by
	 * Nurse.
	 */
	private String description;

	/**
	 * Creates a new Record object which holds the date, time, heart rate, blood
	 * pressure, temperature, and description values.
	 * 
	 * @param date
	 *            holds the date the Record was created
	 * @param time
	 *            holds the time the record was created
	 * @param hr
	 *            holds the heart rate
	 * @param bp
	 *            holds the blood pressure
	 * @param temp
	 *            holds the temperature
	 * @param description
	 *            holds the description of symptoms and any additional comments
	 *            made by Nurse
	 */
	public Record(String date, String time, int hr, int bp, double temp,
			String description) {
		this.time = time;
		this.date = date;
		this.hr = hr;
		this.bp = bp;
		this.temp = temp;
		this.description = description;
	}

	/**
	 * @return the date held in this record.
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * @return the time held in this record.
	 */
	public String getTime() {
		return this.time;
	}

	/**
	 * @return the heart rate held in this record.
	 */
	public int getHr() {
		return this.hr;
	}

	/**
	 * @return the blood pressure held in this record.
	 */
	public int getBp() {
		return this.bp;
	}

	/**
	 * @return the temperature held in this record,
	 */
	public double getTemp() {
		return this.temp;
	}

	/**
	 * @return the description held in this record,
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns a string representation of all the data held in this record. The
	 * format of a record.
	 * 
	 * @return a string of all the data held in this record in a specific
	 *         format.
	 */
	public String toString() {
		return this.time + DefaultValues.param_sep + this.hr
				+ DefaultValues.param_sep + this.bp + DefaultValues.param_sep
				+ this.temp + DefaultValues.param_sep + description;
	}

	/**
	 * Return true iff the data in both records is equal.
	 * 
	 * @return boolean.
	 */
	public boolean equals(Record r2) {		

		return this.getBp() == r2.getBp() &&
				this.getHr() == r2.getHr() &&
				this.getTemp() == this.getTemp() &&
				this.getDescription().equals(r2.getDescription());
				
	}
}
