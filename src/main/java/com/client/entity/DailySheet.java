package com.client.entity;

/*
    DailySheet为经理查看的报表
 */

public class DailySheet {
    Integer roomId;
    Integer openTime;
    Integer totalTime;
    Float totalFee;
    Integer dispatchTime;
    Integer BillNum;
    Integer tempRequest;
    Integer windRequest;

    public Integer getRoomId() {
        return roomId;
    }
    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
    public Integer getOpenTime() {
        return openTime;
    }
    public void setOpenTime(Integer openTime) {
        this.openTime = openTime;
    }
    public Integer getTotalTime() {
        return totalTime;
    }
    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }
    public Float getTotalFee() {
        return totalFee;
    }
    public void setTotalFee(Float totalFee) {
        this.totalFee = totalFee;
    }
    public Integer getDispatchTime() {
        return dispatchTime;
    }
    public void setDispatchTime(Integer dispatchTime) { this.dispatchTime = dispatchTime; }
    public Integer getBillNum() {
        return BillNum;
    }
    public void setBillNum(Integer BillNum) { this.BillNum = BillNum; }
    public Integer getTempRequest() {
        return tempRequest;
    }
    public void setTempRequest(Integer tempRequest) {
        this.tempRequest = tempRequest;
    }
    public Integer getWindRequest() {
        return windRequest;
    }
    public void setWindRequest(Integer windRequest) {
        this.windRequest = windRequest;
    }
}
