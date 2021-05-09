package com.asuka.service;

import com.asuka.domain.Note;
import com.asuka.domain.ResponseSyncToDoMessage;
import com.asuka.domain.ToDo;
import com.asuka.utils.Dao;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Sync {
    private final String TAG = "Sync：";
    private HttpServletRequest request;
    private HttpServletResponse response;


    public Sync() {
    }

    public Sync(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
    }


    /**
     * 上传待办
     * @Author:  XuZhenHui
     * @Time:  2021/4/24 13:12
     */
    public void uploadToDo() throws IOException {
        System.out.println(TAG+"uploadToDo");
        //处理请求体
        BufferedReader reader = request.getReader();
        JsonParser jsonParser = new JsonParser();
        List<ToDo> toDoList = new ArrayList<>();
        JsonArray asJsonArray = jsonParser.parse(reader).getAsJsonArray();
        //查询服务器最大时间戳
        int dbMaxModify = new Dao().getMaxModifyInToDo(request.getHeader("user"));
        System.out.println("当前服务器待办最大时间戳："+dbMaxModify);
        for (JsonElement jsonElement : asJsonArray) {
            ToDo toDo = new Gson().fromJson(jsonElement,ToDo.class);
            toDoList.add(toDo);
        }
        int todoID=0;
        for (ToDo toDo : toDoList) {
            System.out.println("处理内容："+toDo.toString());
            //新请求插入的待办要在服务器最大时间戳上+1
            toDo.setModify(dbMaxModify+1);
            if (toDo.getState()==0){
                todoID = new Dao().addToDo(request.getHeader("user"), toDo);
            }else if (toDo.getState()==1){
                todoID = new Dao().updateToDo(request.getHeader("user"),toDo);
            }
        }
        //回写数据
        if (todoID>0){
            response.setHeader("resultCode",""+1);
        }else {
            response.setHeader("resultCode",""+0);
        }
        response.setHeader("todoID",""+todoID);
        response.setHeader("modify",""+(dbMaxModify+1));
//        PrintWriter writer = response.getWriter();
//        int returnModify = dbMaxModify+1;
//        System.out.println("回传时间戳："+returnModify);
//        ResponseSyncToDoMessage responseSyncToDoMessage =
//                new ResponseSyncToDoMessage("123",todoID, -1, returnModify);
//        if (todoID>0){
//            responseSyncToDoMessage.setResultCode(1);
//        }else {
//            responseSyncToDoMessage.setRequestUser("请求失败");
//            responseSyncToDoMessage.setResultCode(-1);
//            responseSyncToDoMessage.setModify(0);
//        }
//        String s = new Gson().toJson(responseSyncToDoMessage);
//        System.out.println("回写数据："+s);
//        writer.write(s);
    }

    /**
     * 下载待办
     * @Author:  XuZhenHui
     * @Time:  2021/4/24 14:35
     */
    public void downloadTodo() throws IOException{
        System.out.println(TAG+"downloadToDo");
        String requestUser = request.getHeader("user");
        //请求时间戳
        int requestModify = Integer.parseInt(request.getHeader("modify"));
        //本地最大时间戳
        int dbToDoMaxModify = new Dao().getMaxModifyInToDo(requestUser);
        if (requestModify==dbToDoMaxModify){
            //同步完成
        } else if (requestModify>dbToDoMaxModify) {
            //客户端有未同步到客服务器的待办
        }else if (requestModify<dbToDoMaxModify){
            //回写数据
            List<ToDo> allNotSyncToDo = new Dao().getAllNotSyncToDo(requestUser, requestModify);
            String s = new Gson().toJson(allNotSyncToDo);
            PrintWriter writer = response.getWriter();
            System.out.println("回写数据："+s);
            writer.write(s);
        }



    }

    /**
     * 删除待办
     * @Author:  XuZhenHui
     * @Time:  2021/5/9 0:07
     * @throws IOException
     */
    public void deleteToDo() throws IOException {
        System.out.println(TAG + "deleteToDo");
        //获取请求用户
        String user = request.getHeader("user");
        String todoID = request.getHeader("todoID");
        int deleteCount = new Dao().deleteToDo(user, Integer.parseInt(todoID));
        //回写数据
        if (deleteCount>0){
            response.setHeader("resultCode",""+1);
        }else {
            response.setHeader("resultCode",""+0);
        }
        response.setHeader("todoID",""+todoID);
    }


    /**
     * 上传便签
     * @Author:  XuZhenHui
     * @Time:  2021/5/9 16:44
     * @throws IOException
     */
    public void uploadNote() throws IOException {
        System.out.println(TAG+"uploadNote");
        //处理请求体
        BufferedReader reader = request.getReader();
        JsonParser jsonParser = new JsonParser();
        List<Note> noteList = new ArrayList<>();
        JsonArray asJsonArray = jsonParser.parse(reader).getAsJsonArray();
        //查询服务器最大时间戳
        int dbMaxModify = new Dao().getMaxModifyInNote(request.getHeader("user"));
        System.out.println("当前服务器待办最大时间戳："+dbMaxModify);
        for (JsonElement jsonElement : asJsonArray) {
            Note note = new Gson().fromJson(jsonElement,Note.class);
            noteList.add(note);
        }
        int noteID=0;
        for (Note note : noteList) {
            System.out.println("处理内容："+note.toString());
            //新请求插入的待办要在服务器最大时间戳上+1
            note.setModify(dbMaxModify+1);
            if (note.getState()==0){
                noteID = new Dao().addNote(request.getHeader("user"), note);
            }else if (note.getState()==1){
                noteID = new Dao().updateNote(request.getHeader("user"),note);
            }
        }
        //回写数据
        if (noteID>0){
            response.setHeader("resultCode",""+1);
        }else {
            response.setHeader("resultCode",""+0);
        }
        response.setHeader("todoID",""+noteID);
        response.setHeader("modify",""+(dbMaxModify+1));
//        PrintWriter writer = response.getWriter();
//        int returnModify = dbMaxModify+1;
//        System.out.println("回传时间戳："+returnModify);
//        ResponseSyncToDoMessage responseSyncToDoMessage =
//                new ResponseSyncToDoMessage("123",todoID, -1, returnModify);
//        if (todoID>0){
//            responseSyncToDoMessage.setResultCode(1);
//        }else {
//            responseSyncToDoMessage.setRequestUser("请求失败");
//            responseSyncToDoMessage.setResultCode(-1);
//            responseSyncToDoMessage.setModify(0);
//        }
//        String s = new Gson().toJson(responseSyncToDoMessage);
//        System.out.println("回写数据："+s);
//        writer.write(s);
    }

    /**
     * 下载便签
     * @Author:  XuZhenHui
     * @Time:  2021/5/9 16:48
     * @throws IOException
     */
    public void downloadNote() throws IOException{
        System.out.println(TAG+"downloadNote");
        String requestUser = request.getHeader("user");
        //请求时间戳
        int requestModify = Integer.parseInt(request.getHeader("modify"));
        //本地最大时间戳
        int dbToDoMaxModify = new Dao().getMaxModifyInNote(requestUser);
        if (requestModify==dbToDoMaxModify){
            //同步完成
        } else if (requestModify>dbToDoMaxModify) {
            //客户端有未同步到客服务器的待办
        }else if (requestModify<dbToDoMaxModify){
            //回写数据
            List<Note> allNotSyncNote = new Dao().getAllNotSyncNote(requestUser, requestModify);
            String s = new Gson().toJson(allNotSyncNote);
            PrintWriter writer = response.getWriter();
            System.out.println("回写数据："+s);
            writer.write(s);
        }



    }

    /**
     * 删除便签
     * @Author:  XuZhenHui
     * @Time:  2021/5/9 16:50
     * @throws IOException
     */
    public void deleteNote() throws IOException {
        System.out.println(TAG + "deleteNote");
        //获取请求用户
        String user = request.getHeader("user");
        String noteID = request.getHeader("noteID");
        int deleteCount = new Dao().deleteNote(user, Integer.parseInt(noteID));
        //回写数据
        if (deleteCount>0){
            response.setHeader("resultCode",""+1);
        }else {
            response.setHeader("resultCode",""+0);
        }
        response.setHeader("todoID",""+noteID);
    }


//        for (DeleteToDoID id : deleteToDoIDList) {
//            new Dao().deleteToDo("123",id);
//        }

//        //回写数据
//        PrintWriter writer = response.getWriter();
//        int returnModify = dbMaxModify+1;
//        System.out.println("回传时间戳："+returnModify);
//        ResponseSyncToDoMessage responseSyncToDoMessage =
//                new ResponseSyncToDoMessage("123",todoID, -1, returnModify);
//        if (todoID>0){
//            responseSyncToDoMessage.setResultCode(1);
//        }else {
//            responseSyncToDoMessage.setRequestUser("请求失败");
//            responseSyncToDoMessage.setResultCode(-1);
//            responseSyncToDoMessage.setModify(0);
//        }
//        String s = new Gson().toJson(responseSyncToDoMessage);
//        System.out.println("回写数据："+s);
//        writer.write(s);
//    }

}
