package com.asuka.clientServlet;

import com.asuka.domain.Response;
import com.asuka.domain.User;
import com.asuka.service.HandleRequestUser;
import com.asuka.utils.Dao;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//登录
@WebServlet("/login")
public class loginServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("\nlogin_doPost");
        //设置编码，防止乱码
        resp.setCharacterEncoding("utf-8");
        User user = HandleRequestUser.getUser(req);
        Boolean b = new Dao().login(user.getUsername(),user.getPassword());
        System.out.println("允许登录？: "+ b );
        PrintWriter writer = resp.getWriter();
        if (b){
            writer.write(new Gson().toJson(new Response(1,"用户存在")));
        }else {
            writer.write(new Gson().toJson(new Response(0,"用户不存在或密码错误")));
        }
        writer.flush();
        writer.close();
    }
}
