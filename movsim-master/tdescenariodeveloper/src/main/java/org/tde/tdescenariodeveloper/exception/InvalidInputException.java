package org.tde.tdescenariodeveloper.exception;

public class InvalidInputException extends NumberFormatException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3775302581317134863L;
	String msg;
	public InvalidInputException(String msg) {
		super(msg);
		this.msg=msg;
	}
	@Override
	public String toString(){
		return msg;
	}
}
