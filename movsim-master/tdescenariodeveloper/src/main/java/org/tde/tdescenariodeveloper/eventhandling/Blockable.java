package org.tde.tdescenariodeveloper.eventhandling;

import java.util.EventListener;
/**
 * Interface implemented by listeners to be blocked when relevent components are being updated to increase efficiency.
 * @author Shmeel
 *
 */
public interface Blockable extends EventListener {
	/**
	 * used to block or unblock listener
	 * @param blocked if true listener is blocked, unblocked otherwise
	 */
	public void setBlocked(boolean blocked);
}
