package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.Group;
import com.dao.GroupDao;
import com.dao.common.ServiceLocator;

public class GroupDaoImp implements GroupDao {
    DataSource dataSource;

    public GroupDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(Group group) {
        int count = 0;
        String sql = "INSERT INTO plus_one.group " + 
                "(MEMBER_ID, NAME, PROGRESS, GOAL, GROUP_CATEGORY_ID, GROUP_ITEM, CONTACT_NUMBER, PAYMENT_METHOD, " +
                "GROUP_STATUS, CAUTION, PRIVACY_FLAG, TOTAL_AMOUNT, AMOUNT, CONDITION_COUNT, CONDITION_TIME) " + 
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        // 取得自動編號
        String[] generatedColumns = {"GROUP_ID"};
        int insertGroupId = 0;
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, generatedColumns);
        ) {
            ps.setInt(1, group.getMemberId());
            ps.setString(2, group.getName());
            ps.setInt(3, group.getProgress());
            ps.setInt(4, group.getGoal());
            ps.setInt(5, group.getCategoryId());
            ps.setString(6, group.getGroupItem());
            ps.setString(7, group.getContactNumber());
            ps.setInt(8, group.getPaymentMethod());
            ps.setInt(9, group.getGroupStatus());
            ps.setString(10, group.getCaution());
            ps.setBoolean(11, group.getPrivacyFlag());
            ps.setInt(12, group.getTotalAmount());
            ps.setInt(13, group.getAmount());
            ps.setInt(14, group.getConditionCount());
            ps.setTimestamp(15, group.getConditionTime());
            
            count = ps.executeUpdate();
            if (count > 0) {
             // 取得自動編號
                ResultSet rs = ps.getGeneratedKeys();
                rs.next(); // 需先移動游標
                insertGroupId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return insertGroupId;
    }

    @Override
    public int delete(int id) {
        int count = 0;
        String sql = "UPDATE plus_one.group SET DELETE_TIME = NOW() WHERE GROUP_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public Group selectById(int id) {
        String sql = "SELECT * " 
                + "FROM plus_one.group "
                + "WHERE GROUP_ID = ?; ";
        
        Group group = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                group = new Group(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getInt(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getBoolean(12),
                        rs.getInt(13),
                        rs.getInt(14),
                        rs.getInt(15),
                        rs.getTimestamp(16)
                        );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }
    
    @Override
    public List<Group> selectAll() {
        String sql = "SELECT * " + 
        		"FROM plus_one.group " + 
        		"WHERE DELETE_TIME IS null " + 
        		"ORDER BY START_TIME DESC;";
        
        List<Group> groupList = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Group group = new Group(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getInt(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getBoolean(12),
                        rs.getInt(13),
                        rs.getInt(14),
                        rs.getInt(15),
                        rs.getTimestamp(16)
                        );
                groupList.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    @Override
    public List<Group> selectAllByMemberId(int memberId) {
        String sql = "SELECT GROUP_ID, MEMBER_ID, NAME, PROGRESS, GOAL, g.GROUP_CATEGORY_ID, " 
                + "GROUP_ITEM, CONTACT_NUMBER, PAYMENT_METHOD, GROUP_STATUS, CAUTION, "
                + "PRIVACY_FLAG, TOTAL_AMOUNT, AMOUNT, CONDITION_COUNT, CONDITION_TIME, c.CATEGORY "
                + "FROM (plus_one.group g JOIN plus_one.group_category c ON g.GROUP_CATEGORY_ID = c.GROUP_CATEGORY_ID) "
                + "WHERE MEMBER_ID = ? AND g.DELETE_TIME IS NULL "
                + "ORDER BY g.START_TIME DESC;";
        
        List<Group> groupList = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Group group = new Group(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getInt(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getBoolean(12),
                        rs.getInt(13),
                        rs.getInt(14),
                        rs.getInt(15),
                        rs.getTimestamp(16)
                        );
                group.setCategory(rs.getString(17));
                groupList.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupList;
    }

	@Override
	public int update(Group group) {
		String sql = "UPDATE plus_one.group \n" + 
				"SET NAME = ? , PROGRESS = ? ,GOAL = ? ,\n" + 
				"GROUP_CATEGORY_ID = ? , GROUP_ITEM = ? ,\n" + 
				" CONTACT_NUMBER = ? , PAYMENT_METHOD = ? ,\n" + 
				" GROUP_STATUS = ? , CAUTION = ? , PRIVACY_FLAG = ? ,\n" + 
				" TOTAL_AMOUNT = ? ,AMOUNT = ? ,CONDITION_COUNT = ?,\n" + 
				" CONDITION_TIME = ?\n" + 
				"WHERE GROUP_ID = ?;";
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			ps.setString(1, group.getName());
			ps.setInt(2, group.getProgress());
			ps.setInt(3, group.getGoal());
			ps.setInt(4, group.getCategoryId());
			ps.setNString(5, group.getGroupItem());
			ps.setString(6, group.getContactNumber());
			ps.setInt(7, group.getPaymentMethod());
			ps.setInt(8, group.getGroupStatus());
			ps.setNString(9, group.getCategory());
			ps.setBoolean(10, group.getPrivacyFlag());
			ps.setInt(11, group.getTotalAmount());
			ps.setInt(12, group.getAmount());
			ps.setInt(13, group.getConditionCount());
			ps.setTimestamp(14, group.getConditionTime());
			ps.setInt(15, group.getGroupId());
			ps.executeUpdate();
			return 0;
		}catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
	}
    @Override
    public int updateGroupStatus() {
        int count = 0;
        System.out.println("updateGroupStatus");
        String sql = "UPDATE plus_one.group SET GROUP_STATUS = 3 " +
                     "WHERE CONDITION_TIME < NOW();";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }
}
