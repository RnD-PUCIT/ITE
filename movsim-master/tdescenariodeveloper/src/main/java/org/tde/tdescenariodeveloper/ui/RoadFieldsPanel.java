package org.tde.tdescenariodeveloper.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.tde.tdescenariodeveloper.eventhandling.RoadFieldsPanelListener;
/**
 * Used to show {@link Road} Properties like length, name, id and junction id
 * @author Shmeel
 * @see Junction
 * @see Road
 * @see RoadContext
 */
public class RoadFieldsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5593310264102814959L;
	private JButton addRoad,removeRoad;
	private JLabel tfId;
	private JTextField tfName;
	private JLabel tfLength;
	private JComboBox<String> cbJunction;
	RoadContext rdCxt;
	/**
	 * 
	 * @param rpp contains reference to loaded .xodr file and other panels added to it
	 * @param rfl Listener for this {@link RoadFieldsPanel}
	 */
	public RoadFieldsPanel(RoadContext rpp,RoadFieldsPanelListener rfl) {
		rdCxt=rpp;
		addRoad=new JButton("New road",TDEResources.getResources().getStraightRoad());
		removeRoad=new JButton("Remove road",TDEResources.getResources().getRem());
		addRoad.addActionListener(rfl);
		removeRoad.addActionListener(rfl);
		setOpaque(false);
		setLayout(new GridBagLayout());
		Insets ins=new Insets(2,2,2,2);
		JLabel lblId = new JLabel("Id");
		GridBagConstraints gbc_lbl = new GridBagConstraints();
		gbc_lbl.insets = ins;
		gbc_lbl.weightx=2;
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		add(addRoad,gbc_lbl);
		gbc_lbl.gridwidth=GridBagConstraints.REMAINDER;
		add(removeRoad,gbc_lbl);
		gbc_lbl.gridwidth=1;
		add(lblId, gbc_lbl);
		
		tfId = new JLabel();
		lblId.setLabelFor(tfId);
		GridBagConstraints gbc_tf = new GridBagConstraints();
		gbc_tf.insets = ins;
		gbc_tf.fill = GridBagConstraints.BOTH;
		gbc_tf.weightx=3;
		gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
		add(tfId, gbc_tf);
		
		JLabel lblName = new JLabel("Name");
		add(lblName, gbc_lbl);
		tfName = new JTextField();
//		tfName.setHighlighter(null);
		tfName.getDocument().addDocumentListener(rfl);
		lblName.setLabelFor(tfName);
		add(tfName, gbc_tf);
		
		JLabel lblLength = new JLabel("Length");
		add(lblLength, gbc_lbl);
		
		tfLength = new JLabel();
		lblLength.setLabelFor(tfLength);
		add(tfLength, gbc_tf);
		
		JLabel lblJunction = new JLabel("Junction");
		add(lblJunction, gbc_lbl);
		
		cbJunction = new JComboBox<String>();
		cbJunction.addActionListener(rfl);
		lblJunction.setLabelFor(cbJunction);
		add(cbJunction, gbc_tf);
		rfl.setBlocked(false);
	}
	/**
	 * Updates properties panel if selected road is changed and update is requested
	 */
	public void updateFields() {
		if(rdCxt.getSelectedRoad()==null)return;
		tfId.setText(rdCxt.getSelectedRoad().getOdrRoad().getId());
		tfName.setText(rdCxt.getSelectedRoad().getRoadName());
		tfLength.setText(rdCxt.getSelectedRoad().getRoadLength()+"");
		String jnc=rdCxt.getSelectedRoad().getOdrRoad().getJunction();
		if(jnc.equals("-1"))jnc="None";
		String[]jncs=new String[rdCxt.getRn().getOdrNetwork().getJunction().size()];
		for(int i=0;i<jncs.length;i++){
			jncs[i]=rdCxt.getRn().getOdrNetwork().getJunction().get(i).getId();
		}
		cbJunction.removeAllItems();
		cbJunction.addItem("None");
		for(String s:jncs)cbJunction.addItem(s);
		cbJunction.setSelectedItem(jnc);
	}
	public RoadNetwork getRn() {
		return rdCxt.getRn();
	}
	public JLabel getTfId() {
		return tfId;
	}
	public JTextField getTfName() {
		return tfName;
	}
	public JComboBox<String> getCbJunction() {
		return cbJunction;
	}
	/**
	 * Resets this {@link RoadFieldsPanel}
	 */
	public void reset() {
		tfId.setText("");
		tfName.setText("");
		tfLength.setText("");
		cbJunction.removeAllItems();
	}
	public JButton getAddRoad() {
		return addRoad;
	}
	public JButton getRemoveRoad() {
		return removeRoad;
	}
}
