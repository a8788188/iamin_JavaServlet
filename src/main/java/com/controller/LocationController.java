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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.bean.Location;
import com.model.GroupAction;
import com.model.LocationAction;

@WebServlet("/Location")
public class LocationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println("Location doPost - Start");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").create();
        int groupId;
        List<Location> locations;
        Location location;
        String locationJson;
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
        LocationAction locationAction = new LocationAction();
        String action = jsonObject.get("action").getAsString();
        System.out.println("action---: " + action);
        
        switch (action) {
            case "getAllByGroupId":
                groupId = jsonObject.get("groupId").getAsInt();
                locations = locationAction.getAllByGroupId(groupId);
                writeText(response, gson.toJson(locations));
                break;
            case "update":
                locationJson = jsonObject.get("location").getAsString();
                location = new GsonBuilder().setDateFormat("MMM d, yyyy h:mm:ss a").create().fromJson(locationJson, Location.class);
                count = locationAction.update(location);
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
