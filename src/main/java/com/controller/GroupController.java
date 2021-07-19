package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.bean.Group;
import com.bean.GroupCategory;
import com.bean.Merch;
import com.model.GroupAction;
import com.model.MerchAction;

@WebServlet("/Group")
public class GroupController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println("Group doPost - Start");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        Type listType;
        String groupJson;
        Group group;
        List<Double[]> LatLngs;
        int id;
        String merchsIdJson;
        List<Integer> merchsId;
        // 回傳結果
        int count = 0;
        
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
        GroupAction groupAction = new GroupAction();
        String action = jsonObject.get("action").getAsString();
        System.out.println("action---: " + action);
        
        // 更新是否有團購已經流標
        groupAction.updateGroupStatus();
        
        switch (action) {
        case "getAllByMemberId":
            // member ID
            List<Group> groups = groupAction.getAllByMemberId(jsonObject.get("memberId").getAsInt());
            writeText(response, gson.toJson(groups));
            break;
        case "getAllCategory":
            List<GroupCategory> categorys = groupAction.getAllCategory();
            writeText(response, gson.toJson(categorys));
            break;
        case "insert":
            // 團購資料
            groupJson = jsonObject.get("group").getAsString();
            System.out.println("groupJson_group = " + groupJson);
            group = gson.fromJson(groupJson, Group.class);
            // 地圖資料
            groupJson = jsonObject.get("LatLngs").getAsString();
            System.out.println("groupJson_LatLngs = " + groupJson);
            listType = new TypeToken<List<Double[]>>() {}.getType();
            LatLngs = gson.fromJson(groupJson, listType);
            // 存入DB
            count = groupAction.add(group, LatLngs);
            writeText(response, String.valueOf(count));
            break;
        case "deleteById":
            // 團購ID
            id = jsonObject.get("id").getAsInt();
            // 商品清單ID
            merchsIdJson = jsonObject.get("merchsId").getAsString();
            System.out.println("merchsId = " + merchsIdJson);
            listType = new TypeToken<List<Integer>>() {}.getType();
            merchsId = gson.fromJson(merchsIdJson, listType);
            // DB
            count = groupAction.deleteById(id, merchsId);
            writeText(response, String.valueOf(count));
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
