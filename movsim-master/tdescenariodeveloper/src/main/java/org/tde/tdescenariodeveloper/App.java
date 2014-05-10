package org.tde.tdescenariodeveloper;

import org.tde.tdescenariodeveloper.ui.AppFrame;
import org.tde.tdescenariodevelopment.utils.GraphicsHelper;



public class App 
{
    public static void main( String[] args )
    {
    	System.out.println("Shmeel");
    	GraphicsHelper.setWindowsUI();
    	AppFrame fr=new AppFrame();
    	fr.setVisible(true);
    }
}
