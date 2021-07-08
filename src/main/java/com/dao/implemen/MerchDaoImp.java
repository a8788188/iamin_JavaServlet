package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.Merch;
import com.dao.MerchDao;
import com.dao.common.ServiceLocator;


public class MerchDaoImp implements MerchDao{
	
	DataSource dataSource;
	
	public MerchDaoImp() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	@Override
	public List<Merch> selectAllByGroupId(int GroupID) {
		String sql = "select\n" + 
				"	m1.MERCH_ID,m1.MEMBER_ID,m1.NAME,m1.PRICE,m1.MERCH_DESC\n" + 
				"from group_list gl1\n" + 
				"	left join merch m1\n" + 
				"	on gl1.MERCH_ID = m1.MERCH_ID \n" + 
				"where GROUP_ID = ?;";
		List<Merch> merchList = new ArrayList<Merch>();
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
			ps.setInt(1, GroupID);
			ResultSet rs = ps.executeQuery();
            while (rs.next()) {
           	 Merch merchdata = new Merch(
           			 rs.getInt(1),
           			 rs.getInt(2),
           			 rs.getString(3),
           			 rs.getInt(4),
           			 rs.getString(5)
           			 );
           	merchList.add(merchdata);
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return merchList;
	}

}
