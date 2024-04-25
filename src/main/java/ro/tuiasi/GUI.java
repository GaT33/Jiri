package ro.tuiasi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class GUI {

    private JFrame frame;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JLabel facebookLabel, twitterLabel, googleLabel;
    private JButton signUpButton;
    private JLabel signInLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private java.util.List<Userr> usersDatabase = new java.util.ArrayList<>();

    public GUI() {
        frame = new JFrame("Sign Up Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(null);

        initializeGUI();
        addListeners();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
//a
    private void initializeGUI() {

        ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("future-of-AI (1).png"));
        Image image = imageIcon.getImage().getScaledInstance(400, 600, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(0, 0, 400, 600);

        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 400, 600);
        leftPanel.add(imageLabel);
        frame.add(leftPanel);

        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(400, 0, 400, 600);
        formPanel.setBackground(new Color(45, 45, 45));
        frame.add(formPanel);

        nameLabel = new JLabel("Name");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(50, 100, 300, 30);
        formPanel.add(nameLabel);

        emailLabel = new JLabel("Email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(50, 150, 300, 30);
        formPanel.add(emailLabel);

        passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 200, 300, 30);
        formPanel.add(passwordLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(50, 130, 300, 30);
        formPanel.add(nameTextField);

        emailTextField = new JTextField();
        emailTextField.setBounds(50, 180, 300, 30);
        formPanel.add(emailTextField);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 230, 300, 30);
        formPanel.add(passwordField);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(125, 280, 150, 40);
        signUpButton.setBackground(new Color(108, 117, 125));
        signUpButton.setForeground(Color.WHITE);
        formPanel.add(signUpButton);

        ImageIcon originalFacebookIcon = new ImageIcon(getClass().getClassLoader().getResource("fb_Img_11apr.png"));
        Image facebookImage = originalFacebookIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedFacebookIcon = new ImageIcon(facebookImage);
        facebookLabel = new JLabel(resizedFacebookIcon);
        facebookLabel.setBounds(50, 340, 30, 30);
        formPanel.add(facebookLabel);

        ImageIcon originalTwitterIcon = new ImageIcon(getClass().getClassLoader().getResource("tw_Img_11apr.png"));
        Image twitterImage = originalTwitterIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedTwitterIcon = new ImageIcon(twitterImage);
        twitterLabel = new JLabel(resizedTwitterIcon);
        twitterLabel.setBounds(100, 340, 30, 30);
        formPanel.add(twitterLabel);

        ImageIcon originalGoogleIcon = new ImageIcon(getClass().getClassLoader().getResource("google_Img_11apr.png"));
        Image googleImage = originalGoogleIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedGoogleIcon = new ImageIcon(googleImage);
        googleLabel = new JLabel(resizedGoogleIcon);
        googleLabel.setBounds(150, 340, 30, 30);
        formPanel.add(googleLabel);

        signInLabel = new JLabel("Already a member? Sign in", SwingConstants.CENTER);
        signInLabel.setForeground(Color.LIGHT_GRAY);
        signInLabel.setBounds(100, 400, 200, 30);
        formPanel.add(signInLabel);
    }

    private void addListeners() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());

                if (!isEmailValid(email)) {
                    JOptionPane.showMessageDialog(frame, "Adresa de email nu este valida!", "Eroare", JOptionPane.ERROR_MESSAGE);
                } else if (!isPasswordValid(password)) {
                    JOptionPane.showMessageDialog(frame,"Parola nu este valida! Trebuie să conțină cel puțin 8 caractere, " +"inclusiv o literă mare, o literă mică, un număr și un caracter special.","Eroare", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Restul logicii pentru înregistrare dacă emailul și parola sunt valide
                    // Adăugăm utilizatorul în "baza de date"
                    usersDatabase.add(new Userr(name, email, password));
                    JOptionPane.showMessageDialog(frame, "Înregistrare reușită!");
                    for (Userr userr : usersDatabase) {
                        System.out.println("Nume: " + userr.getName() + ", Email: " + userr.getEmail());
                        // Nu afișăm parola pentru motive de securitate
                    }
                }
            }
        });

        facebookLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openWebPage("https://www.facebook.com/");
            }
        });

        twitterLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openWebPage("https://twitter.com/");
            }
        });

        googleLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openWebPage("https://accounts.google.com/");
            }
        });

        signInLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(frame, "Redirecting to sign-in screen...");
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
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        if (password.length() < 8) {
            return false; // Parola trebuie să aibă cel puțin 8 caractere
        }

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }


        return hasUpper && hasLower && hasNumber && hasSpecial;
    }
}

class Userr {
    private String name;
    private String email;
    private String password;

    public Userr(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getteri și setteri pentru name, email și password după cum este necesar
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

//comm