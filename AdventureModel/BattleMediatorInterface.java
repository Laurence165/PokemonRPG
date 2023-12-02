package AdventureModel;

import views.AdventureGameView;

import java.util.ArrayList;

public interface BattleMediatorInterface {
    public double compare_types(Pokemon attacker, Pokemon defender);

    public void update(Integer inc1, Integer inc2, Integer energy1, Integer energy2);

    public Boolean end_battle();

    public boolean battle();

    }
