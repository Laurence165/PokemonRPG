package AdventureModel;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ParsingTests {
    @Test
    void parseOpponents() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        AdventureLoader loader = new AdventureLoader(game,game.getDirectoryName());
        loader.parseVillager();
        assertEquals(game.getVillagers().size(),1);
    }
}