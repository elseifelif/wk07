package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {
    private int id;
    private int patika_id;
    private int user_id;
    private String name;
    private String language;
   private Patika patika;
   private User educator;


    public Course(int id, int patika_id, int user_id, String name, String language) {
        this.id = id;
        this.patika_id = patika_id;
        this.user_id = user_id;
        this.name = name;
        this.language = language;
        this.patika = Patika.getFetchPatika(patika_id);
        this.educator = User.getByID(user_id);
    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatika_id() {
        return patika_id;
    }

    public void setPatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }

    public static ArrayList<Course> getList () {
        ArrayList <Course> courseArrayList = new ArrayList<>();
        Course course;

        try {
            Statement statement = DBConnector.getInstance().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM course");
            while (resultSet.next()) {
                course = new Course(
                        resultSet.getInt("id"),
                        resultSet.getInt("patika_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("language")

                );
                courseArrayList.add(course);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseArrayList;

    }

    public static boolean add(int user_id, int patika_id, String name, String language) {
        String query = "INSERT INTO course (user_id, patika_id, name, language) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2,patika_id);
            pr.setString(3, name);
            pr.setString(4, language);
            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static ArrayList<Course> getListByUser (int user_id) {
        ArrayList <Course> courseArrayList = new ArrayList<>();
        Course course;

        try {
            Statement statement = DBConnector.getInstance().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM course" +
                    " WHERE user_id = " + user_id);
            while (resultSet.next()) {
                course = new Course(
                        resultSet.getInt("id"),
                        resultSet.getInt("patika_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("language")

                );
                courseArrayList.add(course);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseArrayList;

    }

    public static boolean delete (int id) {
        String query = "DELETE FROM course WHERE id = ?";
      //  ArrayList<Course> courseArrayList = Course.getListByUser(id);
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }

    public static ArrayList<Course> searchCourseList (String query) {
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;

        try {
            Statement statement = DBConnector.getInstance().createStatement();
            ResultSet result = statement.executeQuery(query);


            while (result.next()){
                obj = new Course();
                obj.setId(result.getInt("id"));
                obj.setPatika_id(result.getInt("patika_id"));
                obj.setUser_id(result.getInt("user_id"));
                obj.setName(result.getString("name"));
                obj.setLanguage(result.getString("language"));
                courseList.add(obj);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;

    }

}
