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
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.model.MerchAction;

@WebServlet("/Fcm")
public class FcmController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// (測試資料) 儲存所有client送來的token
    private static final Set<String> registrationTokens = Collections.synchronizedSet(new HashSet<>());
    
    @Override
    public void init() throws ServletException {
        // (測試資料)
        registrationTokens.add("efkU2I_dSNOIiPaeQ3nnGf:APA91bFrCgy1Ad5tu-GoKQKYtP9j5ZoesJfwzpDpv1oYNnOLXA43gZH7aEcHyEciwQqMNLDV8fU1SugpzvoXzCSfmZK-QqEV_l_52weAqqQnSZKeTNWd-SUO1Y5uiX6iQLUhlmHpxPg2");
        // 私密金鑰檔案可以儲存在專案以外
        // File file = new File("/path/to/firsebase-java-privateKey.json");
        // 私密金鑰檔案也可以儲存在專案WebContent目錄內，私密金鑰檔名要與程式所指定的檔名相同
        try (InputStream in = getServletContext().getResourceAsStream("/firebaseServerKey.json")) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println("Fcm doPost - Start");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        String title = "";
        String body = "";
        String data = "";
        
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
            case "sendFcmByGroupId":
                title = jsonObject.get("title").getAsString();
                body = jsonObject.get("body").getAsString();
                data = jsonObject.get("data").getAsString();
                
                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build();

                MulticastMessage message = MulticastMessage.builder()
                        .setNotification(notification)
                        .putData("data", data)
                        .addAllTokens(registrationTokens).build(); // addAllTokens 上限是500個
                try {
                    BatchResponse batchResponse = FirebaseMessaging.getInstance().sendMulticast(message);
                    System.out.println(batchResponse.getSuccessCount() + " messages were sent successfully");
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                }
                
                writeText(response, "Group FCMs are sent!");
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
