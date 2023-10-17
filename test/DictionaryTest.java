import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class DictionaryTest {
    @Test
    void dictionaryInit() {
        Dictionary dictionary = null;
        try {
            dictionary = new Dictionary();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertTrue(dictionary.isValidWord("Aardvark"));
        Assertions.assertTrue(dictionary.isValidWord("AARDvArK"));
        Assertions.assertTrue(dictionary.isValidWord("aardvark"));
        Assertions.assertFalse(dictionary.isValidWord("123"));
        Assertions.assertFalse(dictionary.isValidWord("aardvark2"));
        Assertions.assertFalse(dictionary.isValidWord("a ardvark"));
        Assertions.assertFalse(dictionary.isValidWord("aardvar"));

        Assertions.assertTrue(dictionary.isValidWord("ZEBRa"));
        Assertions.assertFalse(dictionary.isValidWord("Zebrt"));

    }
}
