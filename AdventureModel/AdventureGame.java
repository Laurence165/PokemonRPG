package AdventureModel;

import views.AdventureGameView;

import java.io.*;
import java.util.*;


/**
 * The AdventureGame class represents the core of an adventure game.
 * It manages the game state, including rooms, players, items, and NPCs.
 * This class handles game initialization, user commands processing,
 * and saving/loading game states.
 *
 * @author {@author}
 */
public class AdventureGame implements Serializable {
    // Directory name for game files
    private final String directoryName;

    // Help text displayed for the "HELP" command
    private String helpText;

    // Map of all rooms in the game
    private final HashMap<Integer, Room> rooms;

    // Map of synonyms for commands
    private HashMap<String,String> synonyms;

    // Array of action verbs for commands
    private final String[] actionVerbs = {"QUIT","INVENTORY","TAKE","DROP"};

    // Player instance
    public Player player;

    // Map of all available Pokemon in the game
    private final HashMap<Integer, Pokemon> pokedex;

    // Map of all villagers in the game
    private final HashMap<Integer, Villager> villagers;

    // Map of all opponents in the game
    private final HashMap<Integer, Opponent> opponents;

    // View component for the game
    private AdventureGameView view;

    // Passage used for returning from a battle or similar event
    private Passage returnPassage;

    private AdventureMap adventureMap;

    /**
     * Constructs an AdventureGame instance with the specified game name.
     * Initializes game components and loads game data.
     *
     * @param name The name of the adventure game.
     */
    public AdventureGame(String name){
        this.synonyms = new HashMap<>();
        this.rooms = new HashMap<>();
        this.pokedex = new HashMap<>();
        this.villagers = new HashMap<>();
        this.opponents = new HashMap<>();
        this.directoryName = "Games/" + name; //all games files are in the Games directory!
        try {
            setUpGame();
        } catch (IOException e) {

            throw new RuntimeException("An Error Occurred: " + e.getMessage());
        }
    }

