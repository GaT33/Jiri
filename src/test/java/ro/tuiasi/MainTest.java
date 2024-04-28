package ro.tuiasi;

import com.theokanning.openai.audio.CreateSpeechRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.service.OpenAiService;
import javazoom.jl.player.Player;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.easymock.EasyMoc
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private OpenAiService mockService;
    private TxtAudio txtAudio;
    private ImageGPT imageGPT;

    @BeforeAll
    static void beforeAll() {
        // Setup that runs before all tests
    }

    @AfterAll
    static void afterAll() {
        // Cleanup that runs after all tests
    }

    @BeforeEach
    void setUp() {
        mockService = EasyMock.createMock(OpenAiService.class);
        txtAudio = new TxtAudio(mockService);
        imageGPT = new ImageGPT(mockService);
    }

    @AfterEach
    void tearDown() {
        // Cleanup after each test
    }

    @Test
    void transcribeAudio_success() {
        // Arrange
        File audioFile = new File("AudioRecord.wav");
        String expectedTranscription = "This is a test transcription";

        TranscriptionResult mockResult = EasyMock.createMock(TranscriptionResult.class);
        EasyMock.expect(mockResult.getText()).andReturn(expectedTranscription);

        EasyMock.expect(mockService.createTranscription(EasyMock.anyObject(CreateTranscriptionRequest.class), EasyMock.eq(audioFile)))
                .andReturn(mockResult);

        EasyMock.replay(mockService, mockResult);

        // Act
        String actualTranscription = audioTxt.transcribeAudio();

        // Assert
        assertEquals(expectedTranscription, actualTranscription);

        // Verify the interactions
        EasyMock.verify(mockService, mockResult);
    }

    @Test
    void handleChatAndSpeech_success() throws IOException {
        // Arrange
        String transcribedText = "Hello, how are you?";
        String expectedChatResponse = "I am fine, thank you!";
        String expectedAudioFilePath = "src/speechMerge2.mp3";

        List<ChatCompletionChoice> mockChoices = new ArrayList<>();
        ChatCompletionChoice choice = EasyMock.createMock(ChatCompletionChoice.class);
        EasyMock.expect(choice.getMessage()).andReturn(new ChatMessage("assistant", expectedChatResponse)).anyTimes();
        mockChoices.add(choice);

        EasyMock.expect(mockService.createChatCompletion(EasyMock.anyObject(ChatCompletionRequest.class)))
                .andReturn(() -> mockChoices);

        ResponseBody mockResponseBody = EasyMock.createMock(ResponseBody.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        EasyMock.expect(mockResponseBody.byteStream()).andReturn(inputStream);

        EasyMock.expect(mockService.createSpeech(EasyMock.anyObject(CreateSpeechRequest.class)))
                .andReturn(mockResponseBody);

        EasyMock.replay(mockService, choice, mockResponseBody);

        // Act
        String actualFilePath = txtAudio.handleChatAndSpeech(transcribedText);

        // Assert
        assertEquals(expectedAudioFilePath, actualFilePath);

        // Verify the interactions
        EasyMock.verify(mockService, choice, mockResponseBody);
    }

    @Test
    void playAudioFile_success() throws Exception {
        // Arrange
        String filePath = "src/test.mp3";
        Files.createFile(Paths.get(filePath));

        FileInputStream fisMock = EasyMock.createMock(FileInputStream.class);
        Player playerMock = EasyMock.createMock(Player.class);

        // We need to ensure the Player class is properly mocked.
        EasyMock.expectNew(Player.class, fisMock).andReturn(playerMock);
        playerMock.play();
        EasyMock.expectLastCall().once();
        playerMock.close();
        EasyMock.expectLastCall().once();

        EasyMock.replay(fisMock, playerMock);

        // Act
        txtAudio.playAudioFile(filePath);

        // Assert
        assertFalse(Files.exists(Paths.get(filePath)));

        // Verify the interactions
        EasyMock.verify(fisMock, playerMock);
    }

    @Test
    void createAndDownloadImage_success() throws IOException {
        // Arrange
        String prompt = "Generate an image of a sunset";
        String imageUrl = "http://example.com/image.jpg";

        Files.deleteIfExists(Paths.get("downloaded_image.jpg"));

        List<Image> mockImages = new ArrayList<>();
        Image mockImage = EasyMock.createMock(Image.class);
        EasyMock.expect(mockImage.getUrl()).andReturn(imageUrl).anyTimes();
        mockImages.add(mockImage);

        EasyMock.expect(mockService.createImage(EasyMock.anyObject(CreateImageRequest.class)))
                .andReturn(() -> mockImages);

        OkHttpClient clientMock = EasyMock.createMock(OkHttpClient.class);
        Request requestMock = new Request.Builder().url(imageUrl).build();
        Response responseMock = EasyMock.createMock(Response.class);
        ResponseBody responseBodyMock = EasyMock.createMock(ResponseBody.class);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        EasyMock.expect(responseMock.isSuccessful()).andReturn(true);
        EasyMock.expect(responseMock.body()).andReturn(responseBodyMock);
        EasyMock.expect(responseBodyMock.byteStream()).andReturn(inputStream);

        EasyMock.expect(clientMock.newCall(requestMock).execute()).andReturn(responseMock);

        EasyMock.replay(mockService, mockImage, responseMock, responseBodyMock, clientMock);

        // Act
        imageGPT.createAndDownloadImage(prompt);

        // Assert
        assertTrue(Files.exists(Paths.get("downloaded_image.jpg")));

        // Verify the interactions
        EasyMock.verify(mockService, mockImage, responseMock, responseBodyMock, clientMock);
    }
}
