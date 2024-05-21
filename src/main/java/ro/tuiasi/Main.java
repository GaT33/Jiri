package ro.tuiasi;

import com.theokanning.openai.service.OpenAiService;

public class Main {
    public static void main(String[] args) {
        //1 record alex
        //bla bla.. blabla

        //2 conectare API
        OpenAiService service = new OpenAiService("");

        //3 transcription
        AudioTxt transcriptionHandler = new AudioTxt(service);
        String transcribedText = transcriptionHandler.transcribeAudio();

        //4 speech
        TxtAudio chatAndSpeechHandler = new TxtAudio(service);
        try {
            chatAndSpeechHandler.handleChatAndSpeech(transcribedText);
            //4.1 play audio
            chatAndSpeechHandler.playAudioFile("src/speechMerge2.mp3");
        } catch (Exception e) {
            System.out.println("Eroare la redarea fișierului audio: " + e.getMessage());
        }

        //5 pozicaaa
        ImageGPT imageHandler = new ImageGPT(service);
        String taskImagine="horse dancing";//accepta numa engleza
        imageHandler.createAndDownloadImage(taskImagine);

    }
}
