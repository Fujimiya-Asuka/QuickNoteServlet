package com.asuka.clientServlet;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/test")
public class testServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        System.out.println(name+password);
    }
}
