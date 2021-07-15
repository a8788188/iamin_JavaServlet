package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.Group;
import com.dao.GroupDao;
import com.dao.HomedataDao;
import com.dao.MerchDao;
import com.dao.implemen.GroupDaoImp;
import com.dao.implemen.HomedataDaoImp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mysql.cj.protocol.a.result.ByteArrayRow;
import com.dao.common.ImageUtil;


@WebServlet("/Home")
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GroupDao groupDao = null;
	private HomedataDao homedataDao = null;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			int GroupId;
			System.out.println("Home doPost - Start");
	        request.setCharacterEncoding("UTF-8");
	        response.setContentType("application/json; charset=UTF-8");
	        Gson gson = new Gson();
	        
	        if (groupDao == null) {
	        	groupDao = new GroupDaoImp();
			}
	        if (homedataDao == null) {
	        	homedataDao = new HomedataDaoImp();
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
	        List<Group> groups = new ArrayList<>();
	        
	        
	        switch (action) {
	        case "getAllGroup":
	        	groups= groupDao.selectAll();
	        	Gson gson2 = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").create();
	        	writeText(response, gson2.toJson(groups));
	        	break;

	        case "getGroupimage":
	        	OutputStream os = response.getOutputStream();
	        	GroupId = jsonObject.get("groupID").getAsInt();
				int imageSize = jsonObject.get("imageSize").getAsInt();
				byte[] image = homedataDao.getGroupimage(GroupId);
				if (image != null) {
					image = ImageUtil.shrink(image, imageSize);
					response.setContentType("image/*");
					response.setContentLength(image.length);
					os.write(image);
				}
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
