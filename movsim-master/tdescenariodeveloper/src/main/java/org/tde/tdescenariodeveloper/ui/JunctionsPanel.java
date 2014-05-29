package org.tde.tdescenariodeveloper.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.movsim.network.autogen.opendrive.Lane;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Junction.Connection.LaneLink;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link.Predecessor;
import org.movsim.network.autogen.opendrive.OpenDRIVE.Road.Link.Successor;
import org.movsim.simulator.roadnetwork.RoadSegment;
import org.tde.tdescenariodeveloper.eventhandling.ConnectionListener;
import org.tde.tdescenariodeveloper.eventhandling.JunctionsListener;
import org.tde.tdescenariodeveloper.eventhandling.LaneLinkListener;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;

public class JunctionsPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8372150658854713922L;
	JScrollPane sp;
	private JComboBox<String> cbSelectJunc;
	GridBagConstraints c,gbc_lbl,gbc_tf;
	JPanel linkInfoPnl;
	RoadContext rdCxt;
	String selectedJn="";
	JButton add,remove,addCn;
	ImageIcon rem,addIcon;
	public JunctionsPanel(RoadContext rpp, JunctionsListener jl) {
		rdCxt=rpp;
		rem=new ImageIcon(getClass().getClassLoader().getResource("del.png"));
		rem.setImage(rem.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		addIcon=new ImageIcon(getClass().getClassLoader().getResource("add.png"));
		addIcon.setImage(addIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		add=new JButton("New junction");
		remove=new JButton("Remove");
		addCn=new JButton("New connection");
		add.addActionListener(jl);
		remove.addActionListener(jl);
		addCn.addActionListener(jl);
		
		sp=new JScrollPane();
		sp.setPreferredSize(new Dimension(300,700));
		sp.getViewport().add(this);
		linkInfoPnl=new JPanel(new GridBagLayout());
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1, false), new EmptyBorder(10, 5, 10, 5)) , "Junctions", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		cbSelectJunc = new JComboBox<>();
		cbSelectJunc.addActionListener(jl);
		
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
		gbc_tf.anchor=GridBagConstraints.NORTHWEST;
		gbc_tf.weightx=3;
		gbc_tf.gridwidth=GridBagConstraints.REMAINDER;
		
		
		
		JLabel lbl = new JLabel("Select junction");
		lbl.setLabelFor(cbSelectJunc);
		add(lbl,gbc_lbl);
		add(cbSelectJunc,gbc_tf);
		add(add,gbc_lbl);
		add(remove,gbc_tf);
		add(linkInfoPnl,c);
		add(addCn,c);
		c.gridheight=GridBagConstraints.REMAINDER;
		c.weighty=1;
		add(new JLabel(),c);
		c.gridheight=1;
		c.weighty=0.0;
		remove.setEnabled(false);
		addCn.setEnabled(false);
		sp.revalidate();
	
	}
	public ArrayList<RoadSegment> getJunctionRoadSegments(String key){
		if(rdCxt.getRn().getOdrNetwork().getJunction()==null)return null;
		ArrayList<RoadSegment>rs=new ArrayList<RoadSegment>();
		Junction j=getJunction(selectedJn);
		for(Connection cn:j.getConnection()){
			RoadSegment r;
			if(key.equals("all") || key.equals("connecting")){
				r=rdCxt.getRn().findByUserId(cn.getConnectingRoad());
				if(r!=null)LaneLinkPanel.putOrReject(rs, r);
			}
			if(key.equals("all") || key.equals("incoming")){
				r=rdCxt.getRn().findByUserId(cn.getIncomingRoad());
				if(r!=null)LaneLinkPanel.putOrReject(rs, r);
			}
		}
		return rs;
	}
	public Junction getJunction(String id){
		for(Junction j:rdCxt.getRn().getOdrNetwork().getJunction()){
			if(id.equals(j.getId()))return j;
		}
		return null;
	}
	public void updateJunction(){
		cbSelectJunc.removeAll();
		for(Junction j:rdCxt.getRn().getOdrNetwork().getJunction()){
			cbSelectJunc.addItem(j.getId());
		}
		if(rdCxt.getRn().getOdrNetwork().getJunction().size()<1){
			remove.setEnabled(false);
			addCn.setEnabled(false);
		}
		else{
			remove.setEnabled(true);
			addCn.setEnabled(true);
		}
		selectedJn=(String)cbSelectJunc.getSelectedItem();
		updateJunctionPanel(getJunction(selectedJn));
	}
	public void updateJunctionPanel(Junction jn) {
		//set tool tips
		remove.setToolTipText("Remove junction: "+selectedJn);
		
		
		
		linkInfoPnl.removeAll();
		ArrayList<RoadSegment>all=getJunctionRoadSegments("all");
		String[]allRd=new String[all.size()];
		for(int i=0;i<allRd.length;i++){
			allRd[i]=all.get(i).userId();
		}
		
//		JPanel lables=new JPanel(new GridBagLayout());
//		JLabel lbl=new JLabel(" id");
//		lbl.setToolTipText("id of the junction");
//		lables.add(lbl,gbc_lbl);
//		
//
//		lbl=new JLabel(" In. Rd");
//		lbl.setToolTipText("Id of incoming road (Predecessor of connecting road)");
//		lables.add(lbl,gbc_lbl);
//
//		lbl=new JLabel(" con. Rd");
//		lbl.setToolTipText("Id of connecting road (Successor of incoming road)");
//		lables.add(lbl,gbc_lbl);
//		
//		lbl=new JLabel(" Contact point");
//		lables.add(lbl,gbc_tf);
//		
//		
//		c.insets=new Insets(3,3,3,3);
//		linkInfoPnl.add(lables,c);
		for(Connection cn:jn.getConnection())
			linkInfoPnl.add(conToPnl(cn,allRd),c);
		revalidate();
	}
	
	private JPanel conToPnl(Connection cn,String[]allRd){
		JPanel p=new JPanel(new GridBagLayout());
		p.setBorder(new CompoundBorder(new LineBorder(Color.GRAY,1,true),new EmptyBorder(new Insets(10, 8, 10, 8))));
		JLabel id=new JLabel("Connection id: "+cn.getId());
		id.setFont(new Font("Serif",Font.BOLD,12));
		id.setToolTipText("Id of the connection");
		
		JComboBox<String>connecting=new JComboBox<String>(allRd);
		JComboBox<String>incoming=new JComboBox<String>(allRd);
		JComboBox<String>contactPnt=new JComboBox<String>(new String[]{"start","end"});
		connecting.setSelectedItem(cn.getConnectingRoad());
		incoming.setSelectedItem(cn.getIncomingRoad());
		contactPnt.setSelectedItem(cn.getContactPoint());

		JButton removeCn=new JButton("Remove connection");
		removeCn.setToolTipText("Remove connection: "+cn.getId());
		JButton addlnlnk=new JButton("New lane link",addIcon);
		ConnectionListener cl=new ConnectionListener(rdCxt,connecting,incoming,contactPnt,cn,removeCn,addlnlnk);
		removeCn.addActionListener(cl);
		addlnlnk.addActionListener(cl);
		connecting.addActionListener(cl);
		incoming.addActionListener(cl);
		contactPnt.addActionListener(cl);
		cl.setBlocked(false);
		
		
		GridBagConstraints gbc_lbl=new GridBagConstraints();
		gbc_lbl.weightx=1;
		gbc_lbl.fill=GridBagConstraints.BOTH;
		gbc_lbl.insets=new Insets(2, 2, 2, 2);
		gbc_lbl.anchor=GridBagConstraints.NORTHWEST;
		
		
		p.add(id,c);
		p.add(removeCn,c);
		
		JLabel lb=new JLabel("Con. Rd");
		lb.setToolTipText("Connecting road");
		lb.setLabelFor(connecting);
		p.add(lb,gbc_lbl);
		gbc_lbl.gridwidth=GridBagConstraints.REMAINDER;
		p.add(connecting,gbc_lbl);
		gbc_lbl.gridwidth=1;
		
		lb=new JLabel("In. Rd");
		lb.setToolTipText("Incoming road");
		lb.setLabelFor(incoming);
		p.add(lb,gbc_lbl);
		gbc_lbl.gridwidth=GridBagConstraints.REMAINDER;
		p.add(incoming,gbc_lbl);
		gbc_lbl.gridwidth=1;
		
		lb=new JLabel("Cont. point");
		lb.setToolTipText("Contact point");
		lb.setLabelFor(contactPnt);
		p.add(lb,gbc_lbl);
		gbc_lbl.gridwidth=GridBagConstraints.REMAINDER;
		p.add(contactPnt,gbc_lbl);
		gbc_lbl.gridwidth=1;
		
		Road toRoad=rdCxt.getRn().findByUserId(cn.getConnectingRoad()).getOdrRoad();
		Road fromRoad=rdCxt.getRn().findByUserId(cn.getIncomingRoad()).getOdrRoad();
		if(toRoad==null || fromRoad==null){
			GraphicsHelper.showMessage("Referenced connecting road "+cn.getConnectingRoad()+" or incoming road "+cn.getIncomingRoad()+" in connection "+cn.getId()+" are not configured properly");
		}
		boolean toRdPredJunc=isPredecessorJunction(toRoad);
		JPanel p2=new JPanel(new GridBagLayout());
		p2.add(new JLabel("From"),gbc_lbl);
		p2.add(new JLabel("To"),gbc_lbl);
		gbc_lbl.gridwidth=GridBagConstraints.REMAINDER;
		p2.add(new JLabel(),gbc_lbl);
		for(LaneLink ll:cn.getLaneLink()){
			JComboBox<String>from=new JComboBox<String>();
			JComboBox<String>to=new JComboBox<String>();
			for(Lane ln:toRoad.getLanes().getLaneSection().get(0).getRight().getLane()){
				if(toRdPredJunc)to.addItem(ln.getId()+"");
				else from.addItem(ln.getId()+"");
			}
			for(Lane ln:fromRoad.getLanes().getLaneSection().get(0).getRight().getLane()){
				if(toRdPredJunc)from.addItem(ln.getId()+"");
				else to.addItem(ln.getId()+"");
			}
			to.setSelectedItem(ll.getTo()+"");
			from.setSelectedItem(ll.getFrom()+"");
			JButton rmLnLnk=new JButton(rem);
			LaneLinkListener lnLinkLis=new LaneLinkListener(rdCxt, from, to, ll,rmLnLnk,cn);
			rmLnLnk.addActionListener(lnLinkLis);
			to.addActionListener(lnLinkLis);
			from.addActionListener(lnLinkLis);
			lnLinkLis.setBlocked(false);
			gbc_lbl.gridwidth=1;
			p2.add(from,gbc_lbl);
			p2.add(to,gbc_lbl);
			gbc_lbl.gridwidth=GridBagConstraints.REMAINDER;
			p2.add(rmLnLnk,gbc_lbl);
		}
		p2.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),new EmptyBorder(new Insets(10, 8, 10, 8))));
		p2.add(addlnlnk,c);
		p.add(p2,c);
		return p;
	}
	public static boolean isPredecessorJunction(Road toRoad) throws IllegalArgumentException{
		boolean predJun=false;
		boolean sucJun=false;
		Predecessor pr=null;
		Successor sr=null;
		if(toRoad.getLink().getPredecessor()!=null){
			pr=toRoad.getLink().getPredecessor();
			predJun=pr.getElementType().equals("junction");
		}
		if(toRoad.getLink().getSuccessor()!=null){
			sr=toRoad.getLink().getSuccessor();
			sucJun=sr.getElementType().equals("junction");
		}
		if(predJun && sucJun)throw new IllegalArgumentException("connecting road can't have both predecessor and successor junctions");
		if(!predJun && !sucJun)throw new IllegalArgumentException(toRoad.getId()+" connecting road is not connected to any junction");
		return predJun;
	}
	public JScrollPane getSp() {
		return sp;
	}
	public String getSelectedJn() {
		return selectedJn;
	}
	public void setSelectedJn(String selectedJn) {
		this.selectedJn = selectedJn;
	}
	public JComboBox<String> getCbSelectJunc() {
		return cbSelectJunc;
	}
	public JButton getAdd() {
		return add;
	}
	public JButton getRemove() {
		return remove;
	}
	public JButton getAddCn() {
		return addCn;
	}
}