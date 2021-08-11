package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.Report;
import com.dao.ReportDao;
import com.dao.common.ServiceLocator;

public class ReportDaoImp implements ReportDao {
	DataSource dataSource;

	public ReportDaoImp() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
	
	@Override
	public int insert(Report report) {
		String sql = "INSERT INTO report\n" + 
				"(MEMBER_ID,REPORTED_MEMBER_ID,REPORT_ITEM,REPORT_MESSAGE)\n" + 
				"VALUES(?, ?, ?, ?);";
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			ps.setInt(1, report.getMember_id());
			ps.setInt(2, report.getReported_member_id());
			ps.setString(3, report.getReport_item());
			ps.setString(4, report.getReport_message());
			
			ps.executeUpdate();
			return 1;
		}catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
	}

	@Override
	public List<Integer> selectmemberidreport() {
		List<Integer> reported_member_id = new ArrayList<Integer>();
		String sql = "SELECT REPORTED_MEMBER_ID " + 
					 "from plus_one.report " + 
					 "where DELETE_TIME IS NULL " + 
					 "group by REPORTED_MEMBER_ID; ";
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			 ResultSet rs = ps.executeQuery();
			 
			 while (rs.next()) {
				reported_member_id.add(rs.getInt(1));
			}
			 return reported_member_id;
		}catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

	}

	@Override
	public List<Report> selectreportbymemberid(int id) {
		List<Report> reports = new ArrayList<>();
		String sql = "SELECT REPORT_ID,MEMBER_ID,REPORTED_MEMBER_ID,REPORT_ITEM,REPORT_MESSAGE " + 
					 "from report " + 
					 "where REPORTED_MEMBER_ID = ? and DELETE_TIME IS NULL";
		try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
			 ps.setInt(1, id);
			 
			 ResultSet rs = ps.executeQuery();
			 while (rs.next()) {
				Report report = new Report(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5));
				reports.add(report);
			}
			 
			 return reports;
		}catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public int deleteByreportid(int id) {
		int count = 0;
        String sql = "UPDATE plus_one.report SET DELETE_TIME = NOW() WHERE REPORT_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
	}

}