    /**
     * saveModel
     * __________________________
     *
     * Save the current state of the game to a file
     *
     * @param file pointer to file to write to
     */
    public void saveModel(File file) {
        try {
            FileOutputStream outfile = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(outfile);
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setUpGame
     * __________________________
     *
     * @throws IOException in the case of a file I/O error
     */
    public void setUpGame() throws IOException {
        // Load game data from the specified directory
        String directoryName = this.directoryName;
        AdventureLoader loader = new AdventureLoader(this, directoryName);
        loader.loadGame();

        // Initialize the player's starting location
        this.player = new Player(this.rooms.get(1));
        this.adventureMap = new AdventureMap(this.rooms.get(1));
    }

    /**
     * tokenize
     * __________________________
     *
     * Tokenizes the input string from the command line.
     * Converts the input to uppercase, splits it into words, and replaces any synonyms
     * with their corresponding commands based on the synonyms map. This method
     * facilitates command processing by standardizing and simplifying the input.
     *
     * @param input The command line input string.
     * @return An array of strings, each representing a token from the input command.
     */
    public String[] tokenize(String input) {
        input = input.toUpperCase(); // Convert input to upper case for standardization
        String[] commandArray = input.split(" "); // Split input into words

        // Replace synonyms with actual commands
        for (int i = 0; i < commandArray.length; i++) {
            if(this.synonyms.containsKey(commandArray[i])) {
                commandArray[i] = this.synonyms.get(commandArray[i]);
            }
        }
        return commandArray;
    }


    /**
     * movePlayer
     * __________________________
     *
     * Moves the player in a specified direction if possible.
     * This method checks the current room's motion table for available directions and
     * handles the logic of moving the player to a new room or initiating a battle sequence.
     * Blocked passages are checked first. If a passage is blocked, and the player has
     * sufficient Pokemon for a battle, a battle is initiated. Otherwise, the player moves
     * to the new room if the direction is valid. The method also handles forced moves,
     * where the player is automatically moved to a new room without a command.
     *
     * @param direction The direction in which the player intends to move.
     * @return A string indicating the outcome of the move attempt. Possible values include
     *         "BATTLE" if a battle is initiated, "true" if the move is successful,
     *         "false" if the move results in a game-ending condition, or other specific
     *         messages for various scenarios.
     */
    public String movePlayer(String direction) {
        direction = direction.toUpperCase(); // Normalize direction to uppercase
        PassageTable motionTable = this.player.getCurrentRoom().getMotionTable(); // Retrieve motion table of current room

        // Check if the given direction is not available in the current room's motion table
        if (!motionTable.optionExists(direction)) {
            return "true"; // Return 'true' indicating no move possible in this direction
        }

        ArrayList<Passage> possibilities = new ArrayList<>();
        // Iterate through passages to find all possible moves in the given direction
        for (Passage entry : motionTable.getDirection()) {
            if (entry.getDirection().equals(direction)) {
                possibilities.add(entry); // Add possible passage to the list
            }
        }

        Passage chosen = null;
        // Try blocked passages first to trigger any potential battles
        for (Passage entry : possibilities) {
            if (chosen == null && entry.getIsBlocked()) {
                if (this.player.getBackpack().size() < 3) {
                    this.view.formatText("You do not have enough Pokemon to battle. You must have at least 3 Pokemon to battle.");
                    return "BATTLE"; // Insufficient Pokemon, prompt for battle
                }

                // Initialize battle with opponent
                Integer oNum = entry.getOpponent();
                Opponent o = this.opponents.get(oNum - 1);
                ArrayList<Pokemon> o_pokemon = o.get_battle_pokemon();
                Battle B = new Battle(this.view, this, this.player, o, o_pokemon);
                this.returnPassage = entry;
                B.battleInit();
                return "BATTLE";
            } else {
                chosen = entry; // If passage is not blocked, choose this passage
            }
        }

        if (chosen == null) {
            return "true"; // No available passage in the given direction
        }

        // Move player to new room if passage is available
        int roomNumber = chosen.getDestinationRoom();
        Room room = this.rooms.get(roomNumber);
        this.player.setCurrentRoom(room);
        this.adventureMap.visit(room);

        // Check for forced movement
        boolean force = !this.player.getCurrentRoom().getMotionTable().getDirection().get(0).getDirection().equals("FORCED");
        if (force) {
            return "true"; // No forced movement, normal move
        }
        return "false"; // Forced movement detected
    }

    /**
     * resumeMovePlayer
     * __________________________
     *
     * Resumes the movement of the player after a pause in gameplay, such as after a battle.
     * This method is called to continue the player's movement to a new room if they won the battle,
     * or to handle the game state if they lost. It ensures the game's continuity and logical progression
     * following events that temporarily interrupt the player's journey.
     *
     * Note: This method assumes that the battle does not directly lead to a game-ending scenario
     * (like death or victory). It only handles the resumption of normal gameplay.
     *
     * @param won A boolean indicating whether the player won the battle (true) or not (false).
     */
    public void resumeMovePlayer(boolean won) {
        // If the player won the battle, proceed to move them to the intended destination
        if (won) {
            Passage chosen = this.returnPassage; // Get the passage where the player was heading
            int roomNumber = chosen.getDestinationRoom();
            Room room = this.rooms.get(roomNumber);
            this.player.setCurrentRoom(room); // Update the player's current room
        } else {
            // If the player did not win the battle, update the game view accordingly
            this.view.updateScene("");
            // Additional logic can be added here to handle the scenario where the player loses the battle
        }
    }


    /**
     * interpretAction
     * __________________________
     *
     * interpret the user's action.
     * @param command String representation of the command.
     */
    public String interpretAction(String command){

        String[] inputArray = tokenize(command); //use tokenize to look up for synonyms

        PassageTable motionTable = this.player.getCurrentRoom().getMotionTable(); //get the motion table to check possible directions

        //check if the possible direction exist
        if (motionTable.optionExists(inputArray[0])) {
            String mp = movePlayer(inputArray[0]);
            if (mp == "BATTLE"){
                return mp;
            }else if (mp == "false") {
                //check if the game is over
                if (this.player.getCurrentRoom().getMotionTable().getDirection().get(0).getDestinationRoom() == 0)
                    return "GAME OVER";
                else return "FORCED";
            } //something is up here! We are dead or we won.
            return null;
        } else if(Arrays.asList(this.actionVerbs).contains(inputArray[0])) {
            //in this case, we found the synonym !

            if(inputArray[0].equals("QUIT")) { return "GAME OVER"; } //game over!
            else if(inputArray[0].equals("TAKE") && inputArray.length < 2) return "THE TAKE COMMAND REQUIRES AN OBJECT"; //ask user's for Pokémon name for capturePokemon
            else if(inputArray[0].equals("DROP") && inputArray.length < 2) return "THE DROP COMMAND REQUIRES AN OBJECT"; //ask user's for Pokémon name for releasePokemon
            else if(inputArray[0].equals("TAKE") && inputArray.length == 2) {
                if(this.player.getCurrentRoom().checkIfPokemonInRoom(inputArray[1])) {
                    //if the Pokémon was founded in the room, catch the Pokémon
                    this.player.capturePokemon(inputArray[1]);
                    return "YOU HAVE TAKEN:\n " + inputArray[1];
                } else {
                    //cannot find the Pokémon in room
                    return "THIS OBJECT IS NOT HERE:\n " + inputArray[1];
                }
            }
            else if(inputArray[0].equals("DROP") && inputArray.length == 2) {
                //if the Pokémon was founded in the inventory, release the Pokémon
                if(this.player.checkIfPokemonInBackpack(inputArray[1])) {
                    this.player.releasePokemon(inputArray[1]);
                    return "YOU HAVE DROPPED:\n " + inputArray[1];
                } else {
                    //cannot find the Pokémon in player's inventory
                    return "THIS OBJECT IS NOT IN YOUR INVENTORY:\n " + inputArray[1];
                }
            }
        }
        return "INVALID COMMAND.";
    }


    /**
     * getDirectoryName
     * __________________________
     *
     * Getter method for directory
     * @return directoryName
     */
    public String getDirectoryName() {
        return this.directoryName;
    }

    /**
     * getInstructions
     * __________________________
     *
     * Getter method for instructions
     * @return helpText
     */
    public String getInstructions() {
        return helpText;
    }

    /**
     * getPlayer
     * __________________________
     *
     * Getter method for Player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * getRooms
     * __________________________
     *
     * Getter method for rooms
     * @return map of key value pairs (integer to room)
     */
    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    public AdventureMap getAdventureMap(){return this.adventureMap;}

    /**
     * getSynonyms
     * __________________________
     *
     * Getter method for synonyms
     * @return map of key value pairs (synonym to command)
     */
    public HashMap<String, String> getSynonyms() {
        return this.synonyms;
    }
    /**
     * getPokedex
     * __________________________
     *
     * Getter method for Pokedex
     * @return map of key value pairs (index to Pokemon)
     */
    public HashMap<Integer, Pokemon> getPokedex() {return this.pokedex;}

    /**
     * getVillageers
     * __________________________
     *
     * Getter method for Villagers
     * @return map of key value pairs (index to Villager)
     */
    public HashMap<Integer, Villager> getVillagers() {return this.villagers;}

    /**
     * getOpponents
     * __________________________
     *
     * Getter method for Opponents
     * @return map of key value pairs (index to Opponent)
     */
    public HashMap<Integer, Opponent> getOpponents() {return this.opponents;}

    /**
     * setHelpText
     * __________________________
     *
     * Setter method for helpText
     * @param help which is text to set
     */
    public void setHelpText(String help) {
        this.helpText = help;
    }

    /**
     * setView
     * __________________________
     *
     * Setter method for helpText
     * @param v which is view to set
     */
    public void setView (AdventureGameView v){

        this.view = v;
        this.player.set_view(v);
    }



}