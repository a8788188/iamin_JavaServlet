package com.dao;

import java.util.List;

import com.bean.Member;
import com.data.MyWallet;

public interface MemberDao {
	
	Member login(Member member);
	
	Member insert(Member member);
	
	int update(Member member,byte[] image);
	
	int updateTokenbyUid(String uId, String FCM_token);
	
	int delete(int member_id);
	
	boolean timeUpdate(int member_id,String column);
	
	byte[] getImage(int member_id);
	
	Member findbyUuid(String uUid);

	Member findById(int member_id);
	
	boolean followbyId(int myId, int other_id);
	
	boolean unFollowbyId(int myId, int other_id);
	
	int getMyFollowCountById(int memberId);
	
	//取得我的支出
	List<MyWallet> getMyWallet(int member_id);
	//取得我的收入
	List<MyWallet> getMyIncome(int member_id);
	//點擊圓餅圖後出現的細項 需要團購發起人id
	List<MyWallet> getMyWalletDetail(int group_id);
	//取得追蹤者清單
	List<Member> getFollowMember(int member_id);
	//追蹤賣家功能
	void follow(int member_id, int member_id_2);
	
	int chackfollow(int member_id, int member_id_2);

	// 用GroupId抓取Member (Join member_order)
	List<Member> selectByGroupId(int groupId);
	//TEST AREA-------------------------------------------------
	List<Member> showAllMemberNicknameAndUid(String uUid);
}
