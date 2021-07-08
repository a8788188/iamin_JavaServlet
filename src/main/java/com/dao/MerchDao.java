package com.dao;

import java.util.List;

import com.bean.Merch;

public interface MerchDao {
	
	List<Merch> selectAllByGroupId(int GroupID);

}
