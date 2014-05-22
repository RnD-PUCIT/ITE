package org.tde.tdescenariodeveloper.exception;

public class LaneException extends Exception {
	String msg;
	public LaneException(String msg) {
		super(msg);
		this.msg=msg;
	}
	@Override
	public String toString() {
		return "LaneException [msg=" + msg + "]";
	}
}
