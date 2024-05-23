package ro.tuiasi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Audio2TextTest {

    private Audio2Text audio2Text;
    private Path audioPath = Paths.get("AudioRecord.wav");

    @BeforeEach
    public void setUp() throws IOException {
        audio2Text = new Audio2Text(Main.service);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(audioPath);
    }

    @Test
    public void testTranscribeAudio() {
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

        // Transcribe audio
        try {
            String transcription = audio2Text.transcribeAudio();

            assertNotNull(transcription, "Transcription result should not be null");
            assertTrue(!transcription.isEmpty(), "Transcription result should not be empty");

        } catch (Exception e) {
            fail("Audio transcription failed with exception: " + e.getMessage());
        }
    }
}
