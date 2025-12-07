import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WordleGUI extends JDialog {
    private JPanel contentPane;
    private JPanel gridPanel;
    private JPanel inputPanel;
    private JTextField guessField;
    private JButton guessButton;

    private static final int MAX_ROWS = 6;
    private static final int WORD_LENGTH = 5;

    private JLabel[][] cells = new JLabel[MAX_ROWS][WORD_LENGTH];
    private String secretWord;
    private int currentRow = 0;

    public WordleGUI() {
        contentPane = new JPanel(new BorderLayout(10, 10));
        this.setContentPane(contentPane);
        this.setTitle("Wordle FSM");
        this.setBounds(600, 200, 400, 600);
        this.setModal(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("WORDLE FSM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(MAX_ROWS, WORD_LENGTH, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font cellFont = new Font("Arial", Font.BOLD, 24);

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setFont(cellFont);
                cell.setOpaque(true);
                cell.setBackground(Color.WHITE);
                cell.setBorder(new LineBorder(Color.LIGHT_GRAY));
                cells[row][col] = cell;
                gridPanel.add(cell);
            }
        }

        contentPane.add(gridPanel, BorderLayout.CENTER);

        inputPanel = new JPanel(new BorderLayout(5, 5));
        guessField = new JTextField();
        guessButton = new JButton("Guess");

        inputPanel.add(guessField, BorderLayout.CENTER);
        inputPanel.add(guessButton, BorderLayout.EAST);

        contentPane.add(inputPanel, BorderLayout.SOUTH);

        setUpGuessButtonAction();

        pickRandomSecretWord();

        this.setVisible(true);
    }

    private void setUpGuessButtonAction() {
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGuess();
            }
        });

        guessField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGuess();
            }
        });
    }

    private void pickRandomSecretWord() {
        String query = "SELECT full_word FROM words ORDER BY RAND() LIMIT 1";

        try (PreparedStatement ps = Database.connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                secretWord = rs.getString("full_word").toUpperCase();
                System.out.println("[DEBUG] Secret word = " + secretWord);
            } else {
                throw new IllegalStateException("No words found in database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error picking random word from database.",
                    "DB Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private boolean isValidWord(String guess) {
        if (guess == null || guess.length() != WORD_LENGTH) {
            return false;
        }

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

    private String buildFeedback(String guess) {
        StringBuilder feedback = new StringBuilder();

        for (int i = 0; i < WORD_LENGTH; i++) {
            char g = guess.charAt(i);
            char s = secretWord.charAt(i);

            if (g == s) {
                feedback.append('G');
            } else if (secretWord.indexOf(g) >= 0) {
                feedback.append('Y');
            } else {
                feedback.append('X');
            }
        }
        return feedback.toString();
    }

    private void colorRow(int row, String feedback) {
        for (int i = 0; i < WORD_LENGTH; i++) {
            JLabel cell = cells[row][i];
            char fb = feedback.charAt(i);

            switch (fb) {
                case 'G':
                    cell.setBackground(Color.GREEN);
                    cell.setForeground(Color.WHITE);
                    break;
                case 'Y':
                    cell.setBackground(Color.YELLOW);
                    cell.setForeground(Color.BLACK);
                    break;
                default: // 'X'
                    cell.setBackground(Color.LIGHT_GRAY);
                    cell.setForeground(Color.BLACK);
                    break;
            }
        }
    }

    private void handleGuess() {
        if (currentRow >= MAX_ROWS) {
            return;
        }

        String guess = guessField.getText().trim().toUpperCase();

        if (guess.length() != WORD_LENGTH) {
            JOptionPane.showMessageDialog(this,
                    "Guess must be exactly 5 letters.",
                    "Invalid Guess",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidWord(guess)) {
            JOptionPane.showMessageDialog(this,
                    "Word not in dictionary.",
                    "Invalid Guess",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < WORD_LENGTH; i++) {
            cells[currentRow][i].setText(String.valueOf(guess.charAt(i)));
        }

        String feedback = buildFeedback(guess);
        colorRow(currentRow, feedback);

        if (guess.equals(secretWord)) {
            JOptionPane.showMessageDialog(this,
                    "You guessed it! The word was " + secretWord + ".",
                    "You Win!",
                    JOptionPane.INFORMATION_MESSAGE);
            endGame();
        } else if (currentRow == MAX_ROWS - 1) {
            JOptionPane.showMessageDialog(this,
                    "Out of guesses. The word was " + secretWord + ".",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            endGame();
        } else {
            currentRow++;
            guessField.setText("");
            guessField.requestFocus();
        }
    }

    private void endGame() {
        guessField.setEditable(false);
        guessButton.setEnabled(false);
    }

    public static void main(String[] args) {
        Database.connect();
        setupClosingDBConnection();
        new WordleGUI();
    }

    public static void setupClosingDBConnection() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    if (Database.connection != null && !Database.connection.isClosed()) {
                        Database.connection.close();
                        System.out.println("Application Closed - DB Connection Closed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, "Shutdown-thread"));
    }
}
