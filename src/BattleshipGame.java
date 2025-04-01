import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleshipGame extends JFrame {

    private JButton[][] board;
    private JLabel missCounterLabel, strikeCounterLabel, totalMissCounterLabel, totalHitCounterLabel;
    private int missCounter = 0, strikeCounter = 0, totalMissCounter = 0, totalHitCounter = 0;
    private boolean[][] shipLocations;
    private List<Integer> shipSizes = new ArrayList<>(List.of(5, 4, 3, 3, 2));
    private boolean gameOver = false;

    public BattleshipGame() {
        super("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        // Create the game board
        board = new JButton[10][10];
        JPanel boardPanel = new JPanel(new GridLayout(10, 10));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new JButton();
                board[i][j].setBackground(Color.BLUE);
                board[i][j].setText("🌊");
                board[i][j].addActionListener(new CellClickListener(i, j));
                boardPanel.add(board[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Create the status panel
        JPanel statusPanel = new JPanel(new GridLayout(4, 2));
        missCounterLabel = new JLabel("Misses: 0");
        strikeCounterLabel = new JLabel("Strikes: 0");
        totalMissCounterLabel = new JLabel("Total Misses: 0");
        totalHitCounterLabel = new JLabel("Total Hits: 0");
        statusPanel.add(missCounterLabel);
        statusPanel.add(new JLabel());
        statusPanel.add(strikeCounterLabel);
        statusPanel.add(new JLabel());
        statusPanel.add(totalMissCounterLabel);
        statusPanel.add(new JLabel());
        statusPanel.add(totalHitCounterLabel);
        statusPanel.add(new JLabel());
        add(statusPanel, BorderLayout.SOUTH);

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameOver) {
                    int choice = JOptionPane.showConfirmDialog(BattleshipGame.this, "Are you sure you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        resetGame();
                    }
                } else {
                    int choice = JOptionPane.showConfirmDialog(BattleshipGame.this, "Are you sure you want to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        resetGame();
                    }
                }
            }
        });
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(BattleshipGame.this, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        buttonPanel.add(playAgainButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Initialize the game
        resetGame();

        setVisible(true);
    }

    private void resetGame() {
        gameOver = false;
        missCounter = 0;
        strikeCounter = 0;
        totalMissCounter = 0;
        totalHitCounter = 0;
        missCounterLabel.setText("Misses: 0");
        strikeCounterLabel.setText("Strikes: 0");
        totalMissCounterLabel.setText("Total Misses: 0");
        totalHitCounterLabel.setText("Total Hits: 0");
        shipLocations = new boolean[10][10];
        placeShips();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j].setEnabled(true);
                board[i][j].setBackground(Color.BLUE);
                board[i][j].setText("🌊");
            }
        }
    }

    private void placeShips() {
        Random random = new Random();
        for (int shipSize : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = random.nextInt(10);
                int col = random.nextInt(10);
                boolean horizontal = random.nextBoolean();
                if (canPlaceShip(row, col, shipSize, horizontal)) {
                    placeShip(row, col, shipSize, horizontal);
                    placed = true;
                }
            }
        }
    }

    private boolean canPlaceShip(int row, int col, int shipSize, boolean horizontal) {
        if (horizontal) {
            if (col + shipSize > 10) {
                return false;
            }
            for (int i = col; i < col + shipSize; i++) {
                if (shipLocations[row][i]) {
                    return false;
                }
            }
        } else {
            if (row + shipSize > 10) {
                return false;
            }
            for (int i = row; i < row + shipSize; i++) {
                if (shipLocations[i][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeShip(int row, int col, int shipSize, boolean horizontal) {
        if (horizontal) {
            for (int i = col; i < col + shipSize; i++) {
                shipLocations[row][i] = true;
            }
        } else {
            for (int i = row; i < row + shipSize; i++) {
                shipLocations[i][col] = true;
            }
        }
    }

    private class CellClickListener implements ActionListener {
        private int row;
        private int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameOver) {
                return;
            }
            board[row][col].setEnabled(false);
            if (shipLocations[row][col]) {
                board[row][col].setBackground(Color.RED);
                board[row][col].setText("💥");
                totalHitCounter++;
                totalHitCounterLabel.setText("Total Hits: " + totalHitCounter);
                missCounter = 0;
                missCounterLabel.setText("Misses: 0");
                if (checkShipSunk(row, col)) {
                    JOptionPane.showMessageDialog(BattleshipGame.this, "Ship Sunk!");
                }
                if (totalHitCounter == 17) {
                    gameOver = true;
                    JOptionPane.showMessageDialog(BattleshipGame.this, "You Win! Congratulations!");
                }
            } else {
                board[row][col].setBackground(Color.YELLOW);
                board[row][col].setText("💦");
                missCounter++;
                totalMissCounter++;
                missCounterLabel.setText("Misses: " + missCounter);
                totalMissCounterLabel.setText("Total Misses: " + totalMissCounter);
                if (missCounter == 5) {
                    strikeCounter++;
                    strikeCounterLabel.setText("Strikes: " + strikeCounter);
                    missCounter = 0;
                    missCounterLabel.setText("Misses: 0");
                    if (strikeCounter == 3) {
                        gameOver = true;
                        JOptionPane.showMessageDialog(BattleshipGame.this, "You Lose! Better luck next time.");
                    }
                }
            }
        }

        private boolean checkShipSunk(int row, int col) {
            int shipSize = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (shipLocations[i][j]) {
                        shipSize++;
                    }
                }
            }
            return shipSize == totalHitCounter;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BattleshipGame());
    }
}
