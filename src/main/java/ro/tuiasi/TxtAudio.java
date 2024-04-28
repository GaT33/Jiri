
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

public class TxtAudio {
    private OpenAiService service;

    public TxtAudio(OpenAiService service) {
        this.service = service;
    }

    public String handleChatAndSpeech(String transcribedText) throws IOException {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "You are a helpful assistant."));
        messages.add(new ChatMessage("user", transcribedText));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-3.5-turbo-1106")
                .build();

        List<ChatCompletionChoice> responses = service.createChatCompletion(chatCompletionRequest).getChoices();
        responses.forEach(response -> {
            System.out.println(responses);
        });
        String textForSpeech = responses.stream()
                .map(choice -> choice.getMessage().getContent())
                .collect(Collectors.joining(" "));

        CreateSpeechRequest speechRequest = CreateSpeechRequest.builder()
                .model("tts-1")
                .input(textForSpeech)
                .voice("alloy")
                .responseFormat("mp3")
                .speed(1.0)
                .build();

        try {
            ResponseBody responseBody = service.createSpeech(speechRequest);
            if (responseBody != null) {
                Path output = Paths.get("src/speechMerge2.mp3");
                try (InputStream inputStream = responseBody.byteStream()) {
                    Files.copy(inputStream, output);
                    System.out.println("Fișierul audio a fost salvat cu succes la: " + output);
                } catch (IOException e) {
                    System.out.println("Eroare la scrierea fișierului: " + e.getMessage());
                }
            } else {
                System.out.println("Răspunsul este null, verifică cererea trimisă.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Path output= Paths.get("src/speechMerge2.mp3");
        return output.toString();
    }
    public void playAudioFile(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        Player playMP3 = new Player(fis);
        playMP3.play();
        playMP3.close();
        Files.delete(Paths.get(filePath));
    }
}