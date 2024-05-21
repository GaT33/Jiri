package ro.tuiasi;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;
import java.awt.geom.AffineTransform;

/**
 * GUI class represents the graphical user interface for the application.
 * It sets up the main frame, form fields, and handles validation and interactions.
 */
public class GUI {

    /**
     * Checks if "Sign Up" button is pressed.
     * Used for TextField's board color.
     */
    private static boolean isClicked;

    /**
     * Used for showing errors to user.
     */
    JLabel errorLabel;

    /**
     * Constructs the GUI and initializes the components.
     */
    public GUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Jiri");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Custom panel with gradient background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(0, 0, 0);
                Color color2 = new Color(51, 20, 115);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(null);
        frame.add(gradientPanel, BorderLayout.CENTER);

        // Left panel with an image
        ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("Jiri.png"));
        Image image = imageIcon.getImage().getScaledInstance(720, 720, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(0, 0, 400, 600);
        gradientPanel.add(imageLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBounds(400, 0, 400, 600);
        formPanel.setLayout(null);
        formPanel.setOpaque(false);
        gradientPanel.add(formPanel);

        // TextFields for user input
        JTextField keyTextField = createIconKeyField(
                "Key",
                new ImageIcon(getClass().getClassLoader().getResource("Key.png")));
        keyTextField.setBounds(50, 180, 300, 30);
        formPanel.add(keyTextField);

        JTextField emailTextField = createIconEmailField(
                "Email",
                new ImageIcon(getClass().getClassLoader().getResource("Email.png")));
        emailTextField.setBounds(50, 240, 300, 30);
        formPanel.add(emailTextField);

        JPasswordField passwordField = createIconPasswordField(
                "Password",
                new ImageIcon(getClass().getClassLoader().getResource("Password.png")));
        passwordField.setBounds(50, 300, 300, 30);
        formPanel.add(passwordField);

        // Sign up button
        JButton signUpButton = createRoundButton("Sign Up");
        signUpButton.setBounds(125, 360, 150, 40);
        signUpButton.setBackground(new Color(57, 36, 99));
        formPanel.add(signUpButton);

        signUpButton.addActionListener(e -> {
            isClicked = true;
            if (validateFields(keyTextField, emailTextField, passwordField)) {
                frame.setVisible(false);
                new InterfataChat();
            }
        });

        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel);

        // GitHub icon
        ImageIcon originalGitHubIcon = new ImageIcon(getClass().getClassLoader().getResource("GitHub.png"));
        Image gitHubImage = originalGitHubIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedGitHubIcon = new ImageIcon(gitHubImage);
        JLabel gitHubLabel = new JLabel(resizedGitHubIcon);
        gitHubLabel.setBounds(185, 520, 30, 30);
        formPanel.add(gitHubLabel);

        addListeners(gitHubLabel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.getRootPane().requestFocus();
    }

    /**
     * Creates a JTextField with an icon for the key field.
     * @param placeholder Placeholder text.
     * @param icon Icon to be displayed.
     * @return Configured JTextField.
     */
    private static JTextField createIconKeyField(final String placeholder, final Icon icon) {
        JTextField textField = new JTextField() {
            private final String floatingLabelText = placeholder;
            private boolean isFocused = false;
            private float labelY = 0; // Initial Y-position of label

            {
                setOpaque(false);
                setMargin(new Insets(5, 35, 5, 5));

                addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        isFocused = true;
                        animateLabelUp();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (getText().isEmpty()) {
                            setText("");
                            isFocused = false;
                            animateLabelDown();
                        } else {
                            isFocused = true;
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getText().isEmpty() || isFocused) {
                    g2d.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                    g2d.setColor(new Color(255, 255, 255));

                    AffineTransform transform = g2d.getTransform();
                    g2d.translate(30, labelY);
                    g2d.drawString(floatingLabelText, 5, 20);
                    g2d.setTransform(transform);
                }
            }

            private void animateLabelUp() {
                new Thread(() -> {
                    for (float i = labelY; i >= -25; i -= 1) {
                        labelY = i;
                        repaint();
                        try {
                            Thread.sleep(7);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }

            private void animateLabelDown() {
                new Thread(() -> {
                    for (float i = labelY; i <= 0; i += 1) {
                        labelY = i;
                        repaint();
                        try {
                            Thread.sleep(7);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            protected void paintBorder(Graphics g) {
                if (isClicked && !isKeyValid(getText())) {
                    g.setColor(new Color(255, 0, 0));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else if (isClicked && isKeyValid(getText())) {
                    g.setColor(new Color(0, 255, 0));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else if (isFocused) {
                    g.setColor(new Color(3, 161, 252));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else {
                    g.setColor(new Color(70, 22, 74));
                    repaint();
                }
            }
        };

        textField.setOpaque(false);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setMargin(new Insets(5, 35, 5, 5));
        textField.setForeground(new Color(255, 255, 255));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(5, 0, 30, 30);
        textField.setLayout(null);
        textField.add(iconLabel);

        textField.setBackground(new Color(101, 81, 153));

        return textField;
    }

    /**
     * Creates a JTextField with an icon for the email field.
     * @param placeholder Placeholder text.
     * @param icon Icon to be displayed.
     * @return Configured JTextField.
     */
    private static JTextField createIconEmailField(final String placeholder, final Icon icon) {
        JTextField textField = new JTextField() {
            private String floatingLabelText = placeholder;
            private boolean isFocused = false;
            private float labelY = 0;

            {
                setOpaque(false);
                setMargin(new Insets(5, 35, 5, 5));

                addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        isFocused = true;
                        animateLabelUp();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (getText().isEmpty()) {
                            setText("");
                            isFocused = false;
                            animateLabelDown();
                        } else {
                            isFocused = true;
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getText().isEmpty() || isFocused) {
                    g2d.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                    g2d.setColor(new Color(255, 255, 255));

                    AffineTransform transform = g2d.getTransform();
                    g2d.translate(30, labelY);
                    g2d.drawString(floatingLabelText, 5, 20);
                    g2d.setTransform(transform);
                }
            }

            private void animateLabelUp() {
                new Thread(() -> {
                    for (float i = labelY; i >= -25; i -= 1) {
                        labelY = i;
                        repaint();
                        try {
                            Thread.sleep(7);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }

            private void animateLabelDown() {
                new Thread(() -> {
                    for (float i = labelY; i <= 0; i += 1) {
                        labelY = i;
                        repaint();
                        try {
                            Thread.sleep(7);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            protected void paintBorder(Graphics g) {
                if (isClicked && !isEmailValid(getText())) {
                    g.setColor(new Color(255, 0, 0));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else if (isClicked && isEmailValid(getText())) {
                    g.setColor(new Color(0, 255, 0));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else if (isFocused) {
                    g.setColor(new Color(3, 161, 252));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else {
                    g.setColor(new Color(70, 22, 74));
                    repaint();
                }
            }
        };

        textField.setOpaque(false);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setMargin(new Insets(5, 35, 5, 5));
        textField.setForeground(new Color(255, 255, 255));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(5, 0, 30, 30);
        textField.setLayout(null);
        textField.add(iconLabel);

        textField.setBackground(new Color(101, 81, 153));

        return textField;
    }

    /**
     * Creates a JPasswordField with an icon for the password field.
     * @param placeholder Placeholder text.
     * @param icon Icon to be displayed.
     * @return Configured JPasswordField.
     */
    private static JPasswordField createIconPasswordField(final String placeholder, final Icon icon) {
        JPasswordField passwordField = new JPasswordField() {
            private String floatingLabelText = placeholder;
            private boolean isFocused = false;
            private float labelY = 0;

            {
                setOpaque(false);
                setMargin(new Insets(5, 35, 5, 5));

                addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        isFocused = true;
                        animateLabelUp();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (getText().isEmpty()) {
                            isFocused = false;
                            animateLabelDown();
                        } else {
                            isFocused = true;
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getText().isEmpty() || isFocused) {
                    g2d.setFont(getFont().deriveFont(Font.PLAIN, 12f));
                    g2d.setColor(new Color(255, 255, 255));

                    AffineTransform transform = g2d.getTransform();
                    g2d.translate(30, labelY);
                    g2d.drawString(floatingLabelText, 5, 20);
                    g2d.setTransform(transform);
                }
            }

            private void animateLabelUp() {
                new Thread(() -> {
                    for (float i = labelY; i >= -25; i -= 1) {
                        labelY = i;
                        repaint();
                        try {
                            Thread.sleep(7);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }

            private void animateLabelDown() {
                new Thread(() -> {
                    for (float i = labelY; i <= 0; i += 1) {
                        labelY = i;
                        repaint();
                        try {
                            Thread.sleep(7);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            protected void paintBorder(Graphics g) {
                if (isClicked && !isPasswordValid(getText())) {
                    g.setColor(new Color(255, 0, 0));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else if (isClicked && isPasswordValid(getText())) {
                    g.setColor(new Color(0, 255, 0));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else if (isFocused) {
                    g.setColor(new Color(3, 161, 252));
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                    repaint();
                } else {
                    g.setColor(new Color(70, 22, 74));
                    repaint();
                }
            }
        };

        passwordField.setOpaque(false);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setMargin(new Insets(5, 35, 5, 5));
        passwordField.setForeground(new Color(255, 255, 255));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(5, 0, 30, 30);
        passwordField.setLayout(null);
        passwordField.add(iconLabel);

        passwordField.setBackground(new Color(101, 81, 153));

        return passwordField;
    }

    /**
     * Creates a rounded button with specified text.
     * @param text Button text.
     * @return Configured JButton.
     */
    private JButton createRoundButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(new Color(180, 180, 180));
                } else {
                    g.setColor(getBackground());
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(98, 37, 118));
        return button;
    }

    /**
     * Adds listeners to the specified GitHub label.
     * @param gitHubLabel JLabel to which listeners are added.
     */
    private void addListeners(final JLabel gitHubLabel) {
        gitHubLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                openWebPage("https://github.com/");
            }
        });
    }

    /**
     * Opens the specified URL in the default web browser.
     * @param url URL to be opened.
     */
    private void openWebPage(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Validates the email format.
     * @param email Email to be validated.
     * @return true if the email is valid, false otherwise.
     */
    private static boolean isEmailValid(final String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                +
                "[a-zA-Z0-9_+&*-]+)*@"
                +
                "(gmail|yahoo|hotmail)\\.[a-z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Validates the password format.
     * @param password Password to be validated.
     * @return true if the password is valid, false otherwise.
     */
    private static boolean isPasswordValid(final String password) {
        if (password.length() < 7) return false;
        if (!Pattern.compile("[A-Z]").matcher(password).find()) return false;
        if (!Pattern.compile("[0-9]").matcher(password).find()) return false;
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) return false;
        return true;
    }

    /**
     * Validates the API key format.
     * @param key API key to be validated.
     * @return true if the key is valid, false otherwise.
     */
    private static boolean isKeyValid(final String key) {
        String keyRegex = "^sk-[a-zA-Z0-9-]{4,}-[a-zA-Z0-9]{32,}$";
        Pattern pattern = Pattern.compile(keyRegex);
        return pattern.matcher(key).matches();
    }

    /**
     * Validates the input fields and updates the error label accordingly.
     * @param keyTextField Key text field to be validated.
     * @param emailTextField Email text field to be validated.
     * @param passwordField Password field to be validated.
     * @return true if all fields are valid, false otherwise.
     */
    private boolean validateFields(JTextField keyTextField, JTextField emailTextField, JPasswordField passwordField) {
        boolean valid = true;
        String errorMessage = "<html><center>";

        if (!isKeyValid(keyTextField.getText())) {
            errorMessage += "Invalid API Key.<br>";
            valid = false;
        }

        if (!isEmailValid(emailTextField.getText())) {
            errorMessage += "Invalid Email. Check the format.<br>";
            valid = false;
        }

        if (!isPasswordValid(new String(passwordField.getPassword()))) {
            errorMessage += "Invalid Password. Your password should contain at least 7 characters, one upper-case, one number, and one special character.<br>";
            valid = false;
        }

        errorMessage += "</center></html>";
        errorLabel.setText(errorMessage);
        errorLabel.setFont(new Font("Optima", Font.BOLD, 12));
        errorLabel.setBounds(50, 420, 300, 80);

        return valid;
    }
}
