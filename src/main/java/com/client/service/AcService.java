package com.client.service;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.client.entity.Bill;
import com.client.entity.BillList;
import com.client.entity.Request;
import com.client.entity.Room;
import com.client.mapper.AcMapper;
import com.client.util.Dispatch;

@Service
public class AcService {
	@Autowired
	private AcMapper mapper;
	Dispatch dispatch = new Dispatch();
	/*DB-room*/
	int selectState(Integer roomNum) {
		return mapper.selectState(roomNum);
	}
	int updateMode(Integer pattern,Float upperTem, Float lowerTemroomNum) {//更新模式
		return mapper.updateMode(pattern, upperTem, lowerTemroomNum);
	}
	int updateFee(Integer fee) {//更新费率
		return mapper.updateFee(fee);
	}
	int changeState(Integer state,Integer roomNum) {//更新状态 0关机 1开机
		return mapper.changeState(state,roomNum);
	}
	int findWind(Integer roomNum) {
		return mapper.findWind(roomNum);
	}
	List<Room> findRoomList(){//显示所有用户信息
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
	List<BillList> selectBillList(Integer roomId) {
		return mapper.selectBillList(roomId);
	}
	Bill selectBill(Integer roomId) {
		return mapper.selectBill(roomId);
	}
	int ScheduleUpdate() {
		return mapper.ScheduleUpdate();
	}
	int ScheduleUpdate1() {
		return mapper.ScheduleUpdate1();
	}
	int ScheduleUpdate2() {
		return mapper.ScheduleUpdate2();
	}
	int ScheduleUpdate3() {
		return mapper.ScheduleUpdate3();
	}
	int ScheduleUpdate4() {
		return mapper.ScheduleUpdate4();
	}
	int ScheduleUpdate5() {
		return mapper.ScheduleUpdate5();
	}
	double totalFee(Integer roomId) {
		return mapper.totalFee(roomId);
	}
	/*DB-bill*/
	int updateIntime(String inTime,int roomId) {
		return mapper.updateIntime(inTime,roomId);
	}
	int updateOuttime(String outTime,int roomId) {
		return mapper.updateOuttime(outTime,roomId);
	}
	int updateTotalFee(double total,Integer roomId){
		return mapper.updateTotalFee(total, roomId);
	}
	/*DB-request*/
	int newRequest(Integer roomId,Integer wind,double temperature,String create_time) {
		return mapper.newRequest(roomId, wind, temperature, create_time);
	}
	public void start() throws IOException {
        Timer timer = new Timer();
        ScheduleTask scheduleTask = new ScheduleTask();
        Calendar calendar = Calendar.getInstance();
        timer.schedule(scheduleTask,calendar.getTime(),30000); //单位毫秒
		ServerSocket ss = new ServerSocket(8088);
        boolean flag = true;
        while (flag) {
            try {
                Socket s = ss.accept();
                InputStream in = s.getInputStream();
                OutputStream outputStream = s.getOutputStream();
            	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowtime = df.format(new Date());// new Date()为获取当前系统时间
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
                		changeState(1,room_id);	//0 未用 1入住 2工作 3暂停
                		updateIntime(nowtime,room_id);
                	}
                	else if(num2 == 2) {	//用户退房
                		changeState(0,room_id);
                		updateOuttime(nowtime,room_id);
                		double total = totalFee(room_id);
                		updateTotalFee(total,room_id);
                		Bill sb = selectBill(room_id);
                		List<BillList> sb1 = selectBillList(room_id);
                		StringBuffer sb2 = new StringBuffer();
                		for(int i = 0;i < sb1.size();i++){
                		    sb2.append(sb1.get(i).toString()+"\n");
                		}
                		outputStream.write(("Bill:"+sb2).getBytes());
                		outputStream.write(("Total:"+sb.toString()).getBytes());
                	}
                	else if(num2 == 3) {	//查询List
                		//Bill sb = selectBill(room_id);
                		List<BillList> sb1 = selectBillList(room_id);
                		StringBuffer sb2 = new StringBuffer();
                		for(int i = 0;i < sb1.size();i++){
                		    sb2.append(sb1.get(i).toString()+"\n");
                		}
                		outputStream.write(("Bill:"+sb2).getBytes());
                		//outputStream.write(("Total:"+sb.toString()).getBytes());
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else if(ch == '2') { //用户消息
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	int room_id = sc.nextInt();
                	if(num2 == 1) {		//房间空调开机
                		changeState(2,room_id);	//DB-room
                	}
                	else if(num2 == 2) {	//房间空调关机
                		changeState(1,room_id);
                		int result = dispatch.removeWork(room_id);
                		String btime = dispatch.getBtime();
            			double eSpeed = 0.33;	//耗电速度
            			if(findWind(room_id) == 2) eSpeed = 0.5; else if(findWind(room_id) == 3) eSpeed = 1;
                		long windTime = dispatch.getWtime();
                		double fee = windTime*1*eSpeed;
                		if(result == 0) {
                			updateBill(btime,windTime,fee,room_id);
                		}
                		else if(result == 1) {
                			updateBill(btime,windTime,fee,room_id);
                			newBill(room_id,btime,1,eSpeed);
                		}
                	}
                	else if(num2 == 3) {	//风速，温度调节请求
                		int wind = sc.nextInt();
                		float tem = sc.nextFloat();
                        newRequest(room_id, wind, tem, nowtime);//DB-request
                		int dResult = dispatch.newRequset(room_id, wind, tem);
            			double eSpeed = 0.33;	//耗电速度
            			if(wind == 2) eSpeed = 0.5; else if(wind == 3) eSpeed = 1;
                		if(dResult == 0) {//放入等待队列	
                		}
                		else if(dResult == 1) { //工作区未满直接工作
                			String btime = dispatch.getBtime();
                			int r0 = updateTandW(wind,tem,room_id);	//DB-room
                			newBill(room_id,btime,1,eSpeed);	//DB-billList
                		}
                		else {
                			String btime = dispatch.getBtime();
                			int r0 = updateTandW(wind,tem,room_id);	//DB-room
                			int r1 = updateTandW(0,26.0,dResult);	//DB-room
                			long windTime = dispatch.getWtime();	//计算送风时长
                			double fee = windTime*eSpeed*1;	//费用
                			newBill(room_id,btime,1,eSpeed);	//DB-billList
                			updateBill(btime,windTime,fee,dResult);	//DB-billList
                		}
                	}
                	else if(num2 == 4) {
                		Room rm = findRoom(room_id);
                		outputStream.write(("Room:"+rm.toString()).getBytes());
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else if(ch == '3') {	//管理员消息
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	if(num2 == 1) {	//温控模式，及温度上下限
                		int mod = sc.nextInt();	//0制冷 1制热
                		float upper = sc.nextFloat();
                		float lower = sc.nextFloat();
                		int uM = updateMode(mod,upper,lower);
                	}
                	else if(num2 == 2) {	//费率调节
                		int fe = sc.nextInt();
                		int uF = updateFee(fe);
                	}
                	else if(num2 == 3) {
                		List<Room> findAll = findRoomList();
                		StringBuffer fa = new StringBuffer();
                		for(int i = 0;i < findAll.size();i++){
                		    fa.append(findAll.get(i).toString()+"\n");
                		}
                		outputStream.write(("Room:"+fa.toString()).getBytes());
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
                	
                }
                else System.out.println("命令错误，没有此类消息");
                
                System.err.print(data);
                outputStream.write(("OK").getBytes());
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
		@Override
	    public void run() {
			//System.out.println("20s");
			ScheduleUpdate();	ScheduleUpdate1(); ScheduleUpdate2();
			ScheduleUpdate3();   ScheduleUpdate4();  ScheduleUpdate5();//周期更新数据库
			Request[] rs = new Request[2];
			try {
				rs = dispatch.updateWork();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(rs[0].getRoomId()!=0) {
				System.out.println("抢占成功");
				double eSpeed = 0.33;
				if(rs[0].getWind() == 2) eSpeed = 0.5;
				else if(rs[0].getWind() == 3) eSpeed = 1;
				updateTandW(rs[0].getWind(),rs[0].getTemperature(),rs[0].getRoomId());	//DB-room
				updateTandW(0,26,rs[1].getRoomId());	//DB-room
				newBill(rs[0].getRoomId(),rs[0].getBeginTime(),1,eSpeed);
				long windTime = dispatch.getWtime();
				double fee = windTime*eSpeed*1;
				updateBill(rs[0].getBeginTime(),windTime,fee,rs[1].getRoomId());
			}
	    }
	}
}
