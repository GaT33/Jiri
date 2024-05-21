package ro.tuiasi;

import com.theokanning.openai.service.OpenAiService;
import javax.swing.*;

public class Main {

    public static OpenAiService service = new OpenAiService("sk-proj-R1uNHZaMvPUi75XJipmbT3BlbkFJkkMQ0e0XQN0IJHokkU97");

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfataChat();
            }
        });
    }
}