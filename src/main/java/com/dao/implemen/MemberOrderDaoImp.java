package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import com.bean.MemberOrder;
import com.dao.MemberOrderDao;
import com.dao.common.ServiceLocator;
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
        // TODO Auto-generated method stub
        return 0;
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
    public List<MemberOrder> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrder> selectAllByGroupId(int groupId) {
        // TODO Auto-generated method stub
        return null;
    }
}
