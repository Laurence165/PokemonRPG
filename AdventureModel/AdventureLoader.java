package AdventureModel;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class AdventureLoader. Loads an adventure from files.
 */
public class AdventureLoader {

    private AdventureGame game; //the game to return
    private String adventureName; //the name of the adventure

    /**
     * Adventure Loader Constructor
     * __________________________
     *
     * Initializes attribute
     * @param game the game that is loaded
     * @param directoryName the directory in which game files live
     */
    public AdventureLoader(AdventureGame game, String directoryName) {
        this.game = game;
        this.adventureName = directoryName;
    }

     /**
     * Load game from directory
     */
    public void loadGame() throws IOException {
        parseRooms();
        parsePokedex();
        parseSynonyms();
        parseOpponent();
        parsePokemonsInRoom();
        parseVillager();
        this.game.setHelpText(parseOtherFile("help"));
    }

     /**
     * Parse Rooms File
     */
    public void parseRooms() throws IOException {
        int roomNumber;

        String roomFileName = this.adventureName + "/pokeroom.txt";
        //fuck this
        BufferedReader buff = new BufferedReader(new FileReader(roomFileName));

        while (buff.ready()) {

            String currRoom = buff.readLine(); // first line is the number of a room

            roomNumber = Integer.parseInt(currRoom); //current room number

            // now need to get room name
            String roomName = buff.readLine();

            // now we need to get the description
            String roomDescription = "";
            String line = buff.readLine();
            while (!line.equals("-----")) {
                roomDescription += line + "\n";
                line = buff.readLine();
            }
            roomDescription += "\n";

            // now we make the room object
            Room room = new Room(roomName, roomNumber, roomDescription, adventureName);

            // now we make the motion table
            line = buff.readLine(); // reads the line after "-----"
            while (line != null && !line.equals("")) {
                String[] part = line.split(" \s+"); // have to use regex \\s+ as we don't know how many spaces are between the direction and the room number
                String direction = part[0];
                String dest = part[1];
                if (dest.contains("/")) {
                    String[] blockedPath = dest.split("/");
                    String dest_part = blockedPath[0];
                    String object = blockedPath[1];
                    Passage entry = new Passage(direction, dest_part, Integer.parseInt(object));
                    room.getMotionTable().addDirection(entry);
                } else {
                    Passage entry = new Passage(direction, dest);
                    room.getMotionTable().addDirection(entry);
                }
                line = buff.readLine();
            }
            this.game.getRooms().put(room.getRoomNumber(), room);
        }

    }

    /**
     * parsePokedex
     * __________________________
     *
     * Parses the Pokémon data from a text file and populates the Pokedex.
     * This method reads a specified file line by line, where each line represents a Pokemon with its attributes.
     * Attributes are expected to be comma-separated and in a specific order. The method creates Pokemon objects
     * from this data and adds them to the game's Pokedex. It also assigns moves to each Pokemon and sets their
     * image paths. This method assumes the file format is correct and does not handle potential formatting errors.
     *
     * @throws IOException If there is an error reading the file.
     */
    public void parsePokedex() throws IOException {
        String objectFileName = "AdventureModel/Pokemon.txt"; // File path for the Pokemon data
        BufferedReader buff = new BufferedReader(new FileReader(objectFileName)); // Prepare to read the file
        int i = 0; // Counter for Pokémon index
        while(buff.ready()){ // Read file line by line
            i++;
            String str = buff.readLine(); // Read a line representing one Pokemon
            List<String> pokeList = Arrays.asList(str.split(", ")); // Split attributes by comma

            // Create a new Pokémon object with parsed data
            Pokemon newPokemon = new Pokemon(pokeList.get(0), "","", pokeList.get(3),Integer.parseInt(pokeList.get(1)),Integer.parseInt(pokeList.get(2)), i);

            // Set additional Pokémon attributes
            newPokemon.name = pokeList.get(0);
            newPokemon.health = Integer.parseInt(pokeList.get(1));
            newPokemon.energy = Integer.parseInt(pokeList.get(2));
            newPokemon.type = pokeList.get(3);

            // Create and assign moves to the Pokémon
            Moves moves1 = new Moves(pokeList.get(4), Integer.parseInt(pokeList.get(5)), Integer.parseInt(pokeList.get(6))); //assign Pokémon's first move
            Moves moves2 = new Moves(pokeList.get(7), Integer.parseInt(pokeList.get(8)), Integer.parseInt(pokeList.get(9))); //assign Pokémon's first move
            newPokemon.moves.put(1, moves1);
            newPokemon.moves.put(2, moves2);

            // Set the image path for the Pokémon
            newPokemon.image_address = "AdventureModel/pokemon_images/"+Integer.toString(i)+".png";

            // Add the new Pokémon to the Pokedex
            this.game.getPokedex().put(i, newPokemon);
        }

    }

