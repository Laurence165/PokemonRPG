package AdventureModel;

import java.util.ArrayList;
import java.util.HashMap;

public class Opponent extends NPC{

    //
    public Opponent(String name, String description, String image_address, String audio_address, String[] phrases) {
        super(name, description, image_address, audio_address, phrases);
    }

    public ArrayList<Pokemon> get_battle_pokemon(){
        //
    }
    public Moves get_move(Pokemon active){
        // I renamed this method because it's the one I used in Battle class, I think it is what you meant
        // TODO: return random move that the pokemon has energy for, if no energy for anything then return new Moves("PASS", 0, 0);

        return new Moves("PASS", 0, 0);
    }

    public static HashMap<Integer,Opponent> parseOpp(){
        //parse from opponent.txt
        //return map with index and opponent object.
        // copy paste stuff from AdventureLoader.java the method is called parseRooms()
        //fileName = this.adventureName + ""
    }

}
