package org.tde.tdescenariodeveloper;

import javax.swing.JApplet;

import org.tde.tdescenariodeveloper.ui.AppFrame;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class TDEApplet extends JApplet {
	@Override
	public void init() {
		super.init();
		setContentPane(new AppFrame().getContentPane());
	}
}
