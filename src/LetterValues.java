import java.util.HashMap;
import java.util.Map;

public class LetterValues {
    private static Map<Character, Integer> scores = new HashMap<>();

    LetterValues() {
        scores.put('A', 1);
        scores.put('B', 3);
        scores.put('C', 3);
        scores.put('D', 2);
        scores.put('E', 1);
        scores.put('F', 4);
        scores.put('G', 2);
        scores.put('H', 4);
        scores.put('I', 1);
        scores.put('J', 8);
        scores.put('K', 5);
        scores.put('L', 1);
        scores.put('M', 3);
        scores.put('N', 1);
        scores.put('O', 1);
        scores.put('P', 3);
        scores.put('Q', 10);
        scores.put('R', 1);
        scores.put('S', 1);
        scores.put('T', 1);
        scores.put('U', 1);
        scores.put('V', 4);
        scores.put('W', 4);
        scores.put('X', 8);
        scores.put('Y', 4);
        scores.put('Z', 10);
    }

    public static int get(char c) {
        return scores.get(c);
    }
}
