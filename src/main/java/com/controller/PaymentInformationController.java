package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.MemberOrder;
import com.bean.MemberOrderDetails;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.model.GroupAction;
import com.model.PaymentInformationAction;

@WebServlet("/PaymentInformation")
public class PaymentInformationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println("PaymentInformation doPost - Start");
	    request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        Type listType;
        String groupJson;
        List<MemberOrder> memberOrders = new ArrayList<>();
        final List<MemberOrder> tempMemberOrders = new ArrayList<>();
        List<MemberOrderDetails> memberOrderDetails = new ArrayList<>();
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
        JsonArray jsonArray = new JsonArray();
        // action
        PaymentInformationAction paymentInformationAction = new PaymentInformationAction();
        String action = jsonObject.get("action").getAsString();
        System.out.println("action---: " + action);
        
        switch (action) {
            case "getMemberOrderByGroupId":
                memberOrders = paymentInformationAction.getAllByGroupId(jsonObject.get("groupId").getAsInt());
                writeText(response, gson.toJson(memberOrders));
                break;
            case "getMemberOrderDetailsByMemberOrders":
                // 
                jsonArray = jsonObject.get("memberOrders").getAsJsonArray();
                // jsonArray 解析
                jsonArray.forEach(data -> {
                    if (data.isJsonObject()) {
                        final JsonObject jsonObjectOM = data.getAsJsonObject();
                        String memberOrderStr = jsonObjectOM.get("memberOrder").getAsString();
                        if (memberOrderStr != null && !memberOrderStr.isEmpty()) {
                            /** 匿名內部類別實作TypeToken，抓取 泛型 在呼叫方法 */
                            MemberOrder memberOrder = new Gson().fromJson(memberOrderStr, MemberOrder.class);
                            tempMemberOrders.add(memberOrder);
                        }
                    }
                });
                memberOrders = tempMemberOrders;
                
                memberOrderDetails = paymentInformationAction.getAllByMemberOrders(memberOrders);
                writeText(response, gson.toJson(memberOrderDetails));
                break;
            case "updateMemberOrders":
                // 
                jsonArray = jsonObject.get("memberOrders").getAsJsonArray();
                // jsonArray 解析
                jsonArray.forEach(data -> {
                    if (data.isJsonObject()) {
                        final JsonObject jsonObjectOM = data.getAsJsonObject();
                        String memberOrderStr = jsonObjectOM.get("memberOrder").getAsString();
                        if (memberOrderStr != null && !memberOrderStr.isEmpty()) {
                            /** 匿名內部類別實作TypeToken，抓取 泛型 在呼叫方法 */
                            MemberOrder memberOrder = new Gson().fromJson(memberOrderStr, MemberOrder.class);
                            tempMemberOrders.add(memberOrder);
                        }
                    }
                });
                memberOrders = tempMemberOrders;
                
                count = paymentInformationAction.updates(memberOrders);
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
