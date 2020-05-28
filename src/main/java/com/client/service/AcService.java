package com.client.service;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.client.entity.Room;
import com.client.mapper.AcMapper;

@Service
public class AcService {
	@Autowired
	private AcMapper mapper;
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
	
	List findRoomList(){//显示所有用户信息
		return mapper.findRoomList();
	}
	
	Room findRoom(Integer roomNum){//显示所有用户信息
		return mapper.findRoom(roomNum);
	}
	int updateTandW(Integer windSpeed,Float targetTemperature,Integer roomNum) {
		return mapper.updateTandW(windSpeed, targetTemperature, roomNum);
	}
	public void start() throws IOException {
		System.out.println("OK");
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
                	}
                	else if(num2 == 2) {	//房间空调关机
                	}
                	else if(num2 == 3) {	//风速，温度调节请求
                		int wind = sc.nextInt();
                		float tem = sc.nextFloat();
                		int tw = updateTandW(wind,tem,room_id);
                		Room inform = findRoom(room_id);
                		outputStream.write(("详单"+inform.toString()).getBytes("UTF-8"));
                		System.out.println("调节完毕："+inform.toString());
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
}
