package ro.tuiasi;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import static ro.tuiasi.Text2Audio.truncateTextToFit;

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
        frame.setSize(1135, 535);
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
        panelStanga.setBounds(10, 10, 400, 485);
        panelStanga.setLayout(null);

        JTextArea raspuns = new JTextArea();
        raspuns.setEditable(false);
        raspuns.setForeground(new Color(20, 213, 51));
        raspuns.setBackground(new Color(0, 0, 0));
        raspuns.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
        raspuns.setBounds(10, 10, 400, 485);
        panelStanga.add(raspuns);

        gradientPanel.add(panelStanga);

        JPanel panelCentru = new JPanel();
        panelCentru.setBackground(new Color(0, 0, 0));
        panelCentru.setBounds(435, 10, 250, 105);
        panelCentru.setLayout(null);

        JTextArea transcribed = new JTextArea();
        transcribed.setEditable(false);
        transcribed.setForeground(new Color(255, 117, 0));
        transcribed.setBackground(new Color(0, 0, 0));
        transcribed.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
        transcribed.setBounds(10, 20, 250, 105);
        panelCentru.add(transcribed);

        gradientPanel.add(panelCentru);

        JButton butonInregistrare = createRoundButton("Speak");
        butonInregistrare.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    File file1 = new File("..\\PipProject\\AudioRecord.wav");
                    File file2 = new File("..\\PipProject\\src\\Vocal.mp3");
                    String speech = "";

                    if (file1.exists())
                        file1.delete();

                    if (file2.exists())
                        file2.delete();

                    AudioRecorder recorder = new AudioRecorder();
                    System.out.println("Recording started.");
                    recorder.startRecording("AudioRecord.wav");

                    try {
                        recorder.recordingThread.join();  // wait for thread to stop
                    } catch (InterruptedException ex) {
                        System.out.println("Recording interrupted." + ex.getMessage());
                    }
                    System.out.println("Recording stopped and saved to your file.");

                    Audio2Text transcriptionHandler = new Audio2Text(Main.service);
                    String transcribedText = transcriptionHandler.transcribeAudio();
                    String tr_response_f = truncateTextToFit(transcribedText, transcribed, 250, 105);
                    transcribed.setText(tr_response_f);

                    try {
                        String response = new Text2Audio(Main.service).handleChat(transcribedText);
                        String response_f = truncateTextToFit(response, raspuns, 400, 457);
                        SwingUtilities.invokeLater(() -> raspuns.setText(response_f));
                        speech = response_f;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    Text2Audio speechHandler = new Text2Audio(Main.service);
                    try {
                        speechHandler.handleSpeech(speech);
                        speechHandler.playAudioFile("src/Vocal.mp3");
                    } catch (Exception ex) {
                        System.out.println("Error playing audio file: " + ex.getMessage());
                    }
                }).start();
            }
        });
        butonInregistrare.setBounds(488, 188, 160, 38);
        gradientPanel.add(butonInregistrare);

        JPanel panelDreapta = new JPanel();
        panelDreapta.setBackground(new Color(0, 0, 0));
        panelDreapta.setBounds(705, 10, 400, 480);
        panelDreapta.setLayout(null);
        gradientPanel.add(panelDreapta);

        JLabel display = new JLabel("");
        display.setHorizontalAlignment(SwingConstants.CENTER);
        display.setBounds(10, 10, 316, 457);
        panelDreapta.add(display);

        JButton butonImg = createRoundButton("Generate Image");
        butonImg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {

                }).start();
            }
        });
        butonImg.setBounds(488, 236, 160, 38);
        gradientPanel.add(butonImg);

        JLabel iconita1 = new JLabel("");
        iconita1.setHorizontalAlignment(SwingConstants.CENTER);
        iconita1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("picture.png")));
        iconita1.setBounds(449, 236, 45, 38);
        gradientPanel.add(iconita1);

        JLabel iconita2 = new JLabel("");
        iconita2.setHorizontalAlignment(SwingConstants.CENTER);
        iconita2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("megaphone.png")));
        iconita2.setBounds(449, 188, 45, 38);
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

    public static void main(String[] args) {
        new InterfataChat();
    }
}
