package AdventureModel;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextToSpeechTester {

    private static final String VOICENAME_kevin = "kevin16";

    public TextToSpeechTester(String sayText) {
        Voice voice;
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICENAME_kevin);
        voice.allocate();

        voice.speak(sayText);
    }

    public static void main(String []args) {
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        new TextToSpeechTester("hello world 100");
    }
}