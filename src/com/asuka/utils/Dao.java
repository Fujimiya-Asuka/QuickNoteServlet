package com.asuka.utils;

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

}
