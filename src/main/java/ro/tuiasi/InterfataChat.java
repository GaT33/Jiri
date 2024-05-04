package ro.tuiasi;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import java.awt.SystemColor;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JScrollBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InterfataChat {
    public InterfataChat() {
        JFrame frame = new JFrame("Main Interface");
        frame.setBackground(new Color(255, 255, 255));
        frame.getContentPane().setBackground(new Color(47, 54, 56));
        frame.setBounds(100, 100, 1086, 534);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        JPanel panelStanga = new JPanel();
        panelStanga.setBackground(new Color(75, 76, 77));
        panelStanga.setBounds(10, 10, 336, 477);

        JTextArea raspuns = new JTextArea();
        raspuns.setEditable(false);
        raspuns.setForeground(new Color(255, 255, 255));
        raspuns.setBackground(new Color(56, 60, 61));
        raspuns.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
        raspuns.setBounds(10, 10, 280, 457);

        JScrollPane scrol = new JScrollPane();
        scrol.setBounds(10, 10, 316, 457);
        scrol.setBackground(new Color(77, 86, 86));
        scrol.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrol.getViewport().setBackground(Color.WHITE);
        scrol.setViewportView(raspuns);

        panelStanga.add(scrol);

        frame.getContentPane().add(panelStanga);
        panelStanga.setLayout(null);

        JPanel panelDreapta = new JPanel();
        panelDreapta.setBackground(new Color(75, 76, 77));
        panelDreapta.setBounds(726, 10, 336, 477);
        frame.getContentPane().add(panelDreapta);
        panelDreapta.setLayout(null);

        JButton butonInregistrare = new JButton("Vorbeste");
        butonInregistrare.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


            }
        });
        butonInregistrare.setBackground(new Color(97, 106, 107));
        butonInregistrare.setForeground(new Color(255, 255, 255));
        butonInregistrare.setFont(new Font("Tahoma", Font.PLAIN, 14));
        butonInregistrare.setBounds(488, 188, 134, 38);
        frame.getContentPane().add(butonInregistrare);

        JLabel display = new JLabel("");
        display.setHorizontalAlignment(SwingConstants.CENTER);
        display.setBounds(10, 10, 316, 457);
        panelDreapta.add(display);

        JButton butonImg = new JButton("Creare Imagine");
        butonImg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				/*
				    //test de afisare imagine pe panelDreapta
				Image image = new ImageIcon(getClass().getResource("car.png")).getImage();
				display.setIcon(new ImageIcon(image));
				panelDreapta.add(display);
				*/
            }
        });
        butonImg.setForeground(new Color(255, 255, 255));
        butonImg.setBackground(new Color(97, 106, 107));
        butonImg.setFont(new Font("Tahoma", Font.PLAIN, 14));
        butonImg.setBounds(488, 236, 134, 38);
        frame.getContentPane().add(butonImg);

        JLabel iconita1 = new JLabel("");
        iconita1.setHorizontalAlignment(SwingConstants.CENTER);
        iconita1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("picture.png")));
        iconita1.setBounds(433, 236, 45, 38);
        frame.getContentPane().add(iconita1);

        JLabel iconita2 = new JLabel("");
        iconita2.setHorizontalAlignment(SwingConstants.CENTER);
        iconita2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("megaphone.png")));
        iconita2.setBounds(433, 188, 45, 38);
        frame.getContentPane().add(iconita2);
    }
}
