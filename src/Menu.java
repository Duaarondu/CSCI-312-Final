import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    String secretWord;
    String currentGuess;
    int attempts = 0;
    int maxAttempts = 6;

    final Scanner scanner;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void pickRandomSecretWord() {
                String query =
                "SELECT words.full_word " +
                        "FROM words " +
                        "LEFT JOIN used_words ON words.full_word = used_words.full_word " +
                        "WHERE used_words.full_word IS NULL " +
                        "ORDER BY RAND() " +
                        "LIMIT 1";


        try (PreparedStatement ps = Database.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String word = rs.getString("full_word").toUpperCase();
                secretWord = word;
                attempts = 0;

                markWordAsUsed(word);
            } else {
                throw new IllegalStateException("No unused words left in database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error picking random word", e);
        }
    }

    private void markWordAsUsed(String word) throws SQLException {
        String insert = "INSERT INTO used_words (full_word) VALUES (?)";
        try (PreparedStatement ps = Database.connection.prepareStatement(insert)) {
            ps.setString(1, word.toUpperCase());
            ps.executeUpdate();
        }
    }

    public boolean isValidWord(String guess) {
        if (guess == null || guess.length() != 5) return false;

        String sql = "SELECT 1 FROM words WHERE full_word = ? LIMIT 1";

        try (PreparedStatement ps = Database.connection.prepareStatement(sql)) {
            ps.setString(1, guess.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
