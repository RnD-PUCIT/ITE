package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.xml.bind.JAXBException;

import org.movsim.input.network.OpenDriveReader;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.viewer.App;
import org.tde.tdescenariodeveloper.jaxbhandler.Marshalling;
import org.tde.tdescenariodeveloper.utils.FileUtils;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.xml.sax.SAXException;

public class AppFrame extends JFrame {
	private static final long serialVersionUID = 14320973455L;
	private RoadPropertiesPanel rdPrPnl;
	private StatusPanel statusPnl;
	private JunctionsPanel jp;
	public RoadPropertiesPanel getRdPrPnl(){
		return rdPrPnl;
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
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							String[]s={"-f","G:\\Studies\\Eclipse\\movsim-master\\sim\\buildingBlocks\\cloverleaf"};
							try {
								App.main(s);
							} catch (URISyntaxException | IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
			}
		});
		mnFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("save");
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							File f=null;
							f=FileUtils.saveFile("xodr");
							if(f!=null)Marshalling.writeToXml(rdPrPnl.getRn().getOdrNetwork(),f);
						}
					}).start();
			}
		});
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		pack();
		RoadNetwork rn=new RoadNetwork();
		try {
			OpenDriveReader.loadRoadNetwork(rn,"G:\\Studies\\Eclipse\\movsim-master\\sim\\buildingBlocks\\mycleaf.xodr");
		} catch (JAXBException | SAXException e1) {
			e1.printStackTrace();
		}
		rdPrPnl= new RoadPropertiesPanel(rn,this);
		jp=new JunctionsPanel(rdPrPnl);
		jp.updateJunction();
		JPanel drawingPnl=new JPanel();
		DrawingArea drawingArea = new DrawingArea(rdPrPnl);
		rdPrPnl.setDrawingArea(drawingArea);
		DrawingAreaMouseListener ms=(DrawingAreaMouseListener)drawingArea.getMouseMotionListeners()[0];
		drawingPnl.add(drawingArea);
//		drawingPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Drawing Area", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, new Font("arial",Font.BOLD,14),null));
		rdPrPnl.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "Road Properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(drawingPnl, BorderLayout.CENTER);
		getContentPane().add(rdPrPnl.getSp(), BorderLayout.EAST);
		statusPnl=new StatusPanel();
		statusPnl.setStatus("Status");
//		getContentPane().add(statusPnl, BorderLayout.SOUTH);
		getContentPane().add(jp.getSp(), BorderLayout.SOUTH);
		ms.setStatusPnl(statusPnl);
		getContentPane().add(new ToolsPanel(), BorderLayout.WEST);
		add(new ToolBar(drawingArea),BorderLayout.NORTH);
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
}
