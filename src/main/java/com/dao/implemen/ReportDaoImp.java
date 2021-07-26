package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
