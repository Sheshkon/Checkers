import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class OptionPanel extends JDialog {
    private JPanel panel;
    private static final JLabel cellSizeLabel = new JLabel("     Cell Size:", JLabel.CENTER);
    private JTextField cellSizeField;
    private static final String[] colors = {"White", "Black", "Yellow", "Blue", "Light Gray", "Dark Gray"};
    private static final JComboBox<String> firstCellColorComboBox = new JComboBox<>(colors);
    private static final JComboBox<String> secondCellColorComboBox = new JComboBox<>(colors);
    private static final JComboBox<String> firstTeamColorComboBox = new JComboBox<>(colors);
    private static final  JComboBox<String> secondTeamColorComboBox = new JComboBox<>(colors);
    private static final JComboBox<String> backColorComboBox = new JComboBox<>(colors);
    private static final JLabel firstCellColorLabel = new JLabel("  First Cell Color:          ");
    private static final JLabel secondCellColorLabel = new JLabel("Second Cell Color:", JLabel.CENTER);
    private static final JButton saveButton = new JButton("Save");
    private static final JButton cancelButton = new JButton("Cancel");
    private static final JLabel backColorLabel = new JLabel("Background Color: ", JLabel.LEFT);
    private static final JLabel firstTeamColorLabel = new JLabel("First Team Color:            ");
    private static final JLabel secondTeamColorLabel = new JLabel("Second Team Color:          ");
    private static final JLabel setFontLabel = new JLabel("Set Font");
    private static final JLabel setMenuFontLabel = new JLabel("Set Menu Font");
    private static Font font;
    private static Font menuFont;

    OptionPanel(JFrame parent) {
      setModal(true);
        setTitle("Options");
        initOptions();
        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initOptions() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        font = GameField.FONT;
        cellSizeField = new JTextField(String.valueOf(GameField.CELL_SIZE));
        changeFieldStyle(cellSizeField);
        changeComboBoxStyle(firstCellColorComboBox);
        changeComboBoxStyle(secondCellColorComboBox);
        changeComboBoxStyle(backColorComboBox);
        changeComboBoxStyle(firstTeamColorComboBox);
        changeComboBoxStyle(secondTeamColorComboBox);
        firstCellColorComboBox.setSelectedItem(Logger.colorToString(GameField.FIRST_CELL_COLOR));
        secondCellColorComboBox.setSelectedItem(Logger.colorToString(GameField.SECOND_CELL_COLOR));
        firstTeamColorComboBox.setSelectedItem(Logger.colorToString(GameField.FIRST_TEAM_COLOR));
        secondTeamColorComboBox.setSelectedItem(Logger.colorToString(GameField.SECOND_TEAM_COLOR));
        backColorComboBox.setSelectedItem(Logger.colorToString(GameField.BACK_COLOR));
        changeLabelStyle(cellSizeLabel);
        changeLabelStyle(firstCellColorLabel);
        changeLabelStyle(secondCellColorLabel);
        changeLabelStyle(backColorLabel);
        changeLabelStyle(firstTeamColorLabel);
        changeLabelStyle(secondTeamColorLabel);
        changeLabelStyle(setFontLabel);
        changeLabelStyle(setMenuFontLabel);
        GameFrame.changeButtonStyle(saveButton);
        GameFrame.changeButtonStyle(cancelButton);
        setFontLabel.setForeground(GameFrame.BUTTON_COLOR);
        setMenuFontLabel.setForeground(GameFrame.BUTTON_COLOR);
        cancelButton.setBackground(Color.BLACK);

        Container buttons = new Container();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(new Label("   "));
        buttons.add(saveButton);
        buttons.add(new Label("   "));
        buttons.add(cancelButton);
        buttons.add(new Label("   "));

        Container changeFonts = new Container();
        changeFonts.setLayout(new BoxLayout(changeFonts,BoxLayout.X_AXIS));
        changeFonts.add(new Label(" "));
        changeFonts.add(setFontLabel);
        changeFonts.add(new Label(" "));
       // changeFonts.add(setMenuFontLabel);
       // changeFonts.add(new Label(" "));
        saveButton.addActionListener(e -> changeParams());
        cancelButton.addActionListener(e-> setVisible(false));

        setFontLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
              font = FontChooser.showDialog(StartGame.gameFrame,"Choose Font",font);
            }
        });

        setMenuFontLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                menuFont = FontChooser.showDialog(StartGame.gameFrame,"Choose Menu Font",menuFont);
            }
        });

        panel.add(changeFonts);
        panel.add(firstCellColorLabel);
        panel.add(firstCellColorComboBox);
        panel.add(secondCellColorLabel);
        panel.add(secondCellColorComboBox);
        panel.add(cellSizeLabel);
        panel.add(cellSizeField);
        panel.add(backColorLabel);
        panel.add(backColorComboBox);
        panel.add(firstTeamColorLabel);
        panel.add(firstTeamColorComboBox);
        panel.add(secondTeamColorLabel);
        panel.add(secondTeamColorComboBox);
        panel.add(buttons);
    }

    private void changeParams() {
        int n =  JOptionPane.showOptionDialog(this, "To change params necessary to restart the game.\n Do you want continue?",
                "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[] {"Yes", "No"}, JOptionPane.YES_OPTION);

        if (n == JOptionPane.YES_OPTION) {
            GameField.CELL_SIZE = Integer.parseInt(cellSizeField.getText());
            GameField.FIRST_CELL_COLOR = stringToColor(Objects.requireNonNull(firstCellColorComboBox.getSelectedItem()).toString());
            GameField.SECOND_CELL_COLOR = stringToColor(Objects.requireNonNull(secondCellColorComboBox.getSelectedItem()).toString());
            GameField.FIRST_TEAM_COLOR = stringToColor(Objects.requireNonNull(firstTeamColorComboBox.getSelectedItem()).toString());
            GameField.SECOND_TEAM_COLOR = stringToColor(Objects.requireNonNull(secondTeamColorComboBox.getSelectedItem()).toString());
            GameField.BACK_COLOR = stringToColor(Objects.requireNonNull(backColorComboBox.getSelectedItem()).toString());
            GameField.FONT = font;
            GameFrame.MENU_FONT = menuFont;
            GameFrame.start.doClick();
            StartGame.gameFrame.invalidate();
            StartGame.gameFrame.repaint();
            StartGame.gameFrame.validate();
        }
        setVisible(false);
    }

    private void changeComboBoxStyle(JComboBox<String> comboBox) {
        comboBox.setFont(GameFrame.MENU_FONT);
    }

    private void changeLabelStyle(JLabel label) {
        label.setFont(GameFrame.MENU_FONT);
    }

    private void changeFieldStyle(JTextField field) {
        field.setFont(GameFrame.MENU_FONT);
    }

    private static Color stringToColor(String s) {
        if (s.equalsIgnoreCase("red"))
            return Color.RED;

        if (s.equalsIgnoreCase("white"))
            return Color.WHITE;

        if (s.equalsIgnoreCase("yellow"))
            return Color.YELLOW;

        if (s.equalsIgnoreCase("black"))
            return Color.BLACK;

        if (s.equalsIgnoreCase("blue"))
            return Color.BLUE;

        if (s.equalsIgnoreCase("dark gray"))
            return Color.DARK_GRAY;

        if (s.equalsIgnoreCase("light gray"))
            return Color.LIGHT_GRAY;

        return Color.WHITE;
    }
}