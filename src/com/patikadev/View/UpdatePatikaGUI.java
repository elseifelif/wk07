package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;

import javax.swing.*;
import com.patikadev.Model.Patika;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdatePatikaGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_patika_name;
    private JButton btn_update_patika_name;
    private Patika patika;


    public UpdatePatikaGUI (Patika patika) {
        this.patika = patika;
        add (wrapper);
        setSize(300,150);
        setLocation(Helper.screenCenter("x",getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        fld_patika_name.setText(patika.getName());

        btn_update_patika_name.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_name)) {
                Helper.showMessage("fill");
            } else {
                if (Patika.update(patika.getId(),fld_patika_name.getText())) {
                    Helper.showMessage("done");
                }
                dispose();
            }

        });
    }

    public static void main(String[] args) {
        Helper.setLayout();

        Patika patika = new Patika(1,"Frontend Patikası");
        UpdatePatikaGUI updatePatikaGUI = new UpdatePatikaGUI(patika);
    }


}
