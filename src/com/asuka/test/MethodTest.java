package com.asuka.test;

import com.asuka.domain.ToDo;
import com.asuka.utils.Dao;
import com.asuka.utils.JDBCDruidUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import com.asuka.domain.User;

import java.util.List;

public class MethodTest {



    @Test
    public void jsonTest(){
        Gson gson = new Gson();
        String jsonData = "[{\"password\":\"456\",\"username\":\"123\"}]";
        List<User> user = gson.fromJson(jsonData,new TypeToken<List<User>>(){}.getType());
        for (User user1 : user) {
            System.out.println(user1.getUsername()+user1.getPassword());
        }
    }

    @Test
    public void register(){
        String sql = "INSERT INTO account (username,PASSWORD) SELECT ?,? FROM DUAL WHERE NOT EXISTS (SELECT * FROM account WHERE username=?)";
        int n = new JdbcTemplate(JDBCDruidUtils.getDataSource()).update(sql, 111,222,333);
        System.out.println(n);
    }

    @Test
    public void selectMaxModifyInToDo(){
        String tableName = "todo_"+123;
        String sql = "SELECT MAX(modify) FROM "+tableName;
//        String sql2 ="SELECT MAX(MODIFY) FROM todo_123";
        Integer integer = new JdbcTemplate(JDBCDruidUtils.getDataSource()).queryForObject(sql, null, Integer.class);
        System.out.println(integer);
    }

    @Test
    public void getAllNotSyncToDo(){
        String tableName = "todo_"+"123";
        int modify = 1;
        String sql = "SELECT * FROM "+tableName+" WHERE MODIFY>?";
        String sql2 = "SELECT * FROM "+tableName+" WHERE MODIFY>1";
//        List<ToDo> toDoList = new JdbcTemplate(JDBCDruidUtils.getDataSource()).queryForList(sql2,ToDo.class);
        List<ToDo> toDoList = new JdbcTemplate(JDBCDruidUtils.getDataSource()).query(sql2, new BeanPropertyRowMapper<>(ToDo.class));
        for (ToDo toDo : toDoList) {
            System.out.println(toDo);
        }
    }

    @Test
    public void updateToDo(){
        String tableName = "todo_"+123;
        String sql = "UPDATE "+tableName+" SET title=?,time=?,notify=? WHERE id=?";
        new JdbcTemplate(JDBCDruidUtils.getDataSource()).update(sql, 200, 400, 2, 7);
    }

    @Test
    public void createTable(){
        String tableName = "todo_"+456;
        String sql = "CREATE TABLE IF NOT EXISTS "+tableName+"(" +
                "`id` INT PRIMARY KEY," +
                "`title` TEXT," +
                "`time` TEXT," +
                "`notifi` INT," +
                "`modify` INT)";
        new JdbcTemplate(JDBCDruidUtils.getDataSource()).execute(sql);
    }

    @Test
    public void getMaxModifyInNote(){
        String tableName = "note_"+123;
        String sql = "SELECT MAX(modify) FROM "+tableName;
        Integer integer = new JdbcTemplate(JDBCDruidUtils.getDataSource()).queryForObject(sql, null, Integer.class);
        System.out.println(""+integer);
    }

}
