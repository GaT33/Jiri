package ro.tuiasi;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static ro.tuiasi.Audio2Text.truncateTextToFit;

/**
 * Class representing the chat interface.
 */
public class InterfataChat {

    /**
     * Constructor for creating the chat interface.
     */
    public InterfataChat() {
        Text2Audio speechHandlerI = new Text2Audio(Main.service);
        try {
            speechHandlerI.handleSpeech("How can I help you?");
            speechHandlerI.playAudioFile("src/Vocal.mp3");
        } catch (Exception ex) {
            System.out.println("Error playing audio file: " + ex.getMessage());
        }

        JFrame frame = new JFrame("Jiri - Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1135, 540);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);

        // Set custom icon
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Jiri_Icon.png"));
        frame.setIconImage(icon);

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
        butonInregistrare.addActionListener(e -> new Thread(() -> {
            String speech = "";
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
            String trresponsef = truncateTextToFit(transcribedText, transcribed, 250, 105);
            transcribed.setText(trresponsef);

            try {
                String response = new Text2Audio(Main.service).handleChat(transcribedText);
                String responsef = truncateTextToFit(response, raspuns, 400, 457);
                SwingUtilities.invokeLater(() -> raspuns.setText(responsef));
                speech = responsef;
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
        }).start());
        butonInregistrare.setBounds(488, 207, 160, 38);
        gradientPanel.add(butonInregistrare);

        JPanel panelDreapta = new JPanel();
        panelDreapta.setBackground(new Color(0, 0, 0));
        panelDreapta.setBounds(705, 10, 400, 485);
        panelDreapta.setLayout(null);
        gradientPanel.add(panelDreapta);

        JLabel display = new JLabel("");
        display.setHorizontalAlignment(SwingConstants.CENTER);
        display.setBounds(0, 0, 400, 480);
        panelDreapta.add(display);

        JButton butonImg = createRoundButton("Generate Image");
        butonImg.addActionListener(e -> new Thread(() -> {
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
            ImageGPT imageHandler = new ImageGPT(Main.service);
            imageHandler.createAndDownloadImage(transcribedText);
            Image image = new ImageIcon(
                    (new File("../PipProject/OpenAIImage.jpg")).getAbsolutePath()).getImage();
            Image resizedImage = image.getScaledInstance(400, 480, Image.SCALE_SMOOTH);
            SwingUtilities.invokeLater(() -> {
                display.setIcon(new ImageIcon(resizedImage));
                panelDreapta.repaint();
            });
        }).start());
        butonImg.setBounds(488, 255, 160, 38);
        gradientPanel.add(butonImg);

        JPanel panelJos = new JPanel();
        panelJos.setBackground(new Color(0, 0, 0));
        panelJos.setBounds(435, 390, 250, 105);
        panelJos.setLayout(new BorderLayout());

        JTextArea consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);
        consoleTextArea.setForeground(new Color(255, 255, 255));
        consoleTextArea.setBackground(new Color(0, 0, 0));
        consoleTextArea.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollPane.getHorizontalScrollBar().setBackground(Color.BLACK);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.WHITE;
            }
        });
        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.WHITE;
            }
        });

        panelJos.add(scrollPane, BorderLayout.CENTER);

        gradientPanel.add(panelJos);

        PrintStream printStream = new PrintStream(new TextAreaOutputStream(consoleTextArea));
        System.setOut(printStream);
        System.setErr(printStream);

        JLabel iconita1 = new JLabel("");
        iconita1.setHorizontalAlignment(SwingConstants.CENTER);
        iconita1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("picture.png")));
        iconita1.setBounds(449, 255, 45, 38);
        gradientPanel.add(iconita1);

        JLabel iconita2 = new JLabel("");
        iconita2.setHorizontalAlignment(SwingConstants.CENTER);
        iconita2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("megaphone.png")));
        iconita2.setBounds(449, 207, 45, 38);
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

    /**
     * Custom OutputStream for redirecting console output to a JTextArea.
     */
    static class TextAreaOutputStream extends OutputStream {

        /**
         * JTextArea where Console Output will be written.
         */
        private final JTextArea textArea;

        /**
         * Constructs a TextAreaOutputStream with the specified JTextArea.
         *
         * @param textArea The JTextArea to which the output will be redirected.
         */
        TextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            SwingUtilities.invokeLater(() -> textArea.append(String.valueOf((char) b)));
        }

        @Override
        public void write(byte[] b, int off, int len) {
            SwingUtilities.invokeLater(() -> textArea.append(new String(b, off, len)));
        }
    }
}
