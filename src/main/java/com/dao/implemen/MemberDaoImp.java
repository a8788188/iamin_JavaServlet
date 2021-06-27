package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.Member;
import com.dao.MemberDao;
import com.dao.common.ServiceLocator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MemberDaoImp implements MemberDao {
	DataSource dataSource;

	public MemberDaoImp() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	@Override
	public int verification(Member member) {
		String sql = "select * from MEMBER where EMAIL = ? and PASSWORD = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getPassword());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				member.setId(rs.getInt("MEMBER_ID"));
				member.setPassword(rs.getString("PASSWORD"));
				member.setNickname(rs.getString("NICKNAME"));
				member.setPhoneNumber(rs.getString("PHONE"));
				member.setImage(rs.getBytes("IMG"));
				member.setLoginTime(new Timestamp(System.currentTimeMillis()));
			}
			// 回傳memberId供android studio 使用
			return member.getId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int insert(Member member) {
		String sql = "";
		if (member.getNickname() != null && member.getPhoneNumber() != null) {
			sql = "insert into MEMBER (UUID,EMAIL,PASSWORD,START_TIME,NICKNAME,PHONE) values (?,?,?,?,?,?)";
		} else if (member.getNickname() != null) {
			sql = "insert into MEMBER (UUID,EMAIL,PASSWORD,START_TIME,NICKNAME) values (?,?,?,?,?)";
		} else if (member.getPhoneNumber() != null) {
			sql = "insert into MEMBER (UUID,EMAIL,PASSWORD,START_TIME,PHONE) values (?,?,?,?,?)";
		} else {
			sql = "insert into MEMBER (UUID,EMAIL,PASSWORD,START_TIME) values (?,?,?,?)";
		}
		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, new String[] { "ID" });) {
			pstmt.setString(1, member.getuUId());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3, member.getPassword());
			pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			if (member.getNickname() != null && member.getPhoneNumber() != null) {
				pstmt.setString(5, member.getNickname());
				pstmt.setString(6, member.getPhoneNumber());
			} else if (member.getNickname() != null) {
				pstmt.setString(5, member.getNickname());
			} else if (member.getPhoneNumber() != null) {
				pstmt.setString(5, member.getPhoneNumber());
			}
				pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				member.setId(rs.getInt(1));
			}
			// 回傳memberId供android studio 使用
			return member.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 錯誤代碼 -1 回傳
		return -1;
	}

	@Override
	public int update(Member member, byte[] image) {
		String sql = "";
		// image為null就不更新image欄位內容
		if (image != null) {
			sql = "update MEMBER set NICKNAME = ?, " + "PASSWORD = ?,EMAIL = ?,PHONE = ?,UPDATE_TIME = ?,IMG = ?"
					+ "where MEMBER_ID = ?";
		} else {
			sql = "update MEMBER set NICKNAME = ?, " + "PASSWORD = ?,EMAIL = ?,PHONE = ?,UPDATE_TIME = ?"
					+ "where MEMBER_ID = ?";
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setString(1, member.getNickname());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getPhoneNumber());
			pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			if (image != null) {
				pstmt.setBytes(6, image);
				pstmt.setInt(7, member.getId());
			} else {
				pstmt.setInt(6, member.getId());
			}

			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void delete(int id) {
		
	}
	
	@Override
	public byte[] getImage(int id) {
		String sql = "select IMG from MEMBER where MEMBER_ID = ?";
		byte[] image = null;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);) {
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

	@Override
	public String findById(int id, String table) {
		final String sql = "select * from " + table + " where MEMBER_ID = ?";
		Member member = new Member();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (table.equals("MEMBER")) {
					if (rs.next()) {
						
						member.setuUId((rs.getString("UUID")));
						member.setEmail(rs.getString("Email"));
						member.setRating(rs.getDouble("RATING"));
						member.setPassword(rs.getString("PASSWORD"));
						member.setNickname(rs.getString("NICKNAME"));
						member.setPhoneNumber(rs.getString("PHONE"));
						member.setFollow_count(rs.getInt("FOLLOW_COUNT"));
						member.setStartTime(rs.getTimestamp("START_TIME"));
						member.setUpdateDate(rs.getTimestamp("UPDATE_TIME"));
						
//						int follow_count = rs.getInt("FOLLOW_COUNT");
//						double rating = rs.getDouble("RATING");
//						String uUId = rs.getString("uUId");
//						String email = rs.getString("EMAIL");
//						String password = rs.getString("PASSWORD");
//						String nickname = rs.getString("NICKNAME");
//						String phoneNumber = rs.getString("PHONE");
//						
//						member = new Member(id, follow_count, rating, uUId, email, password, nickname, phoneNumber);
//						return member;
					}

					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("MEMBER_ID", id);
					jsonObject.addProperty("EMAIL", member.getEmail());
					jsonObject.addProperty("PASSWORD", member.getPassword());
					jsonObject.addProperty("RATING", member.getRating());
					jsonObject.addProperty("FOLLOW_COUNT", member.getFollow_count());
					if (member.getNickname() != null) {
						jsonObject.addProperty("NICKNAME", member.getNickname());
					} else {
						jsonObject.addProperty("NICKNAME", "");
					}
					if (member.getPhoneNumber() != null) {
						jsonObject.addProperty("PHONE", member.getPhoneNumber());
					} else {
						jsonObject.addProperty("PHONE", "");
					}
					
					return jsonObject.toString();
				}
				if (table.equals("FAVORITE")) {
					List<JsonObject> followList = new ArrayList<>();
					JsonObject follow_member = new JsonObject();
					while(rs.next()) {
						String follow = findById(rs.getInt("MEMBER_ID_2"), "MEMBER");
						follow_member = new Gson().fromJson(follow, JsonObject.class);
						followList.add(follow_member);
					}
					return new Gson().toJson(followList);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "error";
	}

	@Override
	public List<Member> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	
	//追蹤賣家功能
	@Override
	public void follow(int member_id, int member_id_2) {
		String sql = "select count(*) from FAVORITE where MEMBER_ID = ? and MEMBER_ID_2 = ?";
		try (Connection conn = dataSource.getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1, member_id);
			pstmt.setInt(2, member_id_2);
			ResultSet rs = pstmt.executeQuery();
			int  number = 0;
			while(rs.next()) {
				number = rs.getInt("count(*)");
			}
			//會員追隨/取消追隨
			followToggle(member_id ,member_id_2 ,number);
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//follow功能 如果已經有追蹤的話 刪除
	@Override
	public void followToggle(int member_id, int member_id_2, int count) {
		String sql = "";
		String test = "";
		if(count == 1) {
			//已經追蹤 所以刪除
			sql = "delete from FAVORITE where MEMBER_ID = ? and MEMBER_ID_2 = ?";
			test = "unfollow";
		}else if(count == 0){
			//尚未追蹤 所以新增
			sql = "insert into FAVORITE (MEMBER_ID, MEMBER_ID_2) values (?,?)";	
			test = "follow";
		}else{
			System.out.println("followToggle count Error");
		}
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setInt(1, member_id);
			pstmt.setInt(2, member_id_2);
			pstmt.executeUpdate();
			System.out.println(test);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
