package com.client.entity;
/*
	Room为管理员可实时监测的表
 */

public class Room {
	 private Integer roomNum;
	 private Integer pattern; // 0 default,1 cold,2 hot
	 private Float temperature;	//当前温度
	 private Float targetTemperature; //目标温度
	 private Integer windSpeed; //1low,2mid,3high
	 private Integer fee; //计费标准
	 private Float upperTem;
	 private Float lowerTem;
	 private Integer state;
	 private Integer openTime;
	 private Integer windRequest;
	 private Integer tempRequest;
	 private Integer dispatchTime;
	 private Float oriTemperature;
	 public Integer getPattern(){
	        return pattern;
	    }
	    public void setPattern(Integer pattern){
	        this.pattern = pattern;
	    }
	    public Integer getWindSpeed(){
	        return windSpeed;
	    }
	    public void setWindSpeed(Integer windSpeed){
	        this.windSpeed = windSpeed;
	    }
	    public Integer getRoomNum(){
	        return roomNum;
	    }
	    public void setRoomNum(Integer roomNum){
	        this.roomNum = roomNum;
	    }
	    public Float getTemperature(){
	        return temperature;
	    }
	    public void setTemperature(float temperature){
	        this.temperature = temperature;
	    }
	    public Float getTargetTemperature(){
	        return targetTemperature;
	    }
	    public void setTargetTemperature(float targetTemperature){
	        this.targetTemperature = targetTemperature;
	    }
	    public Integer getFee(){
	        return fee;
	    }
	    public void setFee(Integer fee){
	        this.fee = fee;
	    }
	    public Float getUpperTem(){
	        return upperTem;
	    }
	    public void setUpperTem(float upperTem){
	        this.upperTem = upperTem;
	    }
	    public Float getLowerTem(){
	        return lowerTem;
	    }
	    public void setLowerTem(float lowerTem){
	        this.lowerTem = lowerTem;
	    }
	    public Integer getState() {return state; }
	    public void setState(int state) { this.state = state; }
		public Integer getOpenTime() {return openTime; }
		public void setOpenTime(int openTime) { this.openTime = openTime; }
	public Integer getWindRequest() {return windRequest; }
	public void setWindRequest(int windRequest) { this.windRequest = windRequest; }
	public Integer getTempRequest() {return tempRequest; }
	public void setTemperature(int tempRequest) { this.tempRequest = tempRequest; }
	public Integer getDispatchTime() {return dispatchTime; }
	public void setDispatchTime(int dispatchTime) { this.dispatchTime = dispatchTime; }
	public Float getOriTemperature() {return oriTemperature; }
	public void setOriTemperature(Float oriTemperature) { this.oriTemperature = oriTemperature; }
//	    public String toString() {
//	    	return roomNum+" "+pattern+" "+temperature+" "+targetTemperature
//	    +" "+windSpeed+" "+fee+" "+upperTem+" "+lowerTem;
//	    }
}
