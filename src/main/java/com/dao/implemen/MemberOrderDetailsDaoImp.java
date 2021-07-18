package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import com.bean.MemberOrderDetails;
import com.dao.MemberOrderDetailsDao;
import com.dao.common.ServiceLocator;

public class MemberOrderDetailsDaoImp implements MemberOrderDetailsDao {
    DataSource dataSource;

    public MemberOrderDetailsDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(MemberOrderDetails memberOrderDetails) {
        String sql = "INSERT INTO member_order_details" + 
        		"(MEMBER_ORDER_ID,MERCH_ID,QUANTITY,FORMAT_TOTAL)" + 
        		"VALUES(?, ?, ?, ?);";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, memberOrderDetails.getMemberOrderId());
            ps.setInt(2, memberOrderDetails.getMerchId());
            ps.setInt(3, memberOrderDetails.getQuantity());
            ps.setInt(4, memberOrderDetails.getFormat_total());
            ps.executeUpdate();
        }catch (SQLException e) {
			e.printStackTrace();
		} 
        return 0;
    }

    @Override
    public int update(MemberOrderDetails memberOrderDetails) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MemberOrderDetails selectById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrderDetails> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrderDetails> selectAllByMemberOrderDaoId(int MemberOrderDaoId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
