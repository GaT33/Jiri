package ro.tuiasi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class AudioRecorderTest {
    private AudioRecorder audioRecorder;

    @BeforeEach
    void setUp() {
        audioRecorder = new AudioRecorder();
    }

    @AfterEach
    void tearDown() {
        audioRecorder = null;
    }

    @Test
    void testStartAndStopRecording() throws Exception {
        assertNotNull(audioRecorder, "AudioRecorder should be initialized");

        String testFile = "testOutput.wav";
        Thread recordingThread = new Thread(() -> audioRecorder.startRecording(testFile));
        recordingThread.start();

        // Let the recording run for a short period
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Field runningField = AudioRecorder.class.getDeclaredField("running");
        runningField.setAccessible(true);
        runningField.set(audioRecorder, false);

        try {
            recordingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File outputFile = new File(testFile);
        assertTrue(outputFile.exists(), "The output file should be created");
        assertTrue(outputFile.length() > 0, "The output file should not be empty");

    }

    @Test
    void testIsSilent() throws Exception {
        Method isSilentMethod = AudioRecorder.class.getDeclaredMethod("isSilent", byte[].class, int.class);
        isSilentMethod.setAccessible(true);

        byte[] silentBuffer = new byte[1024]; // Buffer filled with zeros
        byte[] noiseBuffer = new byte[1024];
        for (int i = 0; i < noiseBuffer.length; i++) {
            noiseBuffer[i] = (byte) (Math.random() * 256 - 128); // Random noise
        }

        boolean silentResult = (boolean) isSilentMethod.invoke(audioRecorder, silentBuffer, silentBuffer.length);
        boolean noiseResult = (boolean) isSilentMethod.invoke(audioRecorder, noiseBuffer, noiseBuffer.length);

        assertTrue(silentResult, "Buffer should be detected as silent");
        assertFalse(noiseResult, "Buffer should be detected as noise");
    }

}
