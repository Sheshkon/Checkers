import javax.swing.*;
import java.awt.*;

public class MessageDialog extends JDialog {
    JLabel message;
    JPanel panel;
    JButton confirmButton;

    MessageDialog(Frame parent, String title, String message) {
        super(parent, true);
        setTitle(title);
        initPanel(message);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initPanel(String message) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(GameField.BACK_COLOR);
        this.message = new JLabel("  " + message + "  ");
        this.message.setFont(GameField.FONT);
        confirmButton = new JButton("Ok");
        confirmButton.setBackground(GameFrame.BUTTON_COLOR);
        confirmButton.setForeground(GameFrame.BUTTON_TEXT_COLOR);
        confirmButton.setFont(GameFrame.MENU_FONT);
        this.message.setAlignmentX(0.75F);
        panel.add(new JLabel("\n"));
        panel.add(this.message);
        panel.add(new JLabel("\n"));
        panel.add(confirmButton);
        panel.add(new JLabel("\n"));
        add(panel);
        confirmButton.addActionListener(e -> MessageDialog.super.dispose());
    }
}
