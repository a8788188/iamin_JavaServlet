package com.dao;

import java.util.List;

import com.bean.Merch;

public interface GroupListDao {
    int insert(int GroupId, int merchId);

    int delete(int id);
    
    List<Merch> selectMerchIdByGroupId(int id);
    
    int deleteByGroupId(int groupId);
}
