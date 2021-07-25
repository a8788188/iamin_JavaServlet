package com.dao;

import com.bean.Rating;

public interface RatingDao {

	int insert(Rating rating);
	
	
	//取得賣家的評價總分與總數 相除得到賣家的平均評價分數後 updata member
	//取得賣家的評價總分
	int selectSumrating(int seller_id);
	//取得賣家的評價總數
	int selectCountrating(int seller_id);
}
