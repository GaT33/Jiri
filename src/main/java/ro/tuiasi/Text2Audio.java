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
        messages.add(new ChatMessage("system", "You are a helpful assistant. You are prohibited from using more than 300 characters whenever I ask you something."));
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

    /**
     * Truncates text to fit within the specified dimensions of a JTextArea.
     *
     * @param text The text to be truncated.
     * @param textArea The JTextArea for calculating dimensions.
     * @param maxWidth The maximum width allowed.
     * @param maxHeight The maximum height allowed.
     * @return The truncated text that fits within the specified dimensions.
     */
    public static String truncateTextToFit(String text, JTextArea textArea, int maxWidth, int maxHeight) {
        text = removeAccents(text);

        // Get the font metrics for the current font of the JTextArea
        FontMetrics metrics = textArea.getFontMetrics(textArea.getFont());

        int lineHeight = metrics.getHeight();
        int maxLines = maxHeight / lineHeight;

        // Calculate the maximum number of characters that can fit in a single line
        int maxCharsPerLine = maxWidth / metrics.charWidth('A'); // A is reference

        StringBuilder truncatedText = new StringBuilder();

        // Split the text into words
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int linesCount = 0;

        // Loop through each word in the text
        for (String word : words) {
            // Check if adding the word to the current line would exceed the maximum width
            if (metrics.stringWidth(line.toString() + word) < maxWidth) {
                // If not, add the word to the current line
                line.append(word).append(" ");
            } else {
                // If it would, check if we have reached the maximum number of lines
                if (linesCount >= maxLines - 1) {
                    truncatedText.append(line).append("...");
                    break;
                }
                // Otherwise, append the current line to the truncated text and start a new line
                truncatedText.append(line).append("\n");
                line = new StringBuilder(word).append(" ");
                linesCount++;
            }
        }

        // If there is still room for more lines, append the last line
        if (linesCount < maxLines) {
            truncatedText.append(line);
        }

        // Return the truncated text
        return truncatedText.toString();
    }


    /**
     * Removes accents from a given text.
     *
     * @param text The text from which accents need to be removed.
     * @return The text without accents.
     */
    public static String removeAccents(String text) {
        // Normalize the text to decompose combined characters into their base characters and combining diacritical marks
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);

        // Compile a pattern that matches all diacritical marks
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        // Replace all diacritical marks in the normalized text with an empty string, effectively removing them
        return pattern.matcher(normalizedText).replaceAll("");
    }

}
