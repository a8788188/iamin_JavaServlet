package com.dao;

import java.util.List;
import com.bean.Report;

public interface ReportDao {
	
	int insert(Report report);
	
	//取得有被檢舉的memberId(delete_time 不是空)
	List<Integer> selectmemberidreport();

}
