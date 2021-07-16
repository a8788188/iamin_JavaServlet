package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.MemberDao;
import com.dao.implemen.MemberDaoImp;
import com.data.MyWallet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.bean.Member;

@WebServlet("/memberController")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "application/json; charset=UTF-8";
	private MemberDao memberDao = null;
	private byte[] image = null;
	private Member member = null;
	private String jsonMember;
	private int count;
	int mysqlMemberId;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		System.out.println("member doPost - Start");
		Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").create();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		
		System.out.println("input: " + jsonIn);
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);

		if (memberDao == null) {
			memberDao = new MemberDaoImp();
		}

		count = 0;
		mysqlMemberId = -1;
		String action = jsonObject.get("action").getAsString();
		jsonMember = jsonObject.get("member").getAsString();
		member = gson.fromJson(jsonMember, Member.class);
		System.out.println("action---: " + action);
		switch (action) {
			//登入
		case "login":
			member = memberDao.login(member);
			memberDao.timeUpdate(mysqlMemberId,"LOGIN_TIME");
			writeRespond(response, gson.toJson(member));
			break;
			
			//登出
		case "logout":
			boolean b = memberDao.timeUpdate(member.getId(),"LOGOUT_TIME");
			System.out.println("LOGOUT_TIME update : " + b);
			break;
			
			//註冊
		case "signup":
			member = memberDao.insert(member);
			memberDao.timeUpdate(member.getId(),"START_TIME");
			writeRespond(response, gson.toJson(member));
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
			member = memberDao.findById(member.getId());
			writeRespond(response, gson.toJson(member));
			break;
			
			//第三方檢查用
		case "findbyUuid":
			member = memberDao.findbyUuid(member.getuUId());
			writeRespond(response, gson.toJson(member));
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
			List<Member> memberList = memberDao.getFollowMember(member.getId());
			writeRespond(response, gson.toJson(memberList));
			break;
			
		case "getMyWallet":
			List<MyWallet> myWalletList = memberDao.getMyWallet(member.getId());
			writeRespond(response, gson.toJson(myWalletList));
			break;
		
		default:
			break;
		}
	}

	private void writeRespond(HttpServletResponse response, String output) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(output);
		System.out.println("output: " + output);
	}
	
}
