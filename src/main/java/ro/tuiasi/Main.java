package ro.tuiasi;

public class Main {
    public static void main(String[] args) {
        AudioRecorder recorder = new AudioRecorder();
        System.out.println("Recording started.");
        recorder.startRecording("AudioRecord.wav");

        try {
            recorder.recordingThread.join();  // wait for thread to stop
        } catch (InterruptedException e) {
            System.out.println("Recording interrupted.");
        }
        System.out.println("Recording stopped and saved to your file.");
    }
}

