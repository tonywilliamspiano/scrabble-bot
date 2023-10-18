package dictionary;

import java.util.*;

public class LetterBag {


    private Stack<Character> letters = new Stack<>();

    public LetterBag() {
        fill();
        Collections.shuffle(letters);
    }

    private void fill() {
        Map<Character, Integer> letterValues = new HashMap<>();

        letterValues.put('A', 9);
        letterValues.put('B', 2);
        letterValues.put('C', 2);
        letterValues.put('D', 4);
        letterValues.put('E', 12);
        letterValues.put('F', 2);
        letterValues.put('G', 3);
        letterValues.put('H', 2);
        letterValues.put('I', 9);
        letterValues.put('J', 1);
        letterValues.put('K', 1);
        letterValues.put('L', 4);
        letterValues.put('M', 2);
        letterValues.put('N', 6);
        letterValues.put('O', 8);
        letterValues.put('P', 2);
        letterValues.put('Q', 1);
        letterValues.put('R', 6);
        letterValues.put('S', 4);
        letterValues.put('T', 6);
        letterValues.put('U', 4);
        letterValues.put('V', 2);
        letterValues.put('W', 2);
        letterValues.put('X', 1);
        letterValues.put('Y', 2);
        letterValues.put('Z', 1);

        for (Character key : letterValues.keySet()) {
            for (int i = 0; i < letterValues.get(key); i++) {
                letters.add(key);
            }
        }
    }

    public Character getLetter() {
        return letters.pop();
    }

    public List<Character> getLetters(int n) {
        List<Character> returnLetters = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            returnLetters.add(letters.pop());
        }

        return returnLetters;
    }

    public Stack<Character> getLetterBag() {
        return letters;
    }
}
