package com.asuka.test;

import com.asuka.utils.JDBCDruidUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
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

}
