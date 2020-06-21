package com.client.service;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.client.entity.Request;
import com.client.entity.Room;
import com.client.mapper.AcMapper;
import com.client.util.Dispatch;

@Service
public class AcService {
	@Autowired
	private AcMapper mapper;
	List allRoom;
	/*DB-room*/
	int newCustomer(Integer roomNum) {//入住
		return mapper.newCustomer(roomNum);
	}
	int deleteOne(Integer roomNum) {//退房
		return mapper.deleteOne(roomNum);
	}
	int updateMode(Integer mode,Float upperTem, Float lowerTemroomNum) {//更新模式
		return mapper.updateMode(mode, upperTem, lowerTemroomNum);
	}
	int updateFee(Integer fee) {//更新费率
		return mapper.updateFee(fee);
	}
	int changeState(Integer state) {//更新状态 0关机 1开机
		return mapper.updateFee(state);
	}
	List findRoomList(){//显示所有用户信息
		return mapper.findRoomList();
	}
	Room findRoom(Integer roomNum){//显示用户信息
		return mapper.findRoom(roomNum);
	}
	int updateTandW(Integer windSpeed,double tem,Integer roomNum) {
		return mapper.updateTandW(windSpeed, tem, roomNum);
	}
	/*DB-BillList*/
	int newBill(Integer roomId,String beginTime,Integer price,double eSpeed) {
		return mapper.newBill(roomId, beginTime, price, eSpeed);
	}
	int updateBill(String endTime,long windTime,double fee,int dResult) {
		return mapper.updateBill(endTime, windTime, fee, dResult);
	}
	int ScheduleUpdate() {
		return mapper.ScheduleUpdate();
	}
	public void start() throws IOException {
        Timer timer = new Timer();
        ScheduleTask scheduleTask = new ScheduleTask();
        Calendar calendar = Calendar.getInstance();
        //timer.schedule(scheduleTask,calendar.getTime(),20000); //单位毫秒
        Dispatch dispatch = new Dispatch();
		ServerSocket ss = new ServerSocket(8088);
        boolean flag = true;
        while (flag) {
            try {
                Socket s = ss.accept();
                InputStream in = s.getInputStream();
                OutputStream outputStream = s.getOutputStream();
                byte[] buf = new byte[1024];
                int len = in.read(buf);
                String data = new String(buf, 0, len);
                char ch = data.charAt(0);	//判断消息发出者
                if(ch == '1') {	//前台消息
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	int room_id = sc.nextInt();
                	if(num2 == 1) {		//用户入住
                		int create = newCustomer(room_id);
                	}
                	else if(num2 == 2) {	//用户退房
                		Room inform = findRoom(room_id);
                		outputStream.write(("详单"+inform.toString()).getBytes());
                		int delete = deleteOne(room_id);
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else if(ch == '2') { //用户消息
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	int room_id = sc.nextInt();
                	if(num2 == 1) {		//房间空调开机
                		changeState(1);	//DB-room
                	}
                	else if(num2 == 2) {	//房间空调关机
                		changeState(0);
                	}
                	else if(num2 == 3) {	//风速，温度调节请求
                		int wind = sc.nextInt();
                		float tem = sc.nextFloat();
                		int dResult = dispatch.newRequset(room_id, wind, tem);
            			double eSpeed = 0.33;
            			if(wind == 2) eSpeed = 0.5;
            			else if(wind == 3) eSpeed = 1;
                		if(dResult == 0) {//放入等待队列	
                		}
                		else if(dResult == 1) { //工作区未满直接工作
                			String btime = dispatch.getBtime();
                			int r0 = updateTandW(wind,tem,room_id);	//DB-room
                			newBill(room_id,btime,1,eSpeed);	//DB-bill
                		}
                		else {
                			String btime = dispatch.getBtime();
                			int r0 = updateTandW(wind,tem,room_id);	//DB-room
                			int r1 = updateTandW(0,26.0,dResult);	//DB-room
                			long windTime = dispatch.getWtime();	//计算送风时长
                			double fee = windTime*eSpeed*1;	//费用
                			newBill(room_id,btime,1,eSpeed);	//DB-bill
                			updateBill(btime,windTime,fee,dResult);	//DB-bill
                		}
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else if(ch == '3') {	//管理员消息
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	if(num2 == 1) {	//温控模式，及温度上下限
                		int mod = sc.nextInt();
                		float upper = sc.nextFloat();
                		float lower = sc.nextFloat();
                		int uM = updateMode(mod,upper,lower);
                	}
                	else if(num2 == 2) {	//费率调节
                		int fe = sc.nextInt();
                		int uF = updateFee(fe);
                	}
                	else if(num2 == 3) {
                		List findAll = findRoomList();
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else if(ch == '4') {	//经理消息
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	if(num2 == 1) {		//获得报表
                		int beTime = sc.nextInt();	//起始时间
                		int endTime = sc.nextInt();
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else if(ch == '0') {	//other
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	//int room_id = sc.nextInt();
                	if(num2 == 1) {	
                		
                	}
                	else if(num2 == 2) {	
                		
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else System.out.println("命令错误，没有此类消息");
                
                System.err.print(data);
                if (data.equals("关闭")) {
                    flag = false;
                    s.close();
                    ss.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	public class ScheduleTask extends TimerTask{
		Dispatch dispatch = new Dispatch();
		@Override
	    public void run() {
			int x = ScheduleUpdate();	//周期更新数据库
			Request[] rs = new Request[2];
			try {
				rs = dispatch.updateWork();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(rs[0]!=null) {
				double eSpeed = 0.33;
				if(rs[0].getWind() == 2) eSpeed = 0.5;
				else if(rs[0].getWind() == 3) eSpeed = 1;
				updateTandW(rs[0].getWind(),rs[0].getTemperature(),rs[0].getRoomId());	//DB-room
				updateTandW(0,26,rs[1].getRoomId());	//DB-room
				newBill(rs[0].getRoomId(),rs[0].getBeginTime(),1,eSpeed);
				long windTime = dispatch.getWtime();
				double fee = windTime*eSpeed;
				updateBill(rs[0].getBeginTime(),windTime,fee,rs[1].getRoomId());
			}
	    }
	}
}
