package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.dao.HomedataDao;
import com.dao.common.ServiceLocator;

public class HomedataDaoImp implements HomedataDao {

	DataSource dataSource;

    public HomedataDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }
    
	@Override
	public byte[] getGroupimage(int GroupID) {
		String sql = "select\n" + 
				"	m1.IMG_1\n" + 
				"from group_list gl1\n" + 
				"	left join merch m1\n" + 
				"	on gl1.MERCH_ID = m1.MERCH_ID \n" + 
				"where GROUP_ID = ?\n" + 
				"limit 1;";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {
			ps.setInt(1, GroupID);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return image;
	}

}
