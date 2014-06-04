package org.tde.tdescenariodeveloper.eventhandling;

import java.util.EventListener;

public interface Blockable extends EventListener {
	public void setBlocked(boolean b);
}
