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

        // Add multiple characters to hand
        tony.addToHand(3);

        // Test against array list of characters
        List<Character> hand = new ArrayList<>();
        hand.add('a');
        hand.add('b');
        hand.add('c');

        Assert.assertTrue("Hand initialization failed", hand.equals(tony.getHand()));

        tony.addToHand(5);

        Assert.assertThrows(Exception.class, () -> tony.addToHand('a'));
    }

    @Test
    void playerMakeMoveTest() {
        Game game = new Game();
        Player tony = new Player("Tony");
        Player moritz = new Player("Moritz");

        Move move = new Move("ZEBRA", 6, 6, Direction.DOWN);

        tony.addToHand(6);

        tony.makeMove(move);
        game.addWord(move);

        Assertions.assertEquals(1, tony.getHand().size());
    }

}
