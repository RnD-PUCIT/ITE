package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Controller.Control;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Signals.Signal;
import org.tde.tdescenariodeveloper.eventhandling.ControlListener;
import org.tde.tdescenariodeveloper.eventhandling.ControllerListener;
import org.tde.tdescenariodeveloper.eventhandling.ControllerPanelListener;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Container to hold {@link Controller}s
 * @author Shmeel
 *
 */
public class ControllerPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2395369509625914410L;
	RoadContext rdCxt;
	JPanel cntPnl;
//	JButton addNew;
	ControllerPanelListener cpl;
	/**
	 * 
	 * @param rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	public ControllerPanel(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		cntPnl=new JPanel(new GridBagLayout());
		cntPnl.setOpaque(false);
//		addNew=new JButton("New controller",TDEResources.getResources().getAddIcon());
		setLayout(new GridBagLayout());

		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.weighty=1;
		
		cpl=new ControllerPanelListener(rdCxt);
//		addNew.addActionListener(cpl);
		add(cntPnl,c);
		c.weighty=0;
//		add(addNew,c);
		cpl.setBlocked(false);
	}
//	public JButton getAddNew() {
//		return addNew;
//	}
	/**
	 * Reloads controller panel from from {@link Controller}
	 */
	public void updateControllerPanel(){
		cntPnl.removeAll();
		if(rdCxt.getRn().getOdrNetwork().isSetController() && rdCxt.getRn().getOdrNetwork().getController().size()>0)fillControllerPanel(rdCxt.getRn().getOdrNetwork().getController(),cntPnl, rdCxt);
		revalidate();
		repaint();
	}
	/**
	 * Fills panel with all controllers
	 * @param controllers {@link Controller}s
	 * @param cntPnl {@link JPanel} to be filled
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	private static void fillControllerPanel(List<Controller>controllers, JPanel cntPnl,RoadContext rdCxt) {
		cntPnl.removeAll();
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.insets=new Insets(5, 3, 5, 3);
		for(Controller s:controllers){
			JPanel p=controllerToPanel(s,controllers, rdCxt);
			p.setOpaque(false);
			p.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Controller", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			cntPnl.add(p,c);
		}
	}
	/**
	 * Converts {@link Controller} to {@link JPanel}
	 * @param s {@link Controller}
	 * @param controllers {@link List} of {@link Controller}s
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 * @return {@link JPanel} 
	 */
	private static JPanel controllerToPanel(Controller s, List<Controller> controllers,RoadContext rdCxt) {
		JPanel main=new JPanel(new GridBagLayout());
		main.setOpaque(false);
		JPanel signals=new JPanel(new GridBagLayout());
		signals.setOpaque(false);
		signals.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Signals", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		fillSignalsPanel(s,signals,rdCxt);
		main.setBorder(new LineBorder(TDEResources.getResources().CONTROLLERS_BORDER_COLOR, 1, true));
		signals.setBorder(new LineBorder(TDEResources.getResources().CONTROLLERS_BORDER_COLOR, 1, true));
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 5, 2, 5);
		gbc.weightx=1;
		gbc.gridwidth=GridBagConstraints.REMAINDER;

		ControllerListener cl=new ControllerListener(s, controllers, rdCxt);
//		JButton remove=new JButton("Remove this controller",TDEResources.getResources().getRem());
//		remove.addActionListener(cl);
//		cl.setRemove(remove);
//		main.add(remove,gbc);

		gbc.gridwidth=1;
		main.add(new JLabel("Name"),gbc);
		JTextField id=new JTextField(10);
		id.getDocument().addDocumentListener(cl);
		cl.setIdtf(id);
		id.setText(s.getId());
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(id,gbc);
		main.add(signals,gbc);

//		JButton newControl=new JButton("Add new signal to this controller",TDEResources.getResources().getAddIcon());
//		newControl.addActionListener(cl);
//		cl.setNewControl(newControl);
//		main.add(newControl,gbc);
		
		cl.setBlocked(false);
		return main;
	}
	/**
	 * fills {@link JPanel} with data of {@link Controller}
	 * @param s {@link Controller} of which {@link Control}s are to be converted
	 * @param signals {@link JPanel} to be filled
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	private static void fillSignalsPanel(Controller s, JPanel signals,
			RoadContext rdCxt) {
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.insets=new Insets(5, 3, 5, 3);
		for(Control cc:s.getControl()){
			JPanel p=signalToPanel(cc,s, rdCxt);
			p.setOpaque(false);
			p.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Signal", TitledBorder.LEADING, TitledBorder.TOP, null, null));			
			signals.add(p,c);
		}
	}
	/**
	 * Converts {@link Control} to {@link JPanel}
	 * @param cc {@link Control} to be converted
	 * @param controller {@link Controller} containing control referred above
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 * @return {@link JPanel}
	 */
	private static JPanel signalToPanel(Control cc, Controller controller,
			RoadContext rdCxt) {
		JPanel main=new JPanel(new GridBagLayout());
		main.setOpaque(false);
		main.setBorder(new LineBorder(TDEResources.getResources().CONTROLLERS_BORDER_COLOR, 1, true));
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 5, 2, 5);
		gbc.weightx=1;
		gbc.gridwidth=GridBagConstraints.REMAINDER;

