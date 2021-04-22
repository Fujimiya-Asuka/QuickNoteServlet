package com.asuka.clientServlet;

import com.asuka.domain.ResponseSyncToDoMessage;
import com.asuka.domain.ToDo;
import com.asuka.utils.Dao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/SyncServlet")
public class SyncServlet extends HttpServlet {
    private final String TAG = "SyncServlet：";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(TAG+"doPost");
        //设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //处理请求体
        BufferedReader reader = request.getReader();
        JsonParser jsonParser = new JsonParser();
        List<ToDo> toDoList = new ArrayList<>();
        JsonArray asJsonArray = jsonParser.parse(reader).getAsJsonArray();
        for (JsonElement jsonElement : asJsonArray) {
            ToDo toDo = new Gson().fromJson(jsonElement,ToDo.class);
            toDoList.add(toDo);
        }
        int todoID=0;
        for (ToDo toDo : toDoList) {
            todoID = new Dao().insertToDo("123", toDo);
        }

        //回写数据
        if (todoID>0){
            PrintWriter writer = response.getWriter();
            ResponseSyncToDoMessage responseSyncToDoMessage = new ResponseSyncToDoMessage("123",todoID, 1, 1);
            String s = new Gson().toJson(responseSyncToDoMessage);
            System.out.println(s);
            writer.write(s);
        }

    }

}
