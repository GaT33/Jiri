package ro.tuiasi;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecord {

    // path of the wav file
    File wavFile = new File(".\\src\\main\\java\\ro\\tuiasi\\MyRecord.wav");

    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    TargetDataLine line;

    // Silence threshold and duration
    private static final double SILENCE_THRESHOLD = 0.00001;
    private static final long MAX_SILENCE_DURATION = 1000; // in milliseconds

    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,channels, signed, bigEndian);
        return format;
    }

    void recordSound() {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, getAudioFormat());

        try {
            byte[] buffer = new byte[2048];
            boolean isRecording = true;
            long silenceStart = System.currentTimeMillis();

            //start capturing
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(getAudioFormat());
            line.start();
            System.out.println("Capturing started...");


            // start recording
            AudioInputStream ais = new AudioInputStream(line);
            System.out.println("Recording started...");
            AudioSystem.write(ais, fileType, wavFile);




            while (isRecording) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (isSilence(buffer, bytesRead)) {
                    if ((System.currentTimeMillis() - silenceStart) > MAX_SILENCE_DURATION) {
                        isRecording = false; // Stop recording if silence is detected for more than the max duration
                    }
                } else {
                    silenceStart = System.currentTimeMillis(); // Reset silence start time
                }
            }

            System.out.println("Silence detected, recording stopped.");
            line.stop();
            line.close();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isSilence(byte[] buffer, int bytesRead) {
        long sum = 0;
        for (int i = 0; i < bytesRead; i += 2) {
            int sample = buffer[i] + (buffer[i + 1] << 8); // Convert bytes to sample
            sum += sample * sample;
        }
        double rms = Math.sqrt(sum / (bytesRead / 2.0)); // Calculate RMS value
        return rms < SILENCE_THRESHOLD;
    }

}

