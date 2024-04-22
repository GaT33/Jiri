package ro.tuiasi;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.*;
import com.theokanning.openai.audio.*;
import com.theokanning.openai.client.*;
import com.theokanning.openai.completion.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("Here we go.");
        /*List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "You are a helpful assistant."));
        messages.add(new ChatMessage("user", ""));*/

        OpenAiService service = new OpenAiService("");
       /*ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-3.5-turbo-1106")
                .build();
        service.createChatCompletion(chatCompletionRequest)
                .getChoices()
                .forEach(System.out::println);*/

        File audioFile = new File("filepath");
        CreateTranscriptionRequest transcriptionRequest = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .responseFormat("json")
                .build();

        try {
            // Create transcription
            TranscriptionResult transcriptionResult = service.createTranscription(transcriptionRequest, audioFile);
            System.out.println(transcriptionResult);
        } catch (OpenAiHttpException e) {
            // Print out the request body and error message
            System.out.println("HTTP Status Code: " + e.statusCode);
            System.out.println("OpenAI Error Code: " + e.code);
            System.out.println("Error Parameter: " + e.param);
            System.out.println("Error Type: " + e.type);
            System.out.println("Error Message: " + e.getMessage());
        }

    }
}