import java.util.Scanner;
/**
 * Coordinates
 */
public class Coordinates {

    private Long x; //Максимальное значение поля: 741, Поле не может быть null
    private Long y; //Максимальное значение поля: 522, Поле не может быть null

    public Coordinates(Long x, Long y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(String s, String s1) {

        x = Long.parseLong(s);
        y = Long.parseLong(s1);

    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
