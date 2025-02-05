package librarysystem;

import business.Address;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AddMemberWindow extends JFrame implements LibWindow {
    public static final AddMemberWindow INSTANCE = new AddMemberWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;

    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel lowerPanel;
    private JTextField fnameField;
    private JTextField lnameField;
    private JTextField telField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;

    private AddMemberWindow() {}

    public void init() {
        if (isInitialized) return; // Prevent re-initialization

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout());

        defineTopPanel();
        defineMiddlePanel();
        defineLowerPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(lowerPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center the window
        isInitialized = true;
    }

    private void defineTopPanel() {
        topPanel = new JPanel();
        JLabel addMemberLabel = new JLabel("Add Member");
        Util.adjustLabelFont(addMemberLabel, Util.DARK_BLUE, true);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(addMemberLabel);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(7, 2, 10, 10));

        fnameField = new JTextField(20);
        lnameField = new JTextField(20);
        telField = new JTextField(20);
        streetField = new JTextField(20);
        cityField = new JTextField(20);
        stateField = new JTextField(20);
        zipField = new JTextField(20);

        middlePanel.add(new JLabel("First Name:"));
        middlePanel.add(fnameField);
        middlePanel.add(new JLabel("Last Name:"));
        middlePanel.add(lnameField);
        middlePanel.add(new JLabel("Telephone:"));
        middlePanel.add(telField);
        middlePanel.add(new JLabel("Street:"));
        middlePanel.add(streetField);
        middlePanel.add(new JLabel("City:"));
        middlePanel.add(cityField);
        middlePanel.add(new JLabel("State:"));
        middlePanel.add(stateField);
        middlePanel.add(new JLabel("ZIP Code:"));
        middlePanel.add(zipField);
    }

    private void defineLowerPanel() {
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("<== Back to Main");

        submitButton.addActionListener(evt -> handleSubmit());
        backButton.addActionListener(evt -> {
            clearFields(); // Clear form fields before closing
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
        });


        lowerPanel.add(backButton);
        lowerPanel.add(submitButton);
    }

    private void handleSubmit() {
        String fname = fnameField.getText().trim();
        String lname = lnameField.getText().trim();
        String tel = telField.getText().trim();
        String street = streetField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String zip = zipField.getText().trim();

        if (fname.isEmpty() || lname.isEmpty() || tel.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(ci.checkIfMemberExists(tel)) {
            JOptionPane.showMessageDialog(this, "Library member with provided number already exists in the system!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Address add = new Address(street, city, state, zip);
        String id = generateID();

        LibraryMember member = new LibraryMember(id, fname, lname, tel, add);

        ci.addMember(member);

        JOptionPane.showMessageDialog(this, "Member added successfully! ID: " + id , "Success", JOptionPane.INFORMATION_MESSAGE);

        refreshAllMemberIdsWindow();
        clearFields();

        dispose();
    }


    public static String generateID() {
        Random random = new Random();
        // Generate a number with 4 digits
        // 1000 to 9999
        return String.valueOf(random.nextInt(9000) + 1000);
    }

    private void refreshAllMemberIdsWindow() {
        List<String> ids = ci.allMemberIds();
        Collections.sort(ids);
        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append(s).append("\n");
        }
        AllMemberIdsWindow.INSTANCE.init();
        AllMemberIdsWindow.INSTANCE.setData(sb.toString());
        AllMemberIdsWindow.INSTANCE.pack();
        Util.centerFrameOnDesktop(AllMemberIdsWindow.INSTANCE);
        AllMemberIdsWindow.INSTANCE.setVisible(true);
    }

    private void clearFields() {
        fnameField.setText("");
        lnameField.setText("");
        telField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        isInitialized = val;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddMemberWindow.INSTANCE.init();
            AddMemberWindow.INSTANCE.setVisible(true);
        });
    }
}
