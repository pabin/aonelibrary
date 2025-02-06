package librarysystem;

import business.Address;
import business.Author;
import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AddNewBookWindow extends JFrame implements LibWindow {
    private static final long serialVersionUID = 1L;
    public static final AddNewBookWindow INSTANCE = new AddNewBookWindow();
    private boolean isInitialized = false;

    private JTextField isbnField, titleField, copiesField;
    private JTextField afName, aSecName, aPhone, biographyField;
    private JTextField streetField, cityField, stateField, zipField;
    private JButton saveButton;
    private JPanel bottomPanel;
    private JComboBox<String> checkoutLengthComboBox;

    private AddNewBookWindow() {
    }

    public void init() {
        if (isInitialized) {
            refreshUI();
            return;
        }

        setTitle("Add New Book");
        setSize(400, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(createBookPanel());
        mainPanel.add(createAuthorPanel());
        mainPanel.add(createAddressPanel());

        saveButton = new JButton("Save Book");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddNewBook();
            }
        });
        saveButton.setPreferredSize(new Dimension(180, 48));

        JButton backToMainButn = new JButton("<= Back to Main");
        backToMainButn.addActionListener(new BackToMainListener());
        backToMainButn.setPreferredSize(new Dimension(180, 48));

        bottomPanel = new JPanel();
        bottomPanel.add(backToMainButn);
        bottomPanel.add(saveButton);

        mainPanel.add(bottomPanel);

        add(mainPanel, BorderLayout.CENTER);
        isInitialized = true;
    }

    private JPanel createBookPanel() {
        JPanel bookPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        bookPanel.setBorder(BorderFactory.createTitledBorder("Book Information"));

        bookPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        bookPanel.add(isbnField);

        bookPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        bookPanel.add(titleField);

        bookPanel.add(new JLabel("Max Checkout Length (in days):"));
        String[] checkoutOptions = {"7", "21"};
        checkoutLengthComboBox = new JComboBox<>(checkoutOptions);
        bookPanel.add(checkoutLengthComboBox);

        bookPanel.add(new JLabel("Number of Copies:"));
        copiesField = new JTextField();
        bookPanel.add(copiesField);

        return bookPanel;
    }

    private JPanel createAuthorPanel() {
        JPanel authorPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        authorPanel.setBorder(BorderFactory.createTitledBorder("Author Information"));

        authorPanel.add(new JLabel("First Name:"));
        afName = new JTextField();
        authorPanel.add(afName);

        authorPanel.add(new JLabel("Last Name:"));
        aSecName = new JTextField();
        authorPanel.add(aSecName);

        authorPanel.add(new JLabel("Phone Number:"));
        aPhone = new JTextField();
        authorPanel.add(aPhone);

        authorPanel.add(new JLabel("Biography:"));
        biographyField = new JTextField();
        authorPanel.add(biographyField);

        return authorPanel;
    }

    private JPanel createAddressPanel() {
        JPanel addressPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        addressPanel.setBorder(BorderFactory.createTitledBorder("Author's Address Information"));

        addressPanel.add(new JLabel("Street:"));
        streetField = new JTextField();
        addressPanel.add(streetField);

        addressPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        addressPanel.add(cityField);

        addressPanel.add(new JLabel("State:"));
        stateField = new JTextField();
        addressPanel.add(stateField);

        addressPanel.add(new JLabel("Zip:"));
        zipField = new JTextField();
        addressPanel.add(zipField);

        return addressPanel;
    }

    private void handleAddNewBook() {
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String checkoutLength = (String) checkoutLengthComboBox.getSelectedItem();
        String copies = copiesField.getText();

        String firstName = afName.getText();
        String lastName = aSecName.getText();
        String phone = aPhone.getText();
        String biography = biographyField.getText();

        String street = streetField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String zip = zipField.getText();

        DataAccess da = new DataAccessFacade();
        HashMap<String, Book> books = da.readBooksMap();
        List<Book> bookList = new ArrayList<>(books.values());

        if (books.get(isbn) != null) {
            JOptionPane.showMessageDialog(INSTANCE, "Book with given ISBN already exist !!");
            return;
        }

        if (!isbn.isEmpty() && !title.isEmpty() && !checkoutLength.isEmpty() && !copies.isEmpty() &&
                !firstName.isEmpty() && !lastName.isEmpty() && !phone.isEmpty() && !biography.isEmpty() &&
                !street.isEmpty() && !city.isEmpty() && !state.isEmpty() && !zip.isEmpty()) {

            Address address = new Address(street, city, state, zip);
            Author author = new Author(firstName, lastName, phone, address, biography);
            List<Author> authorList = new ArrayList<>();
            authorList.add(author);

            Book bookItem = new Book(isbn, title, Integer.parseInt(checkoutLength), authorList);

            for (int i = 1; i <= Integer.parseInt(copies); i++) {
                bookItem.addCopy();
            }
            bookList.add(bookItem);
            DataAccessFacade.loadBookMap(bookList);

            JOptionPane.showMessageDialog(INSTANCE, "Book added successfully");

            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
            AddNewBookWindow.INSTANCE.dispose();
        } else {
            JOptionPane.showMessageDialog(INSTANCE, "All fields are required!");
        }
    }

    static class BackToMainListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            LibrarySystem.hideAllWindows();
            LibrarySystem.INSTANCE.setVisible(true);
            AddNewBookWindow.INSTANCE.dispose();
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

    private void refreshUI() {
        isbnField.setText("");
        titleField.setText("");
        copiesField.setText("");
        afName.setText("");
        aSecName.setText("");
        aPhone.setText("");
        biographyField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
    }
}