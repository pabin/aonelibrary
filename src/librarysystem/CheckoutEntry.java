package librarysystem;

import business.Book;
import business.BookCopy;
import business.LibraryMember;
import business.SystemController;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.TestData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class CheckoutEntry {
    private String id;
    private String issuedDate;
    private String dueDate;
    private BookCopy bookCopy;
  //  private boolean isAvailable;


    public CheckoutEntry() {}
    public CheckoutEntry(String issuedDate, String dueDate, BookCopy bookCopy) {
       this.issuedDate = issuedDate;
       this.dueDate = dueDate;
       this.bookCopy = bookCopy;
    }

    public static boolean checkLibraryMemberIdExist(Set<String> memberIds, String memberId) {
        for (String m : memberIds) return m.equals(memberId);
        return false;
    }

    public static boolean checkBookExist(Map<String, Book> mapBook, String isbn) {
        for(Map.Entry<String, Book> m: mapBook.entrySet()) {
            return m.getKey().equals(isbn) && m.getValue().isAvailable();
        }
        return false;
    }

    public static void main(String[] args) {

        CheckoutEntry checkoutEntry = new CheckoutEntry();
        checkoutEntry.checkout();

    }


    public void checkout() {
//        TestData testData = new TestData();
//        testData.libraryMemberData();;
//
//        System.out.println(Main.allHavingOverdueBook());
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

        JLabel label = new JLabel("Select due date");
        label.setBounds(50, 300, 300, 30);
        frame.add(comboBox);
        frame.add(label);


        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(500, 350, 150, 30);
        frame.add(checkoutButton);

        DataAccessFacade dataAccessFacade = new DataAccessFacade();
        Map<String, LibraryMember> libraryMemberList = dataAccessFacade.readMemberMap();
        Map<String, Book> listBookMap = dataAccessFacade.readBooksMap();
        System.out.println(listBookMap);

        submitButton.addActionListener( e->{
            String memberId = memberIField.getText();

            //);System.out.println(checkLibraryMemberIdExist(libraryMemberList.keySet(), memberId)
            String isbn = usbnField.getText();
            LibraryMember member2 = libraryMemberList.get(isbn);
           // System.out.println("Member 2" + member2);

            if(!libraryMemberList.containsKey(memberId)
                    || !listBookMap.containsKey(isbn)
                    || memberId.equals("")
                    || isbn.equals("")) {

                errorMessage.setText("Either Member ID or Book ID are invalid");
                errorMessage.setForeground(Color.RED);
            }
            else {
                errorMessage.setText("");
            }
        });
        CheckoutEntry checkoutEntry = new CheckoutEntry();
        comboBox.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            label.setText(selected);


            if(selected.equals("7 days"))
                checkoutEntry.setIssuedDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            else {
                checkoutEntry.setDueDate(LocalDate.now().plusDays(21).format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
               // System.out.println(dueDate);
            }
        });


        checkoutButton.addActionListener(e-> {

            SystemController si = new SystemController();
            LibraryMember libMember = libraryMemberList.get(memberIField.getText());

            List<CheckoutEntry> list = new ArrayList<>();
            checkoutEntry.setBookCopy(null);
            checkoutEntry.setId("8838");
            list.add(checkoutEntry);
            DataAccessFacade da = new DataAccessFacade();
              libMember.setCheckoutEntries(list);
            libraryMemberList.put(memberIField.getText(), libMember);
            List<LibraryMember> members = libraryMemberList.values().stream().toList();
            System.out.println(libraryMemberList.get(memberIField.getText()));
          //  DataAccessFacade.loadMemberMap(members);
//             si.updateMember(libMember);
//             System.out.println("Current Member: " +libMember);
            // m.setCheckoutEntries(m.getCheckoutEntries().add(checkoutEntry));

           // si.updateMember();

//            String[] columns = {"Member ID", "ISBN", "Available", "Start Date", "Due Date"};
//
//            // Table Data
//            Object[][] data = {
//                    {"173-773", 1007, "No", "17/09/2025",  "24/09/2025"},
//
//            };
//
//            // Create JTable
//            JTable table = new JTable(data, columns);
//            JScrollPane scrollPane = new JScrollPane(table);
//            scrollPane.setBounds(30, 400, 700, 300);// Add Scrollbar
//            frame.add(scrollPane);

        });




        frame.setVisible(true);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    @Override
    public String toString() {
        return "Id: "+id + "issued date: " + issuedDate + ", due date: " + dueDate;
    }
}

