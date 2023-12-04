package AdventureModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class contains the information about a
 * room in the Adventure Game.
 */
public class Room implements Serializable {

    private final String adventureName;
    /**
     * The number of the room.
     */
    private int roomNumber;

    /**
     * The name of the room.
     */
    private String roomName;

    /**
     * The description of the room.
     */
    private String roomDescription;

    /**
     * The passage table for the room.
     */
    private PassageTable motionTable = new PassageTable();

    /**
     * The list of objects in the room.
     */
    public Villager villagerInRoom;

    /**
     * The list of Pokemons in the room.
     */
    public ArrayList<Pokemon> pokemonsInRoom = new ArrayList<>();

    /**
     * A boolean to store if the room has been visited or not
     */
    private boolean isVisited;

    /**
     * AdvGameRoom constructor.
     *
     * @param roomName: The name of the room.
     * @param roomNumber: The number of the room.
     * @param roomDescription: The description of the room.
     */
    public Room(String roomName, int roomNumber, String roomDescription, String adventureName){
        this.roomName = roomName;
        this.roomNumber = roomNumber;
        this.roomDescription = roomDescription;
        this.adventureName = adventureName;
        this.isVisited = false;
        this.villagerInRoom = null;
    }


    /**
     * Returns a comma delimited list of every
     * object's description that is in the given room,
     * e.g. "a can of tuna, a beagle, a lamp".
     *
     * @return delimited string of object descriptions
     */

    /**
     * Returns a comma delimited list of every
     * move that is possible from the given room,
     * e.g. "DOWN, UP, NORTH, SOUTH".
     *
     * @return delimited string of possible moves
     */

    public String getObjectString() {
        String s = "";
        if (this.pokemonsInRoom.size() == 0 && this.villagerInRoom == null){ return s; }
        s += "\nWild Pokemons:\n";
        for (Pokemon o: this.pokemonsInRoom){
            s += (o.getName()+", ");
        }
        s = s.substring(0,s.length()-2);
        if (this.villagerInRoom!=null){

            s += "\nVillagers:\n" + this.villagerInRoom.getName();
        }
        return s;
    }

    public String getCommands() {

        // initiating a stringBuilder to add the directions
        StringBuilder n = new StringBuilder();

        //get the table for getting directions
        PassageTable t = getMotionTable();

        // adding the possible moves from a room
        for (Passage d : t.getDirection()){
            n.append(d.getDirection()).append(",");

        }

        return n.toString();
    }

    /**
     * This method adds a game object to the room.
     *
     * @param object to be added to the room.
     */
    public void addGameObject(Labelled object){
        if (object instanceof Villager){
            this.villagerInRoom = (Villager)(object);
        }
        else if (object instanceof Pokemon){
            this.pokemonsInRoom.add((Pokemon) object);
        }
    }

    /**
     * This method removes a game object from the room.
     *
     * @param object to be removed from the room.
     */
    public void removeGameObject(Labelled object){
        if (object instanceof Villager){
            this.villagerInRoom = null;
        }
        else if (object instanceof Pokemon){
            this.pokemonsInRoom.remove(object);
        }
    }

    /**
     * This method checks if an object is in the room.
     *
     * @param objectName Name of the object to be checked.
     * @return true if the object is present in the room, false otherwise.
     */
    public boolean checkIfVillagerInRoom(){
        if (villagerInRoom!=null){
            return true;
        }
        return false;
    }

    public Villager getVillagerInRoom() {
        return villagerInRoom;
    }

    /**
     * Sets the visit status of the room to true.
     */
    public void visit(){
        isVisited = true;
    }

    /**
     * Getter for returning an AdventureObject with a given name
     *
     * @param objectName: Object name to find in the room
     * @return: AdventureObject
     */
    public Pokemon getPokemon(String objectName){
        for(int i = 0; i<pokemonsInRoom.size();i++){
            if(this.pokemonsInRoom.get(i).getName().equalsIgnoreCase(objectName)) return this.pokemonsInRoom.get(i);
        }
        return null;
    }

    /**
     * Getter method for the number attribute.
     *
     * @return: number of the room
     */
    public int getRoomNumber(){
        return this.roomNumber;
    }

    /**
     * Getter method for the description attribute.
     *
     * @return: description of the room
     */
    public String getRoomDescription(){
        return this.roomDescription.replace("\n", " ");
    }


    /**
     * Getter method for the name attribute.
     *
     * @return: name of the room
     */
    public String getRoomName(){
        return this.roomName;
    }


    /**
     * Getter method for the visit attribute.
     *
     * @return: visit status of the room
     */
    public boolean getVisited(){
        return this.isVisited;
    }


    /**
     * Getter method for the motionTable attribute.
     *
     * @return: motion table of the room
     */
    public PassageTable getMotionTable(){
        return this.motionTable;
    }

    /**
     * This method adds a Pokemon tos the room.
     *
     * @param p to be added to the room.
     */
    public void addPokemon(Pokemon p){
        this.pokemonsInRoom.add(p);
    }

    /**
     * This method removes a Pokemon from the room.
     *
     * @param p to be removed from the room.
     */
    public void removePokemon(Pokemon p){
        this.pokemonsInRoom.remove(p);
    }

    /**
     * This method checks if an object is in the room.
     *
     * @param pokemonName Name of the object to be checked.
     * @return true if the object is present in the room, false otherwise.
     */
    public boolean checkIfPokemonInRoom(String pokemonName){
        for(int i = 0; i<pokemonsInRoom.size();i++){
            if(this.pokemonsInRoom.get(i).getName().equalsIgnoreCase(pokemonName)) return true;
        }
        return false;
    }


}
