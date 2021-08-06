package com.bean;

import java.sql.Timestamp;

public class ResetPhone {

	int reset_id;
	int member_id;
	String nickname;
	String email;
	Timestamp startTime;
	
	public ResetPhone(int reset_id, int member_id, String nickname, String email, Timestamp startTime) {
		super();
		this.reset_id = reset_id;
		this.member_id = member_id;
		this.nickname = nickname;
		this.email = email;
		this.startTime = startTime;
	}

	public int getReset_id() {
		return reset_id;
	}

	public void setReset_id(int reset_id) {
		this.reset_id = reset_id;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
	
	
}
