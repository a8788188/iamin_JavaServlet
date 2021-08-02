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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.bean.Merch;
import com.dao.MerchDao;
import com.dao.implemen.MerchDaoImp;
import com.model.MerchAction;

@WebServlet("/Merch")
public class MerchController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println("Merch doPost - Start");
	    request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        String merchJson;
        Merch merch;
        List<Merch> merchs;
        int id;
        final List<byte[]> imgsTemp = new ArrayList<>();
        // 回傳結果
        int count = 0;
        byte[] image;
        List<byte[]> images = new ArrayList<>();
        
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
        MerchAction merchAction = new MerchAction();
        String action = jsonObject.get("action").getAsString();
        System.out.println("action---: " + action);
        
        switch (action) {
        case "getAllByMemberId":
            merchs = merchAction.getAllByMemberId(jsonObject.get("memberId").getAsInt());
            writeText(response, gson.toJson(merchs));
            break;
            
        case "getAllByGroupId":
            merchs = merchAction.getAllByGroupId(jsonObject.get("groupId").getAsInt());
            writeText(response, gson.toJson(merchs));
            break;
            
        case "deleteById":
            id = jsonObject.get("id").getAsInt();
            count = merchAction.deleteById(id);
            writeText(response, String.valueOf(count));
            break;
        
        case "insert":
            merchJson = jsonObject.get("merch").getAsString();
            System.out.println("merchJson = " + merchJson);
            merch = gson.fromJson(merchJson, Merch.class);
            
            // 檢查是否有上傳圖片
            if (jsonObject.get("arrImgBase64") != null) {
                //
                JsonArray jsonArray = new JsonArray();
                jsonArray = jsonObject.get("arrImgBase64").getAsJsonArray();
                //
                jsonArray.forEach(data -> {
                    if (data.isJsonObject()) {
                        final JsonObject jsonObjectImg = data.getAsJsonObject();
                        String imageBase64 = jsonObjectImg.get("image").getAsString();
//                        System.out.println("imageBase64 = " + imageBase64);
                        if (imageBase64 != null && !imageBase64.isEmpty()) {
                            final byte[] img;
                            img = Base64.getMimeDecoder().decode(imageBase64);
                            imgsTemp.add(img);
                        }
                    }
                });
            }
            images = imgsTemp;
            System.out.println("images = " + images);
            
            count = merchAction.add(merch, images);
            writeText(response, String.valueOf(count));
            break;
            
        case "getImage":
            id = jsonObject.get("id").getAsInt();
            image = merchAction.getMerchImgById(id);
            writeText(response, gson.toJson(image));
            break;
        
        case "getImageForIos":
            OutputStream os = response.getOutputStream();
            id = jsonObject.get("id").getAsInt();
            image = merchAction.getMerchImgByIdForIos(id, jsonObject.get("number").getAsInt());
            os.write(image);
            break;
            
        case "getImages":
            id = jsonObject.get("id").getAsInt();
            images = merchAction.getMerchImgsById(id);
            writeText(response, gson.toJson(images));
            break;
            
        case "update":
            merchJson = jsonObject.get("merch").getAsString();
            System.out.println("merchJson = " + merchJson);
            merch = gson.fromJson(merchJson, Merch.class);
            
            // 檢查是否有上傳圖片
            if (jsonObject.get("arrImgBase64") != null) {
                //
                JsonArray jsonArray = new JsonArray();
                jsonArray = jsonObject.get("arrImgBase64").getAsJsonArray();
                //
                jsonArray.forEach(data -> {
                    if (data.isJsonObject()) {
                        final JsonObject jsonObjectImg = data.getAsJsonObject();
                        String imageBase64 = jsonObjectImg.get("image").getAsString();
//                        System.out.println("imageBase64 = " + imageBase64);
                        if (imageBase64 != null && !imageBase64.isEmpty()) {
                            final byte[] img;
                            img = Base64.getMimeDecoder().decode(imageBase64);
                            imgsTemp.add(img);
                        }
                    }
                });
            }
            images = imgsTemp;
            System.out.println("images = " + images);
            
            count = merchAction.update(merch, images);
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
