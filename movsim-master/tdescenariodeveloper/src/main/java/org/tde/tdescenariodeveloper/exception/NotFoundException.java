package org.tde.tdescenariodeveloper.exception;
/**
 * Instance of this class is thrown when requested data/components is not found
 * @author Shmeel
 *
 */
public class NotFoundException extends Exception {
	String msg;
	/**
	 * 
	 * @param msg message to be shown to user
	 */
	public NotFoundException(String msg) {
		super(msg);
		this.msg=msg;
	}
	@Override
	public String toString(){
		return msg;
	}
}
