package AdventureModel;

//>>>>>>> 93b3514e749c3f635fd06ddfef9a7245d974ae1c

import views.AdventureGameView;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.floor;

public class Battle {

    // TODO: revisit naming conventions! IDK how to name these... camel case?
    private final Player player1;
    private Opponent player2;

    private ArrayList<Pokemon> player1Pokemon;

    private final ArrayList<Pokemon> player2Pokemon;

    private Pokemon currentPokemon1;

    private Pokemon currentPokemon2;

    private Integer currentPokemonIndex1 = 0;

    private Integer currentPokemonIndex2 = 0;

    private AdventureGameView view;


    public Battle(AdventureGameView v, Player p, Opponent o, ArrayList<Pokemon> oppPokemon){
        this.view = v;
        this.player1 = p;
        this.player2 = o;

        this.player2Pokemon = oppPokemon;

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

    public boolean battle(){

        this.player1Pokemon = this.player1.get_battle_pokemon();
        this.currentPokemon1 = this.player1Pokemon.get(0);

        this.view.setBattleScene(currentPokemon1, currentPokemon2);

        int turn = 1;

        while(true){
            if (turn == 1){
                Moves m = this.player1.get_move(this.currentPokemon1);
                this.view.formatText(m.use_move(currentPokemon1));
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
                // TODO: should I display the updated health and energy after each round? If I want that to be accessible then I'm going to need text to speech
                this.view.pause();
            } else {
                this.view.formatText(player2.talk());
                Moves m = this.player2.get_move(this.currentPokemon2);
                this.view.formatText(m.use_move(currentPokemon1)); // TODO: might want to make this concatenation rather than overwriting
                if (m.get_points() > 0){
                    double factor = this.compare_types(this.currentPokemon2, this.currentPokemon1);
                    double damage = factor * m.get_points();
                    int damage2 = (int) floor(damage);
                    update(damage2, 0, 0, m.get_energy());
                } else if (m.get_points() == 0){ // indicates pass
                    update(0, 0, 0, (this.currentPokemon2.get_max_energy()/2));
                }else {
                    update(0, m.get_points(), 0, m.get_energy());
                }
                turn = 1;
                this.view.pause();
            }

            // Check if either Pokémon is dead and if so check for end of game
            // If the player wins, return true. If the player loses, return false

            if (currentPokemon2.get_health() < 1) {
                if (currentPokemonIndex2 == 2) {
                    return true;
                } else {
                    currentPokemonIndex2 += 1;
                    currentPokemon2 = player2Pokemon.get(currentPokemonIndex2);
                    this.view.setBattleScene(currentPokemon1, currentPokemon2);
                }
            } else if (currentPokemon1.get_health() < 1){
                if (currentPokemonIndex1 == 2){
                    return false;
                } else {
                    currentPokemonIndex1 += 1;
                    currentPokemon1 = player1Pokemon.get(currentPokemonIndex1);
                    this.view.setBattleScene(currentPokemon1, currentPokemon2);
                }
            }
        }
    }
}
