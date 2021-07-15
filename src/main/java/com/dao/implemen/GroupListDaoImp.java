package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.bean.Merch;
import com.dao.GroupListDao;
import com.dao.common.ServiceLocator;

public class GroupListDaoImp implements GroupListDao {
    DataSource dataSource;

    public GroupListDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(int GroupId, int merchId) {
        int count = 0;
        String sql = "INSERT INTO plus_one.group_list " + 
                "(GROUP_ID, MERCH_ID) " +
                "VALUES(?, ?);";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, GroupId);
            ps.setInt(2, merchId);
            
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Merch> selectMerchIdByGroupId(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int deleteByGroupId(int groupId) {
        int count = 0;
        String sql = "UPDATE plus_one.group_list SET DELETE_TIME = NOW() WHERE GROUP_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, groupId);
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }
    
}
