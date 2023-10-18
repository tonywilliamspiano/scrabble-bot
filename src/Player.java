import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score = 0;
    private List<Character> hand = new ArrayList<>();
    private LetterBag letterBag;
    private long userID;


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

        addToHand(7 - hand.size());
        score += Dictionary.scoreWord(move.getWord());
    }

    public void addToHand(int num) {
        for (int i = 0; i < num; i++) {
            if (hand.size() >=7) {
                throw new RuntimeException("Hand already full!");
            }
            hand.add(letterBag.getLetter());
        }
    }

    public void setLetterBag(LetterBag letterBag) {
        this.letterBag = letterBag;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void addToHand(Character...characters) {
        for (Character c : characters) {
            if (hand.size() >= 7) {
                throw new RuntimeException("Hand already full!");
            }
            hand.add(c);
        }
    }
}
