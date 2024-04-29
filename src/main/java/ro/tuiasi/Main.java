package ro.tuiasi;

public class Main {
    public static void main(String... args) {


        //Nu trebuia dat push in master, clean-up
      //implementare conexiunea API + streaming (consola) mai trebuie speech ul
        OpenAiService service = new OpenAiService("cheie Token");

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are an assistant.");
        messages.add(systemMessage);

        System.out.print("First Query: ");
        Scanner scanner = new Scanner(System.in);
        ChatMessage firstMsg = new ChatMessage(ChatMessageRole.USER.value(), scanner.nextLine());
        messages.add(firstMsg);

        while (true) {
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                    .builder()
                    .model("gpt-3.5-turbo-0613")
                    .messages(messages)
                    .n(1)
                    .maxTokens(256)
                    .build();
            Flowable<ChatCompletionChunk> flowable = service.streamChatCompletion(chatCompletionRequest);

            AtomicBoolean isFirst = new AtomicBoolean(true);
            ChatMessage chatMessage = service.mapStreamToAccumulator(flowable)
                    .doOnNext(accumulator -> {
                        if (isFirst.getAndSet(false)) {
                            System.out.print("Response: ");
                        }
                        if (accumulator.getMessageChunk().getContent() != null) {
                            System.out.print(accumulator.getMessageChunk().getContent());
                        }
                    })
                    .doOnComplete(System.out::println)
                    .lastElement()
                    .blockingGet()
                    .getAccumulatedMessage();
            messages.add(chatMessage); // don't forget to update the conversation with the latest response

    }

}