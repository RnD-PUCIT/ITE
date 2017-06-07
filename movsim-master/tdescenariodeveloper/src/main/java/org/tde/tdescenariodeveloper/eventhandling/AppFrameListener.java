package org.tde.tdescenariodeveloper.eventhandling;

//import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.SwingUtilities;

import org.movsim.input.MovsimCommandLine;
import org.movsim.input.ProjectMetaData;
import org.movsim.logging.Logger;
import org.movsim.roadmappings.RoadMapping;
import org.movsim.roadmappings.RoadMapping.PosTheta;
import org.movsim.roadmappings.RoadMappingArc;
import org.movsim.roadmappings.RoadMappingLine;
import org.movsim.viewer.App;
import org.movsim.viewer.ui.AppFrame;
import org.movsim.viewer.ui.LogWindow;
import org.movsim.viewer.ui.ViewProperties;
import org.movsim.viewer.util.LocalizationStrings;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.FileUtils;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;
/**
 * Class for Listening frame events triggered by user.
 * @author Shmeel
 * @see App
 *
 */
public class AppFrameListener implements ActionListener,Blockable {
	/**
	 * Contains reference to Movsim configuration loaded xml and panels added in it. 
	 */
	MovsimConfigContext mvCxt;
	JMenuItem open,save,run,reset;
	boolean blocked=true;
	JCheckBoxMenuItem name,id,axis,dropRoadAtLast,showSelectedGeometry,showSelectedLane,showSpeedLimits,showLinks,showSignals,useTheme;
	private JMenuItem about;
	private JMenuItem email;
	private JMenuItem toastDelay;
	
