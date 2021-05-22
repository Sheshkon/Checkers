import java.awt.*;

public class Cell {
    int x, y;
    int paint_x, paint_y;
    int center_paint_x, center_paint_y;
    Color color;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.paint_x = x * GameField.CELL_SIZE + GameField.LEFT_SPACE;
        this.paint_y = y * GameField.CELL_SIZE + GameField.UP_SPACE;
        center_paint_x = paint_x+  GameField.CELL_SIZE/2;
        center_paint_y =paint_y +  GameField.CELL_SIZE/2;
        setColor();
    }

    private void setColor() {
        if (y % 2 == 0 && x % 2 == 0) {
            color = GameField.FIRST_CELL_COLOR;
        } else if (y % 2 == 1 && x % 2 == 1) {
            color = GameField.FIRST_CELL_COLOR;
        } else {
            color = GameField.SECOND_CELL_COLOR;
        }
    }
}
