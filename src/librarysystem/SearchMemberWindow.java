package librarysystem;

import business.CheckoutEntry;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchMemberWindow extends JFrame implements LibWindow {
    public static final SearchMemberWindow INSTANCE = new SearchMemberWindow();
    ControllerInterface ci = new SystemController();
    private boolean isInitialized = false;

    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel lowerPanel;
    private JTextField memberIdField;
    private JTable memberTable;
    private JScrollPane tableScrollPane;
    private JLabel memberInfoLabel;

    private SearchMemberWindow() {
    }

    public void init() {
        if (isInitialized) return;

        setSize(800, 600); // Set large default window size

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
        setLocationRelativeTo(null);
        isInitialized = true;
    }

    private void defineTopPanel() {
        topPanel = new JPanel();
        JLabel searchLabel = new JLabel("Search Member");
        Util.adjustLabelFont(searchLabel, Util.DARK_BLUE, true);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(searchLabel);
    }

    private void defineMiddlePanel() {
        middlePanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel memberIdLabel = new JLabel("Enter Member ID:");
        memberIdField = new JTextField(15);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(evt -> handleSearch());

        inputPanel.add(memberIdLabel);
        inputPanel.add(memberIdField);
        inputPanel.add(searchButton);

        memberInfoLabel = new JLabel("No member searched yet");
        memberInfoLabel.setFont(new Font("Arial", Font.BOLD, 11));

        String[] columnNames = {"EntryID", "ISBN", "Issued Date", "Issued Duration"};
        memberTable = new JTable(new DefaultTableModel(columnNames, 0));
        tableScrollPane = new JScrollPane(memberTable);

        middlePanel.add(inputPanel, BorderLayout.NORTH);
        middlePanel.add(memberInfoLabel, BorderLayout.CENTER);
        middlePanel.add(tableScrollPane, BorderLayout.SOUTH);
    }

    private void defineLowerPanel() {
        lowerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("<== Back to Main");

        backButton.addActionListener(evt -> {
            clearFieldsAndTable();
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
        });

        lowerPanel.add(backButton);
    }

    private void handleSearch() {
        String memberId = memberIdField.getText().trim();

        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<LibraryMember> member = ci.getMember(memberId);
        if (member.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<CheckoutEntry> entries = Optional.ofNullable(member.get().getCheckoutEntries()).orElse(new ArrayList<>());

        if (entries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member does not have checkout records!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LibraryMember memberObject = member.get();
        String[] columnNames = {"EntryID", "ISBN", "Issued Date", "Issued Duration"};

        Object[][] data = new Object[entries.size()][4];

        for (int i = 0; i < entries.size(); i++) {
            CheckoutEntry entry = entries.get(i);
            data[i][0] = entry.getId();
            data[i][1] = entry.getBookCopy().getBook().getIsbn();
            data[i][2] = entry.getIssuedDate();
            data[i][3] = entry.getIssuedDuration();
        }

        memberTable.setModel(new DefaultTableModel(data, columnNames));
        memberInfoLabel.setText("Member Found: " + memberObject.getFirstName() + " " + memberObject.getLastName() + " (ID: " + memberObject.getMemberId() + ")");
    }

    private void clearFieldsAndTable() {
        memberIdField.setText("");
        memberTable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"EntryID", "ISBN", "Issued Date", "Issued Duration"}));
        memberInfoLabel.setText("No member searched yet");
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
            SearchMemberWindow.INSTANCE.init();
            SearchMemberWindow.INSTANCE.setVisible(true);
        });
    }
}
