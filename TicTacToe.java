// TicTacToe.java
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private int movesCount = 0;
    private JLabel statusLabel;
    
    //colors used
    private final Color BACKGROUND_COLOR = new Color(40, 44, 52);
    private final Color BUTTON_COLOR = new Color(55, 60, 70);
    private final Color HOVER_COLOR = new Color(70, 75, 85);
    private final Color TEXT_COLOR = new Color(220, 223, 228);
    private final Color X_COLOR = new Color(97, 175, 239);
    private final Color O_COLOR = new Color(152, 195, 121);
    private final Color WIN_COLOR = new Color(229, 192, 123);

    //game functions and style
    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //game board panel
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3, 10, 10));
        boardPanel.setBackground(BACKGROUND_COLOR);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // status label 
        statusLabel = new JLabel("X's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = createStyledButton();
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(e -> handleButtonClick(row, col));
                buttons[i][j].addMouseListener(new ButtonHoverEffect(buttons[i][j]));
                boardPanel.add(buttons[i][j]);
            }
        }

        // reset game button
        JButton resetButton = new JButton("New Game");
        styleResetButton(resetButton);
        resetButton.addActionListener(e -> resetGame());

        // add status label, board panel, reset button
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(resetButton, BorderLayout.SOUTH);

        // add main panel to frame
        add(mainPanel);
    }

    private JButton createStyledButton() {
        JButton button = new JButton("");
        button.setFont(new Font("Segoe UI", Font.BOLD, 48));
        button.setForeground(TEXT_COLOR);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(BACKGROUND_COLOR, 2, true));
        button.setPreferredSize(new Dimension(120, 120));
        return button;
    }

    //game reset button styling
    private void styleResetButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(TEXT_COLOR);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(BACKGROUND_COLOR, 2, true));
        button.setPreferredSize(new Dimension(100, 50));
    }

    //hover effects
    private class ButtonHoverEffect extends MouseAdapter {
        private final JButton button;

        public ButtonHoverEffect(JButton button) {
            this.button = button;
        }

        //pointer change on hover
        @Override
        public void mouseEntered(MouseEvent e) {
            if (button.isEnabled()) {
                button.setBackground(HOVER_COLOR);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));  
            }
        }

        //reset to default styles after hover
        @Override
        public void mouseExited(MouseEvent e) {
            if (button.isEnabled()) {
                button.setBackground(BUTTON_COLOR);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  // Change back to default cursor
                // Reset the status label to the current player's turn
                statusLabel.setText((xTurn ? "X" : "O") + "'s Turn");
                statusLabel.setForeground(xTurn ? X_COLOR : O_COLOR);
            }
        }
    }

    //game logic
    private void handleButtonClick(int row, int col) {
        if (!buttons[row][col].getText().equals("")) {
            return;
        }

        String symbol = xTurn ? "X" : "O";
        buttons[row][col].setText(symbol);
        buttons[row][col].setForeground(xTurn ? X_COLOR : O_COLOR);
        movesCount++;

        //winner
        if (checkWinner(row, col)) {
            highlightWinningCombination(row, col);
            statusLabel.setText(symbol + " Wins!");
            statusLabel.setForeground(WIN_COLOR);
            disableButtons();
            return;
        }

        //draw 
        if (movesCount == 9) {
            statusLabel.setText("It's a Draw!");
            statusLabel.setForeground(TEXT_COLOR);
            return;
        }

        //determines player turns
        xTurn = !xTurn;
        statusLabel.setText((xTurn ? "X" : "O") + "'s Turn");
        statusLabel.setForeground(xTurn ? X_COLOR : O_COLOR);
    }

    private void highlightWinningCombination(int row, int col) {
        String symbol = buttons[row][col].getText();
        
        //checks and highlights winner pattern
        if (checkRow(row, symbol)) {
            highlightButtons(row, 0, row, 1, row, 2);
        } else if (checkColumn(col, symbol)) {
            highlightButtons(0, col, 1, col, 2, col);
        } else if (row == col && checkDiagonal(symbol)) {
            highlightButtons(0, 0, 1, 1, 2, 2);
        } else if (row + col == 2 && checkAntiDiagonal(symbol)) {
            highlightButtons(0, 2, 1, 1, 2, 0);
        }
    }

    private void highlightButtons(int row1, int col1, int row2, int col2, int row3, int col3) {
        buttons[row1][col1].setBackground(WIN_COLOR);
        buttons[row2][col2].setBackground(WIN_COLOR);
        buttons[row3][col3].setBackground(WIN_COLOR);
    }

    private boolean checkRow(int row, String symbol) {
        return buttons[row][0].getText().equals(symbol) &&
               buttons[row][1].getText().equals(symbol) &&
               buttons[row][2].getText().equals(symbol);
    }

    private boolean checkColumn(int col, String symbol) {
        return buttons[0][col].getText().equals(symbol) &&
               buttons[1][col].getText().equals(symbol) &&
               buttons[2][col].getText().equals(symbol);
    }

    private boolean checkDiagonal(String symbol) {
        return buttons[0][0].getText().equals(symbol) &&
               buttons[1][1].getText().equals(symbol) &&
               buttons[2][2].getText().equals(symbol);
    }

    private boolean checkAntiDiagonal(String symbol) {
        return buttons[0][2].getText().equals(symbol) &&
               buttons[1][1].getText().equals(symbol) &&
               buttons[2][0].getText().equals(symbol);
    }

    private boolean checkWinner(int row, int col) {
        String symbol = buttons[row][col].getText();
        return checkRow(row, symbol) ||
               checkColumn(col, symbol) ||
               (row == col && checkDiagonal(symbol)) ||
               (row + col == 2 && checkAntiDiagonal(symbol));
    }

    private void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    //resets to original board
    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(BUTTON_COLOR);
            }
        }
        xTurn = true;
        movesCount = 0;
        statusLabel.setText("X's Turn");
        statusLabel.setForeground(X_COLOR);
    }
}