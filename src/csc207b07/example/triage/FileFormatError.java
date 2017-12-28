package csc207b07.example.triage;

public class FileFormatError extends Exception{
	/**
	 * holds a version number for this serializable class.
	 */
	private static final long serialVersionUID = -3688462180556920100L;
	
	/**
	 * Creates a new FileFormatError object which holds an error_message.
	 * @param error_message the string to be returned when this exception is thrown.
	 */
	public FileFormatError(String error_message){
		super(error_message);		
	}
}
