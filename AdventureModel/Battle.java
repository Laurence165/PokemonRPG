package AdventureModel;

import javafx.application.Platform;
import views.AdventureGameView;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.floor;
//import javax.swing.Timer;

public class Battle implements BattleMediatorInterface {

    public boolean inBattle = false;

    public AdventureGame game = null;
    private final Player player1;
    private Opponent player2;

    private ArrayList<Pokemon> player1Pokemon;

    private final ArrayList<Pokemon> player2Pokemon;

    private Pokemon currentPokemon1;

    private Pokemon currentPokemon2;

    private Integer currentPokemonIndex1 = 0;

    private Integer currentPokemonIndex2 = 0;

    private AdventureGameView view;

    public Moves returnedMove;

    public Battle(AdventureGameView v, AdventureGame g, Player p, Opponent o, ArrayList<Pokemon> oppPokemon) {
        System.out.println("battle instantiation");

        this.view = v;
        this.player1 = p;
        this.player2 = o;

        this.game = g;

        this.player2Pokemon = oppPokemon;

        this.currentPokemon2 = oppPokemon.get(0);
    }

    public double compare_types(Pokemon attacker, Pokemon defender) {
        System.out.println("compare types");

        String a = attacker.get_type();
        String d = defender.get_type();
        double weaker = 0.5;
        double stronger = 1.5;

        if (Objects.equals(a, "Fire")) {
            if (Objects.equals(d, "Water")) {
                return weaker;
            } else if (Objects.equals(d, "Earth")) {
                return stronger;
            }
        } else if (Objects.equals(a, "Earth")) {
            if (Objects.equals(d, "Fire")) {
                return weaker;
            } else if (Objects.equals(d, "Air")) {
                return stronger;
            }
        } else if (Objects.equals(a, "Air")) {
            if (Objects.equals(d, "Earth")) {
                return weaker;
            } else if (Objects.equals(d, "Electric")) {
                return stronger;
            }
        } else if (Objects.equals(a, "Electric")) {
            if (Objects.equals(d, "Air")) {
                return weaker;
            } else if (Objects.equals(d, "Water")) {
                return stronger;
            }
        } else if (Objects.equals(a, "Water")) {
            if (Objects.equals(d, "Electric")) {
                return weaker;
            } else if (Objects.equals(d, "Fire")) {
                return stronger;
            }
        }

        return 1;
    }

    public void update(Integer inc1, Integer inc2, Integer energy1, Integer energy2) {
        System.out.println("Updating health and energy");
        this.currentPokemon1.set_health(inc1);
        this.currentPokemon2.set_health(inc2);
        System.out.println(this.currentPokemon1.getName() + "health " +  this.currentPokemon1.get_health() + " ,current, and max is:" + this.currentPokemon1.get_max_health());
        System.out.println(this.currentPokemon2.getName() + "health " + this.currentPokemon2.get_health() + " ,current, and max is:" + this.currentPokemon2.get_max_health());
        this.currentPokemon1.set_energy(energy1);
        this.currentPokemon2.set_energy(energy2);
        System.out.println(this.currentPokemon1.getName() + "energy " + this.currentPokemon1.get_energy() + " ,current, and max is:" + this.currentPokemon1.get_max_energy());
        System.out.println(this.currentPokemon2.getName() + "energy " + this.currentPokemon2.get_energy() + " ,current, and max is:" + this.currentPokemon2.get_max_energy());
    }

