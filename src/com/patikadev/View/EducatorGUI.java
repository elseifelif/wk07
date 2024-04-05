package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EducatorGUI extends JFrame{
    private JPanel wrapper;
    private JLabel lbl_instructor_page;
    private JTabbedPane tabbedpane_educator;

    private JTextField fld_educator_id;
    private JTextField fld_educator_name;
    private JTextField fld_content_title;
    private JTextField fld_content_link;
    private JTextField fld_content_details;
    private JTextField fld_educator_course_id;
    private JTable tbl_educator_course_list;
    private JTable tbl_course_content;
    private JTable tbl_questions;
    private JButton btn_delete_content;
    private JButton btn_add_content;
    private JTextField fld_quiz_question;
    private JTextField fld_course_content_id;
    private JButton btn_add_quiz;
    private JTextField fld_quiz_id;
    private JButton btn_delete_quiz;

    private DefaultTableModel mdl_course_content;
    private Object[] row_course_content;

    private DefaultTableModel mdl_educator_course_list;
    private Object[] row_educator_course_list;

    private  DefaultTableModel mdl_questions;
    private Object[] row_questions;

    public EducatorGUI(User user) {
        add(wrapper);
        setSize(1200,800);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        mdl_educator_course_list = new DefaultTableModel();


        fld_educator_id.setText(String.valueOf(user.getId()));
        fld_educator_name.setText(user.getName());

        // CourseList

        //ModelUserList
        // Veri Tabanından Tablo Alma ve Manuel Tablo Oluşturma
        mdl_educator_course_list = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }

        };

        Object[] col_courseList = {"Kurs ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_educator_course_list.setColumnIdentifiers(col_courseList);
        row_educator_course_list = new Object[col_courseList.length];

        loadEducatorCourseList(user.getId());

        tbl_educator_course_list.setModel(mdl_educator_course_list);
        tbl_educator_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_educator_course_list.getTableHeader().setReorderingAllowed(false);
        tbl_educator_course_list.getTableHeader().setResizingAllowed(false);

        mdl_questions = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }

        };

        // ## CourseList

        mdl_course_content = new DefaultTableModel();
        Object[] col_course_content = {"İçerik ID", "Başlık", "İçerik", "Video URL", "Kurs ID"};
        mdl_course_content.setColumnIdentifiers(col_course_content);
        row_course_content = new Object[col_course_content.length];

        tbl_course_content.setModel(mdl_course_content);

       if(tbl_educator_course_list.getRowCount() > 0 && !fld_educator_course_id.getText().equals(""))
            loadCourseContent(Integer.parseInt(fld_educator_course_id.getText()));


       // Quiz List
        mdl_questions = new DefaultTableModel();
        Object[] col_quiz = {"Quiz ID", "Soru", "İçerik ID"};
        mdl_questions.setColumnIdentifiers(col_quiz);
        row_questions = new Object[col_quiz.length];

        tbl_questions.setModel(mdl_questions);
        tbl_questions.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_questions.getTableHeader().setReorderingAllowed(false);
        tbl_questions.getTableHeader().setResizingAllowed(false);
        tbl_questions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl_questions.setRowSelectionAllowed(true);






        // Seçili kurs ID numarasını almak için
        tbl_educator_course_list.getSelectionModel().addListSelectionListener(e -> {

            try {
                String select_course_id = tbl_educator_course_list.getValueAt
                        (tbl_educator_course_list.getSelectedRow(), 0).toString();
                fld_educator_course_id.setText(select_course_id);
                loadCourseContent(Integer.parseInt(select_course_id));
            } catch (Exception exception) {

            }
        });

        // Seçili quiz ID numarasını almak için
        tbl_questions.getSelectionModel().addListSelectionListener(e -> {

            try {
                String select_quiz_id = tbl_questions.getValueAt
                        (tbl_questions.getSelectedRow(), 0).toString();
                fld_quiz_id.setText(select_quiz_id);
                loadQuizzesForContent(Integer.parseInt(fld_course_content_id.getText()));
            } catch (Exception exception) {

            }
        });

        btn_add_content.addActionListener(e -> {
            int courseId = Integer.parseInt(fld_educator_course_id.getText());
            String title = fld_content_title.getText();
            String content = fld_content_details.getText();
            String videoUrl = fld_content_link.getText();

            CourseContent newContent = new CourseContent(0, courseId, title, content, videoUrl);
            if (CourseContent.add(newContent)) {
                JOptionPane.showMessageDialog(null, "İçerik başarıyla eklendi.");
                loadCourseContent(courseId);

            } else {
                JOptionPane.showMessageDialog(null, "İçerik eklenirken bir hata oluştu.");
            }

        });
        btn_delete_content.addActionListener(e -> {

            int selectedRow = tbl_course_content.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Lütfen silinecek içeriği seçin.");
                return;
            }

            int contentId = Integer.parseInt(tbl_course_content.getValueAt(selectedRow, 0).toString());
            if (CourseContent.delete(contentId)) {
                JOptionPane.showMessageDialog(null, "İçerik başarıyla silindi.");
                loadCourseContent(Integer.parseInt(fld_educator_course_id.getText()));
            } else {
                JOptionPane.showMessageDialog(null, "İçerik silinirken bir hata oluştu.");
            }

        });

        tbl_course_content.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tbl_course_content.getSelectedRow() != -1) {
                try {
                    String selectedContentId = tbl_course_content.getValueAt(tbl_course_content.getSelectedRow(), 0).toString();
                    fld_course_content_id.setText(selectedContentId);
                    if (!selectedContentId.equals("")) {
                        loadQuizzesForContent(Integer.parseInt(selectedContentId));
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });


        btn_add_quiz.addActionListener(e -> {

            int contentId = Integer.parseInt(fld_course_content_id.getText());
            String question = fld_quiz_question.getText();

            Quiz newQuiz = new Quiz(0, question, contentId);
            if (Quiz.add(newQuiz)) {
                JOptionPane.showMessageDialog(null, "Quiz sorusu başarıyla eklendi.");
                loadQuizzesForContent(contentId);
            } else {
                JOptionPane.showMessageDialog(null, "Quiz sorusu eklenirken bir hata oluştu.");
            }

        });
        btn_delete_quiz.addActionListener(e -> {

            try {
                int quizId = Integer.parseInt(fld_quiz_id.getText());
                if (Quiz.delete(quizId)) {
                    JOptionPane.showMessageDialog(null, "Quiz başarıyla silindi.");

                    int contentId = Integer.parseInt(fld_course_content_id.getText());
                    loadQuizzesForContent(contentId);
                } else {
                    JOptionPane.showMessageDialog(null, "Quiz silinirken bir hata oluştu.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Geçerli bir Quiz ID giriniz.");
            }

        });
    }

    private void loadQuizzesForContent(int contentId) {
        DefaultTableModel model = (DefaultTableModel) tbl_questions.getModel();
        model.setRowCount(0);

        for (Quiz quiz : Quiz.getListByContentId(contentId)) {
            Object[] row = new Object[] { quiz.getQuiz_id(), quiz.getQuestions() };
            row_questions[0] = quiz.getQuiz_id();
            row_questions[1] = quiz.getQuestions();
            row_questions[2] = quiz.getCourse_content_ID();
            mdl_questions.addRow(row_questions);

        }
    }



    private void loadCourseContent(int courseId) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_content.getModel();
        clearModel.setRowCount(0);

        // CourseContent.getListByCourse(courseId) metodunu kullanarak içerikleri alın
        for (CourseContent content : CourseContent.getListByCourse(courseId)) {
            row_course_content[0] = content.getId();
            row_course_content[1] = content.getTitle();
            row_course_content[2] = content.getContent();
            row_course_content[3] = content.getVideo_url();
            row_course_content[4] = content.getCourse_id();
            mdl_course_content.addRow(row_course_content);

        }

    }


    private void loadEducatorCourseList(int id) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_educator_course_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        if (User.getByID(id).getType().equals("operator")) {
            for (Course obj : Course.getList()) {
                i = 0;
                row_educator_course_list[i++] = obj.getId();
                row_educator_course_list[i++] = obj.getName();
                row_educator_course_list[i++] = obj.getLanguage();
                row_educator_course_list[i++] = obj.getPatika().getName();
                row_educator_course_list[i++] = obj.getEducator().getName();
                mdl_educator_course_list.addRow(row_educator_course_list);
             }
        } else {
                // Eğitmenin ID'sini kullanarak sadece onun kurslarını getir
                for (Course obj : Course.getListByUser(id)) {
                    i = 0;
                    row_educator_course_list[i++] = obj.getId();
                    row_educator_course_list[i++] = obj.getName();
                    row_educator_course_list[i++] = obj.getLanguage();
                    row_educator_course_list[i++] = obj.getPatika().getName();
                    row_educator_course_list[i++] = obj.getEducator().getName();
                    mdl_educator_course_list.addRow(row_educator_course_list);
                }
            }

    }

    public static void main(String[] args) {
        Helper.setLayout();
        User user = new User();
        user.setId(23);
        user.setName("ccc");
        user.setUsername("ccc");
        user.setPassword("ccc");
        user.setType("educator");
        EducatorGUI educatorGUI = new EducatorGUI(user);

    }

}
