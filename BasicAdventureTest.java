
import java.io.IOException;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void villagerpaseTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        AdventureLoader loader = new AdventureLoader(game,game.getDirectoryName());
        loader.parseVillager();
        assertEquals(game.getVillagers().size(),1);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }

//    @Test
//    void getObjectString() throws IOException {
//        AdventureGame game = new AdventureGame("TinyGame");
//        String objects = game.player.getCurrentRoom().getObjectString();
//        assertEquals("a water bird", objects);
//    }

}
