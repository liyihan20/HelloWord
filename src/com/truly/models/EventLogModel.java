package com.truly.models;


public class EventLogModel {

	private int id;
	private String username;
	private String ip;
	private String op_time;
	private String model;
	private String sysNum;
	private String event;
	private int unusual;
	
	public String getSysNum() {
		return sysNum;
	}
	public void setSysNum(String sysNum) {
		this.sysNum = sysNum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOp_time() {
		return op_time;
	}
	public void setOp_time(String op_time) {
		this.op_time = op_time;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public int getUnusual() {
		return unusual;
	}
	public void setUnusual(int unusual) {
		this.unusual = unusual;
	}
	
	@Override
	public String toString(){
		return String.format("EventLog:[id=%d,op_time=%s,username=%s,ip=%s,\nmodel=%s,sysNum=%s,event=%s,unusual=%d]", getId(),getOp_time(), getUsername(),getIp(),getModel(),getSysNum(),getEvent(),getUnusual());
		
	}
	
}
