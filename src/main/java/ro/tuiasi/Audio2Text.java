package ro.tuiasi;

import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.service.OpenAiService;

import java.io.File;

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
}
