import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LetterBagTest {
    @Test
    void letterBagTest() {
        LetterBag letterBag = new LetterBag();

        Player tony = new Player("Tony");
        int size = letterBag.getLetterBag().size();
        tony.setLetterBag(letterBag);
        tony.addToHand(7);

        Assertions.assertEquals(size - 7, letterBag.getLetterBag().size());
    }

    @Test
    void letterBagEmptyTest() {

    }
}
