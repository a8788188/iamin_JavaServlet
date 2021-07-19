package com.dao;

import java.util.List;

import com.bean.Member;
import com.data.MyWallet;

public interface MemberDao {
	
	Member login(Member member);
	
	Member insert(Member member);
	
	int update(Member member,byte[] image);
	
	int updateTokenbyUid(String uId,String FCM_token);
	
	void delete(int member_id);
	
	boolean timeUpdate(int member_id,String column);
	
	byte[] getImage(int member_id);
	
	Member findbyUuid(String uUid);

	Member findById(int member_id);
	
	boolean followbyId(int myId, int other_id);
	
	boolean unFollowbyId(int myId, int other_id);
	
	
	
	
	//取得我的錢包明細
	List<MyWallet> getMyWallet(int member_id);
	//點擊圓餅圖後出現的細項 需要團購發起人id
	List<MyWallet> getMyWalletDetail(int group_id);
	//取得追蹤者清單
	List<Member> getFollowMember(int member_id);
	//追蹤賣家功能
	void follow(int member_id, int member_id_2);
	
	//TEST AREA-------------------------------------------------
	List<Member> showAllMemberNicknameAndUid(String uUid);
	
	
}
