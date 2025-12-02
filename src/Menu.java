import java.util.Scanner;

import java.util.*;

public class Menu {
    String secretWord;
    String currentGuess;
    int attempts = 0;
    int maxAttempts = 6;

    Scanner scanner;
    List<String> dictionary;
    Random random = new Random();

    public Menu(Scanner scanner) {
        this.scanner = scanner;
        this.dictionary = Arrays.asList("APPLE", "OTHER", "WORDS", "ABOUT", "THERE");
    }

    public void pickRandomSecretWord() {
        int idx = random.nextInt(dictionary.size());
        secretWord = dictionary.get(idx);
    }
}
