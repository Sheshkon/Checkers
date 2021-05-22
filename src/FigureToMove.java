public class FigureToMove {
    Figure figure;
    Cell cell;
    boolean toBeat;
    FigureToMove(Figure figure, Cell cell,boolean toBeat)  {
        this.figure = figure;
        this.cell = cell;
        this.toBeat = toBeat;
    }
}
