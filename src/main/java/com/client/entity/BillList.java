package com.client.entity;

/*详单*/
public class BillList {
	Integer roomId;
	Integer beginWindTime;	//开始送风时间
	Integer finishWindTime;	//结束送风时间
	Integer windTime;	//送风时长
	Integer price;	//费率
	float eSpeed; //耗电速度 低0.33  中0.5 高1
	float fee;	//费用
}
