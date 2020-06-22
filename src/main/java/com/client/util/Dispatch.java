package com.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.client.entity.Request;
import com.client.mapper.AcMapper;

public class Dispatch {
	Integer workNum = 3; //同时工作房间数
	Integer busyNum; //正在工作的机器数
	Integer waitNum; //等待队列中的数量
	Request[] waitLine = new Request[10];	//请求队列
	Request[] workLine = new Request[5]; //工作队列
	String btime;
	long wtime;
	/*时间与时间戳转换*/
	public static long dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        //res = String.valueOf(ts);
        return ts;
    }
	/*判断是否符合抢占条件*/
	public int newRequset(int roomId, int wind, float tem) throws ParseException {
		//空闲，直接工作1    工作状态不变0    抢占发生返回被抢占code
		int result = 0;	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        btime = df.format(new Date());// new Date()为获取当前系统时间
		if(busyNum <= workNum) {	//有空闲工作位——开始工作
			for(int i=0;i<busyNum;i++) {
				if(roomId == workLine[i].getRoomId()) {
					workLine[i].setWind(wind);	//查看工作队列中是否已有该房间
					return 1;
				}
			}
			if(busyNum < workNum) {
				Request rs = new Request();
				rs.setRoomId(roomId);
				rs.setWind(wind);
				rs.setTemperature(tem);
				rs.setBeginTime(btime);
				workLine[busyNum] = rs;
				busyNum = busyNum + 1;
				outAll();
				return 1;
			}
		}
		if(busyNum >= workNum) {		//判断能否抢占
			int w = waitNum;
			for(int j=0;j<waitNum;j++) {//查看请求队列中是否已有该房间
				if(roomId == waitLine[j].getRoomId()) {
					w = j;
					waitNum--;
				}
			}
			System.out.println("进入请求队列");
			waitLine[w].setRoomId(roomId);
			waitLine[w].setTemperature(tem);
			waitLine[w].setWind(wind);
			for(int i=0;i<workNum;i++) {
				if(workLine[i].getWind() < wind) {
					wind = workLine[i].getWind();
					roomId = workLine[i].getRoomId();
					tem = workLine[i].getTemperature();
					wtime = (dateToStamp(btime) - 
							dateToStamp(workLine[i].getBeginTime()))/1000;//送风时间
					workLine[i].setRoomId(waitLine[w].getRoomId());
					workLine[i].setWind(waitLine[w].getWind());
					workLine[i].setTemperature(waitLine[w].getTemperature());
					workLine[i].setBeginTime(btime);
					waitLine[w].setRoomId(roomId);
					waitLine[w].setWind(wind);
					waitLine[w].setTemperature(tem);
					waitLine[w].setBeginTime("0");
					result = roomId;
					break;
				}
			}
		}
		waitNum = waitNum + 1;
		System.out.println("busy:"+workLine[0].getRoomId()+" "
		+workLine[1].getRoomId()+" "+workLine[2].getRoomId());
		return result;
	}
	//周期性更新，检测超时状况
	public Request[] updateWork() throws ParseException {			
		Request[] rs1 = new Request[2];
		Request r = new Request();
		r.setRoomId(0);
		r.setTemperature(0);
		r.setWind(0);
		r.setBeginTime("0");
		rs1[0] = r; rs1[1] = r;
		if(waitNum == 0) return rs1;
		long nowTime = System.currentTimeMillis();
		long btime0 = 0;
		int maxWind = 0;
		int k;
		for(int i=0;i<workNum;i++) {
			Request[] rs = new Request[2];
			rs[0] = r; rs[1] = r;
			btime0 = dateToStamp(workLine[i].getBeginTime());
			if(nowTime-btime0>60000) {	//超过1分钟
				System.out.println(workLine[i].getRoomId()+"工作时间over，可能被替换[");
				for(int j=0;j<waitNum;j++) {	//查看请求队列中最大的风速请求
					if(maxWind < waitLine[j].getWind())
					maxWind = waitLine[j].getWind();
				}
				if(maxWind<workLine[i].getWind()) break;
				for(k=0;k<waitNum;k++) {//替换
					if(waitLine[k].getWind()==maxWind) {
						System.out.println("["+waitLine[k].getRoomId());
						rs[0].setRoomId(waitLine[k].getRoomId());
						rs[0].setTemperature(waitLine[k].getTemperature());
						rs[0].setWind(waitLine[k].getWind());
						break;
					}
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        btime = df.format(new Date());
		        rs[0].setBeginTime(btime);
				int wind = workLine[i].getWind();
				int roomId = workLine[i].getRoomId();
				float tem = workLine[i].getTemperature();
				workLine[i] = rs[0];
				//System.out.println("["+rs[0].getRoomId());
				waitLine[k].setRoomId(roomId);
				waitLine[k].setTemperature(tem);
				waitLine[k].setWind(wind);
				waitLine[k].setBeginTime(btime);
				rs[1] = waitLine[k];
				waitLine[k].setBeginTime("0");
				System.out.println("["+rs[0].getRoomId());

				System.out.println(rs[0].getRoomId()+"替换"+rs[1].getRoomId());
				rs1 = rs;
			}
		}
		return rs1;
	}
	/*空调关机导致用户请求或工作的移除*/
	public int removeWork(int roomId) throws ParseException {
		int maxWind = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        btime = df.format(new Date());// new Date()为获取当前系统时间
		Request rs = new Request();
		for(int i=0;i<busyNum;i++) {	//1-关机的房间在工作队列
			if(workLine[i].getRoomId() == roomId) {
				wtime = (dateToStamp(btime) - 
						dateToStamp(workLine[i].getBeginTime()))/1000;
				if(waitNum == 0) {	//等待队列中没有请求
					rs = workLine[i];
					workLine[i] = workLine[workNum-1];
					busyNum--;
					return 0;
				}
				for(int j=0;j<waitNum;j++) {	//查看请求队列中最大的风速请求
					if(maxWind < waitLine[j].getWind())
						maxWind = waitLine[j].getWind();	
				}
				for(int k=0;k<waitNum;k++) {	//取出请求，队列前进
					if(waitLine[k].getWind() == maxWind) {
						rs = waitLine[k];
						waitLine[k] = waitLine[waitNum-1];
						waitNum--;
						break;
					}
				}
				workLine[i] = rs;
				workLine[i].setBeginTime(btime);
				return 1;
			}
		}
		for(int i=0;i<waitNum;i++) {
			if(waitLine[i].getRoomId() == roomId) {
				rs = waitLine[i];
				waitLine[i] = waitLine[waitNum-1];
				waitNum--;
			}
		}
		return 2;
	}
	/*初始化*/
	public void init() {
		waitNum = 0;
		busyNum = 0;
		Request r = new Request();
		r.setRoomId(0);
		r.setTemperature(0);
		r.setWind(0);
		r.setBeginTime("0");
		for(int i=0;i<10;i++) {
			if(i<5) workLine[i] = r;
			waitLine[i] = r;
		}
	}
	/*输出*/
	public void outAll() {
		for(int i=0;i<busyNum;i++) {
			System.out.println("busy"+i+":"+workLine[i].getRoomId()+"/"+workLine[i].getWind()
					+"/"+workLine[i].getTemperature());
		}
		for(int j=0;j<waitNum;j++) {
			System.out.println("wait"+j+":"+waitLine[j].getRoomId()+"/"+waitLine[j].getWind()
					+"/"+waitLine[j].getTemperature());
		}
	}
	
	public Dispatch(){
		init();
	}
	public String getBtime() {
		return btime;
	}
	public void setBtime(String btime) {
		this.btime = btime;
	}
	public Integer getWorkNum() {
		return workNum;
	}
	public void setWorkNum(Integer workNum) {
		this.workNum = workNum;
	}
	public long getWtime() {
		return wtime;
	}
	public void setWtime(long wtime) {
		this.wtime = wtime;
	}
	public Integer getBusyNum() {
		return busyNum;
	}
	public void setBusyNum(Integer busyNum) {
		this.busyNum = busyNum;
	}
	public Integer getWaitNum() {
		return waitNum;
	}
	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}

}
