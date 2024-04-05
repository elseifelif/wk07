package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Student;

import javax.swing.*;

public class StudentGUI extends JFrame{
    private JPanel wrapper;
    private JLabel lbl_student_page;

    public StudentGUI(Student student) {
        add(wrapper);
        setSize(400,400);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
    }
}
