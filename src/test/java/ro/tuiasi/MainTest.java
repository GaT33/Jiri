package ro.tuiasi;

import com.theokanning.openai.audio.CreateSpeechRequest;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.service.OpenAiService;
import javazoom.jl.player.Player;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.*;
import org.easymock.EasyMock;
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

        OpenAiService mockService = EasyMock.createMock(OpenAiService.class); // Create mock service
        EasyMock.expect(mockService.createTranscription(EasyMock.anyObject(CreateTranscriptionRequest.class), EasyMock.eq(audioFile)))
                .andReturn(mockResult);

        EasyMock.replay(mockService, mockResult);

        AudioTxt audioTxt = new AudioTxt(mockService); // Assuming constructor accepts OpenAiService

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

        // Mocking ChatCompletionChoice
        ChatCompletionChoice choice = EasyMock.mock(ChatCompletionChoice.class);
        EasyMock.expect(choice.getMessage()).andReturn(new ChatMessage("assistant", expectedChatResponse)).anyTimes();

        List<ChatCompletionChoice> mockChoices = new ArrayList<>();
        mockChoices.add(choice);

        // Mocking ChatCompletionResult
        ChatCompletionResult mockChatCompletionResult = EasyMock.mock(ChatCompletionResult.class);
        EasyMock.expect(mockChatCompletionResult.getChoices()).andReturn(mockChoices).anyTimes();

        // Mocking OpenAiService
        OpenAiService mockService = EasyMock.mock(OpenAiService.class);
        EasyMock.expect(mockService.createChatCompletion(EasyMock.anyObject(ChatCompletionRequest.class)))
                .andReturn(mockChatCompletionResult);

        // Mocking ResponseBody
        ResponseBody mockResponseBody = EasyMock.mock(ResponseBody.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        EasyMock.expect(mockResponseBody.byteStream()).andReturn(inputStream);

        // Creating a mock call for createSpeech
        Call<ResponseBody> mockCall = EasyMock.mock(Call.class);
        EasyMock.expect(mockCall.execute()).andReturn(Response.success(mockResponseBody));
        EasyMock.expect(mockService.createSpeech(EasyMock.anyObject(CreateSpeechRequest.class)))
                .andReturn(mockCall);

        // Putting mocks into replay mode
        EasyMock.replay(choice, mockChatCompletionResult, mockService, mockResponseBody, mockCall);

        // Creating instance of TxtAudio
        TxtAudio txtAudio = new TxtAudio(mockService);

        // Act
        String actualFilePath = txtAudio.handleChatAndSpeech(transcribedText);

        // Assert
        assertEquals(expectedAudioFilePath, actualFilePath);

        // Verify the interactions
        EasyMock.verify(choice, mockChatCompletionResult, mockService, mockResponseBody, mockCall);
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

        TxtAudio txtAudio = new TxtAudio(null); // Assuming constructor accepts OpenAiService

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

        OpenAiService mockService = EasyMock.createMock(OpenAiService.class); // Create mock service
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

        ImageGPT imageGPT = new ImageGPT(mockService); // Assuming constructor accepts OpenAiService

        // Act
        imageGPT.createAndDownloadImage(prompt);

        // Assert
        assertTrue(Files.exists(Paths.get("downloaded_image.jpg")));

        // Verify the interactions
        EasyMock.verify(mockService, mockImage, responseMock, responseBodyMock, clientMock);
    }
}
