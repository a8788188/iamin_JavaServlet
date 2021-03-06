package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.MemberDao;
import com.dao.MemberOrderDao;
import com.dao.MemberOrderDetailsDao;
import com.dao.ReportDao;
import com.dao.implemen.MemberDaoImp;
import com.dao.implemen.MemberOrderDaoImp;
import com.dao.implemen.MemberOrderDetailsDaoImp;
import com.dao.implemen.ReportDaoImp;
import com.data.MyIncome;
import com.data.MyWallet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.model.GroupAction;
import com.bean.Admin;
import com.bean.Group;
import com.bean.Member;
import com.bean.MemberOrder;
import com.bean.MemberOrderDetails;
import com.bean.ResetPhone;

@WebServlet("/memberController")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "application/json; charset=UTF-8";
	private MemberDao memberDao = null;
	private MemberOrderDao memberOrderDao = null;
	private MemberOrderDetailsDao memberOrderDetailsDao = null;
	private byte[] image = null;
	private Member member,otherMember;
	private ResetPhone resetPhone;
	private Admin admin;
	private MemberOrderDetails memberOrderDetails;
	private String jsonMember,otherMemberJson;
	private List<MemberOrderDetails> memberOrderDetailsList;
	private List<MemberOrder> memberOrderList;
	private List<ResetPhone> resetPhones;
	private List<Member> memberList;
	private boolean respond;
	private int count;
	// 
	GroupAction groupAction = new GroupAction();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		System.out.println("member doPost - Start");
		
		memberDao = new MemberDaoImp();
		memberOrderDao = new MemberOrderDaoImp();
		memberOrderDetailsDao = new MemberOrderDetailsDaoImp();
		
		memberOrderList = new ArrayList<MemberOrder>();
		memberList= new ArrayList<Member>();
		resetPhones = new ArrayList<ResetPhone>();
		memberOrderDetails = null;
		member = null;
		otherMember = null;
		count = 0;
		
		Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").create();
		
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		
		System.out.println("input: " + jsonIn);
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		
		if(action.equals("adminLogin")) {
			jsonMember = jsonObject.get("member").getAsString();
			admin = gson.fromJson(jsonMember, Admin.class);
		}else{
			jsonMember = jsonObject.get("member").getAsString();
			member = gson.fromJson(jsonMember, Member.class);
		}
		System.out.println("action---: " + action);
		switch (action) {
			//??????
		case "login":
			member = memberDao.findbyUuid(member);
			if(member!=null) {
			memberDao.timeUpdate(member.getId(),"LOGIN_TIME");
			}
			count = memberDao.updateTokenbyUid(member.getuUId(),member.getFCM_token());
			writeRespond(response, gson.toJson(member));
			break;
			
			//??????
		case "logout":
			boolean b = memberDao.timeUpdate(member.getId(),"LOGOUT_TIME");
			System.out.println("LOGOUT_TIME update : " + b);
			break;
			
			//??????
		case "signup":
			member = memberDao.insert(member);
			memberDao.timeUpdate(member.getId(),"START_TIME");
			memberDao.timeUpdate(member.getId(),"LOGIN_TIME");
			writeRespond(response, gson.toJson(member));
			break;
			
			//???????????????
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
			
			//???MEMBER_ID??????????????????
		case "findById":
			member = memberDao.findById(member.getId());
			writeRespond(response, gson.toJson(member));
			break;
			
			//??????????????????
		case "findbyUuid":
			member = memberDao.findbyUuid(member);
//			count = memberDao.updateTokenbyUid(member.getuUId(),member.getFCM_token());
			System.out.println("findbyUuid fcm update: " + count);
			writeRespond(response, gson.toJson(member));
			break;	
			
			//?????????
		case "getImage":
			OutputStream os = response.getOutputStream();
			image = memberDao.getImage(member.getId());
			if (image != null) {
				response.setContentType("image/*");
				response.setContentLength(image.length);
				os.write(image);
				}
			os.close();
			break;
			
		case "getIosimage":
			os = response.getOutputStream();
			String iosMemberjson = jsonObject.get("member").getAsString();
			Member iosMember = gson.fromJson(iosMemberjson, Member.class);
			System.out.println(iosMember.getId() + iosMember.getNickname());
			byte[] member_image = null;
			member_image = memberDao.getImage(iosMember.getId());
			if (member_image != null) {
				response.setContentType("image/*");
				response.setContentLength(member_image.length);
				os.write(member_image);
			}
			os.close();
			break;
			
			//?????????????????????
		case "follow":
			int buyer_id = jsonObject.get("buyer_id").getAsInt();
			int seller_id = jsonObject.get("follwer_id").getAsInt();
			memberDao.follow(buyer_id, seller_id);
			break;
			
			//??????????????????????????????
		case "getFollowMember":
			List<Member> memberList = memberDao.getFollowMember(member.getId());
			writeRespond(response, gson.toJson(memberList));
			break;
			
		case "getMyWallet":
			List<MyWallet> myWalletList = memberDao.getMyWallet(member.getId());
			writeRespond(response, gson.toJson(myWalletList));
			break;
		
		case "getMyIncome":
			List<MyIncome> myIncomes = memberDao.getMyIncome(member.getId());
			writeRespond(response, gson.toJson(myIncomes));
			break;
		
		case "updateTokenbyUid":
			count = memberDao.updateTokenbyUid(member.getuUId(),member.getFCM_token());
			System.out.println("findbyUuid fcm update: " + member.getuUId());
			break;
			
		case "showAllMember":
			memberList = memberDao.showAllMemberNicknameAndUid(member.getuUId());
			writeRespond(response, gson.toJson(memberList));
			break;
		
		case "followMember":
			otherMemberJson = jsonObject.get("otherMember").getAsString();
			otherMember = gson.fromJson(otherMemberJson, Member.class);
			respond = memberDao.followbyId(member.getId(), otherMember.getId());
			System.out.println(respond);
			break;
			
		case "unFollowMember":
			otherMemberJson = jsonObject.get("otherMember").getAsString();
			otherMember = gson.fromJson(otherMemberJson, Member.class);
			respond = memberDao.unFollowbyId(member.getId(), otherMember.getId());
			System.out.println(respond);
			break;
			
		case "getMyMemberOrder":
			memberOrderList = memberOrderDao.selectAllByMemberId(member.getId());
			writeRespond(response, gson.toJson(memberOrderList));
			break;
			
			//?????????????????????
		case "chackfollow":
			int buyer_id_chack = jsonObject.get("buyer_id").getAsInt();
			int seller_id_chack = jsonObject.get("follwer_id").getAsInt();
			int count2 = memberDao.chackfollow(buyer_id_chack, seller_id_chack);
			writeRespond(response, String.valueOf(count2));
			break;
			
		case "delete":
		    // 1
			count = memberDao.delete(member.getId());
			// 2
			ReportDao reportDao = new ReportDaoImp();
			reportDao.deleteByreportid(jsonObject.get("reportid").getAsInt());
			// 3. ??????????????????????????????
			List<Group> groups = new ArrayList<>();
			groups = groupAction.getAllByMemberId(member.getId());
			for (Group group : groups) {
			    groupAction.deleteById(group.getGroupId(), null);
			}
			//
			writeRespond(response, gson.toJson(count));
			break;
			
		case "adminLogin":
			admin = memberDao.adminLogin(admin);
			writeRespond(response, gson.toJson(admin));
			break;
			
		case "selectAllSuspendMember":
			memberList = memberDao.selectAllSuspendMember();
			writeRespond(response, gson.toJson(memberList));
			break;
			
		case "RemoveSuspend":
			memberDao.removeSuspend(member.getId());
			break;
			
		case "ResetPhoneNumberRequest":
			count = memberDao.resetPhoneNumberRequest(member);
			writeRespond(response, String.valueOf(count));
			break;
		//??????ResetPhoneInfo
		case "getResetMemberInfo":
			resetPhones = memberDao.findAllbyId();
			writeRespond(response, gson.toJson(resetPhones));
			break;
			
		case "resetPhoneNumber":
			jsonMember = jsonObject.get("resetPhone").getAsString();
			resetPhone = gson.fromJson(jsonMember, ResetPhone.class);
			count = memberDao.resetPhoneNumber(resetPhone.getMember_id());
			writeRespond(response, String.valueOf(count));
			break;
			
		case "phoneAuthSuccess":
			count = memberDao.phoneAuth(member);
			writeRespond(response, String.valueOf(count));
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
