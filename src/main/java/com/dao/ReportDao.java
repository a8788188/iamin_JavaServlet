package com.dao;

import java.util.List;
import com.bean.Report;

public interface ReportDao {
	
	int insert(Report report);
	
	//取得有被檢舉的memberId(delete_time 不是空)
	List<Integer> selectmemberidreport();
	//用被檢舉人id查該被檢舉人的所有檢舉
	List<Report> selectreportbymemberid(int id);

}
