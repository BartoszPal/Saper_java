import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Board extends JPanel {
    private final int numberOfBombs;
    private final int height;
    private final int width;
    private final Set<Integer> locationOfBombs;
    private final ArrayList<Cube> cubes;
    private ImageIcon[] icons;
    private boolean isFirstMove;
    private int cubeSize;
    private Mainn frame;

    public Board(int height, int width, int bombs, int cubeSize, Mainn frame) {
        this.frame = frame;
        this.cubeSize = cubeSize;
        this.isFirstMove = true;
        this.icons = new ImageIcon[5];
        this.cubes = new ArrayList<>();
        this.locationOfBombs = new HashSet<>();
        this.height = height;
        this.width = width;
        this.numberOfBombs = bombs;
        this.setLayout(new GridLayout(height, width));
        setIcons();
        createBoard();
        addCubesToBoard();
    }

    public void updateFlagCount() {
        int counter = 0;
        for (Cube cube : cubes)
            if (cube.isIcon())
                counter++;
        frame.updateFlags(numberOfBombs - counter);
    }

    private void setIcons() {
        icons = new ImageIcon[6];
        icons[0] = new ImageIcon("src/Icons/new_1.png");
        icons[1] = new ImageIcon("src/Icons/new_2.png");
        icons[2] = new ImageIcon("src/Icons/new_3.png");
        icons[3] = new ImageIcon("src/Icons/new_4.png");
        icons[4] = new ImageIcon("src/Icons/new_5v2.png");
        icons[5] = new ImageIcon("src/Icons/bombv2.png");
    }

    private boolean checkBombsLocation() {
        for (Cube cube : cubes) {
            int bombNeighboursCount = 0;
            for (int neighbourIndex : cube.getNeighbours()) {
                if (locationOfBombs.contains(neighbourIndex)) {
                    bombNeighboursCount++;
                }
            }
            if (bombNeighboursCount > 5) {
                return false;
            }
        }
        return true;
    }

    private void addValueToACube() {
        for (Cube cube : cubes) {
            if (locationOfBombs.contains(cube.getIndex()))
                cube.addValue(-1); // bombValue
            else {
                int bombNeighboursCount = 0;
                for (Integer neighbourIndex : cube.getNeighbours()) {
                    if (locationOfBombs.contains(neighbourIndex)) {
                        bombNeighboursCount++;
                    }
                }
                cube.addValue(bombNeighboursCount);
            }
        }
    }

    private void locateBombs(ArrayList<Integer> neighbours, int index) {
        int randomNumber = 0;
        locationOfBombs.clear();
        while (locationOfBombs.size() != numberOfBombs) {
            randomNumber = (int) (Math.random() * width * height);
            if (!neighbours.contains(randomNumber) && randomNumber != index)
                locationOfBombs.add(randomNumber);
        }
    }

    private void createBoard() {
        for (int i = 0; i < height * width; i++) {
            cubes.add(new Cube(i, height, width, icons, this, cubeSize));
        }
    }

    private void addCubesToBoard() {
        for (Cube cube : cubes) {
            this.add(cube);
        }
    }

    public void firstMoveWasMade(ArrayList<Integer> neighbours, int index) { // upownienie sie ze pierwsze nacisniecie
        if (isFirstMove) {                                                  // nie bedzie zawieralo  bomby ani pola obok bomby
            locateBombs(neighbours, index);
            while (!checkBombsLocation()) {
                locateBombs(neighbours, index);
            }
            addValueToACube();
            firstMovePhysics(neighbours);
        }
        isFirstMove = false;
    }

    public void firstMovePhysics(ArrayList<Integer> neighbours) {
        ArrayList<Cube> cubes1 = getCubes(neighbours);
        for (Cube cube : cubes1) {
            if (!cube.isHasBeenClicked()) {
                cube.setHasBeenClicked(true);
                cube.setEnabled(false);
                cube.setIcon(null);
                cube.setIsIcon(false);
                if (cube.getValue() == 0) {
                    ArrayList<Integer> cubeNeighbours = cube.getNeighbours();
                    firstMovePhysics(cubeNeighbours);
                }
            }
        }
        repaint();
    }


    private ArrayList<Cube> getCubes(ArrayList<Integer> neighbours) {
        ArrayList<Cube> result = new ArrayList<>();
        for (Cube cube : cubes) {
            if (neighbours.contains(cube.getIndex()) && !cube.isHasBeenClicked()) {
                result.add(cube);
            }
        }
        return result;
    }

    public void clickNeighbors(ArrayList<Integer> neighbours) {
        ArrayList<Cube> cubeArrayList = getCubes(neighbours);
        for (Cube cube : cubeArrayList) {
            if (!cube.isIcon()) {
                cube.setIsIcon(false);
                cube.setHasBeenClicked(true);
                cube.repaint();
            }
        }
    }

    public void restartGame() {
        frame.setBoard();
    }
}
