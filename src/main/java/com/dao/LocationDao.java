package com.dao;

import java.util.List;

import com.bean.Location;


public interface LocationDao {
    int insert(Location location);

    int delete(int id);
    
    int update(Location location);

    List<Location> selectAll();
    
    int deleteByGroupId(int groupId);
    
    List<Location> selectAllByGroupId(int GroupId);
}
