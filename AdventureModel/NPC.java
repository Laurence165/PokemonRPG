package AdventureModel;

import java.lang.*;

/**
 * Represents a non-playable character (NPC) in an adventure game.
 * This class extends the Labelled class, inheriting properties like name, description, and image address.
 * It adds functionality specific to NPCs, such as handling dialogues with predefined phrases.
 */
public class NPC extends Labelled {
    // Array of phrases that the NPC can say
    private String[] phrases;

    // Counter to keep track of the current phrase
    private int phrase_count;

    /**
     * Constructs an NPC with the given parameters.
     * Initializes the NPC with a set of phrases for dialogue.
     *
     * @param name The name of the NPC.
     * @param description The description of the NPC.
     * @param image_address The image address of the NPC.
     * @param phrases An array of strings representing the phrases the NPC can say.
     * @param index The index or identifier of the NPC.
     */
    public NPC(String name, String description, String image_address, String[] phrases, int index){
        super(name, description, image_address, index); // Call the constructor of the superclass Labelled
        this.phrases = phrases;
        this.phrase_count = 0; // Initialize phrase count to 0
    }

    /**
     * Outputs the next phrase in the NPC's dialogue sequence.
     * Cycles back to the first phrase after the last one is reached.
     * If the NPC is an instance of Villager, it triggers the villager to give a Pokemon.
     *
     * @return The next phrase in the NPC's dialogue.
     */
    public String talk() {
        String out = phrases[phrase_count]; // Get the current phrase
        phrase_count++; // Increment the phrase counter
        System.out.println(out); // Output the phrase

        // Reset the phrase counter and trigger additional actions for villagers
        if (phrase_count == phrases.length) {
            phrase_count = 0; // Reset counter to start phrases again
            if (this instanceof Villager) {
                ((Villager) this).give_pokemon(); // Trigger specific action for Villager type NPCs
            }
        }
        return out; // Return the current phrase
    }
}
