import java.awt.*;

public class Figure {
    boolean isAlive;
    boolean isQueen;
    Cell cell;
    Color color;
    int team;
    int x, y, paint_x, paint_y;
    int center_paint_x;
    int center_paint_y;

    public Figure(Cell cell, Color color) {
        this.color = color;
        team = color.equals(GameField.FIRST_TEAM_COLOR) ? 1 : -1;
        this.cell = cell;
        x = this.cell.x;
        y = this.cell.y;
        paint_x = this.cell.paint_x;
        paint_y = this.cell.paint_y;
        center_paint_x = paint_x + GameField.CELL_SIZE / 2;
        center_paint_y = paint_y + GameField.CELL_SIZE / 2;
        isAlive = true;
        isQueen = false;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
        this.paint_x = x * GameField.CELL_SIZE + GameField.LEFT_SPACE;
        this.paint_y = y * GameField.CELL_SIZE + GameField.UP_SPACE;
        center_paint_x = paint_x + GameField.CELL_SIZE / 2;
        center_paint_y = paint_y + GameField.CELL_SIZE / 2;
    }

}
