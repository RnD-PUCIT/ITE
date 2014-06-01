package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import org.movsim.viewer.App;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.FileUtils;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;

public class AppFrameListener implements ActionListener {

	MovsimConfigContext mvCxt;
	JMenuItem open,save,run;
	public AppFrameListener(MovsimConfigContext mvCxt) {
		this.mvCxt = mvCxt;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton srcBtn=null;
		JMenuItem mi=null;
		if(e.getSource() instanceof JButton)srcBtn=(JButton)e.getSource();
		if(e.getSource() instanceof JMenuItem)mi=(JMenuItem)e.getSource();
		if(mi==open){
			File f=FileUtils.chooseFile("xprj");
			if(f==null)return;
			MovsimScenario.setScenario(f, mvCxt);
		}
		else if(mi==save){
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
		else if(mi==run){
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
	}
	public void setOpen(JMenuItem open) {
		this.open = open;
	}
	public void setSave(JMenuItem save) {
		this.save = save;
	}
	public void setRun(JMenuItem run) {
		this.run = run;
	}

}
