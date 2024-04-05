package com.patikadev.Model;

import com.mysql.cj.protocol.Resultset;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String username;
    private String password;
    private String type;

    public User() {

    }

    public User(int id, String name, String username, String password, String type) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ArrayList<User> getList () {
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";
        User obj;

        try {
            Statement statement = DBConnector.getInstance().createStatement();
            ResultSet result = statement.executeQuery(query);


            while (result.next()){
                obj = new User();
                obj.setId(result.getInt("id"));
                obj.setName(result.getString("name"));
                obj.setUsername(result.getString("username"));
                obj.setPassword(result.getString("password"));
                obj.setType(result.getString("type"));
                userList.add(obj);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;

    }


    public static boolean add (String name, String username, String password, String type) {

        String query = "INSERT INTO user (name,username,password,type) VALUES(? , ? , ? , ?)";
        User findUser = getByUserName(username);
        if (findUser != null) {
            Helper.showMessage("Bu kullanıcı adı zaten alınmış.");
            return false;
        }

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,username);
            pr.setString(3,password);
            pr.setString(4,type);

            int response =pr.executeUpdate();

            if (response==-1) {
                Helper.showMessage("error");
            }
            return response != -1;

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return true;
    }

    //Fetches the user from the database according to the given id.
    public static User getByUserName ( String username) {

        User obj =null ;
        String query = "SELECT * FROM user WHERE username = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,username);
            ResultSet rs = pr.executeQuery();
           if (rs.next()) {
               obj = new User ();
               obj.setId(rs.getInt("id"));
               obj.setName(rs.getString("name"));
               obj.setUsername(rs.getString("username"));
               obj.setPassword(rs.getString("password"));
               obj.setType(rs.getString("type"));

           }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;

    }

    public static User getByID ( int id) {

        User obj =null ;
        String query = "SELECT * FROM user WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new User ();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUsername(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
                obj.setType(rs.getString("type"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;

    }

    public static boolean delete (int id) {
        String query = "DELETE FROM user WHERE id = ?";
        ArrayList<Course> courseArrayList = Course.getListByUser(id);
        for (Course course : courseArrayList) {
            Course.delete(course.getId());
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    public static boolean update (int id, String name, String username, String password, String type) {
        String query = "UPDATE user SET name = ? , username = ? , password = ? , type = ? WHERE id = ?";
        User findUser = getByUserName(username);
        if (findUser != null && findUser.getId() != id) {
            Helper.showMessage("Bu kullanıcı adı zaten alınmış.");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,username);
            pr.setString(3,password);
            pr.setString(4,type);
            pr.setInt(5,id);
            return pr.executeUpdate() != -1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static User getCredentials (String username, String password) {

        User obj =null ;
        String query = "SELECT * FROM user WHERE username = ? AND password = ? ";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);

            pr.setString(1,username);
            pr.setString(2,password);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {

                switch (rs.getString("type")) {
                    case "operator":
                        obj = new Operator();
                        break;
                    case "educator":
                        obj = new Educator();
                        break;
                    case "student":
                        obj = new Student();
                        break;
                    default:
                        obj = new User();
                }

                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUsername(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
                obj.setType(rs.getString("type"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;

    }

    public static ArrayList<User> searchUserList (String query) {
        ArrayList<User> userList = new ArrayList<>();
        User obj;

        try {
            Statement statement = DBConnector.getInstance().createStatement();
            ResultSet result = statement.executeQuery(query);


            while (result.next()){
                obj = new User();
                obj.setId(result.getInt("id"));
                obj.setName(result.getString("name"));
                obj.setUsername(result.getString("username"));
                obj.setPassword(result.getString("password"));
                obj.setType(result.getString("type"));
                userList.add(obj);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;

    }

    public static String searchQuery (String name, String username, String type) {
        String query = "SELECT * FROM user WHERE username LIKE '%{{username}}%' AND " +
                "name LIKE '%{{name}}%'";
        query = query.replace("{{username}}",username);
        query = query.replace("{{name}}",name);

        if(!type.isEmpty()) {
            query += "AND type='{{type}}'";
            query = query.replace("{{type}}",type);

        }

        return query;
    }

}