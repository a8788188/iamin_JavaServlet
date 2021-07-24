package com.model;

import java.util.List;

import com.bean.Member;
import com.dao.ChatDao;
import com.dao.implemen.ChatDaoImpl;

public class ChatService {
	private ChatDao dao;
	
	public ChatService() {
		dao = new ChatDaoImpl();
	}
	
	public List<Member> selectAllSeller() {
		return dao.selectAllSeller();
	}
}
