package AdventureModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static java.lang.Math.min;

public class Pokemon {
    String name;
    String type;

    private Integer max_health;

    Integer health;

    private Integer max_energy;

    Integer energy;

    HashMap<Integer, Moves> moves;

    public Pokemon(String name, String type, Integer health, Integer energy){
        this.name = name;
        this.type = type;
        this.health = health;
        this.max_health = health;
        this.max_energy = energy;
        this.energy = energy;
    }
    public static Pokemon readPokemon(String pokemonName, BufferedReader buff) throws IOException{
        try{
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
            return newPokemon;
        }catch (IOException ex){
            throw new IOException(ex);
        }
    }
    public String get_name(){return this.name;}

    public String get_type(){return this.type;}

    public Integer get_health(){return this.health;}

    public void set_health(Integer increment) {
        this.health = min(this.health - increment, this.max_health);
    }

    public void set_energy(Integer increment) {
        this.energy = min(this.energy - increment, this.max_energy);
    }

    public void reset_health() {
        this.health = this.max_health;
    }

    public void reset_energy() {
        this.energy = this.max_energy;
    }

    public Integer get_max_energy(){return this.max_energy;}

    public HashMap<Integer, Moves> get_move() { //TODO: HI this should return
        return this.moves;
    }
}