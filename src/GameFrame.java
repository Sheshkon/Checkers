import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class  GameFrame extends JFrame {
    public static final Color BUTTON_COLOR = Color.RED;
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    public static Font MENU_FONT = new Font("Comic Sans MS", Font.BOLD, 18);
    public static GameField game;
    public static JButton start;
    private Timer checkWinnerTimer;
    private static OptionPanel optionPanel;

    GameFrame() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/icon.png")));
        setTitle("Checkers");
        JMenuBar menu = new JMenuBar();
        start = new JButton("Restart");
        JButton options = new JButton("Options");
        changeButtonStyle(start);
        changeButtonStyle(options);
        menu.add(start);
        menu.add(options);
        setJMenuBar(menu);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        game = new GameField();

        optionPanel = new OptionPanel(this);
        start.addActionListener(e -> {
            System.out.println("Restart Game");
            game.initGameField();
            checkWinnerTimer.start();
        });

        options.addActionListener(e -> optionPanel.setVisible(true));

        checkWinnerTimer = new Timer(30, e -> {
            if (GameField.firstWin || game.firstBeatenFigures == 12) {
                new MessageDialog(this, "Game Over", Logger.colorToString(GameField.FIRST_TEAM_COLOR) + " team won");
                Logger.log(Logger.colorToString(GameField.FIRST_TEAM_COLOR) + " team won");
                checkWinnerTimer.stop();
                game.initGameField();
                checkWinnerTimer.start();
            } else if (GameField.secondWin || game.secondBeatenFigures == 12) {
                new MessageDialog(this, "Game Over", Logger.colorToString(GameField.SECOND_TEAM_COLOR) + " team won");
                Logger.log(Logger.colorToString(GameField.FIRST_TEAM_COLOR) + " team won");
                checkWinnerTimer.stop();
                game.initGameField();
                checkWinnerTimer.start();
            }
        });

        checkWinnerTimer.start();
        add(game);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void changeButtonStyle(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setOpaque(true);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(MENU_FONT);
        button.setBorder(new LineBorder(Color.WHITE, 2));
        button.setFocusPainted(false);
    }
}
