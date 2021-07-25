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

import com.bean.MemberOrder;
import com.bean.Member;
import com.bean.Rating;
import com.dao.MemberDao;
import com.dao.RatingDao;
import com.dao.implemen.MemberDaoImp;
import com.dao.implemen.MerchDaoImp;
import com.dao.implemen.RatingDaoImp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.model.MerchAction;

@WebServlet("/Rating")
public class RatingController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private RatingDao ratingDao;  
    private MemberDao memberDao;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 System.out.println("Rating doPost - Start");
		    request.setCharacterEncoding("UTF-8");
	        response.setContentType("application/json; charset=UTF-8");
	        Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").create();
	        // 回傳結果
	        int count = 0;
	        
	        if (ratingDao == null) {
	        	ratingDao = new RatingDaoImp();
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
	        case "insertratingAndupdatamember":
	        	String ratingJson = jsonObject.get("rating").getAsString();
				System.out.println("spotJson = " + ratingJson);
				Rating rating = gson.fromJson(ratingJson, Rating.class);
				count = ratingDao.insert(rating);
				//RatingDaoImp的方法 取得該seller_id的評價總分
				int ratingsum = ratingDao.selectSumrating(rating.getSeller_Id());
				//RatingDaoImp的方法 取得該seller_id的評價總數
				int ratingcount = ratingDao.selectCountrating(rating.getSeller_Id());
				//取得當前的seller(型態是Member,用於方便updata)
				Member member = memberDao.findById(rating.getSeller_Id());
				//setRating為ratingsum(賣家總分)/ratingcount(賣家總數)
				member.setRating(ratingsum/ratingcount);
				
				memberDao.update(member, null);
				writeText(response, String.valueOf(count));
	        	break;
	        }
	}
	
	private void writeText(HttpServletResponse response, String outText) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(outText);
        System.out.println("output: " + outText);
    }

}
