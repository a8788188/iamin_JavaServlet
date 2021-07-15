package com.dao;

import java.util.List;

import com.bean.GroupCategory;

public interface GroupCategoryDao {
    int insert(GroupCategory groupCategory);

    int delete(int id);
    
    int update(GroupCategory groupCategory);

    List<GroupCategory> selectAll();
}
