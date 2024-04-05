package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Quiz {

    int quiz_id;
    String questions;
    int course_content_ID;

    public Quiz() {
    }

    public Quiz(int quiz_id, String questions, int course_content_ID) {
        this.quiz_id = quiz_id;
        this.questions = questions;
        this.course_content_ID = course_content_ID;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public int getCourse_content_ID() {
        return course_content_ID;
    }

    public void setCourse_content_ID(int course_content_ID) {
        this.course_content_ID = course_content_ID;
    }

    public static ArrayList<Quiz> getListByContentId(int contentId) {
        ArrayList<Quiz> quizList = new ArrayList<>();
        String query = "SELECT * FROM quiz WHERE course_content_ID = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, contentId);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                Quiz quiz = new Quiz(
                        rs.getInt("quiz_id"),
                        rs.getString("questions"),
                        rs.getInt("course_content_ID")
                );
                quizList.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return quizList;
    }

    public static boolean add(Quiz quiz) {
        String query = "INSERT INTO quiz (questions, course_content_ID) VALUES (?, ?)";
        try {
            Connection conn = DBConnector.getInstance();
            PreparedStatement pr = conn.prepareStatement(query);
            pr.setString(1, quiz.getQuestions());
            pr.setInt(2, quiz.getCourse_content_ID());

            int result = pr.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int quizId) {
        String query = "DELETE FROM quiz WHERE quiz_id = ?";
        try {
            Connection conn = DBConnector.getInstance();
            PreparedStatement pr = conn.prepareStatement(query);
            pr.setInt(1, quizId);
            int result = pr.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
