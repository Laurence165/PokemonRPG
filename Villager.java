import java.util.Optional;

public class Villager extends NPC{

    private boolean givesPokemon;

    //uncomment once merged with pokemon class
    private Object pokemon;
    public Villager(String name, String description, String image_address, String audio_address, String[] phrases, Boolean givesPokemon, Object pokemonOptional) {
        super(name, description, image_address, audio_address, phrases);
        this.givesPokemon = givesPokemon;
        this.pokemon = pokemonOptional;
    }

    private void give_pokemon(){
        //place pokemon object in room.
    }

}

