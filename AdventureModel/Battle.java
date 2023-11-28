<<<<<<< HEAD
import AdventureModel.Opponent;

=======
package AdventureModel;
>>>>>>> 93b3514e749c3f635fd06ddfef9a7245d974ae1c
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.floor;

public class Battle {

    // TODO: revisit naming conventions! IDK how to name these... camel case?
    private final Player player1;
    private Opponent player2;

    private final ArrayList<Pokemon> player1Pokemon;

    private final ArrayList<Pokemon> player2Pokemon;

    private Pokemon currentPokemon1;

    private Pokemon currentPokemon2;

    public Battle(Player p, Opponent o, ArrayList<Pokemon> playerPokemon, ArrayList<Pokemon> oppPokemon){
        this.player1 = p;
        this.player2 = o;

        this.player1Pokemon = playerPokemon;
        this.player2Pokemon = oppPokemon;

        this.currentPokemon1 = playerPokemon.get(0);
        this.currentPokemon2 = oppPokemon.get(0);
    }

    public double compare_types(Pokemon attacker, Pokemon defender){
        String a = attacker.get_type();
        String d = defender.get_type();
        double weaker = 0.5;
        double stronger = 1.5;

        if (Objects.equals(a, "Fire")){
            if (Objects.equals(d, "Water")){
                return weaker;
            } else if (Objects.equals(d, "Earth")) {
                return stronger;
            }
        } else if (Objects.equals(a, "Earth")){
            if (Objects.equals(d, "Fire")){
                return weaker;
            } else if (Objects.equals(d, "Air")) {
                return stronger;
            }
        } else if (Objects.equals(a, "Air")){
            if (Objects.equals(d, "Earth")){
                return weaker;
            } else if (Objects.equals(d, "Electric")) {
                return stronger;
            }
        } else if (Objects.equals(a, "Electric")){
            if (Objects.equals(d, "Air")){
                return weaker;
            } else if (Objects.equals(d, "Water")) {
                return stronger;
            }
        } else if (Objects.equals(a, "Water")){
            if (Objects.equals(d, "Electric")){
                return weaker;
            } else if (Objects.equals(d, "Fire")) {
                return stronger;
            }
        }

        return 1;
    }

    public void update(Integer inc1, Integer inc2, Integer energy1, Integer energy2){
        this.currentPokemon1.set_health(inc1);
        this.currentPokemon2.set_health(inc2);
        this.currentPokemon1.set_energy(energy1);
        this.currentPokemon2.set_energy(energy2);
    }

    public Boolean end_battle(){
        boolean won = false;
        for (Pokemon p : this.player1Pokemon) {
            if (p.get_health() > 0){
                won = true;
            }
            p.reset_health();
            p.reset_energy();
        }
        for (Pokemon p : this.player2Pokemon) {
            p.reset_health();
            p.reset_energy();
        }
        return won;
    }

    public void battle(){
        Integer turn = 1;
        boolean cont = true;

        while(cont){
            if (turn == 1){
                Moves m = this.player1.get_move(this.currentPokemon1);
                if (m.get_points() > 0){
                    double factor = this.compare_types(this.currentPokemon1, this.currentPokemon2);
                    double damage = factor * m.get_points();
                    int damage2 = (int) floor(damage);
                    update(0, damage2, m.get_energy(), 0);
                } else if (m.get_points() == 0){ // indicates pass
                    update(0, 0, (this.currentPokemon1.get_max_energy()/2), 0);
                }else {
                    update(m.get_points(), 0, m.get_energy(), 0);
                }
                turn = 2;
            } else {
                Moves m = this.player2.get_move(this.currentPokemon2);
                turn = 1;
            }
            // check if either current pokemon is dead
            // check for end of game
        }
    }
}
