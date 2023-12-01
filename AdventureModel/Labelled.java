package AdventureModel;

public class Labelled {

    String name;
    String description;
    String audio_address;
    String image_address;

    protected Labelled(String name, String description, String audio_address, String image_address){
        this.name = name;
        this.image_address = image_address;
        this.audio_address = audio_address;
        this.description = description;
    }
    public String getName(){return this.name;};
    public String getDescription(){return this.description;};
    public String getAudio(){return this.audio_address;};
    public String getImage(){return this.image_address;};
}
