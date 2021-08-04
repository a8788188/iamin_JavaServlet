package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.Rating;
import com.dao.RatingDao;
import com.dao.common.ServiceLocator;

public class RatingDaoImp implements RatingDao {
	DataSource dataSource;
	
	
	public RatingDaoImp() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
	
	
	@Override
	public int insert(Rating rating) {
		String sql = "INSERT INTO rating\n" + 
				"(member_order_id,buy_id,seller_id,order_rating,rating_message,group_name)\n" + 
				"VALUES(?, ?, ?, ?, ?, ?);";
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			ps.setInt(1,rating.getMember_order_id());
			ps.setInt(2, rating.getBuyer_Id());
			ps.setInt(3, rating.getSeller_Id());
			ps.setInt(4, rating.getOrder_rating());
			ps.setString(5, rating.getRating_message());
			ps.setString(6,rating.getGroup_name());
			
			ps.executeUpdate();
			return 1;
		}catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
	}


	@Override
	public int selectSumrating(int seller_id) {
		String sql = "select sum(order_rating) " + 
				"from rating " + 
				"where seller_id = ?;";
		int sum = 0;
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			ps.setInt(1, seller_id);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sum = rs.getInt(1);
			}
			return sum;
		}catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
	}


	@Override
	public int selectCountrating(int seller_id) {
		String sql = "select count(*)  " + 
				"from rating " + 
				"where seller_id = ?;";
		int count = 0;
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			ps.setInt(1, seller_id);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
			return count;
		}catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
	}


	@Override
	public List<Rating> getAllRatingByMemberId(int buy_id) {
		String sql = "select * from rating where seller_id = ?";
		List<Rating> list = new ArrayList<Rating>();
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
			ps.setInt(1, buy_id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				list.add(new Rating(rs.getInt(1), 
						rs.getInt(2),
						rs.getInt(3), 
						rs.getInt(4), 
						rs.getString(5), 
						rs.getTimestamp(6),
						rs.getString(7)));
				
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Action --> getAllRatingByMemberId Error");
		return null;
	}

}
