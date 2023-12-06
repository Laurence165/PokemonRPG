package AdventureModel;

/**
 * Represents a villager character in an adventure game.
 * This class extends the NPC class, adding specific functionalities related to villagers,
 * such as the ability to give a Pokemon to the player.
 */
public class Villager extends NPC {
    // Flag to indicate whether the villager gives a Pokemon
    private boolean givesPokemon;

    // The game instance this villager belongs to
    private AdventureGame adventureGame;

    // The location of the villager
    private Room location;

    // The Pokemon that the villager may give
    private Pokemon pokemon;

    /**
     * Constructs a Villager object with specified details.
     *
     * @param name The name of the villager.
     * @param description The description of the villager.
     * @param image_address The image address of the villager.
     * @param phrases An array of phrases the villager can say.
     * @param givesPokemon A boolean indicating whether the villager gives a Pokemon.
     * @param pokemonOptional The Pokemon that the villager can give (can be null).
     * @param location The room where the villager is located.
     * @param index The index or identifier of the villager.
     */
    public Villager(String name, String description, String image_address, String[] phrases, Boolean givesPokemon, Pokemon pokemonOptional, Room location, int index) {
        super(name, description, image_address, phrases, index); // Initialize the NPC properties
        this.givesPokemon = givesPokemon;
        this.pokemon = pokemonOptional;
        this.location = location;
    }

    /**
     * Gives a Pokemon to the player if the villager has one to give.
     * This method adds the Pokemon to the current location and then sets the flag to false,
     * indicating that the Pokemon has been given.
     */
    public void give_pokemon() {
        if (this.givesPokemon) {
            location.addPokemon(this.pokemon); // Add the Pokemon to the location
            this.givesPokemon = false; // Set the flag to false as the Pokemon has been given
        }
    }
}


