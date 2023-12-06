package AdventureModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents an opponent in the adventure game.
 * This class extends the NPC class, adding specific functionalities needed for an opponent in battles.
 * It implements the BattleColleagueInterface, allowing it to participate in battle scenarios.
 */
public class Opponent extends NPC implements BattleColleagueInterface {

    // List of Pokemon that the opponent has
    private ArrayList<Pokemon> pokemons = new ArrayList<>();

    /**
     * Constructs an Opponent object with specified details and a list of Pokemon.
     *
     * @param name The name of the opponent.
     * @param description The description of the opponent.
     * @param image_address The image address of the opponent.
     * @param phrases An array of phrases the opponent can say.
     * @param pokemons A list of Pokemon that the opponent possesses.
     * @param index The index or identifier of the opponent.
     */
    public Opponent(String name, String description, String image_address, String[] phrases, ArrayList<Pokemon> pokemons, int index) {
        super(name, description, image_address, phrases, index); // Call to the superclass NPC constructor
        this.pokemons = pokemons; // Initialize the opponent's Pokemon list
    }

    /**
     * Selects a subset of the opponent's Pokemon for battle.
     * This method randomly picks a certain number of Pokemon from the opponent's collection to be used in a battle.
     *
     * @return An ArrayList of Pokemon selected for the battle.
     */
    public ArrayList<Pokemon> get_battle_pokemon() {
        ArrayList<Pokemon> battlePokemons = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i <= 3; i++) {
            int randomIndex = rand.nextInt(this.pokemons.size());
            battlePokemons.add(this.pokemons.get(randomIndex));
            // Randomly selects Pokemon for the battle
        }
        return battlePokemons;
    }

    /**
     * Chooses a move for the active Pokemon based on its energy.
     * If the active Pokemon has enough energy for certain moves, those moves are returned.
     * Otherwise, a default 'PASS' move is returned.
     *
     * @param active The active Pokemon for which the move is to be chosen.
     * @return The selected move for the active Pokemon.
     */
    public Moves get_move(Pokemon active, Battle b) {
        // Selects a move based on the Pokemon's energy level
        if (active.energy > active.get_moves().get(1).get_energy()) {
            return active.get_moves().get(1);
        } else if (active.energy > active.get_moves().get(2).get_energy()) {
            return active.get_moves().get(2);
        }
        return new Moves("PASS", 0, 0); // Default move if energy is too low
    }
}
