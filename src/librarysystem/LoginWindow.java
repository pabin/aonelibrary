package librarysystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import business.Book;
import business.ControllerInterface;

import business.LoginException;
import business.SystemController;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;


public class LoginWindow extends JFrame implements LibWindow {
    public static final LoginWindow INSTANCE = new LoginWindow();
    ControllerInterface ci = new SystemController();

    private boolean isInitialized = false;

    private JPanel mainPanel;
    private JPanel upperHalf;
    private JPanel middleHalf;
    private JPanel lowerHalf;
    private JPanel container;

    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel lowerPanel;
    private JPanel leftTextPanel;
    private JPanel rightTextPanel;

    private JTextField username;
    private JTextField password;
    private JLabel label;
    private JButton loginButton;
    private JButton logoutButton;


    public boolean isInitialized() {
        return isInitialized;
    }

    public void isInitialized(boolean val) {
        isInitialized = val;
    }

    private JTextField messageBar = new JTextField();

    public void clear() {
        messageBar.setText("");
    }

    /* This class is a singleton */
    private LoginWindow() {
    }



    public void init() {
        mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding (top, left, bottom, right)

        defineUpperHalf();
        defineMiddleHalf();
        defineLowerHalf();

        BorderLayout bl = new BorderLayout();
        bl.setVgap(30);
        mainPanel.setLayout(bl);

        mainPanel.add(upperHalf, BorderLayout.NORTH);
        mainPanel.add(middleHalf, BorderLayout.CENTER);
        mainPanel.add(lowerHalf, BorderLayout.SOUTH);

        JLabel label = new JLabel("user/pass: (101, xyz), (102, abc), (103, 111)");
        mainPanel.add(label);

        getContentPane().add(mainPanel);
        isInitialized(true);
        pack();
    }


    private void defineUpperHalf() {

        upperHalf = new JPanel();
        upperHalf.setLayout(new BorderLayout());
        defineTopPanel();
        defineMiddlePanel();
        defineLowerPanel();
        upperHalf.add(topPanel, BorderLayout.NORTH);
        upperHalf.add(middlePanel, BorderLayout.CENTER);
        upperHalf.add(lowerPanel, BorderLayout.SOUTH);

    }

    private void defineMiddleHalf() {
        middleHalf = new JPanel();
        middleHalf.setLayout(new BorderLayout());
        JSeparator s = new JSeparator();
        s.setOrientation(SwingConstants.HORIZONTAL);
        //middleHalf.add(Box.createRigidArea(new Dimension(0,50)));
        middleHalf.add(s, BorderLayout.SOUTH);

    }

    private void defineLowerHalf() {

        lowerHalf = new JPanel();
        lowerHalf.setLayout(new FlowLayout(FlowLayout.LEFT));

//        JButton backButton = new JButton("<= Back to Main");
//        addBackButtonListener(backButton);
//        lowerHalf.add(backButton);

    }



    private void defineTopPanel() {
        topPanel = new JPanel();
        JPanel intPanel = new JPanel(new BorderLayout());
        intPanel.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.NORTH);
        JLabel loginLabel = new JLabel("Login");
        Util.adjustLabelFont(loginLabel, Color.BLUE.darker(), true);
        intPanel.add(loginLabel, BorderLayout.CENTER);
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(intPanel);

        



    }


    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        defineLeftTextPanel();
        defineRightTextPanel();
        middlePanel.add(leftTextPanel);
        middlePanel.add(rightTextPanel);
    }

    private void defineLowerPanel() {
        lowerPanel = new JPanel();
        loginButton = new JButton("Login");
        addLoginButtonListener(loginButton);
        loginButton.setPreferredSize(new Dimension(120, 36));
        lowerPanel.add(loginButton);
    }

    private void defineLeftTextPanel() {

        JPanel topText = new JPanel();
        JPanel bottomText = new JPanel();
        topText.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        bottomText.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        username = new JTextField(10);
        label = new JLabel("Username");
        label.setFont(Util.makeSmallFont(label.getFont()));
        topText.add(username);
        bottomText.add(label);

        leftTextPanel = new JPanel();
        leftTextPanel.setLayout(new BorderLayout());
        leftTextPanel.add(topText, BorderLayout.NORTH);
        leftTextPanel.add(bottomText, BorderLayout.CENTER);
    }

    private void defineRightTextPanel() {

        JPanel topText = new JPanel();
        JPanel bottomText = new JPanel();
        topText.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        bottomText.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        password = new JPasswordField(10);
        label = new JLabel("Password");
        label.setFont(Util.makeSmallFont(label.getFont()));
        topText.add(password);
        bottomText.add(label);

        rightTextPanel = new JPanel();
        rightTextPanel.setLayout(new BorderLayout());
        rightTextPanel.add(topText, BorderLayout.NORTH);
        rightTextPanel.add(bottomText, BorderLayout.CENTER);
    }

    private void addBackButtonListener(JButton butn) {
        butn.addActionListener(evt -> {
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
        });
    }

    private void addLoginButtonListener(JButton butn) {
        butn.addActionListener(evt -> {

            String user = username.getText();
            String pass = password.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "username and password is required !!");
                return;
            }

            try {
                ci.login(user, pass);
            } catch (LoginException e) {
                // throw new RuntimeException(e);
                JOptionPane.showMessageDialog(this, "invalid credentials !!");
            }


            if (SystemController.currentAuth != null) {
                System.out.println("currentAuth: " + SystemController.currentAuth);
                username.setText("");
                password.setText("");

                LibrarySystem.hideAllWindows();
                LibrarySystem.INSTANCE.init();
                Main.centerFrameOnDesktop(LibrarySystem.INSTANCE);
                LibrarySystem.INSTANCE.setVisible(true);
                LoginWindow.INSTANCE.dispose();
            }
        });
    }


}
