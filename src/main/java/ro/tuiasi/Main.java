package ro.tuiasi;

import com.theokanning.openai.service.OpenAiService;
import javax.swing.*;

public class Main {

    public static OpenAiService service = new OpenAiService("");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}
