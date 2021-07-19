package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.MemberOrder;
import com.bean.MemberOrderDetails;
import com.bean.Merch;
import com.dao.MemberOrderDetailsDao;
import com.dao.MerchDao;
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
        
        return null;
    }

    @Override
    public List<MemberOrderDetails> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrderDetails> selectAllByMemberOrderId(int MemberOrderDaoId) {
    	List<MemberOrderDetails> detailsList = new ArrayList<>();
    	List<Merch> merchs = new ArrayList<Merch>();
    	MerchDao merchDao = new MerchDaoImp();
    	final String sql = "SELECT * FROM member_order_details WHERE member_order_id = ?";
        MemberOrderDetails memberOrderDetails = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
        	ps.setInt(1, MemberOrderDaoId);
        	ResultSet rs = ps.executeQuery();
        	while(rs.next()) {
        		
        		
        		memberOrderDetails = new MemberOrderDetails(rs.getInt(1), 
        													rs.getInt(2), 
							        						rs.getInt(3), 
							        						rs.getInt(4), 
							        						rs.getInt(5));
        		
        		detailsList.add(memberOrderDetails);
        	}
        	return detailsList;
        } catch (SQLException e) {
			e.printStackTrace();
		}
    	
        return null;
    }
    
    public List<MemberOrderDetails> selectAllByMemberOrderIds(int[] MemberOrderIds) {
        String sql = 
                "SELECT MEMBER_ORDER_DETAILS_ID, MEMBER_ORDER_ID, d.MERCH_ID, QUANTITY, FORMAT_TOTAL, m.NAME " +
                "FROM plus_one.member_order_details d JOIN plus_one.merch m ON d.MERCH_ID = m.MERCH_ID " +
                "WHERE MEMBER_ORDER_ID IN (?);";
        
        String in = "";
        for (int i = 0; i < MemberOrderIds.length; i++) {
            in = in + MemberOrderIds[i];
            if(i < MemberOrderIds.length - 1) {
                in = in + ", ";
            }
        }
        sql = sql.replace("(?)", "(" + in +")");
        System.out.println(sql);
        
        List<MemberOrderDetails> memberOrderDetailss = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                MemberOrderDetails memberOrderDetails  = new MemberOrderDetails(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getString(6)
                        );
                
                memberOrderDetailss.add(memberOrderDetails);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberOrderDetailss;
    }
}
