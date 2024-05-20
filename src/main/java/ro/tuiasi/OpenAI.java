package ro.tuiasi;

import javazoom.jl.player.Player;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.stream.Collectors;

import okhttp3.*;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.audio.CreateSpeechRequest;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;


public class OpenAI {
    public void playSound() {
        //conexiune + mesaj openAI
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "You are a helpful assistant."));
        messages.add(new ChatMessage("user", ""));

        OpenAiService service = new OpenAiService("");//cheie in main
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

        //speech parametrii openAI
        CreateSpeechRequest speechRequest = CreateSpeechRequest.builder()
                .model("tts-1")
                .input(textForSpeech)
                .voice("alloy")
                .responseFormat("mp3")
                .speed(1.0)
                .build();

        try {
            // Realizeaza cererea si primește răspunsul ca un flux de bytes
            ResponseBody responseBody = service.createSpeech(speechRequest);

            //teste
            if (responseBody != null) {
                // Calea unde vrei sa salvezi fișierul audio
                Path output = Paths.get("AudioRecord.wav");

                // Scrie fluxul de bytes în fișierul de la calea specificată
                try (InputStream inputStream = responseBody.byteStream()) {
                    Files.copy(inputStream, output);
                    System.out.println("Fișierul audio a fost salvat cu succes la: " + output);
                } catch (IOException e) {
                    System.out.println("Eroare la scrierea fișierului: " + e.getMessage());
                }
            } else {
                System.out.println("Răspunsul este null, verifică cererea trimisă.");
            }
        } catch (OpenAiHttpException e) {

            System.out.println("HTTP Status Code: " + e.statusCode);
            System.out.println("OpenAI Error Code: " + e.code);
            System.out.println("Error Parameter: " + e.param);
            System.out.println("Error Type: " + e.type);
            System.out.println("Error Message: " + e.getMessage());
        }

        //aici se realizeaza play ul
        try {

            FileInputStream fis = new FileInputStream("AudioRecord.wav");
            Player playMP3 = new Player(fis);

            playMP3.play();
            playMP3.close();
            Files.delete(Paths.get("AudioRecord.wav"));//nu merge sa l suprascrii asa ca efectiv il sterf si recreez cu acelasi nume
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}