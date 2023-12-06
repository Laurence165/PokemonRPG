package AdventureModel;

/**
 * An interface for labelled entities in an adventure game.
 */
public interface LabelledInterface {

    /**
     * Gets the name of the entity.
     *
     * @return The name of the entity.
     */
    String getName();

    /**
     * Gets the description of the entity.
     *
     * @return The description of the entity.
     */
    String getDescription();

    /**
     * Gets the image address of the entity.
     *
     * @return The image address (path) of the entity.
     */
    String getImage();

    /**
     * Gets the index or identifier of the entity.
     *
     * @return The index or identifier of the entity.
     */
    int getIndex();
}

