import javafx.util.Pair;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.util.Vector;

public class GameField extends JPanel {
    private static final int HEIGHT = 720;
    private static final int WIDTH = 1280;
    public static int CELL_SIZE = 75; //75 max
    public static final int LEFT_SPACE = (WIDTH - CELL_SIZE * 8) / 4;
    public static final int UP_SPACE = (HEIGHT - CELL_SIZE * 8) / 2;
    public static  Color FIRST_CELL_COLOR = Color.LIGHT_GRAY;
    public static  Color SECOND_CELL_COLOR = Color.DARK_GRAY;
    public static  Color FIRST_TEAM_COLOR = Color.yellow;
    public static  Color SECOND_TEAM_COLOR = Color.WHITE;
    public static  Color BACK_COLOR = Color.LIGHT_GRAY;
    public static  Color OUTLINE = Color.BLACK;
    public static  Color MAIN_COLOR = Color.BLACK;
    public static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H"};
    public static final int STROKE_SIZE = 4;
    private static final int LINE_WIDTH = 7;
    public static Font FONT = new Font("Comic Sans MS", Font.BOLD, CELL_SIZE / 2);
    private static final int LEFT_BEATEN_SPACE = WIDTH - LEFT_SPACE - CELL_SIZE * 3;
    private static final int FIRST_UP_BEATEN_SPACE = UP_SPACE;
    private static final int SECOND_UP_BEATEN_SPACE = HEIGHT / 2 + CELL_SIZE;

    public int firstBeatenFigures;
    public int secondBeatenFigures;
    private int current_move;
    public Cell[][] paintField;
    private int[][] field;
    public Vector<Figure> firstTeam;
    public Vector<Figure> secondTeam;
    public Vector<FigureToMove> whereToMove;
    public Vector<FigureToMove> whereToBeat;
    private Vector<FigureToMove> movesInField;
    private Boolean toBeat;
    public static boolean firstWin;
    public static boolean secondWin;

