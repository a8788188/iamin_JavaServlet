package com.dao;

import java.util.List;

import com.bean.GroupBlockade;

public interface GroupBlockadeDao {
    int insert(GroupBlockade groupBlockade);

    int delete(int id);
    
    int update(GroupBlockade groupBlockade);

    List<GroupBlockade> selectAll();
    
    List<GroupBlockade> selectAllByMembreId(int memberId);
}
