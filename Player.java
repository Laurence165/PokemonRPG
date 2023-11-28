import AdventureModel.Moves;
import AdventureModel.Pokemon;

import java.util.HashMap;

public class Player {
    public HashMap<int, Moves> get_move(Pokemon p){
        return p.get_move();
        // ask if they want to pass or move
        // display the choices of moves, and health and energy of the pokemon, ask them to choose
    }
}
