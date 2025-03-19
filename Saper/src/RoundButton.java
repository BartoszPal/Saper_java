import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundButton extends JButton {
    private final Color backgroundColor;
    private final Color hoverColor;
    private final Color pressColor;
    private final int arcSize;

    public RoundButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD, 16f));

        backgroundColor = new Color(220, 53, 69); // Kolor czerwony
        hoverColor = new Color(235, 87, 87); // Kolor hover
        pressColor = new Color(200, 16, 36); // Kolor po naciśnięciu
        arcSize = 15;

        setPreferredSize(new Dimension(80, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        if (getModel().isPressed())
            g2d.setColor(pressColor);
        else if (getModel().isRollover())
            g2d.setColor(hoverColor);
        else
            g2d.setColor(backgroundColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcSize, arcSize));
        super.paintComponent(g2d);
        g2d.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
    }
}