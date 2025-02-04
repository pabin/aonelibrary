package librarysystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import business.ControllerInterface;
import business.SystemController;
import dataaccess.Auth;

public class LibrarySystem extends JFrame implements LibWindow {
    ControllerInterface ci = new SystemController();
    public final static LibrarySystem INSTANCE = new LibrarySystem();
    JPanel mainPanel, buttonPanel;
    JLabel backgroundLabel;
    JMenuBar menuBar;
    JMenu options;
    JMenuItem logout, help, about;
    String pathToImage;
    private boolean isInitialized = false;

    private static LibWindow[] allWindows = {
            LibrarySystem.INSTANCE,
            LoginWindow.INSTANCE,
            AllMemberIdsWindow.INSTANCE,
            AllBookIdsWindow.INSTANCE
    };

    public static void hideAllWindows() {
        for (LibWindow frame : allWindows) {
            frame.setVisible(false);
        }
    }

    private LibrarySystem() {
    }

    public void init() {
        formatContentPane();
        setPathToImage();
        insertSplashImage();
        createMenus();
        createButtons();
        setSize(660, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        isInitialized = true;
    }

    private void formatContentPane() {
        mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);
    }

    private void setPathToImage() {
        String os = System.getProperty("os.name").toLowerCase();

        String currDirectory = System.getProperty("user.dir");
        pathToImage = currDirectory + (os.equals("win") ? "\\src\\librarysystem\\library.jpg" : "/src/librarysystem/library.jpg");
    }

    private void insertSplashImage() {
        ImageIcon image = new ImageIcon(pathToImage);
        backgroundLabel = new JLabel(image);
        backgroundLabel.setLayout(new BorderLayout());
        mainPanel.add(backgroundLabel, BorderLayout.CENTER);
    }

    private void createMenus() {
        menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createRaisedBevelBorder());
        addMenuItems();
        setJMenuBar(menuBar);
    }


    private void addMenuItems() {
        options = new JMenu("Options");
        menuBar.add(options);

        help = new JMenuItem("Help");
        help.addActionListener(new HelpListener());
        options.add(help);

        about = new JMenuItem("About");
        about.addActionListener(new AboutListener());
        options.add(about);

        logout = new JMenuItem("Logout");
        logout.addActionListener(new LogoutListener());
        options.add(logout);
    }

    static class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ControllerInterface ci = new SystemController();
            ci.logout();

            LibrarySystem.hideAllWindows();
            // LoginWindow.INSTANCE.init();
            Util.centerFrameOnDesktop(LoginWindow.INSTANCE);
            LoginWindow.INSTANCE.setVisible(true);
        }
    }

    static class HelpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(INSTANCE, "Library System Help:\n" +
                    "\n" +
                    "Welcome to the AONE Library System!\n" +
                    "\t•\tAdd Book: Enter book details like title, author, \n" + "   and ISBN to add a new book to the catalog.\n" +
                    "\t•\tAdd Book Copy: Add additional copies of an existing book using its ISBN.\n" +
                    "\t•\tCheckout: Select a book and borrower to check out a copy.\n" + "   Ensure the book is available.\n" +
                    "\t•\tAdd Member: Register new library member, check info on library member.\n" +
                    "\t•\tCheckout Records: View checkout details for a library member .\n" +
                    "\n" +
                    "Use the search feature to find books quickly. Manage inventory efficiently \n" + "and keep track of borrowed books with ease.");
        }
    }

    static class AboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(INSTANCE, "About Aone Library System:\n" +
                    "\n" +
                    "Aone Library System is a simple, efficient solution for managing\n" + "library operations and handling checkouts seamlessly. It’s\n" + "designed for ease of use and streamlined book management.\n" +
                    "\n" +
                    "Developed by:\n" +
                    "\t•\tAl Hassane Camara – Junior Java Intern\n" +
                    "\t•\tPabin Luitel – Senior Platform Engineer\n" +
                    "\t•\tSushil Karki – Senior Full-Stack Engineer\n" +
                    "\n" +
                    "Our team’s combined expertise ensures a robust, user-friendly library system \n" + "tailored for efficiency and reliability.");
        }
    }

    private void createButtons() {
        buttonPanel = new JPanel(new GridLayout(3, 2, 16, 16));
        buttonPanel.setOpaque(false); // Make panel transparent

        addButton("All Book IDs", new AllBookIdsListener());
        addButton("All Member IDs", new AllMemberIdsListener());
        addButton("Add Member", new AddMemberListener());
        addButton("Add Book Copy", new AddBookCopyListener());
        addButton("Add New Book", new AddNewBookListener());
        addButton("Checkout Book", new AddNewBookListener()); // update to implement book checkout window

        backgroundLabel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setPreferredSize(new Dimension(180, 48));
        buttonPanel.add(button);
    }

    class AllBookIdsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            LibrarySystem.hideAllWindows();
            AllBookIdsWindow.INSTANCE.init();
            List<String> ids = ci.allBookIds();
            Collections.sort(ids);
            StringBuilder sb = new StringBuilder();
            for (String s : ids) {
                sb.append(s).append("\n");
            }
            AllBookIdsWindow.INSTANCE.setData(sb.toString());
            AllBookIdsWindow.INSTANCE.pack();
            Util.centerFrameOnDesktop(AllBookIdsWindow.INSTANCE);
            AllBookIdsWindow.INSTANCE.setVisible(true);
        }
    }

    class AllMemberIdsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            LibrarySystem.hideAllWindows();
            AllMemberIdsWindow.INSTANCE.init();
            List<String> ids = ci.allMemberIds();
            Collections.sort(ids);
            StringBuilder sb = new StringBuilder();
            for (String s : ids) {
                sb.append(s).append("\n");
            }
            AllMemberIdsWindow.INSTANCE.setData(sb.toString());
            AllMemberIdsWindow.INSTANCE.pack();
            Util.centerFrameOnDesktop(AllMemberIdsWindow.INSTANCE);
            AllMemberIdsWindow.INSTANCE.setVisible(true);
        }
    }

    static class AddMemberListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            LibrarySystem.hideAllWindows();
            AddMemberWindow.INSTANCE.init();
            AddMemberWindow.INSTANCE.pack();
            AddMemberWindow.INSTANCE.setVisible(true);
        }
    }

    static class AddBookCopyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (SystemController.currentAuth == Auth.LIBRARIAN) {
                JOptionPane.showMessageDialog(INSTANCE, "Permission denied !!!\n" + "You don't have permission to add book copy !");
            } else {
                LibrarySystem.hideAllWindows();
                BookCopyWindow.INSTANCE.init();
                Util.centerFrameOnDesktop(BookCopyWindow.INSTANCE);
                BookCopyWindow.INSTANCE.setVisible(true);
            }

        }
    }

    static class AddNewBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (SystemController.currentAuth == Auth.LIBRARIAN) {
                JOptionPane.showMessageDialog(INSTANCE, "Permission denied !!!\n" + "You don't have permission to add new book !");
            } else {
                LibrarySystem.hideAllWindows();
                AddNewBookWindow.INSTANCE.init();
                Util.centerFrameOnDesktop(AddNewBookWindow.INSTANCE);
                AddNewBookWindow.INSTANCE.setVisible(true);
            }
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
