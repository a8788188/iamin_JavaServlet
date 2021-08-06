package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Member;
import com.dao.ChatDao;
import com.dao.implemen.ChatDaoImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.dao.common.ImageUtil;

@WebServlet("/Chat")
public class ChatController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=utf-8";
	ChatDao chatDao = null;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}

		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		if (chatDao == null) {
			chatDao = new ChatDaoImpl();
		}
		String action = jsonObject.get("action").getAsString();
		if (action.equals("getAllSeller")) {
//			System.out.println("Chat input: " + jsonIn);
			List<Member> members = chatDao.selectAllSeller();
			writeText(resp, gson.toJson(members));
		} else if (action.equals("getImage")) {
			// 將輸入資料列印出來除錯用
			System.out.println("input: " + jsonIn);
			OutputStream os = resp.getOutputStream();
			int id = jsonObject.get("id").getAsInt();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = chatDao.getImage(id);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				resp.setContentType("image/*");
				resp.setContentLength(image.length);
				os.write(image);
			}
		} else {
			writeText(resp, "-1");
		}

	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 將輸出資料列印出來除錯用
//		System.out.println("output: " + outText);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (chatDao == null) {
			chatDao = new ChatDaoImpl();
		}
		List<Member> members = chatDao.selectAllSeller();
		writeText(resp, new Gson().toJson(members));
	}

}
