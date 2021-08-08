package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.DataSource;

import com.bean.Member;
import com.dao.ChatDao;
import com.dao.common.ServiceLocator;

public class ChatDaoImpl implements ChatDao {
	DataSource dataSource;

	public ChatDaoImpl() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}
	

	@Override
	public List<Member> selectAllSeller() {
		final String sql = "SELECT MEMBER_ID, NICKNAME, UUID, FCM_TOKEN FROM plus_one.member where UUID2 is not null;";
		List<Member> list = new ArrayList<Member>();
		try (
				Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				
			){
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					Integer id = rs.getInt(1);
					String name = rs.getString(2);
					String uuid = rs.getString(3);
					String fcm_token = rs.getString(4);
					Member member = new Member(fcm_token, uuid, name, id);
					list.add(member);
				}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public byte[] getImage(int id) {
		String sql = "SELECT IMG FROM plus_one.member where PHONE is not null and MEMBER_ID = ?";
		byte[] image = null;
		try (
				Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);
			) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				
				image = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return image;
	}

}


