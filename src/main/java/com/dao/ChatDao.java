package com.dao;

import java.util.List;
import com.bean.Member;

public interface ChatDao {
	List<Member> selectAllSeller();
	
	byte[] getImage(int id);
}
