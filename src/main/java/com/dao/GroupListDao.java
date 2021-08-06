package com.dao;

import java.util.List;

import com.bean.Merch;

public interface GroupListDao {
    int insert(int GroupId, int merchId);

    int delete(int id);
    
    List<Integer> selectMerchIdByGroupId(int groupId);
    
    int deleteByGroupId(int groupId);
}
