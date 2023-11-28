package AdventureModel;

import java.util.ArrayList;
import java.util.HashMap;

public class Opponent extends NPC{

    //
    public Opponent(String name, String description, String image_address, String audio_address, String[] phrases) {
        super(name, description, image_address, audio_address, phrases);
    }

    public ArrayList<Pokemon> select_Pokemon(){
        //
    }
    public Moves attack(Pokemon active){

    }

    public static HashMap<Integer,Opponent> parseOpp(){
        //parse from opponent.txt
        //return map with index and opponent object.
        // copy paste stuff from AdventrueLoader.java the method is called parseRooms()
        //fileName = this.adventureName + ""
    }

}
