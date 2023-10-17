public class MoveParser {

    public static Move parseMove(String input) {
        String words[] = input.toUpperCase().split(" ");

        if (isValidMove(words) == false) {
            throw new RuntimeException("Invalid move!");
        }

        // First part is the word to add
        String word = words[0];

        // Second part is the row letter
        int x = words[1].charAt(0) - 'A';

        // Third part the column number
        int y = Integer.parseInt(words[1].substring(1)) - 1;

        // Fourth part direction
        Direction d;

        if (words.length < 3) {
            d = Direction.ACROSS;
        }
        else {
            d = Direction.DOWN;
        }
        System.out.println("Created move with: " + word + " " + x + y + d);
        return new Move(word, x, y, d);
    }

    private static boolean isValidMove(String[] input) {
        return true;
    }
}
