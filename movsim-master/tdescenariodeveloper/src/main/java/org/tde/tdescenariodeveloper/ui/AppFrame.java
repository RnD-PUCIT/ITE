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
import javax.xml.bind.JAXBException;

import org.movsim.input.network.OpenDriveReader;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.viewer.App;
import org.xml.sax.SAXException;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;

public class AppFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 14320973455L;
	RoadPropertiesPanel rdPrPnl;
	public RoadPropertiesPanel getRdPrPnl(){
		return rdPrPnl;
	}
	public AppFrame() {
//		OpenDRIVE od=new OpenDRIVE();
//		Header h=new Header();
//		ArrayList<org.movsim.network.autogen.opendrive.OpenDRIVE.Road>rds=new ArrayList<>();
//		org.movsim.network.autogen.opendrive.OpenDRIVE.Road r=new org.movsim.network.autogen.opendrive.OpenDRIVE.Road();
//		r.setId("2");
//		r.setName("road");
//		rds.add(r);
//		h.setName("aldi");
//		h.setNorth(1.57);
//		od.setHeader(h);
//		od.getRoad().add(r);
//		od.getRoad().add(r);
//		
//		Marshalling.writeToXml(od);
		
		
		
		
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
		DrawingArea drawingArea = new DrawingArea(rn);
		rdPrPnl= new RoadPropertiesPanel();
		rdPrPnl.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Road Properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(drawingArea, BorderLayout.CENTER);
		getContentPane().add(rdPrPnl, BorderLayout.EAST);
	}

}
