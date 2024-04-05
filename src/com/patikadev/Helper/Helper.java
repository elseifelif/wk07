package com.patikadev.Helper;

import java.awt.*;
import javax.swing.*;

public class Helper {

    public static void setLayout() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());

                } catch (ClassNotFoundException  | InstantiationException |IllegalAccessException
                         |UnsupportedLookAndFeelException e ) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }
    public static int screenCenter(String axis, Dimension size) {

        int point=0 ;
        switch (axis) {
            case "x":
                point = (Toolkit.getDefaultToolkit().getScreenSize().width-size.width)/2;
                break;
            case "y":
                point = (Toolkit.getDefaultToolkit().getScreenSize().height-size.height)/2;
                break;
            default:
                point =0;
                break;

        }
        return point;
    }

    public static boolean isFieldEmpty (JTextField field) {

       // field.getText().trim().isEmpty();
        return field.getText().trim().isEmpty();

    }

    public static void showMessage (String text) {
        optionPaneTR();
        String message;
        String title = "Message";

        switch (text) {
            case "fill":
                message = "Lütfen tüm alanları doldurunuz.";
                title = "Hata!";
                break;

            case "done":
                message = "İşlem başarılı.";
                title = "Sonuç";
                break;

            case "error":
                message = "Bir hata oluştu";
                title = "Hata!";
                break;


            default:
                message = text;
                break;
        }
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void optionPaneTR () {
        UIManager.put("OptionPane.okButtonText","Tamam");

    }

    public static boolean confirm(String sure) {
        optionPaneTR();
        String[] buttons = {"Evet", "Hayır"};
        int result = JOptionPane.showOptionDialog(null, sure, "Uyarı",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
        return result == 0;
    }

}
