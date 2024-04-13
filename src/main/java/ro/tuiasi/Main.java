package ro.tuiasi;

import java.util.ArrayList;
import java.util.List;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.*;
import com.theokanning.openai.client.*;
import com.theokanning.openai.completion.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("Here we go.");
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", "You are a helpful assistant."));
        messages.add(new ChatMessage("user", ""));

        OpenAiService service = new OpenAiService("");
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-3.5-turbo-1106")
                .build();
        service.createChatCompletion(chatCompletionRequest)
                .getChoices()
                .forEach(System.out::println);
    }
}