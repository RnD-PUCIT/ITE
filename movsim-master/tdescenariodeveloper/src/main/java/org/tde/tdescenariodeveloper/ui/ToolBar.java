package org.tde.tdescenariodeveloper.ui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.movsim.viewer.App;
import org.tde.tdescenariodeveloper.eventhandling.Shortcuts;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.FileUtils;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;
/**
 * Class is used to represent different tools found in {@link ToolBar}
 * @author Shmeel
 * @see ToolBar
 * @see ToolsPanel
 */
public class ToolBar extends JToolBar implements ActionListener{
	private static final long serialVersionUID = -4127064504014635395L;

JButton open,run,save;
private boolean blocked=true;
private MovsimConfigContext mvCxt;
DrawingArea drawingArea;
/**
 * 
 * @param drawingArea {@link DrawingArea} drawing canvas to show current situation of the scenario 
 */
	public ToolBar(DrawingArea drawingArea) {
		this.drawingArea=drawingArea;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.anchor=GridBagConstraints.WEST;
//		gbc.insets=new Insets(5, 1, 5, 1);
		gbc.ipadx=2;
		gbc.ipady=2;
		
		open=new JButton("Open",TDEResources.getResources().getOpen());
		save=new JButton("Save",TDEResources.getResources().getSave());
		run=new JButton("Run",TDEResources.getResources().getRun());
		
		open.setFocusable(false);
		run.setFocusable(false);
		save.setFocusable(false);
		
		open.addActionListener(this);
		run.addActionListener(this);
		save.addActionListener(this);
		
		add(open,gbc);
		add(save,gbc);
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.weightx=1;
		add(run,gbc);
		
		setOpaqueness(false,open,run,save);
	}
	/**
	 * sets opaqueness of sent buttons
	 * @param b if false transparent if true opaque
	 * @param btns {@link JButton}s to be set
	 */
	public static void setOpaqueness(boolean b,AbstractButton...btns) {
		for(AbstractButton bg:btns){
			bg.setOpaque(b);
		}
	}
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton srcBtn=null;
		if(e.getSource() instanceof JButton)srcBtn=(JButton)e.getSource();
		if(srcBtn==open){
			final File f=FileUtils.chooseFile("xprj");
			if(f==null)return;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MovsimScenario.setScenario(f, mvCxt);
				}
			});
		}
		else if(srcBtn==save){
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
		else if(srcBtn==run){
			new Thread(new Runnable() {
				@Override
				public void run() {
					String path = "History//"  + "tmp.xprj" ;
				    ClassLoader classLoader = Shortcuts.class.getClassLoader();
				    File f = new File(path);
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
	public void setMvCxt(MovsimConfigContext mvCxt) {
		this.mvCxt = mvCxt;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
