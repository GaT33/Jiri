package ro.tuiasi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class representing the chat interface.
 */
public class InterfataChat {
    /**
     * Constructor for creating the chat interface.
     */
    public InterfataChat() {
        JFrame frame = new JFrame("Jiri - Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1086, 534);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);

        // Custom gradient panel for background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(51, 45, 45);
                Color color2 = new Color(36, 25, 50);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(null);
        frame.setContentPane(gradientPanel);

        JPanel panelStanga = new JPanel();
        panelStanga.setBackground(new Color(0, 0, 0));
        panelStanga.setBounds(10, 10, 336, 477);
        panelStanga.setLayout(null);

        JTextArea raspuns = new JTextArea();
        raspuns.setEditable(false);
        raspuns.setForeground(new Color(255, 255, 255));
        raspuns.setBackground(new Color(0, 0, 0));
        raspuns.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
        raspuns.setBounds(10, 10, 280, 457);

        JScrollPane scroll = new JScrollPane();
        scroll.setBounds(10, 10, 316, 457);
        scroll.setBackground(new Color(0, 0, 0));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setBackground(new Color(189, 189, 189));
        scroll.setViewportView(raspuns);

        panelStanga.add(scroll);
        gradientPanel.add(panelStanga);

        JPanel panelDreapta = new JPanel();
        panelDreapta.setBackground(new Color(0, 0, 0));
        panelDreapta.setBounds(726, 10, 336, 477);
        panelDreapta.setLayout(null);
        gradientPanel.add(panelDreapta);

        // Create rounded buttons
        JButton butonInregistrare = createRoundButton("Speak");
        butonInregistrare.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add action here
            }
        });
        butonInregistrare.setBounds(468, 188, 160, 38);
        gradientPanel.add(butonInregistrare);

        JLabel display = new JLabel("");
        display.setHorizontalAlignment(SwingConstants.CENTER);
        display.setBounds(10, 10, 316, 457);
        panelDreapta.add(display);

        JButton butonImg = createRoundButton("Generate Image");
        butonImg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add action here
            }
        });
        butonImg.setBounds(468, 236, 160, 38);
        gradientPanel.add(butonImg);

        JLabel iconita1 = new JLabel("");
        iconita1.setHorizontalAlignment(SwingConstants.CENTER);
        iconita1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("picture.png")));
        iconita1.setBounds(425, 236, 45, 38);
        gradientPanel.add(iconita1);

        JLabel iconita2 = new JLabel("");
        iconita2.setHorizontalAlignment(SwingConstants.CENTER);
        iconita2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("megaphone.png")));
        iconita2.setBounds(425, 188, 45, 38);
        gradientPanel.add(iconita2);

        frame.setVisible(true);
    }

    /**
     * Creates a rounded button with specified text.
     *
     * @param text The text to be displayed on the button.
     * @return The created JButton with rounded corners.
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
        button.setBackground(new Color(0, 0, 0));
        return button;
    }
}
