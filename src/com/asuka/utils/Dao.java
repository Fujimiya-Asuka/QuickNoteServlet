package com.asuka.utils;

import com.asuka.domain.Note;
import com.asuka.domain.ToDo;
import com.asuka.domain.User;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class Dao {
    private final String TAG = "Dao：";
    JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCDruidUtils.getDataSource());

    /**
     * 登录
     * @Author:  XuZhenHui
     * @Time:  2021/4/29 20:23
     * @param requestUser
     * 用户名
     * @param requestPassword
     * 密码
     * @return
     */
    public Boolean login(String requestUser, String requestPassword){
        String sql = "SELECT * FROM account WHERE username=? and password=?";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class),requestUser,requestPassword);
        if (list.size()==1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 注册
     * @Author:  XuZhenHui
     * @Time:  2021/4/29 20:23
     * @param username
     * 用户名
     * @param password
     * 密码
     * @return
     */
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
     * 创建用户对应的便签和待办表
     * @Author:  XuZhenHui
     * @Time:  2021/4/29 20:25
     * @param username
     * 用户名
     */
    public void createTable(String username){
        String noteTable = "note_"+username;
        String noteSql = "CREATE TABLE IF NOT EXISTS "+noteTable+"(" +
                "`id` INT PRIMARY KEY AUTO_INCREMENT," +
                "`title` TEXT," +
                "`data` TEXT," +
                "`image` TEXT," +
                "`time` TEXT," +
                "`modify` INT)";
        String todoTable = "todo_"+username;
        String todoSql = "CREATE TABLE IF NOT EXISTS "+todoTable+"(" +
                "`id` INT PRIMARY KEY AUTO_INCREMENT," +
                "`title` TEXT," +
                "`time` TEXT," +
                "`notify` INT," +
                "`modify` INT)";
        jdbcTemplate.execute(noteSql);
        jdbcTemplate.execute(todoSql);
    }

    /**
     * 添加待办
     * @Author:  XuZhenHui
     * @Time:  2021/4/22 22:16
     * @param user
     * 用户名
     * @param toDo
     * 待办，需要更新其modify
     * @return
     * 插入成功返回成功插入的待办ID
     * 插入失败则返回0
     */
    public int addToDo(String user, ToDo toDo){
        System.out.println(TAG+"执行添加");
        System.out.println("插入内容为："+toDo.toString());
        String tableName = "todo_"+user;
        String sql = "INSERT INTO "+tableName+" (id,title,time,notify,modify) SELECT ?,?,?,?,? FROM DUAL WHERE NOT EXISTS (SELECT * FROM "+tableName+" WHERE id=?)";
        int updateCount = jdbcTemplate.update(sql, toDo.getId(), toDo.getTitle(), toDo.getTime(), toDo.getNotify(),toDo.getModify(), toDo.getId());
        if (updateCount>0){
            System.out.println("执行同步插入待办成功,插入数量："+updateCount);
            return (int)toDo.getId();
        }else {
            System.out.println("数据库内已存在相同ID的待办记录，无法插入");
            return 0;
        }
    }

    /**
     * 修改待办
     * @Author:  XuZhenHui
     * @Time:  2021/4/28 20:35
     * @param user
     * @param toDo
     */
    public int updateToDo(String user,ToDo toDo){
        System.out.println(TAG+"执行更新");
        String tableName = "todo_"+user;
        System.out.println("修改内容为："+toDo.toString());
        String sql = "UPDATE "+tableName+" SET title=?,time=?,notify=?,modify=? WHERE id=?";
        int updateCount = jdbcTemplate.update(sql, toDo.getTitle(), toDo.getTime(), toDo.getNotify(), toDo.getModify(), toDo.getId());
        if (updateCount>0){
            System.out.println("执行同步更新待办成功,更新数量："+updateCount);
            return (int)toDo.getId();
        }else {
            System.out.println("数据库内不存在对应ID的待办记录，无法更新");
            return 0;
        }
    }

    /**
     * 删除待办
     * @Author:  XuZhenHui
     * @Time:  2021/4/28 20:37
     * @param user
     * @param todoID
     * @return
     */
    public int deleteToDo(String user,int todoID){
        String tableName = "todo_"+user;
        System.out.println("删除待办ID："+todoID);
        //DELETE FROM table_name [WHERE Clause]
        String sql = "DELETE  FROM "+tableName+" WHERE id=?";
        int updateCount = jdbcTemplate.update(sql,todoID);
        if (updateCount>0){
            System.out.println("执行同步删除待办成功,删除数量："+updateCount);
            return updateCount;
        }else {
            System.out.println("数据库内不存在对应ID的待办记录，无法删除");
            return 0;
        }
    }

    /**
     * 根据时间戳获取所有同步待办
     * @Author:  XuZhenHui
     * @Time:  2021/4/24 14:57
     * @param user
     * 用户
     * @param modify
     * 客户端请求的时间戳
     * @return
     * 返回ToDo对象列表
     */
    public List<ToDo> getAllNotSyncToDo(String user,int modify){
        String tableName = "todo_"+user;
        String sql = "SELECT * FROM "+tableName+" WHERE MODIFY>?";
        return  jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(ToDo.class), modify);
    }

    /**
     * 获取服务器上指定用户待办的最大时间戳
     * @Author:  XuZhenHui
     * @Time:  2021/4/24 12:03
     * @param user
     * 用户名
     * @return
     * 返回最大时间戳
     */
    public int getMaxModifyInToDo(String user){
        String tableName = "todo_"+user;
        String sql = "SELECT MAX(modify) FROM "+tableName;
        Integer integer = jdbcTemplate.queryForObject(sql, null, Integer.class);
        if (integer!=null){
            return integer;
        }else {
            return 0;
        }
    }

    /**
     * 添加便签
     * @Author:  XuZhenHui
     * @Time:  2021/5/9 16:32
     * @param user
     * 用户名
     * @param note
     * 便签，需要更新其modify
     * @return
     * 插入成功返回成功插入的便签ID
     * 插入失败则返回0
     */
    public int addNote(String user, Note note){
        System.out.println(TAG+"执行添加");
        System.out.println("插入内容为："+note.toString());
        String tableName = "note_"+user;
        String sql = "INSERT INTO "+tableName+" (id,title,data,image,time,modify) SELECT ?,?,?,?,?,? FROM DUAL WHERE NOT EXISTS (SELECT * FROM "+tableName+" WHERE id=?)";
        int updateCount = jdbcTemplate.update(sql, note.getId(), note.getTitle(), note.getData(), note.getImage(), note.getTime(), note.getModify(), note.getId());
        if (updateCount>0){
            System.out.println("执行同步插入便签成功,插入数量："+updateCount);
            return (int)note.getId();
        }else {
            System.out.println("数据库内已存在相同ID的便签记录，无法插入");
            return 0;
        }
    }

    /**
     * 更新便签
     * @Author:  XuZhenHui
     * @Time:  2021/5/9 16:38
     * @param user
     * 请求用户
     * @param note
     * 接收到的note对象
     * @return
     */
    public int updateNote(String user,Note note){
        System.out.println(TAG+"执行更新");
        String tableName = "note_"+user;
        System.out.println("修改内容为："+note.toString());
        String sql = "UPDATE "+tableName+" SET title=?,data=?,image=?,time=?,modify=? WHERE id=?";
        int updateCount = jdbcTemplate.update(sql, note.getTitle(), note.getData(), note.getImage(), note.getTime(), note.getModify(), note.getId());
        if (updateCount>0){
            System.out.println("执行同步更新便签成功,更新数量："+updateCount);
            return (int)note.getId();
        }else {
            System.out.println("数据库内不存在对应ID的便签记录，无法更新");
            return 0;
        }
    }

    /**
     * 删除便签
     * @Author:  XuZhenHui
     * @Time:  2021/5/9 16:41
     * @param user
     * 请求用户
     * @param noteID
     * 便签ID
     * @return
     */
    public int deleteNote(String user,int noteID){
        String tableName = "note_"+user;
        System.out.println("删除便签ID："+noteID);
        //DELETE FROM table_name [WHERE Clause]
        String sql = "DELETE  FROM "+tableName+" WHERE id=?";
        int updateCount = jdbcTemplate.update(sql,noteID);
        if (updateCount>0){
            System.out.println("执行同步删除便签成功,删除数量："+updateCount);
            return updateCount;
        }else {
            System.out.println("数据库内不存在对应ID的便签记录，无法删除");
            return 0;
        }
    }

    /**
     * 根据时间戳获取所有同步待办
     * @Author:  XuZhenHui
     * @Time:  2021/5/9 16:42
     * @param user
     * 用户
     * @param modify
     * 客户端请求的时间戳
     * @return
     * 返回Note对象列表
     */
    public List<Note> getAllNotSyncNote(String user, int modify){
        String tableName = "note_"+user;
        String sql = "SELECT * FROM "+tableName+" WHERE MODIFY>?";
        return  jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Note.class), modify);
    }

    /**
     * 选择服务器上指定用户便签的最大时间戳
     * @Author:  XuZhenHui
     * @Time:  2021/4/24 13:43
     * @param user
     * 用户名
     * @return
     * 返回最大时间戳
     */
    public int getMaxModifyInNote(String user){
        String tableName = "note_"+user;
        String sql = "SELECT MAX(modify) FROM "+tableName;
        Integer integer = jdbcTemplate.queryForObject(sql, null, Integer.class);
        if (integer!=null){
            return integer;
        }else {
            return 0;
        }
    }

}
