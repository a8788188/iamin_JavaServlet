package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet("/FcmChatServlet")
public class FcmChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// client送來的token
	private String registrationToken = "";
	// 儲存所有client送來的token
	private static final Set<String> registrationTokens = Collections.synchronizedSet(new HashSet<>());
    
    @Override
    public void init() throws ServletException {
    	try (
    			InputStream in = getServletContext().getResourceAsStream("/firebaseServerKey.json")
    		) {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(in))
					.build();
			FirebaseApp.initializeApp(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		BufferedReader br = request.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		System.out.println("input: " + jsonIn);
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		String action = jsonObject.get("action").getAsString();
		switch (action) {
		case "register":
			/*
			 * 暫時將client送來的token儲存在單一變數與set內，為了之後 單一/群組 發送訊息 正式作法應該要將token儲存在DB內
			 */
			registrationToken = jsonObject.get("registrationToken").getAsString();
			registrationTokens.add(registrationToken);
			System.out.println("tokens: ");
			registrationTokens.forEach(System.out::println);
			writeText(response, "Registration token is received!");
			break;
		case "singleFcm":
			sendSingleFcm(jsonObject, registrationToken);
			writeText(response, "Single FCM is sent!");
			break;
		default:
			break;
		}
	}
    
    
    
    private void sendSingleFcm(JsonObject jsonObject, String registrationToken) {
		String title = jsonObject.get("title").getAsString();
		String body = jsonObject.get("body").getAsString();
		String data = jsonObject.get("data").getAsString();

		// 主要設定訊息標題與內容，client app一定要在背景時才會自動顯示
		Notification notification = Notification.builder()
				.setTitle(title) // 設定標題
				.setBody(body) // 設定內容
				.build();
		// 發送notification message
		Message message = Message.builder()
				.setNotification(notification) // 設定client app在背景時會自動顯示訊息
				.putData("data", data) // 設定自訂資料，user點擊訊息時方可取值
				.setToken(registrationToken) // 送訊息給指定token的裝置
				.build();

		try {
			String messageId = FirebaseMessaging.getInstance().send(message);
			System.out.println("messageId: " + messageId);
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
		}
	}
    
    private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(outText);
		// 將輸出資料列印出來除錯用
		// System.out.println("output: " + outText);
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<h3>Chat Fcm Session</h3>");
	}

	
	

}
