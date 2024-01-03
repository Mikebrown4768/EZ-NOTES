import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString; // Add this import statement

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Transcribe_File {

    /** Demonstrates using the Speech API to transcribe an audio file. */
    public static void main(String... args) throws Exception {
        // Instantiates a client

        String pathToJsonKeyFile = "/Users/jannetzane/Downloads/eznotes51-0588565a2196.json";
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", pathToJsonKeyFile);

        try (SpeechClient speechClient = SpeechClient.create()) {
            // The path to the local audio file to transcribe
            String localFilePath = "/Users/jannetzane/Documents/GitHub/sphinx4/sphinx4-samples/src/main/resources/edu/cmu/sphinx/demo/speakerid/test.wav"; // Update with your file path

            // Read the content of the audio file
            byte[] audioContent = Files.readAllBytes(Paths.get(localFilePath));

            // Builds the sync recognize request
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(audioContent)).build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech.
                // Use the first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s%n", alternative.getTranscript());
            }
        }
    }
}
