package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection.LaneLink;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.simulator.roadnetwork.RoadNetwork;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.exception.NotFoundException;
import org.tde.tdescenariodevelopment.utils.GraphicsHelper;

public class JunctionsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8372150658854713922L;
	RoadNetwork rn;
	JScrollPane sp;
	private JComboBox<String> cbSelectJunc;
	GridBagConstraints c,gbc_lbl,gbc_tf;
	JPanel linkInfoPnl;
	public JunctionsPanel(RoadNetwork rn) {
		this.rn=rn;
		sp=new JScrollPane();
		sp.setPreferredSize(new Dimension(500,150));
		sp.getViewport().add(this);
		linkInfoPnl=new JPanel(new GridBagLayout());
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder(new LineBorder(new Color(150, 150, 150), 1, false), "Lane link", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		
		cbSelectJunc = new JComboBox<>();
		cbSelectJunc.setMaximumRowCount(4);
		cbSelectJunc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String slct=(String)cbSelectJunc.getSelectedItem();
				try {
					setPanel(slct);
				} catch (NotFoundException e) {
					GraphicsHelper.showToast(e.toString(), 3000);
				}
			}
		});
		
		c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.NORTHWEST;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
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
		
		
		
		JLabel lbl = new JLabel("Select junction (id)");
		lbl.setLabelFor(cbSelectJunc);
		add(lbl,gbc_lbl);
		add(cbSelectJunc,gbc_tf);
		add(linkInfoPnl,c);
		sp.revalidate();
	
	}
	private void setPanel(String slct) throws NotFoundException {
		boolean found=false;
		for(Junction j:rn.getOdrNetwork().getJunction()){
			if(j.getId().equals(slct)){
				found=true;
				updateJunctionPanel(j);
				break;
			}
		}
		if(!found){
			throw new NotFoundException("Junction not found");
		}
	}
	public void updateJunction(){
		cbSelectJunc.removeAll();
		for(Junction j:rn.getOdrNetwork().getJunction()){
			cbSelectJunc.addItem(j.getId());
		}
	}
	private void updateJunctionPanel(Junction jn) {
		linkInfoPnl.removeAll();
		ArrayList<String>conn=new ArrayList<>();
		ArrayList<String>incom=new ArrayList<>();
		for(Connection cn:jn.getConnection()){
			LaneLinkPanel.putOrReject(conn, cn.getConnectingRoad());
			LaneLinkPanel.putOrReject(incom, cn.getIncomingRoad());
		}
		String[]connecting=new String[conn.size()];
		String[]incoming=new String[incom.size()];
		for(int i=0;i<conn.size();i++){
			connecting[i]=conn.get(i);
		}
		for(int i=0;i<incom.size();i++){
			incoming[i]=incom.get(i);
		}
		JPanel lables=new JPanel(new GridLayout(1,4,2,2));
		lables.add(new JLabel(" Id"));
		lables.add(new JLabel(" Connecting road"));
		lables.add(new JLabel(" Incoming road"));
		lables.add(new JLabel(" Contact point"));
		c.insets=new Insets(3,3,3,3);
		linkInfoPnl.add(new JLabel("Junction: "+jn.getId()),c);
		linkInfoPnl.add(lables,c);
		for(Connection cn:jn.getConnection())
			linkInfoPnl.add(conToPnl(cn, connecting, incoming),c);
		sp.revalidate();
	}
	
	private JPanel conToPnl(Connection cn,String[]conn,String[]incom){
		JPanel p=new JPanel(new GridBagLayout());
		p.setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
		JTextField id=new JTextField(5);
		id.setText(cn.getId());
		
		JComboBox<String>connecting=new JComboBox<String>(conn);
		JComboBox<String>incoming=new JComboBox<String>(incom);
		JComboBox<String>contactPnt=new JComboBox<String>(new String[]{"start","end"});
		
		connecting.setSelectedItem(cn.getConnectingRoad());
		incoming.setSelectedItem(cn.getIncomingRoad());
		contactPnt.setSelectedItem(cn.getContactPoint());
		GridBagConstraints gbc_lbl=new GridBagConstraints();
		gbc_lbl.weightx=1;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		gbc_lbl.insets=new Insets(2, 2, 2, 2);
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		p.add(id,gbc_lbl);
		p.add(connecting,gbc_lbl);
		p.add(incoming,gbc_lbl);
		gbc_lbl.gridwidth=GridBagConstraints.REMAINDER;
		p.add(contactPnt,gbc_lbl);
		Road toRoad=getRoad(Integer.parseInt(cn.getConnectingRoad()));
		Road fromRoad=getRoad(Integer.parseInt(cn.getIncomingRoad()));
		for(LaneLink ll:cn.getLaneLink()){
			JComboBox<String>from=new JComboBox<String>();
			JComboBox<String>to=new JComboBox<String>();
			for(Lane ln:toRoad.getLanes().getLaneSection().get(0).getRight().getLane()){
				to.addItem(ln.getId()+"");
			}
			to.setSelectedItem(ll.getTo()+"");
			for(Lane ln:fromRoad.getLanes().getLaneSection().get(0).getRight().getLane()){
				from.addItem(ln.getId()+"");
			}
			from.setSelectedItem(ll.getFrom()+"");
			
			gbc_lbl.gridwidth=1;
			p.add(new JLabel("From"),gbc_lbl);

			p.add(from,gbc_lbl);
			p.add(new JLabel("To"),gbc_lbl);
			
			gbc_lbl.gridwidth=GridBagConstraints.REMAINDER;
			p.add(to,gbc_lbl);
		}
		return p;
	}
	private Road getRoad(int id){
		for(RoadSegment rs:rn){
			if(id==Integer.parseInt(rs.getOdrRoad().getId())){
				return rs.getOdrRoad();
			}
		}
		return null;
	}
	public JScrollPane getSp() {
		return sp;
	}
}
