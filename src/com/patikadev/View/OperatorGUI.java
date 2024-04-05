package com.patikadev.View;

import com.patikadev.Helper.*;
import com.patikadev.Model.Course;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Patika;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {

    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JTextField fld_name;
    private JTextField fld_user_name;
    private JTextField fld_password;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JLabel lbl_id_number;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_search_name;
    private JTextField fld_search_username;
    private JComboBox cmb_search_type;
    private JButton btn_search;
    private JLabel lbl_search_name;
    private JLabel lbl_search_username;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_user_top;
    private JPanel pnl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_language;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private JButton btn_course_add;
    private JButton btn_educator_gui;

    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;

    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;

    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;


    private final Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;
        add(wrapper);
        setSize(1000, 500);
        int x = Helper.screenCenter("x", getSize());
        int y = Helper.screenCenter("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        lbl_welcome.setText("Hoşgeldiniz " + operator.getName());

        //ModelUserList
        // Veri Tabanından Tablo Alma ve Manuel Tablo Oluşturma
        mdl_user_list = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }

        };

        Object[] col_user_list = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Türü"};
        mdl_user_list.setColumnIdentifiers(col_user_list);
        row_user_list = new Object[col_user_list.length];
        loadUserModel();


        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {

            try {
                String select_user_id = tbl_user_list.getValueAt
                        (tbl_user_list.getSelectedRow(), 0).toString();
                fld_user_id.setText(select_user_id);
            } catch (Exception exception) {

            }
        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt
                        (tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String password = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String type = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();

                try {
                    if (User.update(user_id, name, user_name, password, type)) {
                        Helper.showMessage("done");
                    }
                } catch (Exception exception) {
                    System.out.println("Bir hata oluştu.");
                }

                loadUserModel();
                loadEducatorCombo();
                loadCourseList();

            }
        });
        // ## UserList

        // PatikaList
        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Düzenle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int selected_row = Integer.parseInt(tbl_patika_list.
                    getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updatePatikaGUI = new UpdatePatikaGUI(Patika.getFetchPatika(selected_row));
            updatePatikaGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseList();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selected_row = Integer.parseInt(tbl_patika_list.
                        getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                if (Patika.delete(selected_row)) {
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseList();
                } else {
                    Helper.showMessage("error");
                }
            }

        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];

        loadPatikaModel();
        loadPatikaCombo();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);

        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_patika_list.addMouseListener
                (new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Point point = e.getPoint();
                        int selected_row = tbl_patika_list.rowAtPoint(point);
                        tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);

                    }
                });


        // ## PatikaList

        // CourseList

        mdl_course_list = new DefaultTableModel();

        Object[] col_courseList = {"ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];

        loadCourseList();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);

        loadPatikaCombo();
        loadEducatorCombo();

        // ## CourseList

        btn_user_add.addActionListener(e ->

        {
            if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_password) ||
                    Helper.isFieldEmpty(fld_name)) {
                Helper.showMessage("fill");
            } else {
                String name = fld_name.getText();
                String username = fld_user_name.getText();
                String password = fld_password.getText();
                String type = cmb_user_type.getSelectedItem().toString();

                if (User.add(name, username, password, type)) {
                    Helper.showMessage("done");
                    loadUserModel();
                    loadEducatorCombo();
                    fld_user_name.setText(null); // Textfield'ları temizleme
                    fld_name.setText(null); // fld_name.setText(""); ile aynı işlemi yapar.
                    fld_password.setText(null);

                }

            }

        });
        btn_user_delete.addActionListener(e ->

        {
            if (Helper.isFieldEmpty(fld_user_id)) {
                Helper.showMessage("fill");

            } else {
                int user_id = Integer.parseInt(fld_user_id.getText());
                if (User.delete(user_id)) {
                    Helper.showMessage("done");
                    loadUserModel();
                    loadEducatorCombo();
                    loadCourseList();
                    fld_user_id.setText(null);

                } else {
                    Helper.showMessage("error");
                }

            }

        });
        btn_search.addActionListener(e ->

        {
            String name = fld_search_name.getText();
            String username = fld_search_username.getText();
            String type = cmb_search_type.getSelectedItem().toString();
            String query = User.searchQuery(name, username, type);

            // ArrayList<User> searchUserArrayList = User.searchUserList(query);
            //loadUserModel(searchUserArrayList);
            loadUserModel(User.searchUserList(query));


        });

        btn_logout.addActionListener(e ->

        {
            dispose();
            LoginGUI loginGUI = new LoginGUI();

        });
        btn_patika_add.addActionListener(e ->

        {
            if (Helper.isFieldEmpty(fld_patika_name)) {
                Helper.showMessage("fill");
            } else {
                if (Patika.add(fld_patika_name.getText())) {
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patika_name.setText(null);

                } else {
                    Helper.showMessage("error");
                }
            }

        });

        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_user.getSelectedItem();
            if (Helper.isFieldEmpty(fld_course_name) ||
                    Helper.isFieldEmpty(fld_course_language)) {
                Helper.showMessage("fill");

            } else {
                if (Course.add(userItem.getKey(), patikaItem.getKey(),
                        fld_course_name.getText(), fld_course_language.getText())) {
                    Helper.showMessage("done");
                    loadCourseList();
                   // fld_course_name.setText(null);
                   // fld_course_language.setText(null);

                } else {
                    Helper.showMessage("error");
                }

            }

        });
        btn_educator_gui.addActionListener(e -> {
            User user = User.getByID(operator.getId());

            EducatorGUI educatorGUI = new EducatorGUI(user);
            educatorGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadEducatorCombo();
                    loadCourseList();
                }
            });
        });


    }

    private void loadCourseList() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Course obj : Course.getList()) {
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLanguage();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();
            mdl_course_list.addRow(row_course_list);

        }

    }
    // Veri Tabanındaki Güncel Tabloyu Ekrana Yansıtma İşlemi

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i = 0;
        for (Patika obj : Patika.getList()) {
            i = 0;
            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }


    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : User.getList()) {
            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }

    }

    public void loadUserModel(ArrayList<User> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : list) {
            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getType();
            mdl_user_list.addRow(row_user_list);
        }

    }

    public void loadPatikaCombo() {
        cmb_course_patika.removeAllItems();
        for (Patika obj : Patika.getList()) {
            cmb_course_patika.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void loadEducatorCombo() {
        cmb_course_user.removeAllItems();
        for (User obj: User.getList()) {
            if(obj.getType().equals("educator")) {
                cmb_course_user.addItem(new Item(obj.getId(), obj.getName()) );

            }
        }

    }


    public static void main(String[] args) {
        Helper.setLayout();
        Operator operator = new Operator();
        operator.setId(1);
        operator.setName("name");
        operator.setUsername("username");
        operator.setPassword("password");
        operator.setType("type");
        OperatorGUI opGUI = new OperatorGUI(operator);

    }
}
