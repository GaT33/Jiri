package ro.tuiasi;

import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * The ImageGPT class provides functionality for handling image generation using the OpenAiService.
 */
public class ImageGPT {

    /**
     * Path to store the downloaded image.
     */
    private Path imagePath1 = Paths.get("OpenAIImage.jpg");

    /**
     * OpenAiService instance for interacting with OpenAI's API.
     */
    private OpenAiService service;

    /**
     * Constructs an ImageGPT object with the specified OpenAiService.
     *
     * @param service The OpenAiService instance used for generating images.
     */
    public ImageGPT(OpenAiService service) {
        this.service = service;
    }

    /**
     * Creates an image based on the provided prompt, downloads it, and saves it as a JPEG file.
     *
     * @param prompt The prompt used for generating the image.
     */
    public void createAndDownloadImage(String prompt) {
        try {
            // Delete the existing image file, if any
            Files.deleteIfExists(imagePath1);
        } catch (IOException e) {
            System.out.println("An error occurred while deleting the existing file: " + e.getMessage());
        }

        // Create a request to generate an image
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(prompt)             // Prompt for generating the image
                .model("dall-e-2")          // Model type
                .n(1)                    // Number of images to generate
                .quality("standard")        // Quality of image
                .size("512x512")          // Size of the image
                .user("end_user")           // User identifier
                .build();

        // Generate the image
        List<Image> images = service.createImage(request).getData();

        // Iterate through the generated images (usually only one)
        for (Image image : images) {
            if (image.getUrl() != null) {
                // Retrieve the URL of the generated image
                String imageUrl = image.getUrl();
                System.out.println("Generated Image URL: " + imageUrl);

                try {
                    // Download the image from the URL
                    OkHttpClient client = new OkHttpClient();
                    Request downloadRequest = new Request.Builder().url(imageUrl).build();
                    Response response = client.newCall(downloadRequest).execute();

                    // Check if the download was successful
                    if (response.isSuccessful() && response.body() != null) {
                        // Save the downloaded image as a JPEG file
                        InputStream inputStream = response.body().byteStream();
                        Files.copy(inputStream, imagePath1, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Image has been saved as JPEG at: " + imagePath1.toString());
                        inputStream.close();
                    } else {
                        System.out.println("Failed to download the image.");
                    }
                    response.close();
                } catch (Exception e) {
                    System.out.println("An error occurred while downloading or saving the image: " + e.getMessage());
                }
            } else {
                System.out.println("No images were generated or URL is missing.");
            }
        }
    }
}
