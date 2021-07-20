package com.dao;

import java.util.List;

import com.bean.Group;

public interface GroupDao {
    int insert(Group group);
    
    int updateGroupStatus();

    int delete(int id);
    
    int update(Group group);
    
    Group selectById(int id);

    List<Group> selectAll();
    
    List<Group> selectAllByMemberId(int memberId);
}
