package AdventureModel;
public class Moves {
    private Integer points;

    private Integer energy;

    private String name;
    public Moves(String name, Integer energy, Integer points){
        this.name = name;
        this.points = points;
        this.energy = energy;
    }

    public String use_move(Pokemon acting_pokemon){
        return(acting_pokemon.get_name() + " has used " + this.name + "!");
    }

    public String get_description(){
        return (this.name + " has attack points " + this.points.toString() + " and costs  " + this.energy.toString()+" energy points\n");
    }
    public Integer get_points(){return this.points;}

    public Integer get_energy(){return this.energy;}

    public String get_name(){return this.name;}
}


