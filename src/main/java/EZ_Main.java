import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

public class EZ_Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        // Set path to acoustic model
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to dictionary
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        // Set path to language model
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        try {
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
            recognizer.startRecognition(true);

            System.out.println("Speech recognition is ready. Speak now!");

            while (true) {
                String spokenWords = recognizer.getResult().getHypothesis();
                if (spokenWords != null && !spokenWords.isEmpty()) {
                    System.out.println("You said: " + spokenWords);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}