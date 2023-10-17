import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class GameTest {
    @Test
    void gameInit() throws FileNotFoundException {
        Game game = new Game();
        Player player = new Player("player1");
        Player player2 = new Player("player2");

        game.addPlayer(player);
        game.addPlayer(player2);

        Assertions.assertEquals(player, game.getPlayerOne());
        Assertions.assertEquals(player2, game.getPlayerTwo());

        Assertions.assertThrows(Exception.class, () -> game.addPlayer(new Player("")));
    }

    void moveParserTest() {
        MoveParser moveParser = new MoveParser();

        Move move = MoveParser.parseMove("ZEBRA D5 DOWN");

        Assertions.assertEquals("ZEBRA", move.getWord());
        Assertions.assertEquals(3, move.getY());
        Assertions.assertEquals(4, move.getX());
    }


}
