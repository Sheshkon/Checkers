import java.awt.*;

public class Logger {
       public static void log(String s){
           System.out.println(s);
       }

       public static void logTouch(Figure figure){
           System.out.println("Touch: " + GameField.LETTERS[figure.x] + (figure.y + 1) + " (" + Logger.colorToString(figure.color) + ")");
       }

    public static void lobMove(FigureToMove figureToMove){
        System.out.println("Move: " + GameField.LETTERS[figureToMove.figure.x] + (figureToMove.figure.y + 1));
    }

    public static void logBeat(int x, int y){
        System.out.println("Beat: " + GameField.LETTERS[x] + (y + 1));
    }


    public static void logField(int[][] field) {

        System.out.println("--------------------------");
        for (int i = 0; i < 8; i++) {
            System.out.print("|");
            for (int j = 0; j < 8; j++) {
                System.out.print(centerString(3, String.valueOf(field[i][j])));
            }
            System.out.println("|");
        }
        System.out.println("--------------------------");
    }


    public static String colorToString(Color cl) {
        if (cl.equals(Color.RED))
            return "Red";

        if (cl.equals(Color.WHITE))
            return "White";

        if (cl.equals(Color.YELLOW))
            return "Yellow";

        if (cl.equals(Color.BLACK))
            return "Black";

        if (cl.equals(Color.BLUE))
            return "Blue";

        if(cl.equals(Color.DARK_GRAY))
            return "Dark Gray";

        if(cl.equals(Color.LIGHT_GRAY))
            return "Light Gray";
        return null;
    }

    private static String centerString(int width, String s) {
        return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }
}
