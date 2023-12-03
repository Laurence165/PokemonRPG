package AdventureModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static java.lang.Math.min;

public class Pokemon extends Labelled implements Cloneable{
    String name;
    String type;

    private Integer max_health;

    Integer health;

    private Integer max_energy;

    Integer energy;

    HashMap<Integer, Moves> moves;

    public Pokemon(String name, String description, String image_address, String type, Integer health, Integer energy, int index){
        super(name,description,image_address,index);
        this.type = type;
        this.health = health;
        this.max_health = health;
        this.max_energy = energy;
        this.energy = energy;
        this.moves = new HashMap<>();
    }
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

    public HashMap<Integer, Moves> get_moves() {
        return this.moves;
    }

    public Integer get_energy(){return this.energy;}

    @Override
    public Pokemon clone() throws CloneNotSupportedException{
        return (Pokemon) super.clone();
    }
}