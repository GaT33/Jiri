package ro.tuiasi;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.sound.sampled.*;
import java.io.*;

public class AudioRecorder {

    AudioFormat format = new AudioFormat(44100.00F, 16, 2, true, false);

    TargetDataLine microphone;

    Thread recordingThread;


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
        try {
            microphone.start();
            recordingThread = new Thread(() -> {
                try (AudioInputStream audioStream = new AudioInputStream(microphone)) {
                    File wavFile = new File(filename);
                    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            recordingThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stopRecording() {
        microphone.stop();
        microphone.close();
        try {
            recordingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
