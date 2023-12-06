package AdventureModel;

/**
 * Represents a generic labelled entity in an adventure game.
 * This class serves as a base for objects that have a name, description, image, and index.
 * It can be used for various game elements such as items, characters, or locations.
 */
public class Labelled {

    // Name of the entity
    String name;

    // Description of the entity
    String description;

    // Address (path) of the entity's image
    String image_address;

    //Index or identifier of the entity
    int index;

    /**
     * Constructs a Labelled object with specified name, description, image address, and index.
     *
     * @param name          The name of the entity.
     * @param description   The description of the entity.
     * @param image_address The image address (path) of the entity.
     * @param index         The index or identifier of the entity.
     */
    protected Labelled(String name, String description, String image_address, int index){
        this.name = name;
        this.image_address = image_address;
        this.description = description;
        this.index = index;
    }
    /**
     * template method for Labelled to get detail.
     */
    public final void templateMethodGetDetail(){
        System.out.println(this.getName() + " "+ this.getDescription() + " " + this.getImage() + " " + this.getIndex());
    }
    /**
     * Gets the name of the entity.
     *
     * @return The name of the entity.
     */
    public String getName(){return this.name;};

    /**
     * Gets the description of the entity.
     *
     * @return The description of the entity.
     */
    public String getDescription(){return this.description;};

    /**
     * Gets the image address of the entity.
     *
     * @return The image address (path) of the entity.
     */
    public String getImage(){return this.image_address;};

    /**
     * Gets the index or identifier of the entity.
     *
     * @return The index or identifier of the entity.
     */
    public int getIndex(){
        return this.index;
    }
}
