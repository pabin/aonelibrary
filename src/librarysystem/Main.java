package librarysystem;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;


public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() ->
        {
//	            LibrarySystem.INSTANCE.setTitle("A-One Library Application");
//	            LibrarySystem.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//	            LibrarySystem.INSTANCE.init();
//	            centerFrameOnDesktop(LibrarySystem.INSTANCE);
//	            LibrarySystem.INSTANCE.setVisible(true);

            LoginWindow.INSTANCE.setTitle("A-One Library Login");
            LoginWindow.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            LoginWindow.INSTANCE.init();
            centerFrameOnDesktop(LoginWindow.INSTANCE);
            LoginWindow.INSTANCE.setVisible(true);
        });
    }

    public static void centerFrameOnDesktop(Component f) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int height = toolkit.getScreenSize().height;
        int width = toolkit.getScreenSize().width;
        int frameHeight = f.getSize().height;
        int frameWidth = f.getSize().width;
        f.setLocation(((width - frameWidth) / 2), (height - frameHeight) / 3);
    }
}
