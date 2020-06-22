package com.client.entity;

/*统计报表*/
public class ReportForm {
	Integer roomId;
	Integer switchTimes;	//开关次数
	Integer duration;	//时长
	Integer totalFee;
	Integer scheduleTimes;//调度次数
	Integer billNum; //详单数
	Integer tempTimes; //调温次数
	Integer windTimes; //调风次数
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Integer getSwitchTimes() {
		return switchTimes;
	}
	public void setSwitchTimes(Integer switchTimes) {
		this.switchTimes = switchTimes;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}
	public Integer getScheduleTimes() {
		return scheduleTimes;
	}
	public void setScheduleTimes(Integer scheduleTimes) {
		this.scheduleTimes = scheduleTimes;
	}
	public Integer getBillNum() {
		return billNum;
	}
	public void setBillNum(Integer billNum) {
		this.billNum = billNum;
	}
	public Integer getTempTimes() {
		return tempTimes;
	}
	public void setTempTimes(Integer tempTimes) {
		this.tempTimes = tempTimes;
	}
	public Integer getWindTimes() {
		return windTimes;
	}
	public void setWindTimes(Integer windTimes) {
		this.windTimes = windTimes;
	}
}
