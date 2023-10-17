import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {

    List<String> words = new ArrayList<>();
    int dictionarySize;

    Dictionary() throws FileNotFoundException {
        String filePath = "dictionary.txt"; // Replace with the path to your file

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)) ) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidWord(String word) {
        word = word.toUpperCase();

        int left = 0;
        int right = words.size();

        while (left < right) {
            int middle = (right + left) / 2;
            int comparison = word.compareTo(words.get(middle));
            if (comparison == 0) {
                return true;
            }
            else if (comparison < 0) {
                right = middle;
            }
            else {
                left = middle + 1;
            }
        }
        return false;
    }
}
