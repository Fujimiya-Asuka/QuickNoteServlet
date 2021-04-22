package com.asuka.utils;

import com.asuka.domain.ToDo;
import com.asuka.domain.User;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class Dao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCDruidUtils.getDataSource());

    @Test
    public Boolean findUser(String requestUser, String requestPassword){
        String sql = "SELECT * FROM account WHERE username=? and password=?";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class),requestUser,requestPassword);
        if (list.size()==1){
            return true;
        }else {
            return false;
        }
    }

    public Boolean register(String username,String password){
        String sql = "INSERT INTO account (username,password) SELECT ?,? FROM DUAL WHERE NOT EXISTS (SELECT * FROM account WHERE username=?)";
        int n = jdbcTemplate.update(sql,username,password,username);
        if (n==1){
            System.out.println("插入成功");
            return true;
        }else{
            System.out.println("插入失败");
            return false;
        }
    }

    /**
     * 添加待办
     * @Author:  XuZhenHui
     * @Time:  2021/4/22 22:16
     * @param user
     * 用户名
     * @param toDo
     * 待办
     * @return
     */
    public int insertToDo(String user, ToDo toDo){
        String tableName = "todo_"+user;
        System.out.println(tableName);
        System.out.println(toDo.toString());
        String sql = "INSERT INTO "+tableName+" VALUES (?,?,?,?,?)";
        String sql2 = "INSERT INTO "+tableName+" VALUES (?,?,?,?,?) FROM DUAL WHERE NOT EXISTS (SELECT * FROM account WHERE id=?)";
        jdbcTemplate.update(sql, toDo.getId(), toDo.getTitle(), toDo.getTime(), toDo.getNotify(),0);
        return (int)toDo.getId();
    }

}
