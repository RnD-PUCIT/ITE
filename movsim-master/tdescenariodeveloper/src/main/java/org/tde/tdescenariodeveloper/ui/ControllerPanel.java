package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
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

public class ControllerPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2395369509625914410L;
	RoadContext rdCxt;
	JPanel cntPnl;
	JButton addNew;
	ControllerPanelListener cpl;
	public ControllerPanel(RoadContext rdCxt) {
		this.rdCxt=rdCxt;
		cntPnl=new JPanel(new GridBagLayout());
		addNew=new JButton("New controller",TDEResources.getResources().getAddIcon());
		setLayout(new GridBagLayout());

		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.weighty=1;
		
		cpl=new ControllerPanelListener(rdCxt);
		addNew.addActionListener(cpl);
		add(cntPnl,c);
		c.weighty=0;
		add(addNew,c);
		cpl.setBlocked(false);
	}
	public JButton getAddNew() {
		return addNew;
	}
	public void updateControllerPanel(){
		cntPnl.removeAll();
		if(rdCxt.getRn().getOdrNetwork().isSetController() && rdCxt.getRn().getOdrNetwork().getController().size()>0)fillControllerPanel(rdCxt.getRn().getOdrNetwork().getController(),cntPnl, rdCxt);
		revalidate();
		repaint();
	}
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
			p.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Controller", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			cntPnl.add(p,c);
		}
	}
	private static JPanel controllerToPanel(Controller s, List<Controller> controllers,RoadContext rdCxt) {
		JPanel main=new JPanel(new GridBagLayout());
		JPanel signals=new JPanel(new GridBagLayout());
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
		JButton remove=new JButton("Remove this controller",TDEResources.getResources().getRem());
		remove.addActionListener(cl);
		cl.setRemove(remove);
		main.add(remove,gbc);

		gbc.gridwidth=1;
		main.add(new JLabel("Name"),gbc);
		JTextField id=new JTextField(10);
		id.getDocument().addDocumentListener(cl);
		cl.setIdtf(id);
		id.setText(s.getId());
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(id,gbc);
		main.add(signals,gbc);

		
		
		
		JButton newControl=new JButton("Add new signal to this controller",TDEResources.getResources().getAddIcon());
		newControl.addActionListener(cl);
		cl.setNewControl(newControl);
		main.add(newControl,gbc);
		
		cl.setBlocked(false);
		return main;
	}
	private static void fillSignalsPanel(Controller s, JPanel signals,
			RoadContext rdCxt) {
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTH;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.insets=new Insets(5, 3, 5, 3);
		for(Control cc:s.getControl()){
			JPanel p=signalToPanel(cc,s.getControl(), rdCxt);
			p.setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Signal", TitledBorder.LEADING, TitledBorder.TOP, null, null));			
			signals.add(p,c);
		}
	}
	private static JPanel signalToPanel(Control cc, List<Control> controls,
			RoadContext rdCxt) {
		JPanel main=new JPanel(new GridBagLayout());
		main.setBorder(new LineBorder(TDEResources.getResources().CONTROLLERS_BORDER_COLOR, 1, true));
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(2, 5, 2, 5);
		gbc.weightx=1;
		gbc.gridwidth=GridBagConstraints.REMAINDER;

		
		ControlListener cl=new ControlListener(cc,controls, rdCxt);
		JButton remove=new JButton("Remove this controller",TDEResources.getResources().getRem());
		remove.addActionListener(cl);
		cl.setRemove(remove);
		main.add(remove,gbc);

		
		gbc.gridwidth=1;
		main.add(new JLabel("Select signal"),gbc);
		JComboBox<String> cbId=new JComboBox<String>(getSignalsIds(rdCxt));
		cbId.addActionListener(cl);
		cl.setIdCb(cbId);
		cbId.setSelectedItem(cc.getSignalId());
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		main.add(cbId,gbc);
		
		cl.setBlocked(false);
		return main;
	}
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
	public ControllerPanelListener getListener() {
		return cpl;
	}
	public void reset() {
		cntPnl.removeAll();
	}
	public static ArrayList<String> getUsedSignalsIds(RoadContext rdCxt2,
			Controller controller) {
		ArrayList<String>used=new ArrayList<>();
		for(Control c:controller.getControl()){
			LaneLinkPanel.putOrReject(used, c.getSignalId());
		}
		return used;
	}
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
	public static int getControllerCount(String signalId,RoadContext rdCxt){
		int i=0;
		for(Controller c:rdCxt.getRn().getOdrNetwork().getController())
			if(isSignalIn(signalId, c))i++;
		return i;
	}
	public static boolean isSignalIn(String signalId,Controller c){
		for(Control cc:c.getControl())
			if(cc.getSignalId().equals(signalId))return true;
		return false;
	}
	public static void removeRelatedSignals(String signalId,RoadContext rdCxt){
		for(Road r:rdCxt.getRn().getOdrNetwork().getRoad()){
			if(r.isSetSignals()){
				for(Signal s:r.getSignals().getSignal()){
					if(signalId.equals(s.getId()))
						r.getSignals().getSignal().remove(s);
				}
			}
		}
	}
}
