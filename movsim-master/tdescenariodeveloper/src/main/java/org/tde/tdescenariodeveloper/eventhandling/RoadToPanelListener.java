package org.tde.tdescenariodeveloper.eventhandling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.movsim.autogen.Inflow;
import org.movsim.autogen.Road;
import org.movsim.autogen.TrafficComposition;
import org.movsim.autogen.VehicleType;
import org.tde.tdescenariodeveloper.ui.MovsimConfigContext;
import org.tde.tdescenariodeveloper.updation.Conditions;
import org.tde.tdescenariodeveloper.updation.DataToViewerConverter;
import org.tde.tdescenariodeveloper.utils.GraphicsHelper;
import org.tde.tdescenariodeveloper.utils.MovsimScenario;

public class RoadToPanelListener implements ActionListener, ItemListener,Blockable{
	boolean blocked=true;
	private MovsimConfigContext mvCxt;
	JComboBox<String>id;
	private JButton remove;
	private JCheckBox logging;
	private JButton remTrafficComp;
	private JButton addTrafficComp;
	private JButton remTrafficSrc;
	private JButton addTrafficSrc;
	private JButton addinflow;
	Road road;
	List<Road>rdList;
	private JButton newType;
	public RoadToPanelListener(MovsimConfigContext mvCxt2,Road r,List<Road>rdList) {
		this.mvCxt=mvCxt2;
		road=r;
		this.rdList=rdList;
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(blocked)return;
		JCheckBox src=(JCheckBox)e.getSource();
		if(src==logging)
			road.setLogging(logging.isSelected());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(blocked)return;
		JButton srcBtn=null;
		if(e.getSource() instanceof JButton)srcBtn=(JButton)e.getSource();
		JComboBox<String>srcCb=null;
		String s=null;
		if(e.getSource() instanceof JComboBox<?>){
			srcCb=(JComboBox<String>)e.getSource();
			s=(String)srcCb.getSelectedItem();
		}
		if(srcBtn==remove){
			if(rdList.remove(road))
				mvCxt.updatePanels();
			else
				GraphicsHelper.showToast("Road "+road.getId()+" couldn't be deleted", mvCxt.getRdCxt().getToastDurationMilis());
		}else if(srcCb==id){
			if(!s.equals(road.getId())){
				if(Conditions.existsIdInRoads(s, mvCxt)){
					if(!DataToViewerConverter.getUsedRoadIds(rdList).contains(s)){
						road.setId(s);
						mvCxt.updatePanels();
					}
					else {
						GraphicsHelper.showToast(s+" road is already customized", mvCxt.getRdCxt().getToastDurationMilis());
						id.setSelectedItem(road.getId());
					}
				}
				else GraphicsHelper.showToast("Road +"+s+" doesn't exist", mvCxt.getRdCxt().getToastDurationMilis());
			}
		}else if(srcBtn==remTrafficComp){
			road.setTrafficComposition(null);
			mvCxt.updatePanels();
		}else if(srcBtn==addTrafficComp){
			TrafficComposition tc=new TrafficComposition();
			road.setTrafficComposition(tc);
			String nm=DataToViewerConverter.getNotUsedPrototypeLabel(mvCxt, tc);
			VehicleType vt=MovsimScenario.getVehicleType();
			vt.setLabel(nm);
			tc.getVehicleType().add(vt);
			mvCxt.updatePanels();
		}else if(srcBtn==newType){
			String nm=DataToViewerConverter.getNotUsedPrototypeLabel(mvCxt,road.getTrafficComposition());
			if(nm!=null){
				VehicleType vt=MovsimScenario.getVehicleType();
				vt.setLabel(nm);
				road.getTrafficComposition().getVehicleType().add(vt);
				mvCxt.updatePanels();
			}
			else GraphicsHelper.showMessage("All available vehicle prototypes are already used, make another prototype to add new vehicle type");
		}
		else if(srcBtn==addinflow){
			Inflow inflow=MovsimScenario.getInflow();
			road.getTrafficSource().getInflow().add(inflow);
			mvCxt.updatePanels();
		}
		else if(srcBtn==addTrafficSrc){
			road.setTrafficSource(MovsimScenario.getTrafficSource());
			mvCxt.updatePanels();
		}
		else if(srcBtn==remTrafficSrc){
			road.setTrafficSource(null);
			mvCxt.updatePanels();
		}
	}
	
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public void setId(JComboBox<String> id) {
		this.id = id;
	}
	public void setRemove(JButton remove) {
		this.remove=remove;
	}
	public void setLogging(JCheckBox logging) {
		this.logging=logging;
	}
	public void setRemoveTrafficComp(JButton remTrafficComp) {
		this.remTrafficComp=remTrafficComp;
	}
	public void setAddTrafficComp(JButton addTrafficComp) {
		this.addTrafficComp=addTrafficComp;
	}
	public void setRemoveTrafficSrc(JButton remTrafficSrc) {
		this.remTrafficSrc=remTrafficSrc;
	}
	public void setAddTrafficSrc(JButton addTrafficSrc) {
		this.addTrafficSrc=addTrafficSrc;
	}
	public void setAddInflow(JButton addInflow) {
		this.addinflow=addInflow;
	}
	public void setNewType(JButton newType) {
		this.newType=newType;
	}
}
