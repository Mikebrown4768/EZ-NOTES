import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EZ_Main {
    public static void main(String[] args) throws Exception {
        // Replace "path/to/audio/file.wav" with the path to your audio file
        String audioFilePath = "path/to/audio/file.wav";

        // Replace "your-google-cloud-api-key" with your actual API key
        String apiKey = "your-google-cloud-api-key";

        // Create a SpeechClient using your API key
        try (SpeechClient speechClient = SpeechClient.create(SpeechSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream("path/to/your/json/keyfile.json")))).build())) {

            // Read the audio file
            Path path = Paths.get(audioFilePath);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Build the recognition request
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Perform the speech recognition
            RecognizeResponse response = speechClient.recognize(config, audio);

            // Get and print the recognized text
            for (SpeechRecognitionResult result : response.getResultsList()) {
                System.out.println("Transcript: " + result.getAlternativesList().get(0).getTranscript());
            }
        }
    }
}