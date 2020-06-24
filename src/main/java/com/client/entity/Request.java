package com.client.entity;

/*
	Request为请求
 */

public class Request {
	Integer roomId;
	Integer wind;
	float temperature;
	String beginTime; //开始时间
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Integer getWind() {
		return wind;
	}
	public void setWind(Integer wind) {
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
