package org.tde.tdescenariodeveloper.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.xml.bind.JAXBException;

import org.movsim.input.network.OpenDriveReader;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.viewer.App;
import org.xml.sax.SAXException;

public class AppFrame extends JFrame {
	JPanel drawingPnl;
	private static final long serialVersionUID = 14320973455L;
	private RoadPropertiesPanel rdPrPnl;
	private StatusPanel statusPnl;
	public RoadPropertiesPanel getRdPrPnl(){
		return rdPrPnl;
	}
	public AppFrame() {
		setSize(new Dimension(1024, 768));
		setPreferredSize(new Dimension(1024, 768));
		setMinimumSize(new Dimension(700, 500));
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setTitle("Vehicular Traffic  Flow Scenario Development Environment");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		pack();
		RoadNetwork rn=new RoadNetwork();
		try {
			OpenDriveReader.loadRoadNetwork(rn,"G:\\Studies\\Eclipse\\movsim-master\\sim\\buildingBlocks\\cleaf.xodr");
		} catch (JAXBException | SAXException e1) {
			e1.printStackTrace();
		}
		rdPrPnl= new RoadPropertiesPanel(rn);
		drawingPnl=new JPanel();
		DrawingArea drawingArea = new DrawingArea(rn,rdPrPnl);
		rdPrPnl.setDrawingArea(drawingArea);
		DrawingAreaMouseListener ms=(DrawingAreaMouseListener)drawingArea.getMouseMotionListeners()[0];
		drawingPnl.add(drawingArea);
//		drawingPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Drawing Area", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, new Font("arial",Font.BOLD,14),null));
		rdPrPnl.setBorder(new TitledBorder(new EmptyBorder(5, 5, 5, 5), "Road Properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(drawingPnl, BorderLayout.CENTER);
		getContentPane().add(rdPrPnl.getSp(), BorderLayout.EAST);
		statusPnl=new StatusPanel();
		statusPnl.setStatus("Status");
		getContentPane().add(statusPnl, BorderLayout.SOUTH);
		ms.setStatusPnl(statusPnl);
		getContentPane().add(new ToolsPanel(), BorderLayout.WEST);
		add(new ToolBar(drawingArea),BorderLayout.NORTH);
	}
	public StatusPanel getStatusPnl() {
		return statusPnl;
	}
	public void setStatusPnl(StatusPanel statusPnl) {
		this.statusPnl = statusPnl;
	}

}
