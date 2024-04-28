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

public class ImageGPT{

    Path imagePath1 = Paths.get("downloaded_image.jpg");

    private OpenAiService service;

    public ImageGPT(OpenAiService service) {
        this.service = service;
    }

    public void createAndDownloadImage(String prompt) {
        try {
            Files.deleteIfExists(imagePath1);
            System.out.println("Fișierul existent a fost șters: " + imagePath1.toString());
        } catch (IOException e) {
            System.out.println("A apărut o eroare la ștergerea fișierului existent: " + e.getMessage());
        }

        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(prompt)
                .n(1)
                .size("1024x1024")
                .user("example_user")
                .build();



        List<Image> images = service.createImage(request).getData();

        for (int i = 0; i < images.size(); i++) {
            if (!images.isEmpty() && images.get(0).getUrl() != null) {
                String imageUrl = images.get(0).getUrl();
                System.out.println("Generated Image URL: " + imageUrl);

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request downloadRequest = new Request.Builder().url(imageUrl).build();
                    Response response = client.newCall(downloadRequest).execute();

                    if (response.isSuccessful() && response.body() != null) {
                        InputStream inputStream = response.body().byteStream();
                        Path imagePath = Paths.get("downloaded_image.jpg");
                        Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Image has been saved as JPEG at: " + imagePath.toString());
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