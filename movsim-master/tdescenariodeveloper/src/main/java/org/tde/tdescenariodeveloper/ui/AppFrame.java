package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.xml.bind.JAXBException;

import org.movsim.input.network.OpenDriveHandlerJaxb;
import org.movsim.input.network.OpenDriveReader;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.viewer.App;
import org.tde.tdescenariodeveloper.eventhandling.DrawingAreaMouseListener;
import org.tde.tdescenariodeveloper.eventhandling.JunctionsListener;
import org.tde.tdescenariodeveloper.jaxbhandler.Marshalling;
import org.tde.tdescenariodeveloper.utils.FileUtils;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.RoadNetworkUtils;
import org.xml.sax.SAXException;

public class AppFrame extends JFrame {
	private static final long serialVersionUID = 14320973455L;
	private RoadContext rdCxt;
	private StatusPanel statusPnl;
	private JunctionsPanel jp;
	private String prjName="cleaf";
	private RoadNetwork rn;
	private ToolBar toolbar;
	private JunctionsListener jl;
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
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					File f=FileUtils.chooseFile("xodr");
					rn.reset();
					OpenDriveReader.loadRoadNetwork(rn,f.getAbsolutePath());
					jl.setBlocked(true);
					jp.updateJunction();
					jl.setBlocked(false);
					prjName=f.getName().substring(0,f.getName().lastIndexOf("."));
					rdCxt.updateGraphics();
					revalidate();
					repaint();
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				}
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmRun = new JMenuItem("Run");
		mntmRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Marshalling.writeToXml(rdCxt.getRn().getOdrNetwork(),new File("G:\\Studies\\Eclipse\\movsim-master\\sim\\buildingBlocks\\"+prjName+".xodr"));
						String[]s={"-f","G:\\Studies\\Eclipse\\movsim-master\\sim\\buildingBlocks\\"+prjName};
						try {
							App.main(s);
						} catch (URISyntaxException | IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		mnFile.add(mntmRun);
		
		JMenuItem mntmSave = new JMenuItem("save");
		mntmSave.setActionCommand("Save");
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							File f=null;
							f=FileUtils.saveFile("xodr");
							if(f!=null)Marshalling.writeToXml(rdCxt.getRn().getOdrNetwork(),f);
						}
					}).start();
			}
		});
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
		tabPane.addTab("Road properties", icon, rdCxt.getSp(), "Editing panel for currently selected road");
		tabPane.addTab("Junctions Editor", icon2,jp.getSp() , "Editiong panel for junctions");
		getContentPane().add(tabPane, BorderLayout.EAST);
		statusPnl=new StatusPanel();
		statusPnl.setStatus("Status");
//		getContentPane().add(statusPnl, BorderLayout.SOUTH);
		ms.setStatusPnl(statusPnl);
		getContentPane().add(new ToolsPanel(), BorderLayout.WEST);
		getContentPane().add(toolbar,BorderLayout.NORTH);
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
