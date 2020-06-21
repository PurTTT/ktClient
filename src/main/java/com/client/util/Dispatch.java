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
	Integer busyNum = 0; //正在工作的机器数
	Integer waitNum = 0; //等待队列中的数量
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
		System.out.println("id:"+roomId+"  wind:"+wind+"  tem:"+tem);
		//outAll();
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
			//System.out.println("busy:"+busyNum+"  work:"+workNum);
		}
		if(busyNum >= workNum) {		
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
					System.out.println("抢占");
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
		System.out.println("result:"+result);
		return result;
	}
	//周期性更新，检测超时状况
	public Request[] updateWork() throws ParseException {			
		Request[] rs = new Request[2];
		if(busyNum<=workNum) return rs;
		long nowTime = System.currentTimeMillis();
		long btime0;
		int maxWind = 0;
		int k;
		for(int i=0;i<workNum;i++) {
			btime0 = dateToStamp(workLine[i].getBeginTime());
			if(nowTime-btime0>120000) {
				for(int j=0;j<waitNum;j++) {
					if(maxWind < waitLine[j].getWind())
					maxWind = waitLine[j].getWind();
				}
				for(k=0;k<waitNum;k++) {
					if(waitLine[k].getWind()==maxWind) {
						rs[0].setRoomId(waitLine[k].getRoomId());
						rs[0].setTemperature(waitLine[k].getTemperature());
						rs[0].setWind(waitLine[k].getWind());
						break;
					}
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		        btime = df.format(new Date());
		        rs[0].setBeginTime(btime);
				int wind = workLine[i].getWind();
				int roomId = workLine[i].getRoomId();
				float tem = workLine[i].getTemperature();
				workLine[i].setRoomId(waitLine[k].getRoomId());
				workLine[i].setWind(waitLine[k].getWind());
				workLine[i].setTemperature(waitLine[k].getTemperature());
				workLine[i].setBeginTime(btime);
				waitLine[k].setRoomId(roomId);
				waitLine[k].setWind(wind);
				waitLine[k].setTemperature(tem);
				waitLine[k].setBeginTime("0");
				rs[1].setRoomId(roomId);
				rs[1].setTemperature(tem);
				rs[1].setWind(wind);
				rs[1].setBeginTime(btime);
			}
		}
		return rs;
	}
	public void init() {
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

}
