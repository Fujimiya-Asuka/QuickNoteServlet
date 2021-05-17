package com.asuka.service;

import com.asuka.domain.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class HandleRequest {

    public static User getUser(HttpServletRequest request) throws IOException {
        //设置编码，防止乱码
        request.setCharacterEncoding("utf-8");
        //读取request的请求体
        BufferedReader reader = request.getReader();
        String s = reader.readLine();
        Gson gson = new Gson();
        String jsonData = "["+s+"]";
        System.out.println(jsonData);
        //转换成user对象
        List<User> userList = gson.fromJson(jsonData,new TypeToken<List<User>>(){}.getType());
        //请求的用户名与密码
        String requestUser = null;
        String requestPassword = null;
        for (User user : userList) {
            requestUser=user.getUsername();
            requestPassword=user.getPassword();
            System.out.println("请求的用户名和密码是"+requestUser+"和"+requestPassword);
        }
        return userList.get(0);
    }

}
