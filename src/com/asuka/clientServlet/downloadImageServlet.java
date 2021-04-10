package com.asuka.clientServlet;

import com.asuka.domain.Image;
import com.asuka.domain.Response;
import com.asuka.service.HandleRequestImage;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/downloadImage")
public class downloadImageServlet extends HttpServlet {
    public final String TAG = "downloadImageServlet：";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(TAG+"doPost");
        //处理请求体内容
        Image image = HandleRequestImage.getImage(request);
        //如果请求图片不存在
//        if (image==null){
//            writer.write(new Gson().toJson(new Response(0, "请求图片不存在")));
//        }else {
            //获取请求文件名
            String filename = image.getFileName();
            //获取请求文件的真实路径
            ServletContext servletContext = this.getServletContext();
            String realPath = servletContext.getRealPath("/image/" + filename);
            //设置Response响应头
            //设置响应头类型content-type
            String mimeType = servletContext.getMimeType(filename); //根据请求文件名动态获取MimeType
            response.setHeader("content-type",mimeType);
            //设置相应头打开方式：content-disposition
            response.setHeader("content-disposition","attachment;filename"+filename);
            //获取Response字节输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //获取请求文件的字节输入流，并将文件写入Response
            FileInputStream fileInputStream = new FileInputStream(realPath);
            byte[] buff = new byte[1024*8];
            int len = 0;
            while ((len=fileInputStream.read(buff))!=-1){
                outputStream.write(buff,0,len);
            }
            fileInputStream.close();

//        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
