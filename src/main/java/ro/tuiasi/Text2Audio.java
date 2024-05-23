package ro.tuiasi;

import javazoom.jl.player.Player;
import com.theokanning.openai.audio.CreateSpeechRequest;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.ResponseBody;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * The Text2Audio class provides functionality for handling text-to-speech conversion using the OpenAiService.
 */
public class Text2Audio {

    /**
     * OpenAiService instance for interacting with OpenAI's API.
     */
    private final OpenAiService service;

    /**
     * Player for the speech playing.
     */
    private Player audioPlayer;

    /**
     * Checks if the player is used at the moment.
     */
    private boolean isPlaying = false;

    /**
     * Constructs a Text2Audio object with the specified OpenAiService.
     *
     * @param service The OpenAiService instance used for text-to-speech conversion.
     */
    public Text2Audio(OpenAiService service) {
        this.service = service;
    }

    /**
     * Handles transcribed text by generating a response and converting it to speech.
     *
     * @param transcribedText The transcribed text to be processed.
     * @return The text to be used for speech synthesis.
     * @throws IOException If an I/O error occurs.
     */
    public String handleChat(String transcribedText) throws IOException {
        // Create chat messages with system response and user input
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "You are a helpful assistant. You are prohibited from using more than 300 characters whenever I talk to you."));
        messages.add(new ChatMessage("user", transcribedText));

        // Create a chat completion request to generate a response
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-4o-2024-05-13")  // Model for generating response
                .build();

        // Generate response using the OpenAiService
        List<ChatCompletionChoice> responses = service.createChatCompletion(chatCompletionRequest).getChoices();

        // Concatenate response messages into text for speech
        String textForSpeech = responses.stream()
                .map(choice -> choice.getMessage().getContent())
                .collect(Collectors.joining(" "));

        return textForSpeech;
    }

    /**
     * Converts text to speech and saves it to a file.
     *
     * @param textForSpeech The text to be converted to speech.
     * @return The file path of the generated speech audio.
     * @throws IOException If an I/O error occurs.
     */
    public String handleSpeech(String textForSpeech) throws IOException {
        // Create a speech request to convert text to speech
        CreateSpeechRequest speechRequest = CreateSpeechRequest.builder()
                .model("tts-1-hd")                // Text-to-speech model
                .input(textForSpeech)          // Input text for speech
                .voice("shimmer")                // Voice for speech
                .responseFormat("mp3")         // Format of the speech response
                .speed(1.0)                    // Speed of speech
                .build();

        File audioVoc = new File("..\\PipProject\\src\\Vocal.mp3");

        if (audioVoc.exists())
            audioVoc.delete();

        try {
            // Send speech request and retrieve response body
            ResponseBody responseBody = service.createSpeech(speechRequest);

            // Check if response body is not null
            if (responseBody != null) {
                // Specify the output path for the speech audio
                Path output = Paths.get("src/Vocal.mp3");

                // Save the speech audio to the specified output path
                try (InputStream inputStream = responseBody.byteStream()) {
                    Files.copy(inputStream, output);
                } catch (IOException e) {
                    System.out.println("Error writing the file: " + e.getMessage());
                }
            } else {
                System.out.println("The response is null. Please check the request.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        // Return the file path of the generated speech audio
        return "src/Vocal.mp3";
    }

    /**
     * Plays the audio file located at the specified file path.
     *
     * @param filePath The file path of the audio file to be played.
     * @throws Exception If an error occurs while playing the audio file.
     */
    public synchronized void playAudioFile(String filePath) throws Exception {
        // Check if there is already a player playing and stop it
        if (isPlaying && audioPlayer != null) {
            audioPlayer.close();
            isPlaying = false;
        }

        FileInputStream fis = new FileInputStream(filePath);
        audioPlayer = new Player(fis);
        isPlaying = true;

        // Start playing on a separate thread to avoid blocking the UI
        new Thread(() -> {
            try {
                audioPlayer.play();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isPlaying = false;
            }
        }).start();
    }

    /**
     * Stops the current audio playback if it is active.
     */
    public synchronized void stopAudio() {
        if (isPlaying && audioPlayer != null) {
            audioPlayer.close();
            isPlaying = false;
        }
    }
}
