package AdventureModel;

public class Villager extends NPC{

    private boolean givesPokemon;
    private AdventureGame adventureGame;
    //uncomment once merged with pokemon class
    private Pokemon pokemon;
    public Villager(String name, String description, String image_address, String audio_address, String[] phrases, Boolean givesPokemon, Pokemon pokemonOptional, AdventureGame adventureGame) {
        super(name, description, image_address, audio_address, phrases);
        this.givesPokemon = givesPokemon;
        this.pokemon = pokemonOptional;
        this.adventureGame = adventureGame;
    }

    public void give_pokemon(){
        adventureGame.getPlayer().getCurrentRoom().addPokemon(this.pokemon);
    }
}

