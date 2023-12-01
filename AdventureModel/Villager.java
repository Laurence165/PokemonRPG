package AdventureModel;

public class Villager extends NPC{

    private boolean givesPokemon;
    private AdventureGame adventureGame;

    private Room location;
    private Pokemon pokemon;
    public Villager(String name, String description, String image_address, String audio_address, String[] phrases, Boolean givesPokemon, Pokemon pokemonOptional, Room location, int index) {
        super(name, description, image_address, audio_address, phrases, index);
        this.givesPokemon = givesPokemon;
        this.pokemon = pokemonOptional;
    }

    public void give_pokemon(){
        location.addPokemon(this.pokemon);
    }
}

