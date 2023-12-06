package AdventureModel;

import views.AdventureGameView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public class Player implements Serializable, BattleColleagueInterface{
    /**
     * The current room that the player is located in.
     */
    private Room currentRoom;

    private AdventureGameView view;

    /**
     * The list of Pokémon the player is carrying at the moment.
     */
    public ArrayList<Pokemon> backpack;

    /**
     * The list of pokemons the player chooses to battle with
     */
    public ArrayList<Pokemon> playerBattlePokemon = new ArrayList<>();
    ArrayList<Pokemon> pokemonOptions;

    /**
     * Adventure Game Player Constructor
     */
    public Player(Room currentRoom) {
        this.currentRoom = currentRoom;
        this.backpack = new ArrayList<>();
        this.pokemonOptions = new ArrayList<>(this.backpack);
    }

    /**
     * Setter method for the current room view.
     *
     * @param v:  the view which will be set.
     */
    public void set_view(AdventureGameView v){
        this.view = v;
    }

    /**
     * Setter method for the current room attribute.
     *
     * @param currentRoom The location of the player in the game.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }


    /**
     * This method adds the Pokémon in the backpack
     */
    public void addToBackpack(Pokemon p){
        this.backpack.add(p);
    }


    /**
     * Getter method for the current room attribute.
     *
     * @return current room of the player.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Getter method for backpack list
     */
    public ArrayList<Pokemon> getBackpack(){
        return backpack;
    }

    /**
     * Getter method for Pokemon options list
     */
    public ArrayList<Pokemon> getPokemonOptions(){
        return this.pokemonOptions;
    }

    /**
     * checkIfPokemonInBackpack
     * __________________________
     *
     * This method checks to see if a Pokémon is in a player's backpack.
     *
     * @param s the name of the Pokémon
     * @return true if object is in backpack, false otherwise
     */
    public boolean checkIfPokemonInBackpack(String s) {
        for(int i = 0; i<this.backpack.size();i++){
            if(this.backpack.get(i).getName().equalsIgnoreCase(s)) return true;
        }
        return false;
    }


    /**
     *  capturePokemon
     *  __________________________
     *
     * This method adds a Pokémon into players backpack if the Pokémon is present in
     * the room and returns true. If the Pokémon is not present in the room, the method
     * returns false.
     *
     * @param Pokemon name of the Pokémon to pick up
     * @return true if picked up, false otherwise
     */
    public boolean capturePokemon(String Pokemon){
        if(this.currentRoom.checkIfPokemonInRoom(Pokemon)){
            Pokemon object1 = this.currentRoom.getPokemon(Pokemon); // TODO: change return type of room.getObject
            this.currentRoom.removePokemon(object1);
            this.addToBackpack(object1);
            return true;
        } else {
            return false;
        }
    }
    /**
     * releasePokemon
     * --------------------------
     *
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void releasePokemon(String s) {
        for(int i = 0; i<this.backpack.size();i++){
            //check if the Pokémon existed in player's backpack
            if(this.backpack.get(i).getName().equalsIgnoreCase(s)) {
                //release Pokémon from the backpack to the current room
                this.currentRoom.addPokemon(this.backpack.get(i));
                this.backpack.remove(i);
            }
        }
    }

    /**
     * get_move
     * __________________________
     *
     * This method return the moves of the Pokémon
     *
     * @param p Pokémon which we will get the moves from
     */
    public Moves get_move(Pokemon p){
        return new Moves("PASS", 0, 0);
    }
}
