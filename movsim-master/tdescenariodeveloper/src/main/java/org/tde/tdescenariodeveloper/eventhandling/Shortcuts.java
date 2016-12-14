package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.SwingUtilities;
import org.movsim.viewer.App;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.FileUtils;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;


public class Shortcuts implements KeyListener {

   

	public static MovsimConfigContext mvCxt;
	public static int fileCount = -1 , HistoryReverseLimit = 0;
	public static boolean firstTime = true;
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
			final File f=FileUtils.chooseFile("xprj");
			if(f==null)return;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MovsimScenario.setScenario(f, mvCxt);
				}
			});
		}
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					File f=null;
					f=FileUtils.saveFile("xprj");
					if(f!=null){
						DataToViewerConverter.updateFractions(mvCxt);
						MovsimScenario.saveScenario(f,mvCxt);
					}
				}
			});
		}
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_R) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					File f=new File(new File("").getAbsoluteFile()+"\\tmp.xprj");
					DataToViewerConverter.updateFractions(mvCxt);
					MovsimScenario.saveScenario(f, mvCxt);
					String[]s={"-f",f.getAbsolutePath()};
					try {
						App.main(s);
					} catch (URISyntaxException | IOException e) {
						GraphicsHelper.showToast(e.getMessage(), mvCxt.getRdCxt().getToastDurationMilis());
					}
				}
			}).start();
		}
		
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
			
			
			
			if ((fileCount-1) >= HistoryReverseLimit)
			{
				fileCount -= 1;
				final File f=new File(new File("").getAbsoluteFile()+"\\History\\"+ Integer.toString(fileCount % 20) + ".xprj");
				if(f==null)return;
				MovsimScenario.setScenario2(f, mvCxt);
			}
			
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			
			mvCxt.getRdCxt().getAppFrame().getTpnl().setDefaultCursor();
			
		}
			
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	
	public static void setMvCxt(MovsimConfigContext mvCxt2) {
		mvCxt = mvCxt2;
	}
	
	public static void saveAction()
	{
		fileCount++;
		if ( fileCount >= 40 )
			fileCount -= 20 ;
		if (fileCount % 20  ==HistoryReverseLimit  && !firstTime)
		{
			HistoryReverseLimit = (fileCount % 20) +1 ;
			HistoryReverseLimit = HistoryReverseLimit % 20 ;
		}
		firstTime = false ;
		File f=new File(new File("").getAbsoluteFile()+"\\History\\"+ Integer.toString(fileCount % 20) + ".xprj");
		if (mvCxt != null)
		{	
			DataToViewerConverter.updateFractions(mvCxt);
			MovsimScenario.saveScenario(f, mvCxt);
		}
	}


}
