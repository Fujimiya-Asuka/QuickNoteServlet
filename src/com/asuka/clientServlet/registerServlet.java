package com.asuka.clientServlet;

import com.asuka.domain.Response;
import com.asuka.domain.User;
import com.asuka.service.HandleRequestUser;
import com.asuka.utils.Dao;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@WebServlet("/register")
public class registerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("\nregister_doPost");
        //设置编码，防止乱码
        resp.setCharacterEncoding("utf-8");

        User user = HandleRequestUser.getUser(req);
        Boolean b = new Dao().register(user.getUsername(),user.getPassword());
        System.out.println("允许注册？: "+ b );
        PrintWriter writer = resp.getWriter();
        if (b){
            new Dao().createTable(user.getUsername());
            writer.write(new Gson().toJson(new Response(1,"注册成功")));
        }else {
            writer.write(new Gson().toJson(new Response(0,"注册失败")));
        }
        writer.flush();
        writer.close();
    }
}
