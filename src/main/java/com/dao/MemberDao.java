package com.dao;

import java.util.List;

import com.bean.Member;

public interface MemberDao {
	int verification(Member member);
	int insert(Member member);
	int update(Member member,byte[] image);
	void delete(int member_id);
	byte[] getImage(int member_id);
	String findById(int member_id , String table);
	List<Member> getAll();
	//取得我的錢包明細
	String getMyWallet(int member_id);
	//點擊圓餅圖後出現的細項 需要團購發起人id
	//SELECT member_id FROM plus_one.group where group_id = ?;
	String getMyWalletDetail(int group_id);
	//取得追蹤者清單
	String getFollowMember(int member_id);
	//追蹤賣家功能
	void follow(int member_id, int member_id_2);
	
}