    GameField() {
        Logger.log("Start game");
        initGameField();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clicked(e);
            }
        });
    }

    public void initGameField() {
        firstTeam = new Vector<>();
        secondTeam = new Vector<>();
        whereToMove = new Vector<>();
        whereToBeat = new Vector<>();
        movesInField = new Vector<>();
        firstWin = false;
        secondWin = false;
        toBeat = false;
        firstBeatenFigures = 0;
        secondBeatenFigures = 0;
        current_move = -1;
        initField();
        initFigures();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACK_COLOR);
        if (current_move == -1) checkMovesInFiled(secondTeam);
        else checkMovesInFiled(firstTeam);
        repaint();
    }

    private void clicked(MouseEvent e) {
        if (toBeat && whereToBeat.size() != 0)
            beat(e);

        else if (whereToMove.size() != 0)
            move(e);

        if (current_move == 1)
            check(e.getX(), e.getY(), firstTeam);

        if (current_move == -1)
            check(e.getX(), e.getY(), secondTeam);

    }

    private void initField() {
        field = new int[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                field[i][j] = 0;
            }
        }
        paintField = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                paintField[i][j] = new Cell(i, j);
            }
        }
    }

    private void initFigures() {
        for (int i = 0; i < 4; i++) {
            firstTeam.add(new Figure(paintField[i * 2 + 1][0], FIRST_TEAM_COLOR));
            firstTeam.add(new Figure(paintField[i * 2][1], FIRST_TEAM_COLOR));
            firstTeam.add(new Figure(paintField[i * 2 + 1][2], FIRST_TEAM_COLOR));
            secondTeam.add(new Figure(paintField[i * 2][7], SECOND_TEAM_COLOR));
            secondTeam.add(new Figure(paintField[i * 2 + 1][6], SECOND_TEAM_COLOR));
            secondTeam.add(new Figure(paintField[i * 2][5], SECOND_TEAM_COLOR));
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintBoard(g2d);
        paintFigures(g2d);
    }

    private void paintBoard(Graphics2D g2d) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g2d.setColor(paintField[i][j].color);
                g2d.fillRect(paintField[i][j].paint_x, paintField[i][j].paint_y, CELL_SIZE, CELL_SIZE);
            }
        }

        g2d.setColor(MAIN_COLOR);
        g2d.setFont(FONT);
        for (int i = 0; i < 8; i++) {
            TextLayout textLayout = new TextLayout(String.valueOf(i + 1), g2d.getFont(),
                    g2d.getFontRenderContext());
            double textHeight = textLayout.getBounds().getHeight();
            double textWidth = textLayout.getBounds().getWidth();
            g2d.drawString(String.valueOf(i + 1), LEFT_SPACE - CELL_SIZE / 2 - (int) textWidth / 2,
                    UP_SPACE + CELL_SIZE * i + CELL_SIZE / 2 + (int) textHeight / 2);
            g2d.drawString(LETTERS[i], LEFT_SPACE + CELL_SIZE * i + CELL_SIZE / 2 - (int) textWidth / 2,
                    UP_SPACE + CELL_SIZE * 8 + CELL_SIZE / 2 + (int) textHeight / 2);
        }

        Stroke prevStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        g2d.drawRect(LEFT_SPACE, UP_SPACE, CELL_SIZE * 8, CELL_SIZE * 8);
        g2d.setStroke(prevStroke);
    }


    public Figure figureToBeat(int playerN, int x, int y) {
        if (playerN == 1) {
            for (Figure figure : firstTeam) {
                if (figure.isAlive && figure.x == x && figure.y == y) {
                    return figure;
                }
            }
        } else {
            for (Figure figure : secondTeam) {
                if (figure.isAlive && figure.x == x && figure.y == y) {
                    return figure;
                }
            }
        }
        return null;
    }

    private void check(int x, int y, Vector<Figure> player) {
        for (Figure figure : player) {
            if (figure.isAlive && Math.abs(x - figure.center_paint_x) <= CELL_SIZE / 2) {
                if (Math.abs(y - figure.center_paint_y) <= CELL_SIZE / 2) {
                    whereToBeat.clear();
                    whereToMove.clear();
                    if (toBeat) {
                        for (FigureToMove moves : movesInField) {
                            if (moves.figure == figure && moves.toBeat) {
                                whereToBeat.add(moves);
                            }
                        }
                    } else {
                        for (FigureToMove moves : movesInField) {
                            if (moves.figure.equals(figure) && !moves.toBeat) {
                                whereToMove.add(moves);
                            }
                        }
                    }
                    Logger.logTouch(figure);
                    repaint();
                }
            }
        }

    }

    private void where(Figure figure) {
        whereToMove.clear();
        whereToBeat.clear();
        if (!figure.isAlive)
            return;
        if (!figure.isQueen) {
            if (figure.x + 2 < 8 && figure.y + 2 < 8) {
                if (field[figure.y + 1][figure.x + 1] != 0 && field[figure.y + 1][figure.x + 1] != figure.team &&
                        field[figure.y + 2][figure.x + 2] == 0) {
                    movesInField.add(new FigureToMove(figure, new Cell(figure.x + 2, figure.y + 2), true));
                    toBeat = true;
                }
            }

            if (figure.x - 2 >= 0 && figure.y + 2 < 8) {
                if (field[figure.y + 1][figure.x - 1] != 0 && field[figure.y + 1][figure.x - 1] != figure.team &&
                        field[figure.y + 2][figure.x - 2] == 0) {
                    movesInField.add(new FigureToMove(figure, new Cell(figure.x - 2, figure.y + 2), true));
                    toBeat = true;
                }
            }

            if (figure.x + 2 < 8 && figure.y - 2 >= 0) {
                if (field[figure.y - 1][figure.x + 1] != 0 && field[figure.y - 1][figure.x + 1] != figure.team &&
                        field[figure.y - 2][figure.x + 2] == 0) {
                    movesInField.add(new FigureToMove(figure, new Cell(figure.x + 2, figure.y - 2), true));
                    toBeat = true;
                }
            }

            if (figure.x - 2 >= 0 && figure.y - 2 >= 0) {
                if (field[figure.y - 1][figure.x - 1] != 0 && field[figure.y - 1][figure.x - 1] != figure.team &&
                        field[figure.y - 2][figure.x - 2] == 0) {
                    movesInField.add(new FigureToMove(figure, new Cell(figure.x - 2, figure.y - 2), true));
                    toBeat = true;
                }
            }

            if (toBeat) {
                return;
            }


            if (figure.x + 1 < 8 && figure.y + 1 < 8 && figure.team == 1) {
                if (field[figure.y + 1][figure.x + 1] == 0)
                    movesInField.add(new FigureToMove(figure, new Cell(figure.x + 1, figure.y + 1), false));
            }

            if (figure.x + 1 < 8 && figure.y - 1 >= 0 && figure.team == -1) {
                if (field[figure.y - 1][figure.x + 1] == 0)
                    movesInField.add(new FigureToMove(figure, new Cell(figure.x + 1, figure.y - 1), false));
            }

            if (figure.x - 1 >= 0 && figure.y - 1 >= 0 && figure.team == -1) {
                if (field[figure.y - 1][figure.x - 1] == 0)
                    movesInField.add(new FigureToMove(figure, new Cell(figure.x - 1, figure.y - 1), false));
            }

            if (figure.x - 1 >= 0 && figure.y + 1 < 8 && figure.team == 1) {
                if (field[figure.y + 1][figure.x - 1] == 0)
                    movesInField.add(new FigureToMove(figure, new Cell(figure.x - 1, figure.y + 1), false));
            }
            return;
        }
        whereQueen(figure);

    }

    private void whereQueen(Figure figure) {

        int temp_x = figure.x;
        int temp_y = figure.y;

        while (temp_x + 1 < 8 && temp_y + 1 < 8 && field[temp_y + 1][temp_x + 1] == 0) {
            temp_x++;
            temp_y++;
        }

        if (temp_x + 2 < 8 && temp_y + 2 < 8) {
            if (field[temp_y + 1][temp_x + 1] != figure.team && field[temp_y + 2][temp_x + 2] == 0) {
                movesInField.add(new FigureToMove(figure, new Cell(temp_x + 2, temp_y + 2), true));
                temp_x += 2;
                temp_y += 2;
                addQueenMoveRightDown(temp_x, temp_y, figure, movesInField, true);
                toBeat = true;
            }
        }


        temp_x = figure.x;
        temp_y = figure.y;
        while (temp_x - 1 >= 0 && temp_y + 1 < 8 && field[temp_y + 1][temp_x - 1] == 0) {
            temp_x--;
            temp_y++;
        }

        if (temp_x - 2 >= 0 && temp_y + 2 < 8) {
            if (field[temp_y + 1][temp_x - 1] != figure.team && field[temp_y + 2][temp_x - 2] == 0) {
                movesInField.add(new FigureToMove(figure, new Cell(temp_x - 2, temp_y + 2), true));
                temp_x -= 2;
                temp_y += 2;
                addQueenMoveLeftDown(temp_x, temp_y, figure, movesInField, true);
                toBeat = true;

            }
        }


        temp_x = figure.x;
        temp_y = figure.y;
        while (temp_x + 1 < 8 && temp_y - 1 >= 0 && field[temp_y - 1][temp_x + 1] == 0) {
            temp_x++;
            temp_y--;
        }

        if (temp_x + 2 < 8 && temp_y - 2 >= 0) {
            if (field[temp_y - 1][temp_x + 1] != figure.team && field[temp_y - 2][temp_x + 2] == 0) {
                movesInField.add(new FigureToMove(figure, new Cell(temp_x + 2, temp_y - 2), true));
                temp_x += 2;
                temp_y -= 2;
                addQueenMoveRightUp(temp_x, temp_y, figure, movesInField, true);
                toBeat = true;
            }
        }


        temp_x = figure.x;
        temp_y = figure.y;
        while (temp_x - 1 >= 0 && temp_y - 1 >= 0 && field[temp_y - 1][temp_x - 1] == 0) {
            temp_x--;
            temp_y--;
        }

        if (temp_x - 2 >= 0 && temp_y - 2 >= 0) {
            if (field[temp_y - 1][temp_x - 1] != figure.team && field[temp_y - 2][temp_x - 2] == 0) {
                movesInField.add(new FigureToMove(figure, new Cell(temp_x - 2, temp_y - 2), true));
                temp_x -= 2;
                temp_y -= 2;
                addQueenMoveLeftUp(temp_x, temp_y, figure, movesInField, true);
                toBeat = true;

            }
        }

        if (toBeat) {
            return;
        }

        temp_x = figure.x;
        temp_y = figure.y;

        addQueenMoveRightDown(temp_x, temp_y, figure, movesInField, false);
        addQueenMoveRightUp(temp_x, temp_y, figure, movesInField, false);
        addQueenMoveLeftUp(temp_x, temp_y, figure, movesInField, false);
        addQueenMoveLeftDown(temp_x, temp_y, figure, movesInField, false);

    }

    private void addQueenMoveRightUp(int temp_x, int temp_y, Figure figure, Vector<FigureToMove> whereToMoveOrBeat, boolean toBeat) {
        while (temp_x + 1 < 8 && temp_y - 1 >= 0 && field[temp_y - 1][temp_x + 1] == 0) {
            whereToMoveOrBeat.add(new FigureToMove(figure, new Cell(temp_x + 1, temp_y - 1), toBeat));
            temp_x++;
            temp_y--;
        }
    }

    private void addQueenMoveRightDown(int temp_x, int temp_y, Figure figure, Vector<FigureToMove> whereToMoveOrBeat, boolean toBeat) {
        while (temp_x + 1 < 8 && temp_y + 1 < 8 && field[temp_y + 1][temp_x + 1] == 0) {
            whereToMoveOrBeat.add(new FigureToMove(figure, new Cell(temp_x + 1, temp_y + 1), toBeat));
            temp_x++;
            temp_y++;
        }
    }

    private void addQueenMoveLeftUp(int temp_x, int temp_y, Figure figure, Vector<FigureToMove> whereToMoveOrBeat, boolean toBeat) {
        while (temp_x - 1 >= 0 && temp_y - 1 >= 0 && field[temp_y - 1][temp_x - 1] == 0) {
            whereToMoveOrBeat.add(new FigureToMove(figure, new Cell(temp_x - 1, temp_y - 1), toBeat));
            temp_x--;
            temp_y--;
        }
    }

    private void addQueenMoveLeftDown(int temp_x, int temp_y, Figure figure, Vector<FigureToMove> whereToMoveOrBeat, boolean toBeat) {
        while (temp_x - 1 >= 0 && temp_y + 1 < 8 && field[temp_y + 1][temp_x - 1] == 0) {
            whereToMoveOrBeat.add(new FigureToMove(figure, new Cell(temp_x - 1, temp_y + 1), toBeat));
            temp_x--;
            temp_y++;
        }
    }

    private void move(MouseEvent e) {
        for (FigureToMove where : whereToMove) {
            if (Math.abs(where.cell.center_paint_x - e.getX()) <= CELL_SIZE / 2) {
                if (Math.abs(where.cell.center_paint_y - e.getY()) <= CELL_SIZE / 2) {
                    moveFigure(where);
                    current_move *= -1;
                    whereToMove.clear();
                    if (current_move == -1) checkMovesInFiled(secondTeam);
                    else checkMovesInFiled(firstTeam);
                    repaint();
                    return;
                }
            }
        }
    }

    private void moveFigure(FigureToMove where) {
        Figure figure = where.figure;
        field[figure.y][figure.x] = 0;
        where.figure.set(where.cell.x, where.cell.y);
        System.out.println("Move: " + LETTERS[figure.x] + (figure.y + 1));
    }

    private void beat(MouseEvent e) {
        for (FigureToMove where : whereToBeat) {
            if (Math.abs(where.cell.center_paint_x - e.getX()) <= CELL_SIZE / 2) {
                if (Math.abs(where.cell.center_paint_y - e.getY()) <= CELL_SIZE / 2) {
                    if (where.figure.team == 1) {
                        clearBeatFigure(where, 2);
                    } else {
                        clearBeatFigure(where, 1);
                    }

                    if (!toBeat)
                        current_move *= -1;

                    if (current_move == -1)
                        checkMovesInFiled(secondTeam);
                    else
                        checkMovesInFiled(firstTeam);

                    whereToBeat.clear();
                    whereToMove.clear();
                    repaint();

                    return;
                }
            }
        }

    }

    private void clearBeatFigure(FigureToMove figureToMove, int playerN) {
        whereToBeat.clear();
        Pair<Integer, Integer> cords = findToBeat(figureToMove.figure, figureToMove.cell.x, figureToMove.cell.y);
        int x = cords.getKey();
        int y = cords.getValue();
        Figure beat = figureToBeat(playerN, x, y);
        beatenFigures(figureToMove.figure.team, beat);
        beat.isAlive = false;
        field[beat.y][beat.x] = 0;
        field[figureToMove.figure.y][figureToMove.figure.x] = 0;
        figureToMove.figure.set(figureToMove.cell.x, figureToMove.cell.y);
        Logger.lobMove(figureToMove);
        Logger.logBeat(x, y);
        repaint();
        toBeat = false;
        movesInField.clear();
        where(figureToMove.figure);
        repaint();
    }


    private Pair<Integer, Integer> findToBeat(Figure figure, int x2, int y2) {
        int vec_x = x2 - figure.x;
        int vec_y = y2 - figure.y;

        if (vec_y > 0) {
            int j = vec_x > 0 ? figure.x + 1 : figure.x - 1;
            for (int i = figure.y + 1; i < y2 + 1; i++) {
                if (field[i][j] != 0) {
                    return new Pair<>(j, i);
                }
                j += vec_x > 0 ? +1 : -1;
            }
        }
        if (vec_y < 0) {
            int j = vec_x > 0 ? figure.x + 1 : figure.x - 1;
            for (int i = figure.y - 1; i > y2 - 1; i--) {
                if (field[i][j] != 0) {
                    return new Pair<>(j, i);
                }
                j += vec_x > 0 ? +1 : -1;
            }
        }

        return new Pair<>(-1, -1);
    }


    private void paintFigures(Graphics2D g2d) {
        g2d.setColor(FIRST_TEAM_COLOR);
        paintPlayerFigures(g2d, firstTeam, FIRST_TEAM_COLOR);
        paintPlayerFigures(g2d, secondTeam, SECOND_TEAM_COLOR);
        paintSelectedCells(g2d);
    }

    private void paintPlayerFigures(Graphics2D g2d, Vector<Figure> playerFigures, Color color) {
        g2d.setStroke(new BasicStroke(STROKE_SIZE));

        for (Figure figure : playerFigures) {
            g2d.setColor(color);
            g2d.fillOval(figure.paint_x, figure.paint_y, CELL_SIZE, CELL_SIZE);
            g2d.setColor(OUTLINE);
            g2d.drawOval(figure.paint_x, figure.paint_y, CELL_SIZE, CELL_SIZE);
            g2d.drawOval(figure.paint_x + CELL_SIZE / 7, figure.paint_y + CELL_SIZE / 7,
                    CELL_SIZE - 2 * CELL_SIZE / 7, CELL_SIZE - 2 * CELL_SIZE / 7);

            if (!figure.isQueen) {
                g2d.drawOval(figure.paint_x + 2 * CELL_SIZE / 7, figure.paint_y + 2 * CELL_SIZE / 7,
                        CELL_SIZE - 4 * CELL_SIZE / 7, CELL_SIZE - 4 * CELL_SIZE / 7);
            }
        }
    }

    private void paintSelectedCells(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);

        for (FigureToMove el : whereToMove)
            g2d.fillRect(el.cell.paint_x, el.cell.paint_y, CELL_SIZE, CELL_SIZE);

        for (FigureToMove el : whereToBeat)
            g2d.fillRect(el.cell.paint_x, el.cell.paint_y, CELL_SIZE, CELL_SIZE);

    }

    private void beatenFigures(int team, Figure figure) {
        int figuresCount;
        final int SPACE_UP;
        if (team == 1) {
            figuresCount = firstBeatenFigures;
            SPACE_UP = FIRST_UP_BEATEN_SPACE;
        } else {
            figuresCount = secondBeatenFigures;
            SPACE_UP = SECOND_UP_BEATEN_SPACE;
        }
        if (figuresCount < 4) {
            figure.paint_x = LEFT_BEATEN_SPACE + CELL_SIZE * figuresCount;
            figure.paint_y = SPACE_UP;
        } else if (figuresCount < 8) {
            figure.paint_x = LEFT_BEATEN_SPACE + CELL_SIZE * (figuresCount - 4);
            figure.paint_y = SPACE_UP + CELL_SIZE;
        } else if (figuresCount < 12) {
            figure.paint_x = LEFT_BEATEN_SPACE + CELL_SIZE * (figuresCount - 8);
            figure.paint_y = SPACE_UP + CELL_SIZE * 2;
        }

        if (team == 1) firstBeatenFigures++;
        else secondBeatenFigures++;
    }

    private void checkMovesInFiled(Vector<Figure> player) {
        movesInField.clear();
        whereToMove.clear();
        whereToBeat.clear();
        toBeat = false;
        refreshPos();
        for (Figure figure : player) {
            where(figure);
        }
        if(movesInField.isEmpty())
        {
            if(current_move == 1) secondWin = true;
            else firstWin = true;
        }
        repaint();
    }


    public void refreshPos() {
        int x;
        int y;
        for (int i = 0; i < 12; i++) {
            if (firstTeam.elementAt(i).isAlive) {
                x = firstTeam.elementAt(i).x;
                y = firstTeam.elementAt(i).y;
                if (y == 7) {
                    firstTeam.elementAt(i).isQueen = true;
                    field[y][x] = 2;
                } else {
                    field[y][x] = 1;
                }
            }
            if (secondTeam.elementAt(i).isAlive) {
                x = secondTeam.elementAt(i).x;
                y = secondTeam.elementAt(i).y;
                if (y == 0) {
                    secondTeam.elementAt(i).isQueen = true;
                    field[y][x] = -2;
                } else {
                    field[y][x] = -1;
                }
            }
        }

        Logger.logField(field);
    }

}