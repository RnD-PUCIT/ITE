package org.tde.tdescenariodeveloper.ui;

import java.awt.Graphics;

import javax.swing.JMenuBar;

import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class TDEMenuBar extends JMenuBar {
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
