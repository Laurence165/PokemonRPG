package AdventureModel;

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

    /**
     * The list of items that the player is carrying at the moment.
     */
    public ArrayList<AdventureObject> inventory;

    /**
     * The list of pokemons the player is carrying at the moment.
     */
    public ArrayList<Pokemon> backpack;

    /**
     * Adventure Game Player Constructor
     */
    public Player(Room currentRoom) {
        this.inventory = new ArrayList<AdventureObject>();
        this.currentRoom = currentRoom;
        this.backpack = new ArrayList<>();
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
     * This method adds an object to the inventory of the player.
     *
     * @param object Prop or object to be added to the inventory.
     */
    public void addToInventory(AdventureObject object) {
        this.inventory.add(object);
    }


    /**
     * This method adds the pokemon in the backpack
     * .
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
     * Getter method for string representation of inventory.
     *
     * @return ArrayList of names of items that the player has.
     */
    public ArrayList<String> getInventory() {
        ArrayList<String> objects = new ArrayList<>();
        for(int i=0;i<this.inventory.size();i++){
            objects.add(this.inventory.get(i).getName());
        }
        return objects;
    }

    /**
     * Getter function for backpack list
     */
    public ArrayList<Pokemon> getBackpack(){
        return backpack;
    }

    /**
     * checkIfPokemonInBackpack
     * __________________________
     * This method checks to see if a Pokemon is in a player's backpack.
     *
     * @param s the name of the Pokemon
     * @return true if object is in backpack, false otherwise
     */
    public boolean checkIfPokemonInBackpack(String s) {
        for(int i = 0; i<this.backpack.size();i++){
            if(this.backpack.get(i).getName().equals(s)) return true;
        }
        return false;
    }


    /**
     * This method adds a Pokemon into players backpack if the Pokemon is present in
     * the room and returns true. If the Pokemon is not present in the room, the method
     * returns false.
     *
     * @param pokemon name of the pokemon to pick up
     * @return true if picked up, false otherwise
     */
    public boolean capturePokemon(String Pokemon){
        if(this.currentRoom.checkIfPokemonInRoom(Pokemon)){
            Pokemon object1 = this.currentRoom.getPokemon(Pokemon); // TODO: change return type of room.getObject
            // TODO: maybe rename room.getObject into room.getPokemon
            this.currentRoom.removePokemon(object1);
            this.addToBackpack(object1);
            return true;
        } else {
            return false;
        }
    }
    /**
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void releasePokemon(String s) {
        for(int i = 0; i<this.backpack.size();i++){
            if(this.backpack.get(i).getName().equals(s)) {
                this.currentRoom.addPokemon(this.backpack.get(i));
                this.backpack.remove(i);
            }
        }
    }



    public Moves get_move(Pokemon p){
        //TODO: can you implement this?
        // this method is called during the battle to get the move the user wants to make
        // should prompt the player in the text field
        // first ask if they want to pass or attack
        // if they want to pass then return new Moves("PASS", 0, 0)
        // otherwise, display the moves they can choose from THEY MUST HAVE ENOUGH ENERGY which depends on the argument p
        // use p.get_energy() to get energy and compare to each move which is in p.moves()
        // and return the one they choose

//        String n = AdventureGameView.getMoveEvent();
//
//        return new Moves("PASS", 0, 0);
    }

    public ArrayList<Pokemon> get_battle_pokemon(){
        //TODO: can you implement this?
        // this method is called at the beginning of the battle to get the player to choose the three pokemon they want to use in the battle
        // get the user to choose 3 from backpack and return the three they chose
        return null;
    }

}