    /**
     * Parse Opponent
     *
     */
    public void parseOpponent()  throws IOException{
        String npcFileName = this.adventureName + "/opponents.txt";
        BufferedReader buff = new BufferedReader(new FileReader(npcFileName));

        buff.readLine();
        buff.readLine();

        //j is index value
        int j = 0;
        while (buff.ready()){

            String name = buff.readLine();
            String description = buff.readLine();

            //reading phrases
            int numOfPhrases = Integer.parseInt((buff.readLine()));
            String[] phrases = new String[numOfPhrases];
            ArrayList<Pokemon> pokemons = new ArrayList<>();
            for (int i = 0; i < phrases.length; i++){
                phrases[i] = buff.readLine();
            }
            buff.readLine();

            //reading pokemons
            int numOfPokemons = Integer.parseInt(buff.readLine());
            for (int i=0; i< numOfPokemons; i++){
                pokemons.add(clonePokemon(Integer.parseInt(buff.readLine())));
            }
            this.game.getOpponents().put(j, new Opponent(name, description,"",phrases,pokemons,j));
            buff.readLine();
        }
    }
    /**
     * parseVillager
     * __________________________
     *
     * Parses the villager data from a text file and populates the game with villagers.
     * The method reads a specified file to create villager objects with attributes such as name,
     * description, location, and spoken phrases. It also determines whether a villager gives a Pokemon to the player.
     * The villagers are then placed in their respective rooms within the game.
     * This method assumes a specific file format where each villager's details are laid out in a structured manner.
     * Note: It's important that the file format is adhered to, as the method does not handle potential formatting errors.
     *
     * @throws IOException If there is an error reading the file.
     */
    public void parseVillager()  throws IOException{
        String npcFileName = this.adventureName + "/villagers.txt"; // File path for the villagers data
        BufferedReader buff = new BufferedReader(new FileReader(npcFileName));

        // Skip the first two lines (usually headers or metadata)
        buff.readLine();
        buff.readLine();

        int j = 0; // Index for villagers
        while (buff.ready()){
            // Read villager attributes line by line
            String name = buff.readLine();
            String description = buff.readLine();
            String locationStr = buff.readLine();
            Room location = this.game.getRooms().get(Integer.parseInt(locationStr)); // Convert location string to Room object

            // Check if the villager gives a Pokemon
            String gives = buff.readLine();
            Boolean givesPokemon = false;
            Pokemon pokemon = null;
            if ("true".equals(gives)){
                givesPokemon = true;
                pokemon = clonePokemon(Integer.parseInt(buff.readLine())); // Clone the Pokemon for the villager
            }

            // Read villager phrases
            int numOfPhrases = Integer.parseInt(buff.readLine());
            String[] phrases = new String[numOfPhrases];
            for (int i = 0; i < phrases.length; i++){
                phrases[i] = buff.readLine();
            }
            buff.readLine(); // Read and discard a line, usually a separator

            // Create the Villager object
            Villager v = new Villager(name, description, "Games/TinyGame/Images/Villager-images/" + j + ".png", phrases, givesPokemon, pokemon, location, j);

            // Place the villager in the room and add to the game's villager list
            location.villagerInRoom = v;
            this.game.getVillagers().put(j, v);

            j++; // Increment the index for the next villager
        }
    }


