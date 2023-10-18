import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PlayerTest {
    @Test
    void playerInitTest() {
        Player tony = new Player("Tony");

        Assert.assertEquals("Tony", tony.getName());

        tony.setLetterBag(new LetterBag());

        // Add multiple characters to hand
        tony.addToHand(3);

        // Test against array list of characters
        List<Character> hand = new ArrayList<>();
        hand.add('a');
        hand.add('b');
        hand.add('c');

        Assert.assertThrows(Exception.class, () -> tony.addToHand(5));
    }

}
