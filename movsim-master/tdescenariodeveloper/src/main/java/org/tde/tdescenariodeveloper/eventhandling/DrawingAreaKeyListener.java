package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.tde.tdescenariodeveloper.ui.DrawingArea;

public class DrawingAreaKeyListener extends DrawingAreaController implements KeyListener {

    public DrawingAreaKeyListener(DrawingArea trafficCanvas) {
		super(trafficCanvas);
	}


    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        final char c = Character.toUpperCase(e.getKeyChar());
        if (c == KeyEvent.CHAR_UNDEFINED)
            return;
        switch (c) {
        case 'I':
        case '+':
        case '=':
            commandZoomIn();
            break;
        case 'O':
        case '-':
        case '_':
            commandZoomOut();
            break;
        }
        e.consume();
    }
}
