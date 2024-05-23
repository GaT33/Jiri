package ro.tuiasi;

import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.service.OpenAiService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * The AudioTxt class provides functionality for transcribing audio files using the OpenAiService.
 */
public class Audio2Text {

    /**
     *  The OpenAiService instance used for transcribing audio.
     */
    private final OpenAiService service;

    /**
     * Constructs an AudioTxt object with the specified OpenAiService.
     *
     * @param service The OpenAiService instance used for transcribing audio.
     */
    public Audio2Text(OpenAiService service) {
        this.service = service;
    }

    /**
     * Transcribes the audio file "AudioRecord.wav" using the configured OpenAiService.
     *
     * @return The transcribed text from the audio file.
     */
    public String transcribeAudio() {
        // Load the audio file
        File audioFile = new File("AudioRecord.wav");

        // Create a transcription request specifying the model and response format
        CreateTranscriptionRequest transcriptionRequest = CreateTranscriptionRequest.builder()
                .model("whisper-1") // Model for transcription
                .responseFormat("json") // Format of the transcription response
                .build();

        // Request transcription from the OpenAiService
        TranscriptionResult transcriptionResult = service.createTranscription(transcriptionRequest, audioFile);

        return transcriptionResult.getText();
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
        int maxCharsPerLine = maxWidth / metrics.charWidth('A') + 20; // A is reference

        StringBuilder truncatedText = new StringBuilder();

        // Split the text into words
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int linesCount = 0;

        // Loop through each word in the text
        for (String word : words) {
            // Check if adding the word to the current line would exceed the maximum number of characters
            if (line.length() + word.length() <= maxCharsPerLine) {
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
