package org.tde.tdescenariodeveloper.ui;

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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.movsim.viewer.App;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.FileUtils;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;

public class ToolBar extends JToolBar implements ItemListener,ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4127064504014635395L;

JCheckBox name,id,axis;
JButton open,run,save;
private boolean blocked=true;
private MovsimConfigContext mvCxt;

DrawingArea drawingArea;
	public ToolBar(DrawingArea drawingArea) {
		this.drawingArea=drawingArea;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.anchor=GridBagConstraints.WEST;
		gbc.insets=new Insets(5, 10, 5, 10);
		gbc.ipadx=2;
		gbc.ipady=2;
		
		open=new JButton("Open",TDEResources.getResources().getOpen());
		save=new JButton("Save",TDEResources.getResources().getSave());
		run=new JButton("Run",TDEResources.getResources().getRun());
		
		name=new JCheckBox("Draw road names");
		id=new JCheckBox("Draw road id's");
		axis=new JCheckBox("Draw axis");
		name.addItemListener(this);
		id.addItemListener(this);
		axis.addItemListener(this);
		
		open.setFocusable(false);
		run.setFocusable(false);
		save.setFocusable(false);
		
		open.addActionListener(this);
		run.addActionListener(this);
		save.addActionListener(this);
		
		add(open,gbc);
		add(save,gbc);
		add(run,gbc);
		
		add(name,gbc);
		add(id,gbc);
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.weightx=1;
		add(axis,gbc);
		id.setSelected(true);
		axis.setSelected(true);
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(blocked)return;
		JRadioButton rdsrc=null;
		JCheckBox chsrc=null;
		if(e.getSource() instanceof JRadioButton)rdsrc=(JRadioButton)e.getSource();
		else if(e.getSource() instanceof JCheckBox)chsrc=(JCheckBox)e.getSource();
		if(chsrc==name){
			if(name.isSelected() && id.isSelected())id.setSelected(false);
			drawingArea.setDrawRoadId(id.isSelected());
			drawingArea.setDrawRoadNames(name.isSelected());
		}
		else if(chsrc==id){
			if(id.isSelected() && name.isSelected())name.setSelected(false);
			drawingArea.setDrawRoadId(id.isSelected());
			drawingArea.setDrawRoadNames(name.isSelected());
		}
		else if(chsrc==axis){
			drawingArea.setDrawAxis(axis.isSelected());
		}
		drawingArea.getRoadPrPnl().updateGraphics();
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
	public void setMvCxt(MovsimConfigContext mvCxt) {
		this.mvCxt = mvCxt;
	}
}
