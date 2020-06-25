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

import com.client.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import net.sf.json.JSONArray;

import com.client.mapper.AcMapper;
import com.client.util.Dispatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

@Service
public class AcService {
	@Autowired
	private AcMapper mapper;
	Dispatch dispatch = new Dispatch();
	/*DB-room*/
	int selectState(Integer roomNum) {
		return mapper.selectState(roomNum);
	}
	int updateMode(Integer pattern,Float upperTem, Float lowerTemroomNum, Integer fee) {//更新模式
		return mapper.updateMode(pattern, upperTem, lowerTemroomNum, fee);
	}
	int updateFee(Integer fee) {//更新费率
		return mapper.updateFee(fee);
	}
	int updateRoomWhenClose(Integer roomNum) { return mapper.updateRoomWhenClose(roomNum);}
	int changeState(Integer state,Integer roomNum) {//更新状态 0关机 1开机
		return mapper.changeState(state,roomNum);
	}
	int findWind(Integer roomNum) {
		return mapper.findWind(roomNum);
	}
	List<Integer> selectRoomNum() {return mapper.selectRoomNum();}
	List<Integer> selectWorkingRoom(){return mapper.selectWorkingRoom();}
	List<Room> findRoomList(){//显示所有用户信息
		return mapper.findRoomList();
	}
	float selectUT(Integer roomNum) {return mapper.selectUT(roomNum);}
	float selectLT(Integer roomNum) {return mapper.selectLT(roomNum);}
	int updateNotWorking1(Integer roomNum) {return mapper.updateNotWorking1(roomNum);}
	int updateNotWorking2(Integer roomNum) {return mapper.updateNotWorking2(roomNum);}
	int updateNotWorking3(Integer roomNum) {return mapper.updateNotWorking1(roomNum);}
	int pause(Integer roomNum) {return mapper.pause(roomNum);}
	int restart(Integer roomNum) {return mapper.restart(roomNum);}
	List<UserCheck> findRoom(Integer roomNum){//显示用户信息
		return mapper.findRoom(roomNum);
	}
	int updateTandW(Integer windSpeed,double tem,Integer roomNum, Integer state) {
		return mapper.updateTandW(windSpeed, tem, roomNum, state);
	}
	int addOpenTime(Integer roomNum) {
		return mapper.addOpenTime(roomNum);
	}
	int addWR(Integer roomNum) {
		return mapper.addWR(roomNum);
	}
	int addTR(Integer roomNum) {
		return mapper.addTR(roomNum);
	}
	int updateDispatchTime(Integer roomNum) {
		return mapper.updateDispatchTime(roomNum);
	}
	/*DB-BillList*/
	int newBill(Integer roomId,String beginTime,Integer price,double eSpeed) {
		return mapper.newBill(roomId, beginTime, price, eSpeed);
	}
	int updateBill(String endTime,long windTime,double fee,int dResult) {
		return mapper.updateBill(endTime, windTime, fee, dResult);
	}
	List<BillList> selectBillList(Integer roomId, String inTime, String outTime) {
		return mapper.selectBillList(roomId, inTime, outTime);
	}
	List<DailySheet> checkDailySheet(String beginTime, String endTime) {
		return mapper.checkDailySheet(beginTime, endTime);
	}
	List<Bill> selectBill(Integer roomId, String outTime) {
		return mapper.selectBill(roomId, outTime);
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
	int ScheduleUpdate6() {
		return mapper.ScheduleUpdate6();
	}
	int ScheduleUpdate7() {
		return mapper.ScheduleUpdate7();
	}
	int ScheduleUpdate8() {
		return mapper.ScheduleUpdate8();
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
	String selectInTime(Integer roomId, String outTime) {
		return mapper.selectInTime(roomId, outTime);
	}
	List<Bill> selectFullBill(Integer roomId) {
		return mapper.selectFullBill(roomId);
	}
	int newLogIn(Integer roomId) {return mapper.newLogIn(roomId);}
	/*DB-request*/
	int newRequest(Integer roomId,Integer wind,double temperature,String create_time) {
		return mapper.newRequest(roomId, wind, temperature, create_time);
	}
	public void start() throws IOException {
        Timer timer = new Timer();
        ScheduleTask scheduleTask = new ScheduleTask();
        NotWorking notWorking = new NotWorking();
        Calendar calendar = Calendar.getInstance();
        timer.schedule(scheduleTask,calendar.getTime(),30000); //单位毫秒
		timer.schedule(notWorking, calendar.getTime(), 60000); //非工作状态
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
						newLogIn(room_id);
                		updateIntime(nowtime,room_id);
                	}
                	else if(num2 == 2) {	//用户退房
                		changeState(0,room_id);
                		updateOuttime(nowtime,room_id);
                		double total = totalFee(room_id);
                		updateTotalFee(total,room_id);
                		List<Bill> sb = selectBill(room_id, nowtime);
                		List<BillList> sb1 = selectBillList(room_id, selectInTime(room_id, nowtime), nowtime);
						String str = JSON.toJSONString(sb, SerializerFeature.PrettyFormat);
						String str1 = JSON.toJSONString(sb1, SerializerFeature.PrettyFormat);
						outputStream.write(("账单：\n" + str + "\n详单：\n" + str1).getBytes());
                	}
                	else if(num2 == 3) {	//查询某个房间的所有账单
                		List<Bill> sb = selectFullBill(room_id);
						String str = JSON.toJSONString(sb, SerializerFeature.PrettyFormat);
						outputStream.write(str.getBytes());
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else if(ch == '2') { //用户消息
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	int room_id = sc.nextInt();
                	if(num2 == 1) {		//房间空调开机
                		changeState(3,room_id);	//DB-room，未送风时，空调为暂停状态
						addOpenTime(room_id); //房间空调开关机次数+1
                	}
                	else if(num2 == 2) {	//房间空调关机
                		changeState(1,room_id);
                		int result = dispatch.removeWork(room_id);
                		String btime = dispatch.getBtime();
            			double eSpeed = 0.33;	//耗电速度
            			if(findWind(room_id) == 2) eSpeed = 0.5; else if(findWind(room_id) == 3) eSpeed = 1;
                		long windTime = dispatch.getWtime();
                		double fee = windTime*1*eSpeed/60.00; //送风时间*1元/度*耗电标准
                		if(result == 0) { //等待队列为空
                			updateBill(btime,windTime,fee,room_id);
							updateRoomWhenClose(room_id);
                		}
                		else if(result == 1) { //等待队列中最高风速接受服务
                			updateBill(btime,windTime,fee,room_id);
							updateRoomWhenClose(room_id);
                			newBill(room_id,btime,1,eSpeed);
                		}
                	}
                	else if(num2 == 3) {	//风速，温度调节请求
                		int wind = sc.nextInt();
                		float tem = sc.nextFloat();
                		if(wind < 0 || wind > 3 || ((tem < selectLT(room_id) || tem > selectUT(room_id)) && tem != 0) )
                			continue; //判断温控范围
						if(wind != 0) //调风请求数+1
							addWR(room_id);
						else //缺省风速为中风
							wind = 2;
						if(tem != 0) //调温请求数+1
							addTR(room_id);
						else //缺省温度为26度
							tem = 26;
                        newRequest(room_id, wind, tem, nowtime);//DB-request
                		int dResult = dispatch.newRequset(room_id, wind, tem);
            			double eSpeed = 0.33;	//耗电速度
            			if(wind == 2) eSpeed = 0.5; else if(wind == 3) eSpeed = 1;
                		if(dResult == 0) {//放入等待队列	
                		}
                		else if(dResult == 1) { //工作区未满直接工作，且当前房间正被服务
                			//先将当前工作中的请求结账
                			String btime = dispatch.getBtime();
							dispatch.removeWork(room_id);
							double preeSpeed = 0.33;	//耗电速度
							if(findWind(room_id) == 2) preeSpeed = 0.5; else if(findWind(room_id) == 3) preeSpeed = 1;
							long windTime = dispatch.getWtime();
							double fee = windTime*1*preeSpeed/60.00; //送风时间*1元/度*耗电标准
							updateBill(btime,windTime,fee,room_id);
                			//更新room中的设置，申请新请求
							updateTandW(wind,tem,room_id, 2);	//DB-room
							updateDispatchTime(room_id); //被调度次数+1
							System.out.println(room_id + "替换自己");
                			newBill(room_id,btime,1,eSpeed);	//DB-billList
                		}
                		else if(dResult == 2) {
							String btime = dispatch.getBtime();
							updateTandW(wind,tem,room_id, 2);	//DB-room
							updateDispatchTime(room_id); //被调度次数+1
							System.out.println(room_id + "获得服务");
							newBill(room_id,btime,1,eSpeed);	//DB-billList
						}
                		else {
                			String btime = dispatch.getBtime();
                			updateTandW(wind,tem,room_id,2);	//DB-room
							updateDispatchTime(room_id); //被调度次数+1
                			updateTandW(0,26.0,dResult,3);	//DB-room
                			long windTime = dispatch.getWtime();	//计算送风时长
                			double fee = windTime*eSpeed*1/60.00;	//费用
                			newBill(room_id,btime,1,eSpeed);	//DB-billList
                			updateBill(btime,windTime,fee,dResult);	//DB-billList
							System.out.println(room_id + "替换" + dResult);
                		}
                	}
                	else if(num2 == 4) { //实时监测温度
                		List<UserCheck> rm = findRoom(room_id);
						String str = JSON.toJSONString(rm, SerializerFeature.PrettyFormat);
						outputStream.write(str.getBytes());
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
						int fe = sc.nextInt();
                		updateMode(mod,upper,lower,fe);
                	}
                	else if(num2 == 2) { //查看所有房间即时状态信息
                		List<Room> findAll = findRoomList();
						String str = JSON.toJSONString(findAll, SerializerFeature.PrettyFormat);
						outputStream.write(str.getBytes());
                	}
                	else System.out.println("命令错误，没有此项操作");
                }
                else if(ch == '4') {	//经理消息
                	Scanner sc = new Scanner(data);
                	int num1 = sc.nextInt();
                	int num2 = sc.nextInt();
                	String beginTime = data.substring(4, 12);
                	String endTime = data.substring(13, 21);
                	if(num2 == 1) {		//获得日报表
						StringBuilder sb = new StringBuilder(beginTime); //格式化日期
						sb.insert(4, "-");
						sb.insert(7, "-");
						beginTime = sb.toString();
						StringBuilder sb1 = new StringBuilder(endTime);
						sb1.insert(4, "-");
						sb1.insert(7, "-");
						endTime = sb1.toString();
						List<DailySheet> ds = checkDailySheet(beginTime, endTime);
						String str = JSON.toJSONString(ds, SerializerFeature.PrettyFormat);
						outputStream.write(str.getBytes());
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
			ScheduleUpdate6();	ScheduleUpdate7(); ScheduleUpdate8(); //先判断差值很小的情况
			ScheduleUpdate();	ScheduleUpdate1(); ScheduleUpdate2();
			ScheduleUpdate3();   ScheduleUpdate4();  ScheduleUpdate5();//周期更新数据库
			Request[] rs = new Request[2];
			try {
				rs = dispatch.updateWork();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(rs[0].getRoomId()!=0) {
				System.out.println("时间片轮转:"+rs[0].getRoomId()+"替换"+rs[1].getRoomId());
				double eSpeed = 0.33;
				if(rs[0].getWind() == 2) eSpeed = 0.5;
				else if(rs[0].getWind() == 3) eSpeed = 1;
				updateTandW(rs[0].getWind(),rs[0].getTemperature(),rs[0].getRoomId(),2);	//DB-room
				updateDispatchTime(rs[0].getRoomId()); //被调度次数+1
				updateTandW(0,26,rs[1].getRoomId(),3);	//DB-room
				newBill(rs[0].getRoomId(),rs[0].getBeginTime(),1,eSpeed);
				long windTime = dispatch.getWtime();
				double fee = windTime*eSpeed*1/60.00;
				updateBill(rs[0].getBeginTime(),windTime,fee,rs[1].getRoomId());
			}

			List<Integer> roomNum = selectWorkingRoom();
			for(Integer i : roomNum) {
				pause(i); //判断是否要暂停，state=3
				restart(i); //判断是否重新启动，state=2
				System.out.println("被服务房间判断暂停或复工更新");
			}
	    }
	}

	public class NotWorking extends TimerTask { //非工作状态
		@Override
		public void run() {
			List<Integer> roomNum = selectRoomNum();
			for(Integer i : roomNum) {
				updateNotWorking3(i); //差距小于0.5度的情况
				updateNotWorking1(i); //当前温度>初始温度
				updateNotWorking2(i); //当前温度<初始温度
				System.out.println(i + "非工作状态回温更新");
			}
		}
	}
}
