package AdventureModel;

public class Villager extends NPC{
    //hello
    private boolean givesPokemon;
    private AdventureGame adventureGame;

    private Room location;
    private Pokemon pokemon;

    public Villager(String name, String description, String image_address, String[] phrases, Boolean givesPokemon, Pokemon pokemonOptional, Room location, int index) {
        super(name, description, image_address, phrases, index);
        this.givesPokemon = givesPokemon;
        this.pokemon = pokemonOptional;
        this.location = location;
    }

    public void give_pokemon(){
        if (this.givesPokemon) {
            location.addPokemon(this.pokemon);
            this.givesPokemon = false;
        }
    }
}

