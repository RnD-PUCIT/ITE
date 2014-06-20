package org.tde.tdescenariodeveloper.exception;
/**
 * Instance of this class is thrown when user enters data which is not valid number.
 * @author Shmeel
 *
 */
public class InvalidInputException extends NumberFormatException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3775302581317134863L;
	String msg;
	/**
	 * 
	 * @param msg message to be shown to user
	 */
	public InvalidInputException(String msg) {
		super(msg);
		this.msg=msg;
	}
	@Override
	public String toString(){
		return msg;
	}
}
