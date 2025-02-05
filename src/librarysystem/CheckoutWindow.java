package librarysystem;

import business.*;
import dataaccess.DataAccessFacade;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class CheckoutWindow extends JFrame implements LibWindow {
    public static final CheckoutWindow INSTANCE = new CheckoutWindow();
    ControllerInterface ci = new SystemController();

  private boolean isInitialized = false;

    public CheckoutWindow() {}


    public static void main(String[] args) {
        CheckoutWindow checkoutWindow = new CheckoutWindow();
        checkoutWindow.checkout();
    }

    public void checkout() {
        JFrame frame = new JFrame("Checkout Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setLayout(null);

        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setBounds(50, 50, 100, 30);
        frame.add(memberIdLabel);

        JTextField memberIField = new JTextField();
        memberIField.setBounds(150, 50, 180, 30);
        frame.add(memberIField);

        JLabel usbnLabel = new JLabel("USBN:");
        usbnLabel.setBounds(400, 50, 100, 30);
        frame.add(usbnLabel);

        JTextField usbnField = new JTextField();
        usbnField.setBounds(450, 50, 180, 30);
        frame.add(usbnField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(550, 150, 80, 30);
        frame.add(submitButton);

        JLabel errorMessage = new JLabel();
        errorMessage.setBounds(30, 200, 300, 30);
        frame.add(errorMessage);

        String[] colors = {"Select Due Date...","7 days", "21 days"};
        JComboBox<String> comboBox = new JComboBox<>(colors);
        comboBox.setBounds(50, 300, 300, 30);
        frame.add(comboBox);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(500, 350, 150, 30);
        frame.add(checkoutButton);

        DataAccessFacade dataAccessFacade = new DataAccessFacade();
        Map<String, LibraryMember> libraryMemberList = dataAccessFacade.readMemberMap();
        Map<String, Book> listBookMap = dataAccessFacade.readBooksMap();

        submitButton.addActionListener(e -> {
            String memberId = memberIField.getText();
            String isbn = usbnField.getText();

            if (!libraryMemberList.containsKey(memberId) || !listBookMap.containsKey(isbn) || memberId.isEmpty() || isbn.isEmpty()) {
                errorMessage.setText("Either Member ID or Book ID are invalid");
                errorMessage.setForeground(Color.RED);
            } else {
                errorMessage.setText("");
            }
        });

        CheckoutEntry checkoutEntry = new CheckoutEntry();
        comboBox.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            if (selected.equals("7 days")) {
                checkoutEntry.setIssuedDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            } else if (selected.equals("21 days")) {
                checkoutEntry.setDueDate(LocalDate.now().plusDays(21).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            }
        });

        checkoutButton.addActionListener(e -> {
            String memberId = memberIField.getText();
            String isbn = usbnField.getText();

            LibraryMember libMember = libraryMemberList.get(memberId);
            Book book = listBookMap.get(isbn);

            if (libMember != null && book != null && book.isAvailable()) {
                BookCopy availableCopy = book.getNextAvailableCopy();
                if (availableCopy != null) {
                    availableCopy.changeAvailability(); // Mark book as checked out
                    book.updateCopies(availableCopy);
                    listBookMap.put(isbn, book);
                }

                List<CheckoutEntry> list = new ArrayList<>(Optional.ofNullable(libMember.getCheckoutEntries()).orElse(Collections.emptyList()));
                checkoutEntry.setBookCopy(availableCopy);
                checkoutEntry.setId(UUID.randomUUID().toString());
                list.add(checkoutEntry);
                libMember.setCheckoutEntries(list);

                libraryMemberList.put(memberId, libMember);
                DataAccessFacade.loadMemberMap(libraryMemberList.values().stream().toList());

                System.out.println(ci.getMember(memberId));

//                for ()

//                System.out.println(libMember.getCheckoutEntries());
//                dataAccessFacade.saveLibraryMembers(libraryMemberList);
//                dataAccessFacade.saveBooks(listBookMap);

                JOptionPane.showMessageDialog(frame, "Checkout successful!");
            } else {
                JOptionPane.showMessageDialog(frame, "Book is not available or Member ID is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    @Override
    public void init() {
        checkout();
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {

    }
}