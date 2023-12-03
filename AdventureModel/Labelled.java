package AdventureModel;

public class Labelled {

    String name;
    String description;
    String image_address;

    int index;

    protected Labelled(String name, String description, String image_address, int index){
        this.name = name;
        this.image_address = image_address;
        this.description = description;
        this.index = index;
    }
    public String getName(){return this.name;};
    public String getDescription(){return this.description;};
    public String getImage(){return this.image_address;};

    public int getIndex(){
        return this.index;
    }
}
