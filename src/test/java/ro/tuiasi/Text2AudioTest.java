package ro.tuiasi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Text2AudioTest {

    private Text2Audio text2Audio;
    private Path audioPath = Paths.get("src/Vocal.mp3");

    @BeforeEach
    public void setUp() throws IOException {
        text2Audio = new Text2Audio(Main.service);
        Files.deleteIfExists(audioPath);
    }

    @Test
    public void testHandleSpeechAndPlayAudioFile() {
        try {
            URL url = new URL("https://www.google.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            assertTrue(code == 200, "Connection to Google failed with status code: " + code);
        } catch (IOException e) {
            fail("Connection test failed with exception: " + e.getMessage());
        }

        // Handle speech
        try {
            String textForSpeech = "Hello, this is a test.";
            String filePath = text2Audio.handleSpeech(textForSpeech);

            assertTrue(Files.exists(audioPath), "Audio file should be saved to the specified path");

            long fileSize = Files.size(audioPath);
            assertTrue(fileSize > 0, "Generated audio file should not be empty");

            text2Audio.playAudioFile(filePath);

        } catch (Exception e) {
            fail("Speech handling or playing failed with exception: " + e.getMessage());
        }
    }
}
