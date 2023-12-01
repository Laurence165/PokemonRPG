package AdventureModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
     * Initializes attributes
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
        this.game.setHelpText(parseOtherFile("help"));
    }

     /**
     * Parse Rooms File
     */
    private void parseRooms() throws IOException {
        // TODO: Update so that we can add Villager
        // instantiate villagers

        int roomNumber;

        String roomFileName = this.adventureName + "/rooms.txt";
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


            //now we get the people inside this room
            line = buff.readline();// reads the line after "*****"
            while(line != "-----"){
                String[] div = line.split("\s+");
                String p_type = div[0];
                String p_num  = div[1];
                if(p_type.equalIgnoreCase("VILLAGER")){
                    Villager v = new Villager();
                }
            }

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
                    Passage entry = new Passage(direction, dest_part, object);
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
     * Parse Objects File
     */
    public void parsePokedex() throws IOException {

        String objectFileName = "/Pokemon.txt";
        BufferedReader buff = new BufferedReader(new FileReader(objectFileName));
        int i = 0;
        while(buff.ready()){
            i++;
            Pokemon newPokemon = new Pokemon("a", "b", 100, 100);
            String str = buff.readLine();
            List<String> pokeList = Arrays.asList(str.split(","));
            newPokemon.name = pokeList.get(0);
            newPokemon.health = Integer.parseInt(pokeList.get(1));
            newPokemon.energy = Integer.parseInt(pokeList.get(2));
            newPokemon.type = pokeList.get(3);
            Moves moves1 = new Moves(pokeList.get(4), Integer.parseInt(pokeList.get(5)), Integer.parseInt(pokeList.get(6)));
            Moves moves2 = new Moves(pokeList.get(7), Integer.parseInt(pokeList.get(8)), Integer.parseInt(pokeList.get(9)));
            newPokemon.moves.put(1, moves1);
            newPokemon.moves.put(2, moves2);
            this.game.getPokedex().put(i, newPokemon);
        }
    }

     /**
     * Parse Synonyms File
     */
    public void parseSynonyms() throws IOException {
        String synonymsFileName = this.adventureName + "/synonyms.txt";
        BufferedReader buff = new BufferedReader(new FileReader(synonymsFileName));
        String line = buff.readLine();
        while(line != null){
            String[] commandAndSynonym = line.split("=");
            String command1 = commandAndSynonym[0];
            String command2 = commandAndSynonym[1];
            this.game.getSynonyms().put(command1,command2);
            line = buff.readLine();
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
