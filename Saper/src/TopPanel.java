import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class TopPanel extends JPanel {
    private final RoundButton restartButton;
    private final Mainn frame;
    private String selected;
    private final CustomComboBox difficultyComboBox;
    private final JLabel flagLabel;
    private final JLabel textLabel;
    private Font customFont;

    public TopPanel(Mainn frame) {
        this.frame = frame;
        this.restartButton = new RoundButton("Restart");
        this.setBackground(new Color(74, 117, 44));
        setLayout(null);
        try {
            File fontFile = new File("src/Fonts/Typo_Round_Bold_Demo.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            customFont = customFont.deriveFont(Font.BOLD, 20);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new CustomComboBox(difficultyLevels);

        ImageIcon flagIcon = new ImageIcon("src/Icons/flaga.png");
        Image originalImage = flagIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        flagLabel = new JLabel(scaledIcon);
        textLabel = new JLabel("10");
        textLabel.setFont(customFont);
        textLabel.setForeground(Color.WHITE);
        restartButton.setFont(customFont);

        customFont = customFont.deriveFont(Font.BOLD, 16f);
        difficultyComboBox.setFont(customFont);

        selected = (String) difficultyComboBox.getSelectedItem();
        difficultyComboBox.addActionListener(e -> {
            selected = (String) difficultyComboBox.getSelectedItem();
            assert selected != null;
            frame.setBoard();
        });

        restartButton.addActionListener(e -> frame.setBoard());

        setPreferredSize(new Dimension(700, 40));

        difficultyComboBox.setBounds(5, 5, frame.getWidth() / 5 + 20, 30);
        flagLabel.setBounds(frame.getWidth() / 2 - 40, 0, 40, 40);
        textLabel.setBounds(frame.getWidth() / 2, 0, 40, 40);
        restartButton.setBounds(frame.getWidth() - frame.getWidth() / 5 - 39, 5, frame.getWidth() / 5 + 20, 30);

        add(restartButton);
        add(difficultyComboBox);
        add(flagLabel);
        add(textLabel);
    }

    public void setNewBounds() {
        setSize(frame.getWidth(), 40);
        flagLabel.setBounds(frame.getWidth() / 2 - 40, 0, 40, 40);
        difficultyComboBox.setBounds(5, 5, frame.getWidth() / 5 + 20, 30);
        textLabel.setBounds(frame.getWidth() / 2, 0, 40, 40);
        restartButton.setBounds(frame.getWidth() - frame.getWidth() / 5 - 39, 5, frame.getWidth() / 5 + 20, 30);
    }

    public void setFlagCount(int flagCount){
        this.textLabel.setText(String.valueOf(flagCount));
        repaint();
    }

    public String getSelected() {
        return selected;
    }
}


class CustomComboBox extends JComboBox<String> {

    public CustomComboBox(String[] items) {
        super(items);
        setRenderer(new CustomComboBoxRenderer());
        setEditor(new CustomComboBoxEditor());
        setForeground(Color.WHITE);
        setBackground(new Color(97, 155, 59));
    }

    private static class CustomComboBoxRenderer extends DefaultListCellRenderer {
        private final Border border = BorderFactory.createEmptyBorder(5, 10, 5, 10);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            rendererComponent.setBorder(border);
            rendererComponent.setBackground(isSelected ? new Color(89, 119, 212) : new Color(97, 155, 59));
            rendererComponent.setForeground(Color.WHITE);
            return rendererComponent;
        }
    }

    private static class CustomComboBoxEditor implements ComboBoxEditor {
        private final JPanel editorComponent;
        private final JTextField textField;

        public CustomComboBoxEditor() {
            editorComponent = new JPanel(new BorderLayout());
            editorComponent.setOpaque(true);
            editorComponent.setBackground(new Color(97, 155, 59));
            textField = new JTextField();
            textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            textField.setForeground(Color.WHITE);
            textField.setBackground(new Color(97, 155, 59));
            textField.setFont(textField.getFont().deriveFont(Font.BOLD, 15f));
            editorComponent.add(textField, BorderLayout.CENTER);
        }

        @Override
        public Component getEditorComponent() {
            return editorComponent;
        }

        @Override
        public Object getItem() {
            return textField.getText();
        }

        @Override
        public void setItem(Object anObject) {
            if (anObject != null)
                textField.setText(anObject.toString());
            else
                textField.setText("");
        }

        @Override
        public void selectAll() {
            textField.selectAll();
        }

        @Override
        public void addActionListener(ActionListener l) {
            textField.addActionListener(l);
        }

        @Override
        public void removeActionListener(ActionListener l) {
            textField.removeActionListener(l);
        }
    }
}

