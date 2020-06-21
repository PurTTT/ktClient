package com.client.entity;

public class Request {
	int roomId;
	int wind;
	float temperature;
	String beginTime; //开始时间
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getWind() {
		return wind;
	}
	public void setWind(int wind) {
		this.wind = wind;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
}
