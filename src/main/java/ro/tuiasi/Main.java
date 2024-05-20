package ro.tuiasi;
import javax.swing.*;

//sk-proj-R1uNHZ' 'aMvPUi75XJ' 'ipmbT3BlbkFJkkMQ0' 'e0XQN0IJHokkU97  ;;sterge ' ' pentru cheie
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}