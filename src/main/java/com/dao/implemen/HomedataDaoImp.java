package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.dao.HomedataDao;
import com.data.Homedata;
import com.dao.common.ServiceLocator;

public class HomedataDaoImp implements HomedataDao {

	DataSource dataSource;

    public HomedataDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }
    
	@Override
	public List<Homedata> selectAllgroyp() {
		String sql = "select \n" + 
				"	g1.GROUP_ID,g1.NAME,m1.PRICE,g1.PROGRESS,g1.GOAL,g1.CONDITION_TIME\n" + 
				"from plus_one.group g1\n" + 
				"	join merch m1\n" + 
				"where MERCH_ID = 1\n" + 
				"group by GROUP_ID;";
		List<Homedata> homedataList = new ArrayList<>();
         
         try (
                 Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql);
         ) {
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
            	 Homedata homedata = new Homedata(
            			 rs.getInt(1),
            			 rs.getString(2),
            			 rs.getInt(3),
            			 rs.getInt(4),
            			 rs.getInt(5),
            			 rs.getTimestamp(6));
				homedataList.add(homedata);
			}
         }catch (SQLException e) {
			e.printStackTrace();
		}
		return homedataList;
	}

}