    public Boolean end_battle() {
        System.out.println("end battle");
        boolean won = false;
        for (Pokemon p : this.player1Pokemon) {
            if (p.get_health() > 0) {
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


    public void resumeBattle() {
        StringBuilder Out = new StringBuilder();

        System.out.println("resume battle");
        Moves m = this.returnedMove;
        System.out.println("returned move: " + m.get_name());

        Out.append(m.use_move(currentPokemon1));
        if (m.get_points() > 0) {
            double factor = this.compare_types(this.currentPokemon1, this.currentPokemon2);
            double damage = factor * m.get_points();
            int damage2 = (int) floor(damage);
            update(0, damage2, m.get_energy(), 0);
            Out.append(currentPokemon1.getName() + " did " + damage2 + " damage to " + currentPokemon2.getName() + " and lost " + m.get_energy() + " energy. \n");
//
        } else if (m.get_points() == 0) { // indicates pass
            update(0, 0, (this.currentPokemon1.get_max_energy() / 2), 0);
        } else {
            Out.append(currentPokemon1.getName() + " has healed by "+ (m.get_points() * (-1)) + " points.");
            update(m.get_points(), 0, m.get_energy(), 0);
        }

        Out.append(currentPokemon1.getName() + ": " + currentPokemon1.get_health() + "HP, " + currentPokemon1.get_energy() + " Energy  \n"+ currentPokemon2.getName() + ": " + currentPokemon2.get_health() + "HP, " + currentPokemon2.get_energy() + " Energy  \n");

        //TODO: TEXT TO SPEECH


//        this.view.formatText(String.valueOf(Out));
        Platform.runLater(() -> {
            this.view.formatText(String.valueOf(Out));
        });
        new Thread(() -> {
            try {
                Thread.sleep(10000); // Delay for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // After delay, continue on the JavaFX thread
            Platform.runLater(() -> {
                continueBattle(Out);
            });
        }).start();
        System.out.println("pausing");
    }

    public void continueBattle(StringBuilder Out){
        int status = checkBattleEnd();
        if (status == 0){
            System.out.println("Opponent turn");
            String phrase = player2.talk();
            Out.setLength(0);
            Out.append(this.player2.getName() + ":  '" + phrase + "' \n");
            this.view.textToSpeech(phrase);
            Moves m = this.player2.get_move(this.currentPokemon2);
            Out.append(m.use_move(currentPokemon2));
            if (m.get_points() > 0) {
                double factor = this.compare_types(this.currentPokemon2, this.currentPokemon1);
                double damage = factor * m.get_points();
                int damage2 = (int) floor(damage);
                update(damage2, 0, 0, m.get_energy());
                Out.append(currentPokemon2.getName() + " did " + damage2 + " damage to " + currentPokemon1.getName() + " and lost " + m.get_energy() + " energy. \n");
            } else if (m.get_points() == 0) { // indicates pass
                update(0, 0, 0, (this.currentPokemon2.get_max_energy() / 2));
            } else {
                Out.append("Poke 1 did "+ m.get_points() + "damage to Poke 2 and lost " + m.get_energy() + " energy");
                update(0, m.get_points(), 0, m.get_energy());
            }
            Out.append(currentPokemon1.getName() + ": " + currentPokemon1.get_health() + "HP, " + currentPokemon1.get_energy() + " Energy  \n"+ currentPokemon2.getName() + ": " + currentPokemon2.get_health() + "HP, " + currentPokemon2.get_energy() + " Energy  \n");
            Out.append("It is your turn to move again.");
            // TODO: PAUSE
        } else{
            boolean won = this.end_battle();
            this.game.resumeMovePlayer(won);
        }
        Platform.runLater(() -> {
            this.view.formatText(String.valueOf(Out));
        });
        status = checkBattleEnd();
        if (status == 0){
            System.out.println("continue game after opponent move");
            this.view.getMoveEvent(this.currentPokemon1, this);
        } else {
            boolean won = this.end_battle();
            this.game.resumeMovePlayer(won);
        }
    }

    public int checkBattleEnd() {
        System.out.println("check battle end");
        if (currentPokemon2.get_health() < 1) {
            if (currentPokemon2.get_health() < 1) {
                currentPokemonIndex2 += 1;
                if (currentPokemonIndex2 >= player2Pokemon.size()) {
                    return 1; // All opponent's Pokémon have fainted
                } else {
                    currentPokemon2 = player2Pokemon.get(currentPokemonIndex2);
                    this.view.setBattleScene(currentPokemon1, currentPokemon2);
                }
            } else if (currentPokemon1.get_health() < 1) {
                currentPokemonIndex1 += 1;
                if (currentPokemonIndex1 >= player1Pokemon.size()) {
                    return -1; // All player's Pokémon have fainted
                } else {
                    currentPokemon1 = player1Pokemon.get(currentPokemonIndex1);
                    this.view.setBattleScene(currentPokemon1, currentPokemon2);
                }
            }
        }
        return 0;
    }

    public void battleInit() {
        System.out.println("battle init");

//        this.player1Pokemon = this.player1.get_battle_pokemon(); // TODO: THIS IS WHAT WE NEED FOR INTERFACE

        this.view.updateSelectionItems(this); // this is what we use to get pokemon selection from user
    }

    public void battleInit2(){
        System.out.println("battle init 2");

        this.player1Pokemon = this.view.arraySelectedPokemon;
        this.currentPokemon1 = this.player1Pokemon.get(0);

        this.view.setBattleScene(currentPokemon1, currentPokemon2);

        this.view.getMoveEvent(this.currentPokemon1, this);

    }
}