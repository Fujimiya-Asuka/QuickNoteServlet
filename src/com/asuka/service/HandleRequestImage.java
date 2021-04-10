package com.asuka.service;

import com.asuka.domain.Image;
import com.asuka.domain.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class HandleRequestImage {

    public static Image getImage(HttpServletRequest request) throws IOException {
        //设置编码，防止乱码
        request.setCharacterEncoding("utf-8");
        //读取request的请求体
        BufferedReader reader = request.getReader();
        String s = reader.readLine();
        Gson gson = new Gson();
        String jsonData = "["+s+"]";
        System.out.println(jsonData);
        //转换成Image对象
        List<Image> imageList = gson.fromJson(jsonData,new TypeToken<List<Image>>(){}.getType());
        //请求的用户名与密码
        String requestUser = null;
        String requestFileName = null;
        for (Image image : imageList) {
            requestUser=image.getUserName();
            requestFileName=image.getFileName();
            System.out.println("请求的用户名和文件名是"+requestUser+"和"+requestFileName);
        }
        return imageList.get(0);
    }

}
