package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.bean.MemberOrder;
import com.bean.MemberOrderDetails;
import com.bean.Merch;
import com.dao.MemberOrderDao;
import com.dao.MemberOrderDetailsDao;
import com.dao.common.ServiceLocator;
import com.data.MyWallet;
import com.google.firebase.database.Transaction.Result;

public class MemberOrderDaoImp implements MemberOrderDao {
    DataSource dataSource;

    public MemberOrderDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(MemberOrder memberOrder) {
    	int MEMBER_ORDER_ID = 0;
    	String sql = "INSERT INTO member_order\n" + 
    			"(MEMBER_ID, GROUP_ID, PAYENT_METHOD, TOTAL, RECEIVE_PAYMENT_STATUS, DELIVER_STATUS)\n" + 
    			"VALUES(?, ?, ?, ?, ?, ?);";
    	 try (
                 Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
         ) {
             ps.setInt(1, memberOrder.getMemberId());
             ps.setInt(2, memberOrder.getGroupId());
             ps.setInt(3, memberOrder.getPayentMethod());
             ps.setInt(4, memberOrder.getTotal());
             ps.setBoolean(5, memberOrder.isReceivePaymentStatus());
             ps.setBoolean(6, memberOrder.isDeliverStatus());               
             ps.executeUpdate();
             ResultSet rs = ps.getGeneratedKeys();
             if (rs.next()) {
				MEMBER_ORDER_ID = rs.getInt(1);
			}
    	 }catch (SQLException e) {
             e.printStackTrace();
         }
        return MEMBER_ORDER_ID;
    }

    @Override
    public int update(MemberOrder memberOrder) {
        int count = 0;
        String sql = "UPDATE plus_one.member_order SET MEMBER_ID = ?, GROUP_ID = ?, PAYENT_METHOD = ?, " +
                     "TOTAL = ?, RECEIVE_PAYMENT_STATUS = ?, DELIVER_STATUS = ? " +
                     "WHERE MEMBER_ORDER_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, memberOrder.getMemberId());
            ps.setInt(2, memberOrder.getGroupId());
            ps.setInt(3, memberOrder.getPayentMethod());
            ps.setInt(4, memberOrder.getTotal());
            ps.setBoolean(5, memberOrder.isReceivePaymentStatus());
            ps.setBoolean(6, memberOrder.isDeliverStatus());
            ps.setInt(7, memberOrder.getMemberOrderId());
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
    public MemberOrder selectById(int id) {
    	// TODO Auto-generated method stub
        return null;
    }
    
    @Override
	public List<MemberOrder> selectAllByMemberId(int memberId) {
    	List<MemberOrder> list = new ArrayList<MemberOrder>();
    	MemberOrderDetailsDao memberOrderDetailsDao = new MemberOrderDetailsDaoImp();
    	String sql = "SELECT * FROM member_order WHERE member_id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
        	ps.setInt(1, memberId);
        	ResultSet rs = ps.executeQuery();
        	while(rs.next()) {
        		
        		List<MemberOrderDetails> detailsList = 
        				memberOrderDetailsDao.selectAllByMemberOrderId(rs.getInt("MEMBER_ORDER_ID"));
        		
        		MemberOrder memberOrder = new MemberOrder(
        										rs.getInt(1), 
        										rs.getInt(2), 
        										rs.getInt(3), 
        										rs.getInt(4), 
        										rs.getInt(5), 
        										rs.getBoolean(6), 
        										rs.getBoolean(7),
        										detailsList);
        		
        		list.add(memberOrder);
        	}
        	return list;
        	
        } catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

    @Override
    public List<MemberOrder> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrder> selectAllByGroupId(int groupId) {
        String sql = "SELECT MEMBER_ORDER_ID, o.MEMBER_ID, GROUP_ID, PAYENT_METHOD, TOTAL, RECEIVE_PAYMENT_STATUS, " +
                     "DELIVER_STATUS, m.NICKNAME, m.PHONE " +
                     "FROM plus_one.member_order o JOIN plus_one.member m ON o.MEMBER_ID = m.MEMBER_ID " +
                     "WHERE GROUP_ID = ? " +
                     "ORDER BY o.START_TIME DESC;";
        
        List<MemberOrder> memberOrders = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                MemberOrder memberOrder = new MemberOrder(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getBoolean(6),
                        rs.getBoolean(7),
                        rs.getString(8),
                        rs.getString(9)
                        );
                
                memberOrders.add(memberOrder);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberOrders;
    }

    @Override
    public Map<MemberOrder, String> selectAllAndTokenByGroupId(int groupId) {
        String sql = "SELECT MEMBER_ORDER_ID, o.MEMBER_ID, GROUP_ID, PAYENT_METHOD, TOTAL, RECEIVE_PAYMENT_STATUS, " +
                     "DELIVER_STATUS, m.FCM_TOKEN " +
                     "FROM plus_one.member_order o JOIN plus_one.member m ON o.MEMBER_ID = m.MEMBER_ID " +
                     "WHERE GROUP_ID = ? " +
                     "ORDER BY o.START_TIME DESC;";
        
        Map<MemberOrder, String> map = new HashMap<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                MemberOrder memberOrder = new MemberOrder(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getBoolean(6),
                        rs.getBoolean(7)
                        );
                
                map.put(memberOrder, rs.getString(8));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public int updateDeliverStatus(int memberOrderId, boolean status) {
        int count = 0;
        String sql = "UPDATE plus_one.member_order SET DELIVER_STATUS = ? " +
                     "WHERE MEMBER_ORDER_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setBoolean(1, status);
            ps.setInt(2, memberOrderId);
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }
}
