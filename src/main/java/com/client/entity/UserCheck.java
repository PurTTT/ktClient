package com.client.entity;

/*
    UserCheck是用户监测反馈的表
 */

public class UserCheck {
    private Integer roomNum; //房间号
    private Integer pattern; //温控模式 0-制冷 1-制热
    private float temperature; //此时温度
    private Integer windSpeed; //风速

    public Integer getRoomNum() { return roomNum; }
    public void setRoomNum(Integer roomNum) { this.roomNum = roomNum; }
    public Integer getPattern() { return pattern; }
    public void setPattern(Integer pattern) { this.pattern = pattern; }
    public float getTemperature() { return temperature; }
    public void setTemperature(float temperature) { this.temperature = temperature; }
    public Integer getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Integer windSpeed) { this.windSpeed = windSpeed; }
}
