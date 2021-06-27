package com.dao;

import java.util.List;

import com.bean.Member;

public interface MemberDao {
	int verification(Member member);
	int insert(Member member);
	int update(Member member,byte[] image);
	void delete(int id);
	byte[] getImage(int id);
	String findById(int id , String table);
	List<Member> getAll();
	
	//追蹤賣家功能
	void follow(int member_id, int member_id_2);
	void followToggle(int member_id, int member_id_2, int number);
	
}