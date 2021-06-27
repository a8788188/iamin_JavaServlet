package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import com.dao.HomedataDao;
import com.data.Homedata;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;
import com.dao.common.ServiceLocator;

public class HomedataDaoImp implements HomedataDao {

	DataSource dataSource;

    public HomedataDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }
    
	@Override
	public List<Homedata> selectAllgroup() {
		String sql = "select \n" + 
				"	 g1.GROUP_ID,\n" + 
				"    g1.NAME,\n" + 
				"    g1.PROGRESS,\n" + 
				"    g1.GOAL,\n" + 
				"    g1.CONDITION_TIME\n" + 
				"from plus_one.group g1\n" + 
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
            			 rs.getTimestamp(5));
				homedataList.add(homedata);
			}
         }catch (SQLException e) {
			e.printStackTrace();
		}
		return homedataList;
	}

	@Override
	public List<Homedata> selectAllgroupPrice(int GroupID) {
		String sql = "select " + 
				"	  	m1.PRICE " + 
				"	  from group_list gl1\n" + 
				"     left join merch m1\n" + 
				"     on gl1.MERCH_ID = m1.MERCH_ID\n" + 
				"     where GROUP_ID = ?;";
		List<Homedata> homedataPricelist = new ArrayList<>();
		List<Integer> prices = new ArrayList<Integer>();
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			ps.setInt(1, GroupID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            int price = rs.getInt(1);
            prices.add(price);
            Collections.sort(prices);
			}
            Homedata homedata = new Homedata(prices);
           	homedataPricelist.add(homedata);
        }catch (SQLException e) {
			e.printStackTrace();
		}
		return homedataPricelist;
	}

}
