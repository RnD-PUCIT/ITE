package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link;
import org.tde.tdescenariodeveloper.eventhandling.LanesPanelListener;
import org.tde.tdescenariodeveloper.eventhandling.LinkPanelListener;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
/**
 * Class used to show {@link Link} information of the selected {@link Road}
 * @author Shmeel
 * @see Road
 * @see Link
 * @see LinkPanelListener
 */
public class LinkPanel extends JPanel {
	private static final long serialVersionUID = 76435681L;
	private JComboBox<String> cbElementType;
	private JLabel cbElementId;
	private JLabel cbContactPoint;
	private JComboBox<String> cbSelectLink;
	GridBagConstraints c,gbc_lbl,gbc_tf;
	RoadContext rdCxt;
	LinkPanelListener lpl;
	/**
	 * 
	 * @param rpp contains reference to loaded .xodr file and other panels added to it
	 */
	public LinkPanel(RoadContext rpp) {
		rdCxt=rpp;
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Link", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		cbSelectLink = new JComboBox<>();
		cbSelectLink.setToolTipText("Predecessor/Sucsessor road");
		cbSelectLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateLinkPanel();
			}
		});
		c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTHWEST;
		c.fill=GridBagConstraints.BOTH;
		gbc_lbl = new GridBagConstraints();
		Insets ins=new Insets(5,5,5,5);
		gbc_lbl.insets = ins;
		gbc_lbl.weightx=2;
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		
		gbc_tf = new GridBagConstraints();
		gbc_tf.insets = ins;
		gbc_tf.fill = GridBagConstraints.BOTH;
		gbc_tf.weightx=3;
		gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
		
		LinkPanelListener lp=new LinkPanelListener(rpp, this);
		lpl=lp;
		
		add(cbSelectLink,c);
		JLabel lblElementId = new JLabel("Element id");
		add(lblElementId,gbc_lbl);
		cbElementId = new JLabel("Id");
		lblElementId.setLabelFor(cbElementId);
		cbElementId.setToolTipText("Id of predecessor/successor road");
		cbElementId.setRequestFocusEnabled(false);
		cbElementId.setFocusable(false);
		add(cbElementId,gbc_tf);
		JLabel lblType = new JLabel("Element type");
		add(lblType,gbc_lbl);
		cbElementType = new JComboBox<>();
		cbElementType.addActionListener(lp);
		lblType.setLabelFor(cbElementType);
		add(cbElementType,gbc_tf);
		JLabel lblContactPoint = new JLabel("Contact Point");
		add(lblContactPoint,gbc_lbl);
		cbContactPoint = new JLabel();
		cbContactPoint.setToolTipText("Where the road should be attached");
		add(cbContactPoint,gbc_tf);
		lp.setBlocked(false);
	}
	/**
	 * used to udpate {@link LinkPanel}
	 */
	public void updateLinkPanel() {
		if(rdCxt.getSelectedRoad()==null){
			return;
		}
		cbSelectLink.removeAllItems();
		cbSelectLink.addItem("Predecessor");
		cbSelectLink.addItem("Successor");
		
		
		cbElementType.removeAllItems();
		cbElementType.addItem("road");
		cbElementType.addItem("junction");
		
		int curLinkSelected = cbSelectLink.getSelectedIndex();
		switch (curLinkSelected) {
		case 0:
			if (rdCxt.getSelectedRoad().getOdrRoad().getLink().getPredecessor()!=null) {
				setLinkFields("Predecessor");
				break;
			}
		case 1:
			if (rdCxt.getSelectedRoad().getOdrRoad().getLink().getSuccessor()!=null) {
				setLinkFields("Successor");
				break;
			}
		default:
			if (rdCxt.getSelectedRoad().getOdrRoad().getLink().getPredecessor()!=null)
				setLinkFields("Predecessor");
			else
				setLinkFields("Successor");
		}
	}
	private void setLinkFields(String linkType) {
		cbSelectLink.setSelectedItem(linkType);
		String id="",type="",cntPnt="";
		if(linkType.equals("Predecessor")){
			id=rdCxt.getSelectedRoad().getOdrRoad().getLink().getPredecessor().getElementId();
			type=rdCxt.getSelectedRoad().getOdrRoad().getLink().getPredecessor().getElementType();
			cntPnt=rdCxt.getSelectedRoad().getOdrRoad().getLink().getPredecessor().getContactPoint();
		}
		else{
			id=rdCxt.getSelectedRoad().getOdrRoad().getLink().getSuccessor().getElementId();
			type=rdCxt.getSelectedRoad().getOdrRoad().getLink().getSuccessor().getElementType();
			cntPnt=rdCxt.getSelectedRoad().getOdrRoad().getLink().getSuccessor().getContactPoint();
		}
		cbElementId.setText(id);
		cbElementType.setSelectedItem(type);
		cbContactPoint.setText(cntPnt);
	}
	/**
	 * resets this {@link LinkPanel}
	 */
	public void reset() {
		cbElementId.setText("");
		cbContactPoint.setText("");
		cbElementType.removeAllItems();
		cbSelectLink.removeAllItems();
	}
	public JComboBox<String> getCbSelectLink() {
		return cbSelectLink;
	}
	public JComboBox<String> getCbElementType() {
		return cbElementType;
	}
	public LinkPanelListener getListener() {
		return lpl;
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		GraphicsHelper.drawGradientBackground(g,getWidth(),getHeight());
	}
}
