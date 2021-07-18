package com.dao;

import java.util.List;

import com.bean.MemberOrder;

public interface MemberOrderDao {
	
    int insert(MemberOrder memberOrder);

    int update(MemberOrder memberOrder);

    int delete(int id);
    
    MemberOrder selectById(int id);
    
    List<MemberOrder> selectAll();
    
    List<MemberOrder> selectAllByGroupId(int groupId);
}
