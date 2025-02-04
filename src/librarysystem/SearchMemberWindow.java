package librarysystem;

import business.ControllerInterface;
import business.SystemController;
import business.LibraryMember;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchMemberWindow extends JFrame implements LibWindow {
    public static final SearchMemberWindow INSTANCE = new SearchMemberWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;

    private JTextField memberIdField;
    private JButton searchButton;
    private JTable memberTable;
    private JScrollPane tableScrollPane;
    private JPanel lowerPanel;

    private SearchMemberWindow() {}

    public void init() {
        if (isInitialized) return;

        setTitle("Search Member");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdField = new JTextField(20);
        memberIdField.setPreferredSize(new Dimension(250, 30));
        searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });

        searchPanel.add(memberIdLabel);
        searchPanel.add(memberIdField);
        searchPanel.add(searchButton);

        String[] columnNames = {"Member ID", "Name", "Phone"};
        Object[][] data = {};
        memberTable = new JTable(data, columnNames);
        memberTable.setEnabled(false);
        tableScrollPane = new JScrollPane(memberTable);
        tableScrollPane.setVisible(false);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        defineLowerPanel();
        panel.add(lowerPanel, BorderLayout.SOUTH);

        add(panel);

        isInitialized = true;
    }

    private void handleSearch() {
        String memberId = memberIdField.getText().trim();
        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
//
//        LibraryMember member = ci.searchMember(memberId);
//        if (member == null) {
//            JOptionPane.showMessageDialog(this, "Member not found", "Error", JOptionPane.ERROR_MESSAGE);
//            tableScrollPane.setVisible(false);
//        } else {
//            Object[][] rowData = {{member.getMemberId(), member.getFullName(), member.getTelephone()}};
//            String[] columnNames = {"Member ID", "Name", "Phone"};
//            memberTable.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames));
//            tableScrollPane.setVisible(true);
//        }

        revalidate();
        repaint();
    }

    public void defineLowerPanel() {
        JButton backToMainButn = new JButton("<= Back to Main");
        backToMainButn.addActionListener(new BackToMainListener());
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lowerPanel.add(backToMainButn);
    }

    class BackToMainListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
            SearchMemberWindow.INSTANCE.dispose(); // Dispose the current window
        }
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        isInitialized = val;
    }
}