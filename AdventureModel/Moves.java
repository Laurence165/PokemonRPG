package AdventureModel;

/**
 * Represents a move in an adventure game, typically associated with Pokémon.
 * This class encapsulates the details of a move, including its name, energy cost, and attack points.
 */
public class Moves {
    // Points associated with the move (e.g., attack points)
    private Integer points;

    // Energy cost of performing the move
    private Integer energy;

    // Name of the move
    private String name;

    /**
     * Constructs a Moves object with the specified name, energy cost, and points.
     *
     * @param name The name of the move.
     * @param energy The energy cost of the move.
     * @param points The points associated with the move (e.g., attack points).
     */
    public Moves(String name, Integer energy, Integer points){
        this.name = name;
        this.points = points;
        this.energy = energy;
    }

    /**
     * Executes the move for a given Pokémon and returns a descriptive string.
     *
     * @param acting_pokemon The Pokémon performing the move.
     * @return A descriptive string detailing the move usage.
     */
    public String use_move(Pokemon acting_pokemon){
        return(acting_pokemon.getName() + " has used " + this.name + "! \n");
    }

    /**
     * Gets a description of the move including its name, attack points, and energy cost.
     *
     * @return A string describing the move.
     */
    public String get_description(){
        return (this.name + " has " + this.points.toString() + " attack points and costs " + this.energy.toString() + " energy points.\n");
    }

    /**
     * Gets the points (e.g., attack points) associated with the move.
     *
     * @return The points associated with the move.
     */
    public Integer get_points(){ return this.points; }

    /**
     * Gets the energy cost of performing the move.
     *
     * @return The energy cost of the move.
     */
    public Integer get_energy(){ return this.energy; }

    /**
     * Gets the name of the move.
     *
     * @return The name of the move.
     */
    public String get_name(){ return this.name; }
}
