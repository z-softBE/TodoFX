package be.zsoft.todo.ui.splash;


import be.zsoft.todo.ui.ResourceLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SplashScreen extends JFrame {

    public SplashScreen() throws IOException {
        setUndecorated(true);
        setSize(500,500);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

        add(new SplashScreenJPanel());

        // Center the JFrame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        setIconImage(Toolkit.getDefaultToolkit().getImage(ResourceLoader.loadURL("logo.png")));

        setVisible(true);
    }
}

class SplashScreenJPanel extends JPanel {

    private final BufferedImage image;

    SplashScreenJPanel() throws IOException {
        setSize(500,500);

        image = ImageIO.read(ResourceLoader.loadURL("splash.png"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}
