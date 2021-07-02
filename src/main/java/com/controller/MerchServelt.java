package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Merch;
import com.dao.HomedataDao;
import com.dao.MerchDao;
import com.dao.implemen.HomedataDaoImp;
import com.dao.implemen.MerchDaoImp;
import com.data.Homedata;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


@WebServlet("/Merch")
public class MerchServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MerchDao merchDao = null;
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
