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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ImageGPTTest {

    private ImageGPT imageGPT;
    private Path imagePath = Paths.get("OpenAIImage.jpg");

    @BeforeEach
    public void setUp() throws IOException {
        imageGPT = new ImageGPT(Main.service);
        Files.deleteIfExists(imagePath);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(imagePath);
    }

    @Test
    public void testCreateAndDownloadImage() {
        try {
            URL url = new URL("https://www.google.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            assertTrue(code == 200, "Connection to Internet failed");
        } catch (IOException e) {
            fail("Connection test failed with exception: " + e.getMessage());
        }

        imageGPT.createAndDownloadImage("a cute puppy");

        assertTrue(Files.exists(imagePath), "Image should be saved to the specified path");

        // Verify the image file is not empty
        try {
            long fileSize = Files.size(imagePath);
            assertTrue(fileSize > 0, "Downloaded image file should not be empty");
        } catch (IOException e) {
            fail("Failed to verify image file size: " + e.getMessage());
        }
    }
}
