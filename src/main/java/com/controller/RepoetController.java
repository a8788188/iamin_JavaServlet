package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Member;
import com.bean.Report;
import com.dao.MemberDao;
import com.dao.ReportDao;
import com.dao.implemen.MemberDaoImp;
import com.dao.implemen.ReportDaoImp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@WebServlet("/Report")
public class RepoetController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReportDao reportDao;
	private MemberDao memberDao;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Rrport doPost - Start");
	    request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").create();
        // 回傳結果
        int count = 0;
        if (reportDao == null) {
			reportDao = new ReportDaoImp();
		}
        
        if (memberDao == null) {
        	memberDao = new MemberDaoImp();
		}
        
        // 以列為單位讀入 (純文字)
        BufferedReader br = request.getReader();
        StringBuilder requstStr = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            requstStr.append(line);
        }
        // Gson
        JsonObject jsonObject = gson.fromJson(requstStr.toString(), JsonObject.class);
        // action

        String action = jsonObject.get("action").getAsString();
        System.out.println("action---: " + action);
        
        switch (action) {
		case "reportinsert":
			String reportJson = jsonObject.get("report").getAsString();
			System.out.println("spotJson = " + reportJson);
			Report report = gson.fromJson(reportJson, Report.class);
			count = reportDao.insert(report);
			writeText(response, gson.toJson(count));
			break;
			
		case "reportedmember":
			List<Integer> reported_ids = reportDao.selectmemberidreport();
			List<Member> reported_member = new ArrayList<>();
			for (Integer integer : reported_ids) {
				reported_member.add(memberDao.findById(integer));
			}
			writeText(response, gson.toJson(reported_member));
			break;
			
		case "membersreport":
			List<Report> reports = new ArrayList<>();
			reports = reportDao.selectreportbymemberid(jsonObject.get("reportedid").getAsInt());
			writeText(response, gson.toJson(reports));
			break;
			
		case "deleteByreportid":
			count = reportDao.deleteByreportid(jsonObject.get("reportid").getAsInt());
			writeText(response, gson.toJson(count));
			break;
			
		default:
			break;
		}
	}
	private void writeText(HttpServletResponse response, String outText) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(outText);
        System.out.println("output: " + outText);
    }
}
