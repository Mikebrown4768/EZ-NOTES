import com.google.cloud.speech.v1.*;
import com.google.cloud.language.v1.*;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Organize_Class {

    public static void main(String... args) {
        String pathToJsonKeyFile = "/Users/jannetzane/Downloads/eznotes51-0588565a2196.json";
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", pathToJsonKeyFile);

        try (SpeechClient speechClient = SpeechClient.create();
             LanguageServiceClient languageServiceClient = LanguageServiceClient.create()) {

            
            String localFilePath = "/Users/jannetzane/Documents/GitHub/sphinx4/sphinx4-samples/src/main/resources/edu/cmu/sphinx/demo/speakerid/test.wav";
            byte[] audioContent = Files.readAllBytes(Paths.get(localFilePath));

            // Transcribe the audio
            String transcript = transcribeAudio(speechClient, audioContent);

            // Analyze sentiment of the text
            Sentiment sentiment = analyzeSentiment(languageServiceClient, transcript);

            // Analyze named entities
            List<Entity> entities = analyzeEntities(languageServiceClient, transcript);

            // Generate and display essay
            generateAndDisplayEssay(transcript, sentiment, entities);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String transcribeAudio(SpeechClient speechClient, byte[] audioContent) {
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000)
                .setLanguageCode("en-US")
                .build();

        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(audioContent))
                .build();

        RecognizeResponse response = speechClient.recognize(config, audio);
        List<SpeechRecognitionResult> results = response.getResultsList();

        StringBuilder transcriptBuilder = new StringBuilder();
        for (SpeechRecognitionResult result : results) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            transcriptBuilder.append(alternative.getTranscript()).append(" ");
        }

        return transcriptBuilder.toString().trim();
    }

    private static Sentiment analyzeSentiment(LanguageServiceClient languageServiceClient, String text) {
        Document document = Document.newBuilder()
                .setContent(text)
                .setType(Document.Type.PLAIN_TEXT)
                .build();

        return languageServiceClient.analyzeSentiment(document).getDocumentSentiment();
    }

    private static List<Entity> analyzeEntities(LanguageServiceClient languageServiceClient, String text) {
        Document document = Document.newBuilder()
                .setContent(text)
                .setType(Document.Type.PLAIN_TEXT)
                .build();

        return languageServiceClient.analyzeEntities(document).getEntitiesList();
    }

    private static void generateAndDisplayEssay(String transcript, Sentiment sentiment, List<Entity> entities) {
        System.out.println("Transcription: " + transcript);
        System.out.println("Sentiment: " + sentiment);

        System.out.println("Generated Essay:");

        // Introduction
        System.out.println("Introduction:");
        System.out.println("In the recorded speech, the speaker conveyed the following:");

        // Body
        System.out.println("\nBody:");
        if (sentiment.getScore() > 0.2) {
            System.out.println("The overall tone of the speech is positive. The speaker expressed optimistic views.");
        } else if (sentiment.getScore() < -0.2) {
            System.out.println("The overall tone of the speech is negative. The speaker conveyed concerns and challenges.");
        } else {
            System.out.println("The overall tone of the speech is neutral. The speaker presented information without strong sentiment.");
        }

        // Named Entities
        if (!entities.isEmpty()) {
            System.out.println("\nNamed Entities:");
            for (Entity entity : entities) {
                System.out.printf("Entity: %s, Type: %s%n", entity.getName(), entity.getType());
                // You can include more details or analysis based on the named entities.
            }
        }

        // Conclusion
        System.out.println("\nConclusion:");
        System.out.println("In summary, the transcribed speech covered various topics with a focus on...");

        // More custom logic and details will be added here.

        System.out.println(); // Add a newline for better readability
    }
}


