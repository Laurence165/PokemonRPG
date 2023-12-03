package AdventureModel;

import java.lang.*;

public class NPC extends Labelled{
    private String phrases[];
    private int phrase_count;
    private String image_address, name, audio_address,description;

    public NPC(String name, String description, String image_address, String[] phrases, int index){
        super(name, description,image_address,index);
        this.phrases = phrases;
        this.phrase_count = 0;
    }

    //prints out the next phrase
    public String talk()
    {


        String out = phrases[phrase_count];
        phrase_count++;
        System.out.println(out);
        //If there is a next phrase, print it otherwise start from the first phrase again
        if (phrase_count == phrases.length){
            phrase_count = 0;
            if (this instanceof Villager){
                ((Villager) this).give_pokemon();
            }
        }
        return out;

    }
}
