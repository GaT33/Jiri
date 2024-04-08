package ro.tuiasi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{


    private JPanel LogInPanel;
    private JLabel label;
    private JLabel Titlu;
    private JButton b_log_in;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel numeLabel;
    private JLabel parolaLabel;

    public GUI(){
        setContentPane(LogInPanel);
        setTitle("Simple GUI App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);
        setVisible(true);
        b_log_in.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel MainPanel=new JPanel();
                setContentPane(MainPanel);
                setSize(500,500);
                setVisible (true);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setLocationRelativeTo(null);
            }
        });
    }


}
