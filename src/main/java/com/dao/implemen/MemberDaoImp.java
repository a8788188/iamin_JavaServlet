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
import com.data.MyWallet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MemberDaoImp implements MemberDao {
	DataSource dataSource;

	public MemberDaoImp() {
		dataSource = ServiceLocator.getInstance().getDataSource();
	}

	@Override
	public int login(Member member) {
		String sql = "select * from MEMBER where EMAIL = ? and PASSWORD = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getPassword());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				member.setId(rs.getInt("MEMBER_ID"));
				
			}
			return member.getId();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@Override
	public boolean loginTimeUpdate(int member_id) {
		String sql = "update MEMBER set LOGIN_TIME = now() where MEMBER_ID = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);){
			pstmt.setInt(1, member_id);
			pstmt.executeUpdate();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
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
			sql = "update MEMBER set NICKNAME = ?, PASSWORD = ?,EMAIL = ?,PHONE = ?,UPDATE_TIME = ?,IMG = ?"
					+ "where MEMBER_ID = ?";
		} else {
			sql = "update MEMBER set NICKNAME = ?, PASSWORD = ?,EMAIL = ?,PHONE = ?,UPDATE_TIME = ? "
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
	public Member findById(int id, String table) {
		final String sql = "select * from " + table + " where MEMBER_ID = ?";
		Member member = Member.getInstance();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						
						String uUid = rs.getString("UUID");
						String email = rs.getString("Email");
						int followCount = rs.getInt("FOLLOW_COUNT");
						double rating = rs.getDouble("RATING");
						String password = rs.getString("PASSWORD");
						String nickname = rs.getString("NICKNAME") != null ? rs.getString("NICKNAME") : "";
						String phoneNumber = rs.getString("PHONE") != null ? rs.getString("PHONE") : "";
						Timestamp loginTime = rs.getTimestamp("LOGIN_TIME");
						Timestamp updateTime = rs.getTimestamp("UPDATE_TIME");
						Timestamp startTime = rs.getTimestamp("START_TIME");
						Timestamp logoutTime = rs.getTimestamp("LOGOUT_TIME");
						Timestamp deleteTime = rs.getTimestamp("DELETE_TIME");
						String FCM_token = rs.getString("FCM_TOKEN");
						
						member = new Member(id,
									 	    followCount,
									 	    rating,
									 	    uUid,
									 	    email,
									 	    password,
									 	    nickname,
									 	    phoneNumber,
									 	    startTime,
									 	    updateTime,
									 	    logoutTime,
									 	    loginTime,
									 	    deleteTime,
									 	    FCM_token);
					}
					return member;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("findbyId return null");
		return null;
	}

	//追蹤賣家功能
	@Override
	public void follow(int member_id, int member_id_2) {
		String sql = "select count(*) from FAVORITE where MEMBER_ID = ? and MEMBER_ID_2 = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			pstmt.setInt(1, member_id);
			pstmt.setInt(2, member_id_2);
			ResultSet rs = pstmt.executeQuery();
			int number = 0;
			while (rs.next()) {
				number = rs.getInt("count(*)");
			}
			// 會員追隨/取消追隨
			String text = "";
			if (number == 1) {
				// 已經追蹤 所以刪除
				sql = "delete from FAVORITE where MEMBER_ID = ? and MEMBER_ID_2 = ?";
				text = "unfollow";
			} else if (number == 0) {
				// 尚未追蹤 所以新增
				sql = "insert into FAVORITE (MEMBER_ID, MEMBER_ID_2) values (?,?)";
				text = "follow";
			} else {
				System.out.println("followToggle count Error");
			}
			try (Connection connection = dataSource.getConnection();
					PreparedStatement pstmt2 = connection.prepareStatement(sql);) {
				pstmt2.setInt(1, member_id);
				pstmt2.setInt(2, member_id_2);
				pstmt2.executeUpdate();
				System.out.println(text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<MyWallet> getMyWallet(int member_id) {
		//join merch group group_category
		final String sql = 
				"	select  "+ 
				"	 	m.*," + 
				"    	g.GROUP_CATEGORY_ID," +
				"		c.CATEGORY" +
				"	from " + 
				"		plus_one.MEMBER_ORDER as m " + 
				"	left join" + 
				"		plus_one.GROUP as g" + 
				"	on " +
				"		m.GROUP_ID = g.GROUP_ID" + 
				"	left join " + 
				"		plus_one.GROUP_CATEGORY as c " + 
				"		ON " +
				"		g.GROUP_CATEGORY_ID = c.GROUP_CATEGORY_ID " + 
				"	WHERE " +
				"		m.MEMBER_ID = ?" +
				"		AND " +
				"		m.DELIVER_STATUS = 2";
		List<MyWallet> myWalletList = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql);) {
			pstmt.setInt(1,member_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				
				int group_id = rs.getInt("GROUP_ID");
				int totoalPrice = rs.getInt("TOTAL");
				int deliverStatus = rs.getInt("DELIVER_STATUS");
				Timestamp startTime = rs.getTimestamp("START_TIME");
				Timestamp updateTime = rs.getTimestamp("UPDATE_TIME");
				String category = rs.getString("CATEGORY");
				List<MyWallet> groupDetail = getMyWalletDetail(rs.getInt("GROUP_ID"));
				
				myWalletList.add(
						new MyWallet(group_id,
									 totoalPrice,
									 deliverStatus,
									 startTime,
									 updateTime,
									 category,
									 groupDetail));
//				System.out.println("myWalletList: " + myWalletList);
			}
			
			return myWalletList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<MyWallet> getMyWalletDetail(int group_id) {
		//取得group底下的merch名稱跟價錢 
		String sql = 
			"select " +  
				"g.GROUP_ID," +
				"m.NAME," + 
				"m.PRICE " +
			"from " + 
				"plus_one.GROUP_LIST as g " +
			"right join " + 
				"plus_one.MERCH as m " +
			"on " + 
				"g.MERCH_ID = m.MERCH_ID " +
			"where " + 
				"g.GROUP_ID = ?";
		List<MyWallet> groupDetails = new ArrayList<>();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);){
			pstmt.setInt(1,group_id);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String name = rs.getString("NAME");
				int price = rs.getInt("PRICE");
				groupDetails.add(
							new MyWallet(
										 name,
										 price));
//				System.out.println("groupDetails: " + groupDetails);
			}
			
			return groupDetails;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Member> getFollowMember(int member_id) {
		final String sql = "select * from FAVORITE where MEMBER_ID = ?";
			List<Member> memberList = new ArrayList<>();
			try (Connection conn = dataSource.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(sql);) {
				pstmt.setInt(1,member_id);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					Member followMember = findById(rs.getInt("MEMBER_ID_2"), "MEMBER");
					System.out.println(followMember);
					memberList.add(followMember);
				}
				return memberList;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
	}
}
