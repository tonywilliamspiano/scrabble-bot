public class Move {
    private  String word;
    private  int x;
    private int y;
    private Direction direction;

    public Move(String word, int x, int y, Direction direction) {
        this.word = word;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void increment() {
        if (direction == Direction.DOWN) {
            y++;
        }
        else {
            x++;
        }
    }
}
