package ro.tuiasi;

import javax.sound.sampled.*;
import java.io.*;

public class AudioRecorder {
    private final AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);
    private TargetDataLine microphone;
    public Thread recordingThread;
    volatile private boolean running = true;

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

    public void startRecording(String filename) {
        recordingThread = new Thread(() -> {
            try {
                microphone.start();
                Thread.sleep(500);
                System.out.println("You can speak now.");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int numBytesRead;
                long lastSoundTime = System.currentTimeMillis();

                while (running) {
                    numBytesRead = microphone.read(buffer, 0, buffer.length);
                    if (numBytesRead == -1) break;

                    if (isSilent(buffer, numBytesRead)) {
                        if (System.currentTimeMillis() - lastSoundTime > 1500) { // 1.5s waiting for silence
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
            } catch (InterruptedException| IOException ex) {
                ex.printStackTrace();
            }
        });
        recordingThread.start();
    }

    private boolean isSilent(byte[] buffer, int numBytesRead) {
        double rms = calculateRMS(buffer, numBytesRead);
        return rms < 5.5;  // RMS threshold for silence
    }

    private double calculateRMS(byte[] buffer, int numBytesRead) {
        long sum = 0;
        for (int i = 0; i < numBytesRead; i += 2) {
            int value = ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));
            sum += (long) value * value;
        }
        return Math.sqrt(sum / (numBytesRead / 2.0));
    }

    private void saveToFile(byte[] audioData, String filename) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream audioStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());
        File outputFile = new File(filename);
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outputFile);
    }

}


