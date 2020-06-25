package com.client.entity;

/*
	Bill为前台可以查看的账单
*/
public class Bill {
	Integer roomId;	//房间ID
	Float totalFee;	//总费用
	String inTime;	//入住时间
	String outTime;	//退房时间
	public Integer getRoomId(){
		return roomId;
	}
	public void setRoomId(Integer roomId){
		this.roomId = roomId;
	}
	public Float getTotalFee(){
		return totalFee;
	}
	public void setTotalFee(Float totalFee){
		this.totalFee = totalFee;
	}
	public String getIntime() {
		return inTime;
	}
	public void setIntime(String inTime) {
		this.inTime = inTime;
	}
	public String getOuttime() {
		return outTime;
	}
	public void setOuttime(String outTime) {
		this.outTime = outTime;
	}
//	public String toString() {
//		return roomId+" "+inTime+" "+outTime+" "+totalFee;
//	}
}
