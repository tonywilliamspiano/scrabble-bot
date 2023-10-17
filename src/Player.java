import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score = 0;
    private List<Character> hand = new ArrayList<>();
    private LetterBag letterBag;


    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Character> getHand() {
        return hand;
    }

    // Move should be made before game board is updated!
    public void makeMove(Move move) {
        String word = move.getWord();
        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            hand.remove(c);
        }

        addToHand(letterBag.getLetters(7 - hand.size()));
    }

    public void addToHand(Character...characters) {
        for (Character c : characters) {
            if (hand.size() >= 7) {
                throw new RuntimeException("Could not add - hand is full!");
            }
            hand.add(c);
        }
    }

    public void addToHand(List<Character> characters) {
        for (Character c : characters) {
            if (hand.size() >= 7) {
                throw new RuntimeException("Could not add - hand is full!");
            }
            hand.add(c);
        }
    }

    public void setLetterBag(LetterBag letterBag) {
        this.letterBag = letterBag;
    }
}
