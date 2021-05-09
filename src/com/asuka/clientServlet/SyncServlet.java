package com.asuka.clientServlet;


import com.asuka.service.Sync;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SyncServlet")
public class SyncServlet extends HttpServlet {
    private final String TAG = "\nSyncServlet：";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(TAG+"doPost");
//        //设置编码
//        request.setCharacterEncoding("utf-8");
//        response.setCharacterEncoding("utf-8");
//
//        //获取请求头参数
//        String user = request.getHeader("user");
//        String method = request.getHeader("method");
//        System.out.println("请求的用户是:"+user+"\n请求方法是:"+method);
//        //处理请求体
//        BufferedReader reader = request.getReader();
//        JsonParser jsonParser = new JsonParser();
//        List<ToDo> toDoList = new ArrayList<>();
//        JsonArray asJsonArray = jsonParser.parse(reader).getAsJsonArray();
//        //查询服务器最大时间戳
//        int dbMaxModify = new Dao().selectMaxModifyInToDo(user);
//        System.out.println("当前服务器待办最大时间戳："+dbMaxModify);
//        for (JsonElement jsonElement : asJsonArray) {
//            ToDo toDo = new Gson().fromJson(jsonElement,ToDo.class);
//            toDoList.add(toDo);
//        }
//        int todoID=0;
//        for (ToDo toDo : toDoList) {
//            //新请求插入的待办要在服务器最大时间戳上+1
//            toDo.setModify(dbMaxModify+1);
//            todoID = new Dao().insertToDo("123", toDo);
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
        //设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //获取请求头参数
        String user = request.getHeader("user");
        String method = request.getHeader("method");
        System.out.println("请求的用户是:"+user+"\n请求方法是:"+method);
        switch (method){
            case "uploadToDo" :
                new Sync(request,response).uploadToDo();
                break;

            case "downloadToDo" :
                new Sync(request,response).downloadTodo();
                break;

            case "deleteToDo" :
                new Sync(request,response).deleteToDo();
                break;

            case "uploadNote" :
                new Sync(request,response).uploadNote();
                break;

            case "downloadNote" :
                new Sync(request,response).downloadNote();
                break;

            case "deleteNote" :
                new Sync(request,response).deleteNote();
                break;

            default:
                break;
        }


    }

}
