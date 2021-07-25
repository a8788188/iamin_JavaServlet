package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
				"(buy_id,seller_id,order_rating,rating_message)\n" + 
				"VALUES(?, ?, ?, ?);";
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			ps.setInt(1, rating.getBuyer_Id());
			ps.setInt(2, rating.getSeller_Id());
			ps.setInt(3, rating.getOrder_rating());
			ps.setString(4, rating.getRating_message());
			
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
				rs.getInt(1);
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
				rs.getInt(1);
			}
			return count;
		}catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
	}

}
