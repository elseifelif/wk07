package com.patikadev.Model;

import com.mysql.cj.protocol.Resultset;
import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Patika {
    private int id;
    private String name;

    public Patika(int id, String name) {
        this.id = id;
        this.name = name;
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

    public static ArrayList<Patika> getList() {
        ArrayList<Patika> patikaList = new ArrayList<>();
        Patika obj;
        try {
            Statement statement = DBConnector.getInstance().createStatement();
            ResultSet resultset = statement.executeQuery("SELECT * FROM patika");

            while (resultset.next()) {
                obj = new Patika(resultset.getInt("id"),
                        resultset.getString("patika_name"));
                patikaList.add(obj);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patikaList;

    }

    public static boolean add(String name) {
        String query = "INSERT INTO patika (patika_name) VALUES (?)";
        try {
            PreparedStatement statement = DBConnector.getInstance().prepareStatement(query);
            statement.setString(1, name);
            return statement.executeUpdate() != -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean update (int id, String name) {
        String query = "UPDATE patika SET patika_name = ? WHERE id = ?";
        try {
            PreparedStatement statement = DBConnector.getInstance().prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, id);
            return statement.executeUpdate() != -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static Patika getFetchPatika(int id) {
        Patika obj = null;
        String query = "SELECT * FROM patika WHERE id = ?";
        try {
            PreparedStatement statement = DBConnector.getInstance().prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultset = statement.executeQuery();

            if (resultset.next()) {
                obj = new Patika(resultset.getInt("id"),
                        resultset.getString("patika_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;

    }

    public static boolean delete (int id) {
        String query = "DELETE FROM patika WHERE id = ?";
        ArrayList<Course> courseArrayList = Course.getList();
        for (Course course : courseArrayList) {
            if (course.getPatika().getId()==id) {
                Course.delete(course.getId());
            }
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
}
