package com.asuka.clientServlet;

import com.asuka.domain.Image;
import com.asuka.service.HandleRequestImage;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Enumeration;

@WebServlet("/uploadImage")
@MultipartConfig //表示该Servlet希望处理的请求时multipart/form-data类型的。
public class uploadImageServlet extends HttpServlet {
    public final String TAG = "uploadImageServlet：";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(TAG+"doPost");
        //处理请求体
        //设置编码
        request.setCharacterEncoding("UTF-8");
        //获取请求用户名
        String userName = request.getParameter("userName");
        String noteID = request.getParameter("noteID");
        //获取part对象
        Part part = request.getPart("upFile");
        //获取上传文件名
        String fileName = "noteID"+"_"+noteID+"_"+part.getSubmittedFileName();
        System.out.println(fileName);
        //获取对应上传文件夹的真实路径
        String realPath = request.getServletContext().getRealPath("/image/"+userName+"/");
        //如果目录不存在则创建目录
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdir();
        }
        System.out.println("文件已经存放在："+realPath);
        System.out.println("文件名："+fileName);
        //将文件写入对应目录
        part.write(realPath+fileName);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
