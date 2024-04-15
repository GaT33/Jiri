package ro.tuiasi;

public class Main {
    public static void main(String[] args) {
        AudioRecorder recorder = new AudioRecorder();
        recorder.startRecording("Record.wav");

        // Record 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recorder.stopRecording();
        System.out.println("Recording stopped.");
    }
}
