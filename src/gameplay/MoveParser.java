package gameplay;

public class MoveParser {

    public static Move parseMove(String input) {
        String words[] = input.toUpperCase().split(" ");

        if (isValidMove(words) == false) {
            throw new RuntimeException("Invalid move! Double check formatting");
        }

        // First part is the word to add
        String word = words[0];

        // Second part is the row letter
        int x = words[1].charAt(0) - 'A';

        // Third part the column number
        int y;
        try {
            y = Integer.parseInt(words[1].substring(1)) - 1;
        } catch (Exception e) {
            throw new RuntimeException("Couldn't parse move, double check your formatting");
        }

        if (x > Game.WIDTH || y > Game.HEIGHT
                || x < 0 || y < 0) {
            throw new RuntimeException("Couldn't parse move, double check your formatting");
        }

        // Fourth part direction
        Direction d;

        if (words[2].toUpperCase().equals("ACROSS")) {
            d = Direction.ACROSS;
        } else if (words[2].toUpperCase().equals("DOWN")) {
            d = Direction.DOWN;
        } else {
            throw new RuntimeException("Invalid direction in word!");
        }
        System.out.println("Created move with: " + word + " " + x + y + d);
        return new Move(word, x, y, d);
    }

    private static boolean isValidMove(String[] input) {
        if (input.length != 3) {
            return false;
        }
        return true;
    }
}
