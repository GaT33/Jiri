package ro.tuiasi;

import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.service.OpenAiService;

import java.io.File;

public class AudioTxt {
    private OpenAiService service;

    public AudioTxt(OpenAiService service) {
        this.service = service;
    }

    public String transcribeAudio() {
        File audioFile = new File("AudioRecord.wav");
        CreateTranscriptionRequest transcriptionRequest = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .responseFormat("json")
                .build();

        TranscriptionResult transcriptionResult = service.createTranscription(transcriptionRequest, audioFile);
        System.out.println(transcriptionResult.getText());
        return transcriptionResult.getText();

    }
}
