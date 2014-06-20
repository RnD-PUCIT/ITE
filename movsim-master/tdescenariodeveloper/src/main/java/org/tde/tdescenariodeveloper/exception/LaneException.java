package org.tde.tdescenariodeveloper.exception;
/**
 * Instance of this class is thrown if lane data validation fails.
 * @author Shmeel
 *
 */
public class LaneException extends Exception {
	String msg;
	/**
	 * 
	 * @param msg message to be show to user
	 */
	public LaneException(String msg) {
		super(msg);
		this.msg=msg;
	}
	@Override
	public String toString() {
		return "LaneException [msg=" + msg + "]";
	}
}
