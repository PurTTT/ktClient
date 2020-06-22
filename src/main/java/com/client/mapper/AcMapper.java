package com.client.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.client.entity.Bill;
import com.client.entity.BillList;
import com.client.entity.Room;
@Mapper
public interface AcMapper {
	
	@Update("update room set pattern=(#{pattern}),upperTem=(#{upperTem}),lowerTem=(#{lowerTem})")
	int updateMode(@Param("pattern") Integer mode,@Param("upperTem") 
	Float upperTem,@Param("lowerTem") Float lowerTemroomNum);
	
	@Update("update room set fee=(#{fee})")
	int updateFee(@Param("fee") Integer fee);
	
	@Update("update room set state=(#{state}) where roomNum = (#{roomNum})")
	int changeState(@Param("state") Integer state,@Param("roomNum") Integer roomNum);
	
	@Update("update room set windSpeed=(#{windSpeed}),targetTemperature=(#{targetTemperature})"
			+ " where roomNum = (#{roomNum})")
	int updateTandW(@Param("windSpeed") Integer windSpeed,@Param("targetTemperature") 
	double tem,@Param("roomNum") Integer roomNum);
	

	@Select("select state from room where roomNum = (#{roomNum})")
	int selectState(@Param("roomNum") Integer roomNum);
	
	@Select("select windSpeed from room where roomNum = (#{roomNum})")
	int findWind(@Param("roomNum") Integer roomNum);
	
	@Select("select * from room")
	List<Room> findRoomList();

	@Select("select * from room where roomNum = (#{roomNum})")
	Room findRoom(@Param("roomNum") Integer roomNum);
	
	@Update("update room set temperature = temperature + 0.4 where windSpeed = 1 "
			+ "and targetTemperature > temperature and state = 2")
	int ScheduleUpdate();
	@Update("update room set temperature = temperature + 0.5 where windSpeed = 2 "
			+ "and targetTemperature > temperature and state = 2")
	int ScheduleUpdate1();
	@Update("update room set temperature = temperature + 0.6 where windSpeed = 3 "
			+ "and targetTemperature > temperature and state = 2")
	int ScheduleUpdate2();
	@Update("update room set temperature = temperature - 0.4 where windSpeed = 1 "
			+ "and targetTemperature < temperature and state = 2")
	int ScheduleUpdate3();
	@Update("update room set temperature = temperature - 0.5 where windSpeed = 2 "
			+ "and targetTemperature < temperature and state = 2")
	int ScheduleUpdate4();
	@Update("update room set temperature = temperature - 0.6 where windSpeed = 3 "
			+ "and targetTemperature < temperature and state = 2")
	int ScheduleUpdate5();
	
	/*DB-BillList*/
	@Insert("insert into BillList(roomId,beginTime,price,eSpeed) "
			+ "value(#{roomId},#{beginTime},#{price},#{eSpeed})")
	int newBill(@Param("roomId") Integer roomId,@Param("beginTime") String beginTime,
			@Param("price") Integer price,@Param("eSpeed") double eSpeed);
	
	@Update("update BillList set endTime = (#{endTime}),windTime = (#{windTime}),"
			+ "fee = (#{fee}) where roomId = (#{roomId}) and endTime is null")
	int updateBill(@Param("endTime") String endTime,@Param("windTime") long windTime,
			@Param("fee") double fee,@Param("roomId") Integer roomId);
	
	@Select("select * from BillList where roomId = (#{roomId}) and fee is not null")
	List<BillList> selectBillList(@Param("roomId") Integer roomId);
	@Select("select * from Bill where roomId = (#{roomId})")
	Bill selectBill(@Param("roomId") Integer roomId);
	@Select("select sum(fee) From BillList where roomId = (#{roomId})")
	double totalFee(@Param("roomId") Integer roomId);
	
	/*DB-totalBill*/
	@Update("update Bill set inTime = (#{inTime}) where roomId = (#{roomId})")
	int updateIntime(@Param("inTime") String inTime,@Param("roomId") Integer roomId);
	@Update("update Bill set outTime = (#{outTime}) roomId = (#{roomId})")
	int updateOuttime(@Param("outTime") String outTime,@Param("roomId") Integer roomId);
	@Update("update Bill set totalFee = (#{totalFee}) where roomId = (#{roomId})")
	int updateTotalFee(@Param("totalFee") double totalFee,@Param("roomId") Integer roomId);
	
	/*DB-request*/
	@Insert("insert into Request(roomId,wind,temperature,create_time) "
			+ "value(#{roomId},#{wind},#{temperature},#{create_time})")
	int newRequest(@Param("roomId") Integer roomId,@Param("wind") Integer wind,
			@Param("temperature") double temperature,@Param("create_time") String create_time);
	
}
