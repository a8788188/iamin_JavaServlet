package com.dao;

import java.util.List;

import com.data.Homedata;

public interface HomedataDao {
	
	List<Homedata> selectAllgroup();
	
	List<Homedata> selectAllgroupPrice(int GroupID);
	
	byte[] getGroupimage(int GroupID);
}
