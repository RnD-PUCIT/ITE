package org.tde.tdescenariodeveloper.updation;

import org.movsim.simulator.roadnetwork.LaneSegment;
import org.movsim.simulator.roadnetwork.RoadSegment;

public class RoadLaneSegmentPair {
	private RoadSegment rs;
	private LaneSegment ls;
	private String roadId;
	private int laneId;
	public RoadLaneSegmentPair(RoadSegment rs, LaneSegment ls, String roadId,
			int laneId) {
		this.rs = rs;
		this.ls = ls;
		this.roadId = roadId;
		this.laneId = laneId;
	}
	public LaneSegment getLs() {
		return ls;
	}
	public void setLs(LaneSegment ls) {
		this.ls = ls;
	}
	public RoadSegment getRs() {
		return rs;
	}
	public void setRs(RoadSegment rs) {
		this.rs = rs;
	}
	public String getRoadId() {
		return roadId;
	}
	public void setRoadId(String roadId) {
		this.roadId = roadId;
	}
	public int getLaneId() {
		return laneId;
	}
	public void setLaneId(int laneId) {
		this.laneId = laneId;
	}
}
