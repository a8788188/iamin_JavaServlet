package com.dao;

import java.util.List;

import com.bean.Group;

public interface GroupDao {
    int insert(Group group);

    int delete(int id);

    Group selectById(int id);

    List<Group> selectAll();
    
    List<Group> selectAllByMemberId(int memberId);
}