//		
		ControlListener cl=new ControlListener(cc,controller, rdCxt);
//		JButton remove=new JButton("Remove this controller",TDEResources.getResources().getRem());
//		remove.addActionListener(cl);
//		cl.setRemove(remove);
//		main.add(remove,gbc);

//		
//		gbc.gridwidth=1;
//		main.add(new JLabel("Select signal"),gbc);
//		JComboBox<String> cbId=new JComboBox<String>(getSignalsIds(rdCxt));
//		cbId.addActionListener(cl);
//		cl.setIdCb(cbId);
//		cbId.setSelectedItem(cc.getSignalId());
//		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(new JLabel(cc.getSignalId()),gbc);
//		
		gbc.gridwidth=1;
		main.add(new JLabel("Move to controller"),gbc);
		JComboBox<String> move=new JComboBox<String>(getOtherControllerIds(rdCxt, controller));
		move.addActionListener(cl);
		cl.setMove(move);
		move.setSelectedItem("This");
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(move,gbc);
		
		cl.setBlocked(false);
		return main;
	}
	/**
	 * used to get ids of all signals
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 * @return return array of signal ids
	 */
	public static String[] getSignalsIds(RoadContext rdCxt){
		String[]s;
		HashSet<String>nms=new HashSet<>();
		nms.add("None");
		for(Road r:rdCxt.getRn().getOdrNetwork().getRoad()){
			if(r.isSetSignals()){
				for(Signal sig:r.getSignals().getSignal()){
					nms.add(sig.getId());
				}
			}
		}
		s=new String[nms.size()];
		int i=0;
		for(String m:nms){
			s[i++]=m;
		}
		return s;
	}
	/**
	 * used to get {@link Controller} ids other than ctr referred below
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 * @param ctr {@link Controller} its id is not included in the {@link Array} 
	 * @return {@link Array} containing ids
	 */
	public static String[] getOtherControllerIds(RoadContext rdCxt,Controller ctr){
		String[]s;
		HashSet<String>nms=new HashSet<>();
		nms.add("This");
		for(Controller r:rdCxt.getRn().getOdrNetwork().getController()){
			if(!ctr.getId().equals(r.getId()))nms.add(r.getId());
		}
		nms.add("New controller");
		s=new String[nms.size()];
		int i=0;
		for(String m:nms){
			s[i++]=m;
		}
		return s;
	}
	public ControllerPanelListener getListener() {
		return cpl;
	}
	public void reset() {
		cntPnl.removeAll();
	}
	/**
	 * used to get all ids within a {@link Controller} 
	 * @param rdCxt2 rdCxt contains reference to loaded .xodr file and other panels added to it
	 * @param controller {@link Controller} of which ids are included
	 * @return {@link Array} of signal ids of {@link Controller}
	 */
	public static ArrayList<String> getUsedSignalsIds(RoadContext rdCxt2,
			Controller controller) {
		ArrayList<String>used=new ArrayList<>();
		for(Control c:controller.getControl()){
			LaneLinkPanel.putOrReject(used, c.getSignalId());
		}
		return used;
	}
	/**
	 * Used to get id which is not yet referred by any {@link Control}
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 * @param cont {@link Controller} to be considered
	 * @return returns id which is not used in given {@link Controller}
	 */
	public static String getNotUsedSignalId(RoadContext rdCxt,Controller cont){
		String[]nms=getSignalsIds(rdCxt);
		ArrayList<String>used=getUsedSignalsIds(rdCxt, cont);
		for(String s:nms){
			if(!s.equals("None") && !used.contains(s)){
				return s;
			}
		}
		return null;
	}
	/**
	 * Used to get no of {@link Controller}s of a given signal
	 * @param signalId id of signal
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 * @return count of controller attached to given signal
	 */
	public static int getControllerCount(String signalId,RoadContext rdCxt){
		int i=0;
		for(Controller c:rdCxt.getRn().getOdrNetwork().getController())
			if(isSignalIn(signalId, c))i++;
		return i;
	}
	/**
	 * Used to check if given {@link Signal} is in given {@link Controller}
	 * @param signalId id of {@link Signal}
	 * @param c {@link Controller} to be checked in
	 * @return true if {@link Signal} is controlled by given {@link Controller}
	 */
	public static boolean isSignalIn(String signalId,Controller c){
		for(Control cc:c.getControl())
			if(cc.getSignalId().equals(signalId))return true;
		return false;
	}
	/**
	 * removes all {@link Signal}s referred by given id 
	 * @param signalId id of {@link Signal} to be removed
	 * @param rdCxt rdCxt contains reference to loaded .xodr file and other panels added to it
	 */
	public static void removeRelatedSignals(String signalId,RoadContext rdCxt){
		for(Road r:rdCxt.getRn().getOdrNetwork().getRoad()){
			if(r.isSetSignals()){
				HashSet<Signal>sgnls=new HashSet<>();
				for(Signal s:r.getSignals().getSignal()){
					if(signalId.equals(s.getId()))
						sgnls.add(s);
				}
				r.getSignals().getSignal().removeAll(sgnls);
				if(r.getSignals().getSignal().size()<1)r.setSignals(null);
			}
		}
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
