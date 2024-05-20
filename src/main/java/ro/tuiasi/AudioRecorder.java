package ro.tuiasi;

import javax.sound.sampled.*;
import java.io.*;

/**
 * This class handles audio recording using a microphone.
 */
public class AudioRecorder {
    /**
     * The audio format used for recording.
     * 44100.0 Hz sample rate, 16-bit sample size, 2 channels (stereo), signed samples, little-endian byte order.
     */
    private final AudioFormat format = new AudioFormat(
            44100.0f, // Sample rate (Hz)
            16,       // Sample size in bits
            2,        // Number of channels (stereo)
            true,     // Signed samples
            false     // Little-endian byte order
    );

    /**
     * The target data line from which audio data is captured.
     */
    private TargetDataLine microphone;

    /**
     * The thread responsible for recording audio.
     */
    protected Thread recordingThread;

    /**
     * A flag indicating whether the recording is currently running.
     */
    private volatile boolean running = true;

    /**
     * Constructor that initializes the microphone line with the specified audio format.
     */
    public AudioRecorder() {
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Starts the recording process and saves the recorded audio to the specified file.
     *
     * @param filename The name of the file to save the recorded audio.
     */
    public void startRecording(String filename) {
        recordingThread = new Thread(() -> {
            try {
                microphone.start();
                Thread.sleep(500); // Wait for 0.5 seconds before starting to record
                System.out.println("You can speak now.");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int numBytesRead;
                long lastSoundTime = System.currentTimeMillis();

                while (running) {
                    numBytesRead = microphone.read(buffer, 0, buffer.length);
                    if (numBytesRead == -1) {
                        break;
                    }

                    if (isSilent(buffer, numBytesRead)) {
                        if (System.currentTimeMillis() - lastSoundTime > 1500) { // 1.5 seconds waiting for silence
                            break;
                        }
                    } else {
                        lastSoundTime = System.currentTimeMillis();
                    }
                    out.write(buffer, 0, numBytesRead);
                }
                microphone.stop();
                microphone.close();
                saveToFile(out.toByteArray(), filename);
            } catch (InterruptedException | IOException ex) {
                ex.printStackTrace();
            }
        });
        recordingThread.start();
    }

    /**
     * Checks if the audio buffer contains silence.
     *
     * @param buffer       The audio buffer.
     * @param numBytesRead The number of bytes read from the microphone.
     * @return True if the buffer contains silence, false otherwise.
     */
    private boolean isSilent(byte[] buffer, int numBytesRead) {
        double rms = calculateRMS(buffer, numBytesRead);
        return rms < 8.5;  // RMS threshold for silence
    }

    /**
     * Calculates the Root Mean Square (RMS) value of the audio buffer.
     *
     * @param buffer       The audio buffer.
     * @param numBytesRead The number of bytes read from the microphone.
     * @return The RMS value of the buffer.
     */
    private double calculateRMS(byte[] buffer, int numBytesRead) {
        long sum = 0;
        for (int i = 0; i < numBytesRead; i += 2) {
            // Combines two consecutive bytes from the buffer to form a 16-bit value
            // (buffer[i + 1] << 8) shifts the higher-order byte to the left by 8 bits
            // (buffer[i] & 0xFF) ensures the lower-order byte is treated as unsigned
            int value = ((buffer[i + 1] << 8) | (buffer[i] & 0xFF)); // Combine two bytes to form a 16-bit value
            sum += (long) value * value; // Square the value and add to sum
        }
        return Math.sqrt(sum / (numBytesRead / 2.0)); // Calculate the RMS value
    }

    /**
     * Saves the recorded audio data to a file.
     *
     * @param audioData The audio data to save.
     * @param filename  The name of the file to save the audio data.
     * @throws IOException If an I/O error occurs.
     */
    private void saveToFile(byte[] audioData, String filename) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream audioStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());
        File outputFile = new File(filename);
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outputFile);
    }
}
