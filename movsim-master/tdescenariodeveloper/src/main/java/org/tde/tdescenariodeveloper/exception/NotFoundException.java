package org.tde.tdescenariodeveloper.exception;

public class NotFoundException extends Exception {
	String msg;
	public NotFoundException(String msg) {
		super(msg);
		this.msg=msg;
	}
	@Override
	public String toString(){
		return msg;
	}
}
