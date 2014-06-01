package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;

public class MovsimConfigContextMouseListener implements MouseListener,
		MouseMotionListener {
	MovsimConfigContext mvCxt;
	Rectangle bnds;
	public MovsimConfigContextMouseListener(MovsimConfigContext mvCxt) {
		this.mvCxt=mvCxt;
		bnds=mvCxt.getBounds();
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch(bnds.outcode(e.getPoint())){
		case Rectangle.OUT_RIGHT:
			mvCxt.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			break;
		case Rectangle.OUT_LEFT:
			mvCxt.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
			break;
		case Rectangle.OUT_TOP:
			mvCxt.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			break;
		case Rectangle.OUT_BOTTOM:
			mvCxt.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
			break;
		default:
			mvCxt.setCursor(Cursor.getDefaultCursor());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
