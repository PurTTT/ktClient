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
	
	@Update("update room set windSpeed=(#{windSpeed}),targetTemperature=(#{targetTemperature})"
			+ " where roomNum = (#{roomNum})")
	int updateTandW(@Param("windSpeed") Integer windSpeed,@Param("targetTemperature") 
	Float targetTemperature,@Param("roomNum") Integer roomNum);
	
	@Select("select * from room")
	List findRoomList();
	
	@Select("select * from room where roomNum = (#{roomNum})")
	Room findRoom(@Param("roomNum") Integer roomNum);
	
}
