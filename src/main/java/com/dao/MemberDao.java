package com.dao;

import java.util.List;

import com.bean.Admin;
import com.bean.Member;
import com.bean.ResetPhone;
import com.data.MyIncome;
import com.data.MyWallet;

public interface MemberDao {
	//登入
	Member login(Member member);
	//註冊
	Member insert(Member member);
	//電話驗證更新欄位
	int phoneAuth(Member member);
	//選擇全部被停權
	List<Member> selectAllSuspendMember();
	//從電話table提取
	List<ResetPhone> findAllbyId();
	//更新
	int update(Member member,byte[] image);
	//更新FCM_TOKEN
	int updateTokenbyUid(String uId, String FCM_token);
	
	int delete(int member_id);

	int updateRatingById(Member member);
	
	boolean timeUpdate(int member_id,String column);
	
	byte[] getImage(int member_id);
	
	Member findbyUuid(Member memberWithUid);

	Member findById(int member_id);
	
	boolean followbyId(int myId, int other_id);
	
	boolean unFollowbyId(int myId, int other_id);
	
	int getMyFollowCountById(int memberId);
	
	Admin adminLogin(Admin admin);
	
	void removeSuspend(int member_id);
	
	int resetPhoneNumberRequest(Member member);
	
	int resetPhoneNumber(int id);
		
	//取得我的支出
	List<MyWallet> getMyWallet(int member_id);
	//取得我的收入
	List<MyIncome> getMyIncome(int member_id);
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
