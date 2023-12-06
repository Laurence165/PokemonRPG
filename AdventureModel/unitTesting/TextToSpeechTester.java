package AdventureModel.unitTesting;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.junit.jupiter.api.Test;

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
        new TextToSpeechTester("You are standing at the end of a road before a small brick building.  A small stream flows out of the building and down a gully to the south.  A road runs up a small hill to the west. You are standing at the end of a road before a small brick building. \n  A small stream flows out of the building and down a gully to the south.  A road runs up a small hill to the west.");
    }
}