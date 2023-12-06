package AdventureModel;

import java.util.HashMap;
import static java.lang.Math.min;

/**
 * Represents a Pokemon entity in an adventure game.
 * Extends from Labelled to include common attributes such as name, description, and image address,
 * and implements Cloneable for creating duplicate objects.
 */
public class Pokemon extends Labelled implements Cloneable {
    // The name of the Pokemon
    String name;

    // The type of the Pokemon, such as "Fire", "Water", etc.
    String type;

    // Maximum health the Pokemon can have
    private Integer max_health;

    // Current health of the Pokemon
    Integer health;

    // Maximum energy the Pokemon can have
    private Integer max_energy;

    // Current energy of the Pokemon
    Integer energy;

    // List of moves available to the Pokemon
    HashMap<Integer, Moves> moves;

    /**
     * Constructor to initialize a Pokemon with specified attributes.
     *
     * @param name The name of the Pokemon.
     * @param description The description of the Pokemon.
     * @param image_address The image address for the Pokemon.
     * @param type The type of the Pokemon.
     * @param health The initial and maximum health of the Pokemon.
     * @param energy The initial and maximum energy of the Pokemon.
     * @param index The index or identifier for the Pokemon.
     */
    public Pokemon(String name, String description, String image_address, String type, Integer health, Integer energy, int index) {
        super(name, description, image_address, index);
        this.type = type;
        this.health = health;
        this.max_health = health;
        this.max_energy = energy;
        this.energy = energy;
        this.moves = new HashMap<>();
    }

    /**
     * Getter method for the Pokemon's type.
     */
    public String get_type(){return this.type;}

    /**
     * Getter method for the Pokemon's current health.
     */
    public Integer get_health(){return this.health;}

    /**
     * Getter method for the Pokemon's current energy.
     */
    public Integer get_energy(){return this.energy;}

    /**
     * set health after opposition's moves
     *
     * @param increment: damage opposition's moves did
     */
    public void set_health(Integer increment) {
        this.health = min(this.health - increment, this.max_health);
    }

    /**
     * set energy after using moves
     *
     * @param increment: energy used during the turn
     */
    public void set_energy(Integer increment) {
        this.energy = min(this.energy - increment, this.max_energy);
    }

    /**
     * reset Pokemon's current health to its maximum health
     */
    public void reset_health() {
        this.health = this.max_health;
    }

    /**
     * reset Pokemon's current energy to its maximum energy
     */
    public void reset_energy() {
        this.energy = this.max_energy;
    }

    /**
     * Getter method for the Pokemon's maximum energy.
     */
    public Integer get_max_energy(){return this.max_energy;}

    /**
     * Getter method for the Pokemon's maximum health.
     */
    public Integer get_max_health(){return this.max_health;}

    /**
     * Getter method for the Pokemon's moves.
     */
    public HashMap<Integer, Moves> get_moves() {
        return this.moves;
    }

    /**
     * Creates a clone of this Pokemon object.
     * Used for creating an exact copy of this Pokemon.
     *
     * @return A clone of the current Pokemon object.
     * @throws CloneNotSupportedException if the Pokemon object cannot be cloned.
     */
    @Override
    public Pokemon clone() throws CloneNotSupportedException{
        return (Pokemon) super.clone();
    }
}