package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseContent {
    private int id;
    private int course_id;
    private String title;
    private String content;
    private String video_url;

    public CourseContent() {
    }

    public CourseContent(int id, int course_id, String title, String content, String video_url) {
        this.id = id;
        this.course_id = course_id;
        this.title = title;
        this.content = content;
        this.video_url = video_url;
    }

    public int getId() {
        return id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }


    public static boolean add (CourseContent content) {

        String query = "INSERT INTO course_content (course_id, course_content_title, content_details, content_link) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, content.getCourse_id());
            pr.setString(2, content.getTitle());
            pr.setString(3, content.getContent());
            pr.setString(4, content.getVideo_url());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean delete(int id) {
        String query = "DELETE FROM course_content WHERE course_content_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<CourseContent> getListByCourse(int courseId) {
        ArrayList<CourseContent> contentList = new ArrayList<>();
        String query = "SELECT * FROM course_content WHERE course_id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, courseId);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                CourseContent content = new CourseContent(
                        rs.getInt("course_content_id"),
                        rs.getInt("course_id"),
                        rs.getString("course_content_title"),
                        rs.getString("content_details"),
                        rs.getString("content_link")
                );
                contentList.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }

}