    //parse pokemon

    /**
     * parsePokemonsInRoom
     * --------------------------
     *
     * Parses the information about Pokémon locations from a text file and places them in respective rooms.
     * This method reads a specified file, where each line contains data about a Pokémon and its location.
     * The method processes this data to place Pokémon in the correct rooms within the game.
     * The format of each line in the file is expected to be 'pokemonID,roomID'. The method assumes
     * the file format is correct and does not handle potential formatting errors.
     *
     * @throws IOException If there is an error reading the file.
     */
    public void parsePokemonsInRoom() throws IOException{
        String fileName = this.adventureName + "/pokemon_locations.txt"; // File path for the Pokemon locations data
        BufferedReader buff = new BufferedReader(new FileReader(fileName));

        // Skip the first two lines (usually headers or metadata)
        buff.readLine();
        buff.readLine();

        while(buff.ready()){
            String line = buff.readLine(); // Read a line representing one Pokémon's location
            String[] s = line.split(","); // Split the line into Pokémon ID and Room ID

            // Place the Pokémon in the specified room
            this.game.getRooms().get(Integer.parseInt(s[1])).addPokemon(clonePokemon(Integer.parseInt(s[0])));
        }
    }

    /**
     * clonePokemon
     * -------------------------
     *
     * Creates a clone of a specified Pokémon from the Pokedex.
     * This method is used to generate a duplicate of a Pokémon based on its index in the Pokedex.
     * It is useful in scenarios where a new instance of a Pokémon is needed, separate from the one stored in the Pokedex.
     * The method assumes that the Pokémon object implements Cloneable and handles the CloneNotSupportedException.
     *
     * @param i The index of the Pokémon in the Pokedex to be cloned.
     * @return A new Pokémon object that is a clone of the specified Pokémon.
     * @throws RuntimeException If the Pokémon cannot be cloned (typically due to not supporting cloning).
     */
    public Pokemon clonePokemon(int i) {
        try {
            // Attempt to clone the specified Pokemon from the Pokedex
            return this.game.getPokedex().get(i).clone();
        }
        catch (CloneNotSupportedException ignored){
            // Handle the case where the Pokemon cannot be cloned
            System.out.println("Error cloning Pokemon");
            throw new RuntimeException("Pokemon cloning not supported");
        }
    }

    /**
     * parseSynonyms
     * -------------------------
     *
     * Parses synonyms for commands from a text file and populates the game's synonyms map.
     * This method reads a specified file line by line. Each line contains a pair of synonymous commands,
     * separated by an equals sign. The method splits these pairs and adds them to the game's synonyms map,
     * enabling the game to recognize and process different words or phrases as equivalent commands.
     * The method assumes the file format is correct and does not handle potential formatting errors.
     *
     * @throws IOException If there is an error reading the file.
     */
    public void parseSynonyms() throws IOException {
        String synonymsFileName = this.adventureName + "/synonyms.txt"; // File path for the synonyms data
        BufferedReader buff = new BufferedReader(new FileReader(synonymsFileName));

        String line = buff.readLine(); // Read the first line from the file
        while(line != null){
            // Split each line into two parts: the command and its synonym
            String[] commandAndSynonym = line.split("=");

            // Add the command and its synonym to the game's synonyms map
            this.game.getSynonyms().put(commandAndSynonym[0], commandAndSynonym[1]);

            line = buff.readLine(); // Read the next line
        }
    }



    /**
     * Parse Files other than Rooms, Objects and Synonyms
     *
     * @param fileName the file to parse
     */
    public String parseOtherFile(String fileName) throws IOException {
        String text = "";
        fileName = this.adventureName + "/" + fileName + ".txt";
        BufferedReader buff = new BufferedReader(new FileReader(fileName));
        String line = buff.readLine();
        while (line != null) { // while not EOF
            text += line+"\n";
            line = buff.readLine();
        }
        return text;
    }

}
