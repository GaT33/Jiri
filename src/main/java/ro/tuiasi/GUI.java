package ro.tuiasi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class GUI {

    public GUI() {
        // Create the main frame
        JFrame frame = new JFrame("Sign Up Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Made the frame larger
        frame.setLayout(null);

        ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("exempluimgproiect.png"));
        Image image = imageIcon.getImage().getScaledInstance(400, 600, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(0, 0, 400, 600);
// Create a new panel for the background image and add it to the frame
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 400, 600);
        leftPanel.add(imageLabel);
        frame.add(leftPanel);

// Right side (Your form)
        JPanel formPanel = new JPanel();
        formPanel.setBounds(400, 0, 400, 600);
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(45, 45, 45)); // Set a background color for the right side
        frame.add(formPanel);



        // Create labels
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setForeground(Color.WHITE); // Set text color to white
        nameLabel.setBounds(50, 100, 300, 30);
        formPanel.add(nameLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(50, 150, 300, 30);
        formPanel.add(emailLabel);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 200, 300, 30);
        formPanel.add(passwordLabel);

        // Create text fields
        JTextField nameTextField = new JTextField();
        nameTextField.setBounds(50, 130, 300, 30);
        formPanel.add(nameTextField);

        JTextField emailTextField = new JTextField();
        emailTextField.setBounds(50, 180, 300, 30);
        formPanel.add(emailTextField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(50, 230, 300, 30);
        formPanel.add(passwordField);

        // Create sign up button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(125, 280, 150, 40);
        signUpButton.setBackground(new Color(108, 117, 125)); // Set a custom background color
        signUpButton.setForeground(Color.WHITE); // Set text color to white
        formPanel.add(signUpButton);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new InterfataChat();
            }
        });

        // Pictograma Facebook
        ImageIcon originalFacebookIcon = new ImageIcon(getClass().getClassLoader().getResource("fb_Img_11apr.png"));
        Image facebookImage = originalFacebookIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedFacebookIcon = new ImageIcon(facebookImage);
        JLabel facebookLabel = new JLabel(resizedFacebookIcon);
        facebookLabel.setBounds(50, 340, 30, 30);
        formPanel.add(facebookLabel);

// Pictograma Twitter
        ImageIcon originalTwitterIcon = new ImageIcon(getClass().getClassLoader().getResource("tw_Img_11apr.png"));
        Image twitterImage = originalTwitterIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedTwitterIcon = new ImageIcon(twitterImage);
        JLabel twitterLabel = new JLabel(resizedTwitterIcon);
        twitterLabel.setBounds(100, 340, 30, 30);
        formPanel.add(twitterLabel);

// Pictograma Google
        ImageIcon originalGoogleIcon = new ImageIcon(getClass().getClassLoader().getResource("google_Img_11apr.png"));
        Image googleImage = originalGoogleIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedGoogleIcon = new ImageIcon(googleImage);
        JLabel googleLabel = new JLabel(resizedGoogleIcon);
        googleLabel.setBounds(150, 340, 30, 30);
        formPanel.add(googleLabel);

        // Sign in link
        JLabel signInLabel = new JLabel("Already a member? Sign in", SwingConstants.CENTER);
        signInLabel.setForeground(Color.LIGHT_GRAY);
        signInLabel.setBounds(100, 400, 200, 30);
        formPanel.add(signInLabel);

        // Add mouse listener to the sign-in label for click events
       signInLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Here you can add the logic to switch to the sign-in screen
                JOptionPane.showMessageDialog(frame, "Redirecting to sign-in screen...");
            }
        });

        addListeners(facebookLabel, twitterLabel, googleLabel);

        // Set the frame to be visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addListeners(JLabel facebookLabel, JLabel twitterLabel, JLabel googleLabel) {
        // Adăugarea MouseListener-elor pentru deschiderea linkurilor
     //   JLabel facebookLabel = new JLabel(); // Asumăm că facebookLabel este accesibil aici
        facebookLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openWebPage("https://www.facebook.com/");
            }
        });

     //   JLabel twitterLabel = new JLabel(); // Asumăm că twitterLabel este accesibil aici
        twitterLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openWebPage("https://twitter.com/");
            }
        });

     //   JLabel googleLabel = new JLabel(); // Asumăm că googleLabel este accesibil aici
        googleLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openWebPage("https://accounts.google.com/");
            }
        });
    }

    private void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}