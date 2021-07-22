package com.dao;

import java.util.List;

import com.bean.MemberOrderDetails;

public interface MemberOrderDetailsDao {
    int insert(MemberOrderDetails memberOrderDetails);

    int update(MemberOrderDetails memberOrderDetails);

    int delete(int id);
    
    MemberOrderDetails selectById(int id);
    
    List<MemberOrderDetails> selectAll();
    //單數
    List<MemberOrderDetails> selectAllByMemberOrderId(int MemberOrderId);
    //複數
    List<MemberOrderDetails> selectAllByMemberOrderIds(int[] MemberOrderIds);
}
