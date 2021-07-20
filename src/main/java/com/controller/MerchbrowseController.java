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

import com.bean.Group;
import com.bean.MemberOrder;
import com.bean.MemberOrderDetails;
import com.bean.Merch;
import com.dao.GroupDao;
import com.dao.HomedataDao;
import com.dao.MemberOrderDao;
import com.dao.MemberOrderDetailsDao;
import com.dao.MerchDao;
import com.dao.implemen.GroupDaoImp;
import com.dao.implemen.HomedataDaoImp;
import com.dao.implemen.MemberOrderDaoImp;
import com.dao.implemen.MemberOrderDetailsDaoImp;
import com.dao.implemen.MerchDaoImp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


@WebServlet("/Merchbrowse")
public class MerchbrowseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MerchDao merchDao = null;
	GroupDao groupDao = null;
	MemberOrderDao memberOrderDao = null;
	MemberOrderDetailsDao memberOrderDetailsDao = null;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			System.out.println("Merch doPost - Start");
	        request.setCharacterEncoding("UTF-8");
	        response.setContentType("application/json; charset=UTF-8");
	        Gson gson = new Gson();
	       
	        if (merchDao == null) {
	        	merchDao = new MerchDaoImp();
			}
	        if (groupDao == null) {
				groupDao = new GroupDaoImp();
			}
	        if (memberOrderDao == null) {
				memberOrderDao = new MemberOrderDaoImp();
			}
	        if (memberOrderDetailsDao == null) {
				memberOrderDetailsDao = new MemberOrderDetailsDaoImp();
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
	        case "getAllByGroupIdId":
	        	int GroupId = jsonObject.get("groupId").getAsInt();
	        	List<Merch> Merch_browse = merchDao.selectAllByGroupId(GroupId);
	        	writeText(response, gson.toJson(Merch_browse));
	        	break;
	        case "getGroupbyGroupId":
	        	int GroupId2 = jsonObject.get("groupId").getAsInt();
	        	Gson gson2 = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").create();
	        	Group group = groupDao.selectById(GroupId2);
	        	writeText(response, gson2.toJson(group));
	        	break;
	        case "insertMemberOrder":
	        	String memberorderJson = jsonObject.get("memberorder").getAsString();
				System.out.println("spotJson = " + memberorderJson);
				MemberOrder memberOrder = gson.fromJson(memberorderJson, MemberOrder.class);
				int count = memberOrderDao.insert(memberOrder);
				writeText(response, String.valueOf(count));
	        	break;
	        case "insertMemberOrderDetails":
	        	int count2 = 0;
	        	String memberorderdetailsJson = jsonObject.get("memberorderdetails").getAsString();
				System.out.println("spotJson = " + memberorderdetailsJson);
				List<MemberOrderDetails> orderDetails = new ArrayList<>();
				Type listType = new TypeToken<List<MemberOrderDetails>>(){}.getType();
				orderDetails = gson.fromJson(memberorderdetailsJson, listType);
				
				for (MemberOrderDetails memberOrderDetails : orderDetails) {
					count2 = memberOrderDetailsDao.insert(memberOrderDetails);
				}
				writeText(response, String.valueOf(count2));
	        	break;
	        case "update":
	        	int countupdate = 0;
	        	String updateGroupJson = jsonObject.get("updategroup").getAsString();
				System.out.println("spotJson = " + updateGroupJson);
				Group updateGroup = gson.fromJson(updateGroupJson, Group.class);
				countupdate = groupDao.update(updateGroup);
				writeText(response, String.valueOf(countupdate));
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
