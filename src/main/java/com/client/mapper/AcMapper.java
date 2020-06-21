package com.client.mapper;
import java.awt.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.client.entity.Room;
@Mapper
public interface AcMapper {
	@Insert("insert into room(roomNum) value(#{roomNum})")
	int newCustomer(@Param("roomNum") Integer roomNum);
	
	@Delete("delete from room where roomNum = (#{roomNum})")
	int deleteOne(@Param("roomNum") Integer roomNum);
	
	@Update("update room set mode=(#{mode}),upperTem=(#{upperTem}),lowerTem=(#{lowerTem})")
	int updateMode(@Param("mode") Integer mode,@Param("upperTem") 
	Float upperTem,@Param("lowerTem") Float lowerTemroomNum);
	
	@Update("update room set fee=(#{fee})")
	int updateFee(@Param("fee") Integer fee);
	
	@Update("update room set state=(#{state})")
	int changeState(@Param("state") Integer state);
	
	@Update("update room set windSpeed=(#{windSpeed}),targetTemperature=(#{targetTemperature})"
			+ " where roomNum = (#{roomNum})")
	int updateTandW(@Param("windSpeed") Integer windSpeed,@Param("targetTemperature") 
	double tem,@Param("roomNum") Integer roomNum);
	
	@Select("select * from room")
	List findRoomList();
	
	@Select("select * from room where roomNum = (#{roomNum})")
	Room findRoom(@Param("roomNum") Integer roomNum);
	
	@Update("update room set temperature = temperature + 0.4 where windSpeed = 1 and targetTemperature > temperature")
	int ScheduleUpdate();
	
	/*DB-BillList*/
	@Insert("insert into BillList(roomId,beginTime,price,eSpeed) "
			+ "value(#{roomId},#{beginTime},#{price},#{eSpeed})")
	int newBill(@Param("roomId") Integer roomId,@Param("beginTime") String beginTime,
			@Param("price") Integer price,@Param("eSpeed") double eSpeed);
	@Update("update BillList set endTime = (#{endTime}),windTime = (#{windTime}),"
			+ "fee = (#{fee}) where roomId = (#{roomId}) and endTime is null")
	int updateBill(@Param("endTime") String endTime,@Param("windTime") long windTime,
			@Param("fee") double fee,@Param("roomId") Integer roomId);
}
