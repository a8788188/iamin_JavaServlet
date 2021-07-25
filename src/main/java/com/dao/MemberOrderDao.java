package com.dao;

import java.util.List;
import java.util.Map;

import com.bean.MemberOrder;

public interface MemberOrderDao {
	
    int insert(MemberOrder memberOrder);

    int update(MemberOrder memberOrder);

    int delete(int id);
    
    MemberOrder selectById(int id);
    
    List<MemberOrder> selectAllByMemberId(int memberId);
    
    List<MemberOrder> selectAll();
    
    List<MemberOrder> selectAllByGroupId(int groupId);
    
    Map<MemberOrder, String> selectAllAndTokenByGroupId(int groupId);
    
    int updateDeliverStatus(int memberOrderId, boolean status);

    int updateReceivePaymentStatus(int memberOrderId, boolean status);
}
