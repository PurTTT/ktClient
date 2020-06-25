package com.client.mapper;
import java.util.List;

import com.client.entity.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AcMapper {

	@Update("update room set pattern=(#{pattern}),upperTem=(#{upperTem}),lowerTem=(#{lowerTem}),fee=(#{fee})")
	int updateMode(@Param("pattern") Integer mode, @Param("upperTem")
			Float upperTem, @Param("lowerTem") Float lowerTemroomNum, @Param("fee") Integer fee);

	@Update("update room set fee=(#{fee})")
	int updateFee(@Param("fee") Integer fee);

	@Update("update room set state=(#{state}) where roomNum = (#{roomNum})")
	int changeState(@Param("state") Integer state, @Param("roomNum") Integer roomNum);

	@Update("update room set windSpeed=(#{windSpeed}),targetTemperature=(#{targetTemperature}), state=(#{state}) "
			+"where roomNum = (#{roomNum})")
	int updateTandW(@Param("windSpeed") Integer windSpeed, @Param("targetTemperature")
			double tem, @Param("roomNum") Integer roomNum, @Param("state") Integer state);

	@Update("update room set openTime = openTime + 1 where roomNum = (#{roomNum})")
	int addOpenTime(@Param("roomNum") Integer roomNum);

	@Update("update room set windRequest = windRequest + 1 where roomNum = (#{roomNum})")
	int addWR(@Param("roomNum") Integer roomNum);

	@Update("update room set tempRequest = tempRequest + 1 where roomNum = (#{roomNum})")
	int addTR(@Param("roomNum") Integer roomNum);

	@Update("update room set dispatchTime = dispatchTime + 1 where roomNum = (#{roomNum})")
	int updateDispatchTime(@Param("roomNum") Integer roomNum);

	@Update("update room set windSpeed = 0, tagetTemperature = 26 where roomNum = (#{roomNum})")
	int updateRoomWhenClose(@Param("roomNum") Integer roomNum);


	@Select("select state from room where roomNum = (#{roomNum})")
	int selectState(@Param("roomNum") Integer roomNum);

	@Select("select windSpeed from room where roomNum = (#{roomNum})")
	int findWind(@Param("roomNum") Integer roomNum);

	@Select("select roomNum from room")
	List<Integer> selectRoomNum();

	@Select("select roomNum from room where windSpeed != 0")
	List<Integer> selectWorkingRoom();

	@Select("select * from room")
	List<Room> findRoomList();

	@Select("select roomNum,pattern,temperature,windSpeed from room where roomNum = (#{roomNum})")
	List<UserCheck> findRoom(@Param("roomNum") Integer roomNum);

	@Select("select upperTem from room where roomNum = (#{roomNum})")
	float selectUT(@Param("roomNum") Integer roomNum);

	@Select("select lowerTem from room where roomNum = (#{roomNum})")
	float selectLT(@Param("roomNum") Integer roomNum);

	@Update("update room set temperature = temperature + 0.2 where windSpeed = 1 "
			+ "and targetTemperature > temperature and state = 2")
	int ScheduleUpdate();

	@Update("update room set temperature = temperature + 0.25 where windSpeed = 2 "
			+ "and targetTemperature > temperature and state = 2")
	int ScheduleUpdate1();

	@Update("update room set temperature = temperature + 0.3 where windSpeed = 3 "
			+ "and targetTemperature > temperature and state = 2")
	int ScheduleUpdate2();

	@Update("update room set temperature = temperature - 0.2 where windSpeed = 1 "
			+ "and targetTemperature < temperature and state = 2")
	int ScheduleUpdate3();

	@Update("update room set temperature = temperature - 0.25 where windSpeed = 2 "
			+ "and targetTemperature < temperature and state = 2")
	int ScheduleUpdate4();

	@Update("update room set temperature = temperature - 0.3 where windSpeed = 3 "
			+ "and targetTemperature < temperature and state = 2")
	int ScheduleUpdate5();

	@Update("update room set temperature = temperature - 0.5 where roomNum = (#{roomNum}) "
			+ "and temperature - oriTemperature >= 0.5 and state != 2")
	int updateNotWorking1(@Param("roomNum") Integer roomNum);
	@Update("update room set temperature = temperature + 0.5 where roomNum = (#{roomNum}) "
			+ "and oriTemperature - temperature >= 0.5 and state != 2")
	int updateNotWorking2(@Param("roomNum") Integer roomNum);
	@Update("update room set temperature = oriTemperature where roomNum = (#{roomNum}) "
			+ "and temperature - oriTemperature < 0.5 and state != 2")
	int updateNotWorking3(@Param("roomNum") Integer roomNum);
	@Update("update room set temperature = oriTemperature where roomNum = (#{roomNum}) "
			+ "and oriTemperature - temperature < 0.5 and state != 2")
	int updateNotWorking4(@Param("roomNum") Integer roomNum);

	@Update("update room set state = 3 where roomNum = (#{roomNum})"
			+ " and ((pattern = 0 and temperature <= targetTemperature) or (pattern = 1 and temperature >= targetTemperature))")
	int pause(@Param("roomNum") Integer roomNum);

	@Update("update room set state = 2 where roomNum = (#{roomNum})"
			+ " and ((pattern = 0 and temperature >= targetTemperature + 1.0) or (pattern = 1 and temperature <= targetTemperature - 1.0))")
	int restart(@Param("roomNum") Integer roomNum);

	/*DB-BillList*/
	@Insert("insert into BillList(roomId,beginTime,price,eSpeed) "
			+ "value(#{roomId},#{beginTime},#{price},#{eSpeed})")
	int newBill(@Param("roomId") Integer roomId, @Param("beginTime") String beginTime,
				@Param("price") Integer price, @Param("eSpeed") double eSpeed);

	@Update("update BillList set endTime = (#{endTime}),windTime = (#{windTime}), fee = (#{fee}) "
	+ "where roomId = (#{roomId}) and endTime is null")
	int updateBill(@Param("endTime") String endTime, @Param("windTime") long windTime,
				   @Param("fee") double fee, @Param("roomId") Integer roomId);

	@Select("SELECT * FROM BillList WHERE roomId = (#{roomId}) and fee IS NOT NULL"
	+ " and beginTime >= (#{inTime}) and endTime <= (#{outTime})")
	List<BillList> selectBillList(@Param("roomId") Integer roomId, @Param("inTime") String inTime, @Param("outTime") String outTime);

	@Select("select * from Bill where roomId = (#{roomId}) and outTime = (#{outTime})")
	List<Bill> selectBill(@Param("roomId") Integer roomId, @Param("outTime") String outTime);

	@Select("select sum(fee) From BillList where roomId = (#{roomId})")
	double totalFee(@Param("roomId") Integer roomId);

	@Select("SELECT roomId, MAX(openTime) as openTime, SUM(windTime) as totalTime, SUM(fee) as totalFee, MAX(dispatchTime) as dispatchTime, COUNT(roomId) as BillNum, MAX(tempRequest) as tempRequest, MAX(windRequest) as windRequest "
			+ "FROM BillList NATURAL JOIN roomList "
			+"WHERE date_format(endTime,'%Y-%m-%d')<= (#{endTime})"
			+ " and date_format(beginTime,'%Y-%m-%d') >= (#{beginTime}) GROUP BY roomId")
	List<DailySheet> checkDailySheet(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

	/*DB-totalBill*/
	@Insert("insert into Bill(roomId) value(#{roomId})")
	int newLogIn(@Param("roomId") Integer roomId);

	@Update("update Bill set inTime = (#{inTime}) where roomId = (#{roomId})")
	int updateIntime(@Param("inTime") String inTime, @Param("roomId") Integer roomId);

	@Update("update Bill set outTime = (#{outTime}) where roomId = (#{roomId})")
	int updateOuttime(@Param("outTime") String outTime, @Param("roomId") Integer roomId);

	@Update("update Bill set totalFee = (#{totalFee}) where roomId = (#{roomId})")
	int updateTotalFee(@Param("totalFee") double totalFee, @Param("roomId") Integer roomId);

	@Select("select inTime from Bill where roomId = (#{roomId}) and outTime = (#{outTime})")
	String selectInTime(@Param("roomId") Integer roomId, @Param("outTime") String outTime);

	@Select("select * from Bill where roomId = (#{roomId}) and totalFee is not null")
	List<Bill> selectFullBill(@Param("roomId") Integer roomId);

	/*DB-request*/
	@Insert("insert into Request(roomId,wind,temperature,create_time) "
			+ "value(#{roomId},#{wind},#{temperature},#{create_time})")
	int newRequest(@Param("roomId") Integer roomId, @Param("wind") Integer wind,
				   @Param("temperature") double temperature, @Param("create_time") String create_time);

}
