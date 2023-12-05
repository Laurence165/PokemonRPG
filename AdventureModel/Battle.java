package AdventureModel;

import javafx.application.Platform;
import views.AdventureGameView;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.floor;
//import javax.swing.Timer;

public class Battle implements BattleMediatorInterface {

    // TODO: revisit naming conventions! IDK how to name these... camel case?

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
        this.currentPokemon2.set_health(inc1);
        this.currentPokemon1.set_health(inc2);
        this.currentPokemon1.set_energy(energy1);
        this.currentPokemon2.set_energy(energy2);
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
            Out.append(currentPokemon1.getName() + " did " + damage2 + "damage to " + currentPokemon2.getName() + "and lost " + m.get_energy() + "energy \n");
            Out.append("Current health of " + currentPokemon1.getName() + " is " + currentPokemon1.get_health() + "and Current health of "+ currentPokemon2.getName() + " is " + currentPokemon2.get_health());

        } else if (m.get_points() == 0) { // indicates pass
            update(0, 0, (this.currentPokemon1.get_max_energy() / 2), 0);
        } else {
            Out.append("Poke 1 did "+ m.get_points() + "damage to Poke 2 and lost " + m.get_energy() + "energy");
            update(m.get_points(), 0, m.get_energy(), 0);
        }

//        this.view.formatText(String.valueOf(Out));
        Platform.runLater(() -> {
            this.view.formatText(String.valueOf(Out));
        });
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Delay for 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // After delay, continue on the JavaFX thread
            Platform.runLater(() -> {
                continueBattle(Out);
            });
        }).start();
        // TODO: should I display the updated health and energy after each round? If I want that to be accessible then I'm going to need text to speech
        System.out.println("pausing");
    }

    public void continueBattle(StringBuilder Out){
        int status = checkBattleEnd();
        if (status == 0){
            System.out.println("Opponent turn");
            String phrase = player2.talk();
            Out.append(phrase);
            this.view.textToSpeech(phrase);
            Moves m = this.player2.get_move(this.currentPokemon2);
            Out.append(m.use_move(currentPokemon1));
            if (m.get_points() > 0) {
                double factor = this.compare_types(this.currentPokemon2, this.currentPokemon1);
                double damage = factor * m.get_points();
                int damage2 = (int) floor(damage);
                update(damage2, 0, 0, m.get_energy());
            } else if (m.get_points() == 0) { // indicates pass
                update(0, 0, 0, (this.currentPokemon2.get_max_energy() / 2));
            } else {
                update(0, m.get_points(), 0, m.get_energy());
            }
            this.view.pause(8); //TODO: does this line work?
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

    public int checkBattleEnd(){
        System.out.println("check battle end");
        if (currentPokemon2.get_health() < 1) {
            if (currentPokemonIndex2 == 2) {
                return 1;
            } else {
                currentPokemonIndex2 += 1;
                currentPokemon2 = player2Pokemon.get(currentPokemonIndex2);
                this.view.setBattleScene(currentPokemon1, currentPokemon2);
            }
        } else if (currentPokemon1.get_health() < 1) {
            if (currentPokemonIndex1 == 2) {
                return -1;
            } else {
                currentPokemonIndex1 += 1;
                currentPokemon1 = player1Pokemon.get(currentPokemonIndex1);
                this.view.setBattleScene(currentPokemon1, currentPokemon2);
            }
        }

        return 0;
    }

    public void battleInit() {
        System.out.println("battle init");

//        this.player1Pokemon = this.player1.get_battle_pokemon();
        this.player1Pokemon = this.player2Pokemon;
        this.currentPokemon1 = this.player1Pokemon.get(0);

        this.view.setBattleScene(currentPokemon1, currentPokemon2);

        this.view.getMoveEvent(this.currentPokemon1, this);

    }
}