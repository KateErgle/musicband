/**
 * Class of elements which is contained in music band
 */
public class Album {
    /**
     * Fields Name and length couldn't be null.
     * Length should be more than 0.
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Long length; //Поле не может быть null, Значение поля должно быть больше 0

    public Album(String name, String length) {
        this.name = name;
        this.length = Long.parseLong(length);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}
