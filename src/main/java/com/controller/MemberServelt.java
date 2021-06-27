package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.MemberDao;
import com.dao.implemen.MemberDaoImp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.bean.Member;

@WebServlet("/memberServelt")
public class MemberServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "application/json; charset=UTF-8";
	private MemberDao memberDao = null;
	private byte[] image = null;
	private Member member = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		
		System.out.println("input: " + jsonIn + "\n");
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);

		if (memberDao == null) {
			memberDao = new MemberDaoImp();
		}

		String action = jsonObject.get("action").getAsString();
		String memberJson = jsonObject.get("member").getAsString();
		member = gson.fromJson(memberJson, Member.class);
		
		int count = 0;
		int mysqlMemberId = -1;
		
		switch (action) {
			//登入
		case "login":
			mysqlMemberId = memberDao.verification(member);
			writeRespond(response, String.valueOf(mysqlMemberId));
			break;
			//註冊
		case "signup":
			mysqlMemberId = memberDao.insert(member);
			writeRespond(response, String.valueOf(mysqlMemberId));
			break;
			//更新使用者
		case "update":
			if (jsonObject.get("imageBase64") != null) {
				String imageBase64 = jsonObject.get("imageBase64").getAsString();
				if (imageBase64 != null && !imageBase64.isEmpty()) {
					image = Base64.getMimeDecoder().decode(imageBase64);
				}
			}
			count = memberDao.update(member,image);
			writeRespond(response, String.valueOf(count));
			break;
			//用MEMBER_ID取得會員資訊
		case "findById":
			String obj = memberDao.findById(member.getId(),"MEMBER");
			writeRespond(response, obj.toString());
			break;
			//抓圖片
		case "getImage":
			OutputStream os = response.getOutputStream();
			image = memberDao.getImage(member.getId());
			if (image != null) {
				response.setContentType("image/*");
				response.setContentLength(image.length);
				os.write(image);}
			break;
			//追隨或取消追隨
		case "follow":
			int member_id_2 = jsonObject.get("follwer_id").getAsInt();
			memberDao.follow(member.getId(), member_id_2);
			break;
			//取得被追蹤的會員資料
		case "getFollowMember":
			String followList = memberDao.findById(member.getId(),"FAVORITE");
			writeRespond(response, followList);
			break;
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
        String name = "jojo";
        PrintWriter out = resp.getWriter();
        out.print(name);
	}

	private void writeRespond(HttpServletResponse response, String output) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(output);
	}
	

	

}
