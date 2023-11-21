public class Moves {
    private Integer points;

    private Integer energy;

    private String name;

    public void use_move(Pokemon acting_pokemon){
        System.out.println(acting_pokemon.get_name() + " has used " + this.name + "!");
        // This might turn into changing a label instead
    }

    public Integer get_points(){return this.points;}

    public Integer get_energy(){return this.energy;}
}


