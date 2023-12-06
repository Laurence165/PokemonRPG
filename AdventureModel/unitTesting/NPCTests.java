package AdventureModel.unitTesting;

import AdventureModel.Villager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NPCTests {

    @Test
    void talkTest(){
        String phrases[] = {"Hi","I'm Bob"};


        Villager v  = new Villager("BOB","tsra","tsratsra",phrases, false,null,null,0);

        assertEquals(v.talk(), "Hi");
        assertEquals(v.talk(), "I'm Bob");
        assertEquals(v.talk(), "Hi");
        assertEquals(v.talk(), "I'm Bob");
    }


}

