import javax.swing.*;


public class Mainn extends JFrame{
    private Board board;
    private int cubeSize;
    private final TopPanel topPanel;

    public Mainn() {
        this.setLayout(null);
        cubeSize = 40;
        board = new Board(8,10, 10,cubeSize, this);
        // todo zrobic zakonczenie gry
        board.setBounds(0,40,10 * cubeSize,8 * cubeSize);
        this.add(board);
        this.setSize(414,397);
        topPanel = new TopPanel(this);
        topPanel.setBounds(0,0,getWidth(), 40);
        this.add(topPanel);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void setBoard(){
        String selected = topPanel.getSelected();
        remove(board);
        if(selected.equals("Hard")){
            cubeSize = 70;
            board = new Board(20,24, 100,cubeSize, this);
            board.setBounds(-2,40,10 * cubeSize,8 * cubeSize);
            topPanel.setFlagCount(100);
            this.add(board);
            this.setSize(710,637);
        } else if (selected.equals("Medium")) {
            cubeSize = 60;
            board = new Board(14,18, 40,cubeSize, this);
            board.setBounds(-3,39,10 * cubeSize,8 * cubeSize -4);
            topPanel.setFlagCount(40);
            this.add(board);
            this.setSize(608,550);
        }else {
            cubeSize = 40;
            board = new Board(8,10, 10,cubeSize, this);
            board.setBounds(0,40,10 * cubeSize,8 * cubeSize);
            topPanel.setFlagCount(10);
            this.add(board);
            this.setSize(414,397);
        }
        topPanel.setNewBounds();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mainn::new);
    }

    public void updateFlags(int flagCount) {
        topPanel.setFlagCount(flagCount);
    }
}
