import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Cube extends JButton {

    private final int index;
    private final ArrayList<Integer> neighbours;
    private final int height;
    private final int width;
    private final ImageIcon[] icons;
    private final Board board;
    private boolean hasBeenClicked;
    private int value;
    private boolean isIcon;
    private boolean isBomb;

    public Cube(int index, int height, int width, ImageIcon[] icons, Board board, int size) {
        this.isBomb = false;
        this.isIcon = false;
        this.setSize(size, size);
        this.board = board;
        this.icons = icons;
        this.hasBeenClicked = false;
        this.index = index;
        this.height = height;
        this.width = width;
        this.neighbours = new ArrayList<>();
        setColor();
        calculateNeighbours();
        setBorderPainted(false);
        setRolloverEnabled(false);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setEnabled(isIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setEnabled(false);
                if (!hasBeenClicked) {
                    Container parent = getParent();
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        hasBeenClicked = true;
                        if (value == 0)
                            board.firstMoveWasMade(neighbours, index);
                        if (isIcon) {
                            isIcon = false;
                            board.updateFlagCount();
                            setIcon(null);
                        }
                    }
                    if (SwingUtilities.isRightMouseButton(e)) {
                        setEnabled(true);
                        if (getIcon() == null) {
                            isIcon = true;
                            board.updateFlagCount();
                            ImageIcon flagIcon = new ImageIcon("src/Icons/flaga.png");
                            Image scaledImage = flagIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                            ImageIcon scaledIcon = new ImageIcon(scaledImage);
                            setIcon(scaledIcon);
                        } else {
                            isIcon = false;
                            board.updateFlagCount();
                            setIcon(null);
                        }
                        repaint();
                        parent.repaint();
                    }
                } else {
                    board.clickNeighbors(neighbours);
                }
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hasBeenClicked && value != 0) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getColorForG2D());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            ImageIcon icon;
            if (value == -1) {
                icon = icons[5]; // bomb icon
                if (!isBomb) {
                    restartGame();
                    isBomb = true;
                }
            } else {
                isBomb = false;
                icon = icons[value - 1];
            }
            Image img = icon.getImage();
            int width = getWidth();
            int height = getHeight();
            double scaleX = (double) width / img.getWidth(null);
            double scaleY = (double) height / img.getHeight(null);

            g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            AffineTransform transform = AffineTransform.getScaleInstance(scaleX, scaleY);
            g2d.drawImage(img, transform, this);
            g2d.dispose();
        } else {
            if (value == 0 && hasBeenClicked) {
                setColor();
                board.firstMovePhysics(neighbours);
            }
        }
    }

    public boolean isIcon() {
        return isIcon;
    }

    public void setIsIcon(boolean icon) {
        isIcon = icon;
    }

    private void setColor() {
        int row = index / width;
        int col = index % width;
        if ((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0)) {
            if (hasBeenClicked)
                setBackground(new Color(213, 178, 143));
            else
                setBackground(new Color(158, 204, 46));
        } else {
            if (hasBeenClicked)
                setBackground(new Color(229, 194, 159));
            else
                setBackground(new Color(170, 215, 81));
        }
    }

    private Color getColorForG2D() {
        int row = index / width;
        int col = index % width;
        Color color;
        if ((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0)) {
            color = new Color(213, 178, 143);
        } else {
            color = new Color(229, 194, 159);
        }
        return color;
    }

    private void calculateNeighbours() {
        int leftIndex = index - 1;
        int rightIndex = index + 1;
        int topIndex = index - width;
        int bottomIndex = index + width;

        if (topIndex >= 0) neighbours.add(topIndex);
        if (topIndex - 1 >= 0 && (topIndex - 1) % width != width - 1) neighbours.add(topIndex - 1);
        if (topIndex + 1 >= 0 && (topIndex + 1) % width != 0) neighbours.add(topIndex + 1);

        if (leftIndex >= 0 && leftIndex % width != width - 1) neighbours.add(leftIndex);
        if (rightIndex < height * width && rightIndex % width != 0) neighbours.add(rightIndex);

        if (bottomIndex < height * width) neighbours.add(bottomIndex);
        if (bottomIndex - 1 < height * width && (bottomIndex - 1) % width != width - 1) neighbours.add(bottomIndex - 1);
        if (bottomIndex + 1 < height * width && (bottomIndex + 1) % width != 0) neighbours.add(bottomIndex + 1);
    }

    public void addValue(int value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Integer> getNeighbours() {
        return neighbours;
    }

    public boolean isHasBeenClicked() {
        return hasBeenClicked;
    }

    public void setHasBeenClicked(boolean hasBeenClicked) {
        this.hasBeenClicked = hasBeenClicked;
    }

    public int getValue() {
        return this.value;
    }

    private void restartGame() {
        SwingUtilities.invokeLater(() -> {
            int choice = showDialog("You lost! Do you wanna play again?", "Restart");
            if (choice == JOptionPane.YES_OPTION) {
                //board.restartGame();
            }
        });
    }

    private int showDialog(String message, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(74, 117, 44));

        Font customFont = null;
        try {
            File fontFile = new File("src/Fonts/Typo_Round_Bold_Demo.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            customFont = customFont.deriveFont(Font.BOLD, 15);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(customFont);
        assert customFont != null;
        customFont = customFont.deriveFont(Font.BOLD, 20);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        RoundButton yesButton = new RoundButton("Yes");
        yesButton.setFont(customFont);
        yesButton.addActionListener(e -> {
            board.restartGame();// todo
        });

        RoundButton noButton = new RoundButton("No");
        noButton.setFont(customFont);
        noButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(74, 117, 44));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return JOptionPane.CLOSED_OPTION;
    }
}
