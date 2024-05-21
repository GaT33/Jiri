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
 * The TxtAudio class provides functionality for handling text-to-speech conversion using the OpenAiService.
 */
public class Text2Audio {
    // OpenAiService instance for interacting with OpenAI's API
    private final OpenAiService service;

    /**
     * Constructs a TxtAudio object with the specified OpenAiService.
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
     * @return The file path of the generated speech audio.
     * @throws IOException If an I/O error occurs.
     */
    public String handleChat(String transcribedText) throws IOException {
        // Create chat messages with system response and user input
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "You are an helpful assistant. You are prohibited from using more than 500 characters whenever I ask you something."));
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

    public String handleSpeech(String textForSpeech) throws IOException {
        // Create a speech request to convert text to speech
        CreateSpeechRequest speechRequest = CreateSpeechRequest.builder()
                .model("tts-1-hd")                // Text-to-speech model
                .input(textForSpeech)          // Input text for speech
                .voice("shimmer")                // Voice for speech
                .responseFormat("mp3")         // Format of the speech response
                .speed(1.0)                    // Speed of speech
                .build();

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
                    System.out.println("The audio file has been successfully saved at: " + output);
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
    public void playAudioFile(String filePath) throws Exception {
        // Create FileInputStream to read the audio file
        FileInputStream fis = new FileInputStream(filePath);

        // Create a Player instance to play the audio
        Player playMP3 = new Player(fis);

        // Play the audio file
        playMP3.play();

        // Close the Player instance
        playMP3.close();

        // Delete the audio file after it has been played
        Files.delete(Paths.get(filePath));
    }


    public static String truncateTextToFit(String text, JTextArea textArea, int maxWidth, int maxHeight) {
        text = removeAccents(text);  // Remove diacritics
        FontMetrics metrics = textArea.getFontMetrics(textArea.getFont());
        int lineHeight = metrics.getHeight();
        int maxLines = maxHeight / lineHeight;
        int maxCharsPerLine = maxWidth / metrics.charWidth('A');

        StringBuilder truncatedText = new StringBuilder();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        int linesCount = 0;

        for (String word : words) {
            if (metrics.stringWidth(line.toString() + word) < maxWidth) {
                line.append(word).append(" ");
            } else {
                if (linesCount >= maxLines - 1) {
                    truncatedText.append(line).append("...");
                    break;
                }
                truncatedText.append(line).append("\n");
                line = new StringBuilder(word).append(" ");
                linesCount++;
            }
        }
        if (linesCount < maxLines) {
            truncatedText.append(line);  // adaugă ultima linie
        }

        return truncatedText.toString();
    }

    public static String removeAccents(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalizedText).replaceAll("");
    }
}

