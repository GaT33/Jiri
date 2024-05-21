
package ro.tuiasi;
import javazoom.jl.player.Player;//imi trebuie,aia e
import com.theokanning.openai.audio.CreateSpeechRequest;
import com.theokanning.openai.completion.chat.*;
        import com.theokanning.openai.service.OpenAiService;
import okhttp3.ResponseBody;

import java.io.*;
        import java.nio.file.*;
        import java.util.*;
        import java.util.stream.Collectors;

/**
 * The TxtAudio class provides functionality for handling text-to-speech conversion using the OpenAiService.
 */
public class TxtAudio {
    // OpenAiService instance for interacting with OpenAI's API
    private OpenAiService service;

    /**
     * Constructs a TxtAudio object with the specified OpenAiService.
     *
     * @param service The OpenAiService instance used for text-to-speech conversion.
     */
    public TxtAudio(OpenAiService service) {
        this.service = service;
    }

    /**
     * Handles transcribed text by generating a response and converting it to speech.
     *
     * @param transcribedText The transcribed text to be processed.
     * @return The file path of the generated speech audio.
     * @throws IOException If an I/O error occurs.
     */
    public String handleChatAndSpeech(String transcribedText) throws IOException {
        // Create chat messages with system response and user input
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "You are a helpful assistant."));
        messages.add(new ChatMessage("user", transcribedText));

        // Create a chat completion request to generate a response
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-3.5-turbo-1106")  // Model for generating response
                .build();

        // Generate response using the OpenAiService
        List<ChatCompletionChoice> responses = service.createChatCompletion(chatCompletionRequest).getChoices();

        // Concatenate response messages into text for speech
        String textForSpeech = responses.stream()
                .map(choice -> choice.getMessage().getContent())
                .collect(Collectors.joining(" "));

        // Create a speech request to convert text to speech
        CreateSpeechRequest speechRequest = CreateSpeechRequest.builder()
                .model("tts-1")                // Text-to-speech model
                .input(textForSpeech)          // Input text for speech
                .voice("alloy")                // Voice for speech
                .responseFormat("mp3")         // Format of the speech response
                .speed(1.0)                    // Speed of speech
                .build();

        try {
            // Send speech request and retrieve response body
            ResponseBody responseBody = service.createSpeech(speechRequest);

            // Check if response body is not null
            if (responseBody != null) {
                // Specify the output path for the speech audio
                Path output = Paths.get("src/speechMerge2.mp3");

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
        return "src/speechMerge2.mp3";
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
}