	public void setUseTheme(JCheckBoxMenuItem useTheme) {
		this.useTheme = useTheme;
	}
	/**
	 * 
	 * @param mvCxt MovsimConfigurationContext containing reference to loaded .xprj
	 */
	public AppFrameListener(MovsimConfigContext mvCxt) {
		this.mvCxt = mvCxt;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JMenuItem mi=null;
		JCheckBoxMenuItem chsrc=null;
		if(e.getSource() instanceof JCheckBoxMenuItem)chsrc=(JCheckBoxMenuItem)e.getSource();
		if(e.getSource() instanceof JMenuItem)mi=(JMenuItem)e.getSource();
		if(mi==open){
			final File f=FileUtils.chooseFile("xprj");
			if(f==null)return;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					mvCxt.getRdCxt().getAppFrame().getJp().reset();
					MovsimScenario.setScenario(f, mvCxt);
				}
			});
		}
		else if(mi==about){
			
			ClassLoader classLoader = getClass().getClassLoader();
			//final File file = new File(classLoader.getResource("About.txt").getFile());
			StringBuilder result = new StringBuilder("");
			
			try {
			    BufferedReader in = new BufferedReader(new FileReader(classLoader.getResource("About.txt").getFile()));
			    String str;
			    while ((str = in.readLine()) != null)
			    	result.append(str).append("\n");
			    in.close();
			} catch (IOException e2) {
				
			}
			
			
			String message = result.toString();

			/*GraphicsHelper.showMessage("Vehiculat Traffic Flow Scenario Development Environment"
					+ "\nVersion 1.0"
					+ "\nThis project is submitted in Partial fulfilment of the requirements for the degree of"
					+ "\nMasters of Science "
					+ "in Information Technology"
					+ " by"
					+ "\nShmeel Ahmad"
					+ "-MITF12M019"
					+ "\nSupervised by"
					+ ": Dr. Waqar-ul-Qounain"
					+ "\nAssistant Professor"
					+ "\nArtificial Intelligence and Multi-disciplinary Research Laboratory (AIM-RL)"
					+ "\nDate: 20 June, 2014"
					+ "\nPunjab University College of Information Technology"
					+ "\n\nshmeelahmad@gmail/hotmail/yahoo.com");*/
			GraphicsHelper.showMessage(message);
		}
		else if(mi==email){
			try {
				Desktop.getDesktop().mail(new URI("mailto:shmeelahmad@gmail.com?subject=Sent%20from%20Traffic%20simulator&body=Dear%20Shmeel%20Ahmad%20please%20send%20me%20some%20tutorials&cc=shmeelahmad@yahoo.com"));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Error: "+e1.getMessage());
			} catch (URISyntaxException e1) {
				JOptionPane.showMessageDialog(null, "Error: "+e1.getMessage());
			}
		}
		else if(mi==toastDelay){
			try{
				int duration = (int)(1000 * Double.parseDouble(GraphicsHelper.valueFromUser("Enter delay in seconds"))) ;
				while (duration < 0)
				{
					GraphicsHelper.showToast("Enter a Valid Duration", mvCxt.getRdCxt().getToastDurationMilis());
					duration = (int)(1000 * Double.parseDouble(GraphicsHelper.valueFromUser("Enter delay in seconds"))) ;
				}
				
				mvCxt.getRdCxt().setToastDurationMilis(duration);
				GraphicsHelper.showToast("Delay time changed", mvCxt.getRdCxt().getToastDurationMilis());
				
				
			
			}catch(NumberFormatException ee){
				GraphicsHelper.showToast("Enter valid number", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}
		else if(mi==save){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					File f=null;
					f=FileUtils.saveFile("xprj");
					if(f!=null){
						try {
							saveForUnity();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DataToViewerConverter.updateFractions(mvCxt);
						MovsimScenario.saveScenario(f,mvCxt);
					}
				}
			});
		}
		else if(mi==reset){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					mvCxt.getRdCxt().getAppFrame().getJp().reset();
					MovsimScenario.resetScenario(mvCxt);
				}
			});
		}
		else if(mi==run){
			new Thread(new Runnable() {
				@Override
				public void run() {
					String path = System.getProperty("java.io.tmpdir") + "TDE_History.tmp//"  + "tmp.xprj" ;
				    ClassLoader classLoader = Shortcuts.class.getClassLoader();
				    File f = new File(path);
					//File f=new File(new File("").getAbsoluteFile()+"\\tmp.xprj");
					DataToViewerConverter.updateFractions(mvCxt);
					MovsimScenario.saveScenario(f, mvCxt);
					String[]args={"-f",f.getAbsolutePath()};
					
					Locale.setDefault(Locale.US);
			        final ResourceBundle resourceBundle = ResourceBundle.getBundle(LocalizationStrings.class.getName(),Locale.getDefault());
			        LogWindow.setupLog4JAppender();
			        final ProjectMetaData projectMetaData = ProjectMetaData.getInstance();
			        Logger.initializeLogger();
			        MovsimCommandLine.parse(args);
			        Properties properties = ViewProperties.loadProperties(projectMetaData);
			        mvCxt.getRdCxt().getAppFrame().setMovsimFrame(new AppFrame(resourceBundle, projectMetaData, properties));
				}
			}).start();
		}
		if(chsrc==name){
			if(name.isSelected() && id.isSelected())id.setSelected(false);
			mvCxt.getRdCxt().getDrawingArea().setDrawRoadId(id.isSelected());
			mvCxt.getRdCxt().getDrawingArea().setDrawRoadNames(name.isSelected());
		}
		else if(chsrc==id){
			if(id.isSelected() && name.isSelected())name.setSelected(false);
			mvCxt.getRdCxt().getDrawingArea().setDrawRoadId(id.isSelected());
			mvCxt.getRdCxt().getDrawingArea().setDrawRoadNames(name.isSelected());
		}
		else if(chsrc==axis){
			mvCxt.getRdCxt().getDrawingArea().setDrawAxis(axis.isSelected());
		}
		else if(chsrc==showLinks){
			mvCxt.getRdCxt().getDrawingArea().setDrawLaneLinks(showLinks.isSelected());
		}
		else if(chsrc==showSignals){
			mvCxt.getRdCxt().getDrawingArea().setDrawSignals(showSignals.isSelected());
		}
		else if(chsrc==showSelectedGeometry){
			mvCxt.getRdCxt().getDrawingArea().setDrawSelectedGeometry(showSelectedGeometry.isSelected());
		}
		else if(chsrc==showSelectedLane){
			mvCxt.getRdCxt().getDrawingArea().setDrawSelectedLane(showSelectedLane.isSelected());
		}
		else if(chsrc==showSpeedLimits){
			mvCxt.getRdCxt().getDrawingArea().setDrawSpeedLimits(showSpeedLimits.isSelected());
		}
		/*else if(chsrc==useTheme){
			TDEResources.setUseTheme(useTheme.isSelected());
			Preferences.userRoot().node(TDEResources.class.getName()).putBoolean("useTheme", useTheme.isSelected());
			if(useTheme.isSelected()){
				//mvCxt.getRdCxt().getAppFrame().getTpnl().getColorDensity().setEnabled(true);
				//mvCxt.getRdCxt().getAppFrame().getTpnl().getChooseColor().setEnabled(true);
				TDEResources.getResources().setThemeColor(new Color(Preferences.userRoot().node(TDEResources.class.getName()).getInt("themeColor", new Color(250,250,250).getRed())));
				TDEResources.getResources().setColorDensity(Preferences.userRoot().node(TDEResources.class.getName()).getInt("colorDensity", 0));
				//mvCxt.getRdCxt().getAppFrame().getTpnl().getColorDensity().setValue(Preferences.userRoot().node(TDEResources.class.getName()).getInt("colorDensity", 0));
				mvCxt.updatePanels();
				mvCxt.getRdCxt().updatePanel();
				mvCxt.getRdCxt().getAppFrame().getStatusPnl().repaint();
			}else{
				//mvCxt.getRdCxt().getAppFrame().getTpnl().getColorDensity().setEnabled(false);
				//mvCxt.getRdCxt().getAppFrame().getTpnl().getChooseColor().setEnabled(false);
				TDEResources.getResources().resetThemeColor();
				TDEResources.getResources().setColorDensity(0);
				mvCxt.updatePanels();
				mvCxt.getRdCxt().updatePanel();
				mvCxt.getRdCxt().getAppFrame().getStatusPnl().repaint();
			}
		}*/
		mvCxt.getRdCxt().getDrawingArea().getRoadPrPnl().updateGraphics();
	
	}
	
	public void saveForUnity() throws IOException
	{
		
		
		
		FileWriter fw1 = new FileWriter("C:/cleaf/StraightRoads.txt");
        PrintWriter pw1 = new PrintWriter(fw1);
       // BufferedWriter bw = new BufferedWriter(fw1);
        
        FileWriter fw2 = new FileWriter("C:/cleaf/Arcs_LeftEdge.txt", false);
        PrintWriter pw2 = new PrintWriter(fw2);
        FileWriter fw3 = new FileWriter("C:/cleaf/Arcs_RightEdge.txt", false);
        PrintWriter pw3 = new PrintWriter(fw3);
        FileWriter fw4 = new FileWriter("C:/cleaf/Gradient.txt", false);
        PrintWriter pw4 = new PrintWriter(fw4);
        
        
        int size = mvCxt.getRdCxt().getRn().getRoadSegments().size();
        for (int i=0; i < size; i++)
        {
        		double offset=0.0;
        		
		        PosTheta posTheta, trackPosTheta;
		        double x1,y1,x2,y2, tx, ty, gx, gy, rise, run;
		        
		        RoadMapping mp = mvCxt.getRdCxt().getRn().getRoadSegments().get(i).roadMapping();
		       
		        final Class<? extends RoadMapping> roadMappingClass =mp.getClass();
		        final double roadLength = mp.roadLength();
		        
		        // forget about offset, it always gives zero
		       // offset = mp.getLaneGeometries().getLeft().getLaneCount()
		         //       * mp.getLaneGeometries().getLaneWidth();
		        
		      //Below if else condition helps me to find the left edge (x and y coordinates)
		        if (roadMappingClass == RoadMappingLine.class  )
		        {
			        posTheta = mp.startPos(offset);			        
			        x1=posTheta.x ;
			        y1=-posTheta.y;
			        
			        posTheta = mp.endPos(offset);
			        x2=posTheta.x ;
			        y2=-posTheta.y ;
			        
			        pw1.println(mvCxt.getRdCxt().getRn().getRoadSegments().get(i).userId());
			        pw1.println(mp.laneCount() );
				    pw1.println(x1);
				    pw1.println(y1);
				    pw1.println(x2);
				    pw1.println(y2);
				    //bw.write("hello");
		        }
		        
		        else if(roadMappingClass == RoadMappingArc.class)
	            {
		            final double sectionLength = 5.0;
		            double roadPos = 0.0;
		           
		            posTheta = mp.startPos(offset);
		            x1=posTheta.x;
		            y1=-posTheta.y;
		            while (roadPos < roadLength) 
		            {
		                roadPos += sectionLength;
		                posTheta = mp.map(Math.min(roadPos, roadLength), offset);
		                x2=posTheta.x ;
		                y2=-posTheta.y;
		               
		                pw2.println(x1);
			            pw2.println(y1);
			            pw2.println(x2);
			            pw2.println(y2); 
		               
			            x1=x2;
		                y1=y2;
		            }
		        }
		      
		       
		        //code to find x y coord of lanes
		        //this is where I find the no of lanes, left road edge is found from code above
		        
		        
		        //int maxRightLane = -mp.getLaneGeometries().getRight().getLaneCount();
		        //int maxLeftLane = mp.getLaneGeometries().getLeft().getLaneCount();
		        
		        int LaneCount = -mp.laneCount();
		        //int maxLeftLane = mp.laneCount();
		        
		        
		        
		        
		        // For the middle lanes/edges
		        //for (int lane = maxRightLane + 1; lane < maxLeftLane; lane++) 
		        for (int lane = 0 - 1; lane > LaneCount; lane--)
		        {
		            offset = lane * mp.laneWidth();
		        
		        	posTheta = mp.startPos(offset);
				        
			        x1=posTheta.x;
			        y1=-posTheta.y;
			        
			        posTheta = mp.endPos(offset);
			        
			        x2=posTheta.x;
			        y2=-posTheta.y;
			    
			        pw1.println(x1);
		            pw1.println(y1);
		            pw1.println(x2);
		            pw1.println(y2);
		       }
		       
	            // edge of most outer edge
		        // this code helps me find the x y coordinates of right most edge of the road
		        
		        
		        //offset = mp.getMaxOffsetRight();
		        
		        
		        
		        offset = -mp.laneCount() * mp.laneWidth();
//		                * mp.getLaneGeometries().getLaneWidth();
		        if (roadMappingClass == RoadMappingLine.class  )
		        {
			        posTheta = mp.startPos(offset);
			        
			        x1=posTheta.x;
			        y1=-posTheta.y;
			        
			        posTheta = mp.endPos(offset);
			        
			        x2=posTheta.x;
			        y2=-posTheta.y;
			        
			        pw1.println("Right edge");
			        pw1.println(x1);
		            pw1.println(y1);
		            pw1.println(x2);
		            pw1.println(y2);
		        }
		       
	            else if(roadMappingClass == RoadMappingArc.class)
	            {
		            final double sectionLength = 5.0;
		            double roadPos = 0.0;
		            
		            posTheta = mp.startPos(offset);
		            x1=posTheta.x;
		            y1=-posTheta.y;
		            while (roadPos < roadLength) 
		            {
		                roadPos += sectionLength;
		                posTheta = mp.map(Math.min(roadPos, roadLength), offset);
		                x2=posTheta.x;
		                y2=-posTheta.y;
		               
		                pw3.println(x1);
			            pw3.println(y1);
			            pw3.println(x2);
			            pw3.println(y2);
			            x1=x2;
		                y1=y2;
		            }
		            pw3.println("end of arc");
	            }
		        
		        //setVisible(true);
        }
        pw1.close();
        fw1.close();
        pw2.close();
        fw2.close();
        pw3.close();
        fw3.close();
        pw4.close();
        fw4.close();
        
        
	}
	
	public void setEmail(JMenuItem email) {
		this.email = email;
	}
	public void setOpen(JMenuItem open) {
		this.open = open;
	}
	public void setSave(JMenuItem save) {
		this.save = save;
	}
	public void setReset(JMenuItem reset) {
		this.reset = reset;
	}
	public void setRun(JMenuItem run) {
		this.run = run;
	}
	public void setAbout(JMenuItem about) {
		this.about = about;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public void setName(JCheckBoxMenuItem name) {
		this.name = name;
	}
	public void setId(JCheckBoxMenuItem id) {
		this.id = id;
	}
	public void setAxis(JCheckBoxMenuItem axis) {
		this.axis = axis;
	}
	public void setDropRoadAtLast(JCheckBoxMenuItem dropRoadAtLast) {
		this.dropRoadAtLast = dropRoadAtLast;
	}
	public void setShowSelectedGeometry(JCheckBoxMenuItem showSelectedGeometry) {
		this.showSelectedGeometry = showSelectedGeometry;
	}
	public void setShowSelectedLane(JCheckBoxMenuItem showSelectedLane) {
		this.showSelectedLane = showSelectedLane;
	}
	public void setShowSpeedLimits(JCheckBoxMenuItem showSpeedLimits) {
		this.showSpeedLimits = showSpeedLimits;
	}
	public void setShowLinks(JCheckBoxMenuItem showLinks) {
		this.showLinks = showLinks;
	}
	public void setShowSignals(JCheckBoxMenuItem showSignals) {
		this.showSignals = showSignals;
	}
	public void setChangeToastDelay(JMenuItem mntmChangeToastDelay) {
		toastDelay=mntmChangeToastDelay;
	}

}
