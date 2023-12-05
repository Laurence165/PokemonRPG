package AdventureModel;

import org.junit.jupiter.api.MethodOrderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Opponent extends NPC implements BattleColleagueInterface{

    private ArrayList<Pokemon> pokemons = new ArrayList<>();
    //
    public Opponent(String name, String description, String image_address, String[] phrases, ArrayList<Pokemon> pokemons, int index) {
        super(name, description, image_address, phrases, index);
        this.pokemons = pokemons;
    }

    public ArrayList<Pokemon> get_battle_pokemon(){
        ArrayList<Pokemon> battlePokemons = new ArrayList<Pokemon>();
        Random rand = new Random();
        for (int i = 0; i <=3; i++) {
            int randomV = rand.nextInt(this.pokemons.size());
            battlePokemons.add(this.pokemons.get(randomV));
//            battlePokemons.remove(this.pokemons.get(randomV));
        }
        return battlePokemons;
    }
    public Moves get_move(Pokemon active){
        // I renamed this method because it's the one I used in Battle class, I think it is what you meant

        if (active.energy > active.get_moves().get(1).get_energy()) {
            return active.get_moves().get(1);
        }
        else if (active.energy > active.get_moves().get(2).get_energy()){
            return active.get_moves().get(2);
        }
        return new Moves("PASS", 0, 0);
    }

}
