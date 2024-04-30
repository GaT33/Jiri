package ro.tuiasi;

import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;


import java.io.*;
import java.util.*;
import java.nio.file.*;
import okhttp3.*;

import com.theokanning.openai.service.OpenAiService;


public class Main {
    public static void main(String[] args) {
        //conexiune
        OpenAiService service = new OpenAiService();//aici pui tu cheia ("")
        //creare imagine+ parametrii
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("horse fighting")//Extra tip , nu accepta prompt uri in romana
                .n(1) // Number of images to generate
                .size("1024x1024") // Size of the generated images
                .user("example_user") // User identifier
                .build();

        //VARIANTA 1 ne da linkuri pt https
//        List<Image> images = service.createImage(request).getData();
//        for (int i = 0; i < images.size(); i++) {
//            System.out.println(images.get(i).getUrl());
//        }

        //VARIANTA 2 download format jpg
        List<Image> images = service.createImage(request).getData();


        for (int i = 0; i < images.size(); i++) {
            if (!images.isEmpty() && images.get(0).getUrl() != null) {
                String imageUrl = images.get(0).getUrl();
                System.out.println("Generated Image URL: " + imageUrl);

                // Download and save the image as a JPEG file

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request downloadRequest = new Request.Builder().url(imageUrl).build();
                    Response response = client.newCall(downloadRequest).execute();

                    if (response.isSuccessful() && response.body() != null) {
                        // Get the input stream of the image data
                        InputStream inputStream = response.body().byteStream();

                        // Define the path where the image will be saved
                        Path imagePath = Paths.get("downloaded_image.jpg");

                        // Use Files.copy to write the InputStream to a file
                        Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Image has been saved as JPEG at: " + imagePath.toString());

                        inputStream.close(); // Close the stream
                    } else {
                        System.out.println("Failed to download the image.");
                    }
                    response.close(); // Close the response to free resources
                } catch (IOException e) {
                    System.out.println("An error occurred while downloading or saving the image: " + e.getMessage());
                }
            } else {
                System.out.println("No images were generated or URL is missing.");
            }

        }
    }
}