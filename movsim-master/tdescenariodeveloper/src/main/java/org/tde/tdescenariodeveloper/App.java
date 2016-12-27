package org.tde.tdescenariodeveloper;

import javax.swing.JOptionPane;

import org.tde.tdescenariodeveloper.ui.AppFrame;


/**
 * The main class containing entry point.
 * @author Shmeel
 *
 */
public class App 
{
	/**
	 * Main method : entry point
	 * @param args arguments
	 */
    public static void main( String[] args )
    {
    	//JOptionPane.showMessageDialog(null, "I am too happy.");
    	AppFrame fr=new AppFrame();
    	fr.setVisible(true);
    }
}
