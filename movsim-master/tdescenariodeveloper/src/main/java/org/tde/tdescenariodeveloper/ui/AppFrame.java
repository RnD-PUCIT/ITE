package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.xml.bind.JAXBException;

import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.input.network.OpenDriveReader;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.viewer.App;
import org.movsim.xml.MovsimInputLoader;
import org.tde.tdescenariodeveloper.eventhandling.AppFrameListener;
import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaMouseListener;
import org.tde.tdescenariodeveloper.eventhandling.JunctionsListener;
import org.tde.tdescenariodeveloper.jaxbhandler.Marshalling;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.FileUtils;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
import org.xml.sax.SAXException;

public class AppFrame extends JFrame {
	private static final long serialVersionUID = 14320973455L;
	private RoadContext rdCxt;
	private StatusPanel statusPnl;
	private JunctionsPanel jp;
	private RoadNetwork rn;
	private ToolBar toolbar;
	private JunctionsListener jl;
	private MovsimConfigContext mvCxt;
	public RoadContext getrdCxt(){
		return rdCxt;
	}
	public AppFrame() {
		setPreferredSize(new Dimension(1024, 768));
		setMinimumSize(new Dimension(700, 500));
		setTitle("Vehicular Traffic  Flow Scenario Development Environment");
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open",TDEResources.getResources().getOpen());
		mnFile.add(mntmOpen);
		
		JMenuItem mntmRun = new JMenuItem("Run",TDEResources.getResources().getRun());
		mnFile.add(mntmRun);
		
		JMenuItem mntmSave = new JMenuItem("save",TDEResources.getResources().getSave());
		mnFile.add(mntmSave);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmChangeToastDelay = new JMenuItem("Change toast delay");
		mnEdit.add(mntmChangeToastDelay);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		JTabbedPane tabPane=new JTabbedPane();
		ImageIcon icon=new ImageIcon(getClass().getClassLoader().getResource("road_icon.png"));
		icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		ImageIcon icon2=new ImageIcon(getClass().getClassLoader().getResource("Junctions_tab_icon.png"));
		icon2.setImage(icon2.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		rn=new RoadNetwork();
		new OpenDriveHandlerJaxb().create("", RoadNetworkUtils.getNewOdr(), rn);
		rdCxt= new RoadContext(rn,this);
		jl=new JunctionsListener(rdCxt);
		jp=new JunctionsPanel(rdCxt,jl);
		if(rdCxt!=null && rdCxt.getRn().getOdrNetwork()!=null && rdCxt.getRn().getOdrNetwork().getJunction()!=null && rdCxt.getRn().getOdrNetwork().getJunction().size()>0)jp.updateJunction();
		jl.setBlocked(false);
		JPanel drawingPnl=new JPanel();
		DrawingArea drawingArea = new DrawingArea(rdCxt);
		rdCxt.setDrawingArea(drawingArea);
		DrawingAreaMouseListener ms=(DrawingAreaMouseListener)drawingArea.getMouseMotionListeners()[0];
		drawingPnl.add(drawingArea);
		drawingPnl.setBorder(BorderFactory.createLoweredBevelBorder());
		rdCxt.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "Road Properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(drawingPnl, BorderLayout.CENTER);
		
		toolbar=new ToolBar(drawingArea);
		toolbar.setBlocked(false);
		tabPane.addTab("Road properties", icon, rdCxt.getSp(), "Editing panel for currently selected road");
		tabPane.addTab("Junctions Editor", icon2,jp.getSp() , "Editiong panel for junctions");
		getContentPane().add(tabPane, BorderLayout.EAST);
		statusPnl=new StatusPanel();
		statusPnl.setStatus("Status");
//		getContentPane().add(statusPnl, BorderLayout.SOUTH);
		ms.setStatusPnl(statusPnl);
		getContentPane().add(new ToolsPanel(), BorderLayout.WEST);
		getContentPane().add(toolbar,BorderLayout.NORTH);
		
		mvCxt=new MovsimConfigContext(MovsimScenario.getMovsim(),rdCxt);
		rdCxt.setMvCxt(mvCxt);
		
		AppFrameListener appListener=new  AppFrameListener(mvCxt);
		appListener.setOpen(mntmOpen);
		appListener.setSave(mntmSave);
		appListener.setRun(mntmRun);
		mntmOpen.addActionListener(appListener);
		mntmRun.addActionListener(appListener);
		mntmSave.addActionListener(appListener);
		
		JPanel southPanel=new JPanel(new BorderLayout());
		southPanel.add(mvCxt,BorderLayout.CENTER);
		southPanel.add(statusPnl,BorderLayout.SOUTH);
		getContentPane().add(southPanel,BorderLayout.SOUTH);
		GraphicsHelper.finalizeFrame(this);
	}
	public StatusPanel getStatusPnl() {
		return statusPnl;
	}
	public void setStatusPnl(StatusPanel statusPnl) {
		this.statusPnl = statusPnl;
	}
	public JunctionsPanel getJp() {
		return jp;
	}
	public ToolBar getToolbar() {
		return toolbar;
	}
	public JunctionsListener getJl() {
		return jl;
	}
}
