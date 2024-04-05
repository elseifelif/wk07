package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wrapper_top;
    private JPanel wrapper_bottom;
    private JTextField fld_username;
    private JTextField fld_password;
    private JButton btn_login;
    private JLabel lbl_username;
    private JLabel lbl_password;

    public LoginGUI() {
        add(wrapper);
        setSize(400,400);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_password)) {

                Helper.showMessage("fill");
            } else {
                User u = User.getCredentials(fld_username.getText(), fld_password.getText());
                if (u == null) {
                    Helper.showMessage("Kullanıcı adı veya şifre hatalı!");
                } else {
                    switch (u.getType()) {
                        case "operator":
                            OperatorGUI operatorGUI = new OperatorGUI((Operator) u);
                            break;
                        case "educator":
                            EducatorGUI educatorGUI = new EducatorGUI( (Educator) u);
                            break;
                        case "student":
                            StudentGUI studentGUI = new StudentGUI ((Student) u);
                            break;
                        default:
                            break;
                    }
                    dispose();
                }
            }

        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI loginGUI = new LoginGUI();
    }

}
