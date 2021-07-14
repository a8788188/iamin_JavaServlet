package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.GroupCategory;
import com.dao.GroupCategoryDao;
import com.dao.common.ServiceLocator;

public class GroupCategoryDaoImp implements GroupCategoryDao {
    DataSource dataSource;
    
    public GroupCategoryDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(GroupCategory groupCategory) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(GroupCategory groupCategory) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<GroupCategory> selectAll() {
        String sql = "SELECT GROUP_CATEGORY_ID, CATEGORY " 
                + "FROM group_category ";
        
        List<GroupCategory> groupCategories = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                GroupCategory groupCategory = new GroupCategory(
                        rs.getInt(1), rs.getString(2));
                
                groupCategories.add(groupCategory);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return groupCategories;
    }

}
