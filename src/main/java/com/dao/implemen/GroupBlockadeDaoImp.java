package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.GroupBlockade;
import com.bean.Location;
import com.dao.GroupBlockadeDao;
import com.dao.common.ServiceLocator;

public class GroupBlockadeDaoImp implements GroupBlockadeDao {
    DataSource dataSource;
    
    public GroupBlockadeDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(GroupBlockade groupBlockade) {
        int count = 0;
        String sql = "INSERT INTO plus_one.group_blockade" + 
                "(GROUP_ID, MEMBER_ID, GROUP_NAME, REASON) " + 
                "VALUES(?, ?, ?, ?);";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, groupBlockade.getGroupId());
            ps.setInt(2, groupBlockade.getMemberId());
            ps.setString(3, groupBlockade.getGroupName());
            ps.setString(4, groupBlockade.getReason());
            
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
    public int update(GroupBlockade groupBlockade) {
        int count = 0;
        String sql = "UPDATE plus_one.group_blockade SET NOTIFY = ? " 
                    +"WHERE GROUP_BLOCKADE_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setBoolean(1, true);
            ps.setInt(2, groupBlockade.getGroupBlockadeId());
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public List<GroupBlockade> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<GroupBlockade> selectAllByMembreId(int memberId) {
        String sql = "SELECT GROUP_BLOCKADE_ID, GROUP_ID, MEMBER_ID, GROUP_NAME, REASON, NOTIFY " 
                + "FROM plus_one.group_blockade "
                + "WHERE MEMBER_ID = ?;";
        
        List<GroupBlockade> groupBlockades = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                GroupBlockade groupBlockade = new GroupBlockade(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getBoolean(6)
                        );
                groupBlockades.add(groupBlockade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupBlockades;
    }
}
