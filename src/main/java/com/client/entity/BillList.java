package com.client.entity;

/*详单*/
public class BillList {
	Integer roomId;
	String beginTime;	//开始送风时间
	String endTime;	//结束送风时间
	Integer windTime;	//送风时长
	Integer price;	//费率
	double eSpeed; //耗电速度 低0.33  中0.5 高1
	double fee;	//费用
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getWindTime() {
		return windTime;
	}
	public void setWindTime(Integer windTime) {
		this.windTime = windTime;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public double geteSpeed() {
		return eSpeed;
	}
	public void seteSpeed(double eSpeed) {
		this.eSpeed = eSpeed;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
    public String toString() {
    	return roomId+" "+beginTime+" "+endTime+" "+windTime
    +" "+price+" "+eSpeed+" "+fee;
    }
}
