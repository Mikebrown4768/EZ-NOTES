import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class EZ_Class1 {

    public static void main(String[] args) {
    	 String pathToJsonKeyFile = "/Users/jannetzane/Downloads/eznotes51-0588565a2196.json";
         System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", pathToJsonKeyFile);  

        try (SpeechClient speechClient = SpeechClient.create()) {
            // Create a thread for audio capture
            Thread audioCaptureThread = new Thread(() -> {
                try {
                    captureAndRecognize(speechClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Start the audio capture thread
            audioCaptureThread.start();

            // You can perform other tasks in the main thread while audio is being captured
            // For example, wait for a certain condition before exiting

            // Join the audio capture thread to wait for it to finish
            audioCaptureThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void captureAndRecognize(SpeechClient speechClient) throws Exception {
        // Set up audio format for microphone
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        // Check if the microphone is supported
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Microphone is not supported.");
            return;
        }

        // Open the microphone
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        System.out.println("Listening for speech...");

        // Set up the recognition config
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000)
                .setLanguageCode("en-US")
                .build();

        while (true) {
            // Capture audio from the microphone
            byte[] data = new byte[1024];
            int bytesRead = line.read(data, 0, data.length);

            // Skip empty frames
            if (bytesRead > 0) {
                ByteString audioBytes = ByteString.copyFrom(data, 0, bytesRead);
                RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

                // Perform speech recognition on the captured audio
                RecognizeResponse response = speechClient.recognize(config, audio);
                List<SpeechRecognitionResult> results = response.getResultsList();

                for (SpeechRecognitionResult result : results) {
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    System.out.println("Transcription: " + alternative.getTranscript());
                }
            }
        }
    }
}
