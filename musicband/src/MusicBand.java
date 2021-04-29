import java.io.*;
import java.time.LocalDate;
import java.util.*;
/**
 * Class of elements which is contained in collection
 */
public class MusicBand implements Serializable {

    static HashSet<Integer> uniqueId = new HashSet<>();
    private static Random random = new Random();
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long numberOfParticipants; //Поле не может быть null, Значение поля должно быть больше 0
    private Long albumsCount; //Поле не может быть null, Значение поля должно быть больше 0
    private MusicGenre genre; //Поле может быть null
    private Album bestAlbum; //Поле может быть null

    public MusicBand(String name, Coordinates coordinates, Long numberOfParticipants, Long albumsCount, MusicGenre genre, Album bestAlbum) {
        int id = random.nextInt(Integer.MAX_VALUE);
        while (uniqueId.contains(id)) {
            id = random.nextInt(Integer.MAX_VALUE);
        }
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        creationDate = LocalDate.now();
        this.numberOfParticipants = numberOfParticipants;
        this.albumsCount = albumsCount;
        this.genre = genre;
        this.bestAlbum = bestAlbum;
    }


    public static MusicBand createBand(List<String> text) {

        List<String> list = Arrays.asList(text.get(0).split(" "));

        return new MusicBand(list.get(0),
                new Coordinates(list.get(1), list.get(2)),
                Long.parseLong(list.get(3)),
                Long.parseLong(list.get(4)),
                MusicGenre.valueOf(list.get(5)),
                new Album(list.get(6), list.get(7)));

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Long getAlbumsCount() {
        return albumsCount;
    }

    public void setAlbumsCount(Long albumsCount) {
        this.albumsCount = albumsCount;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public Album getBestAlbum() {
        return bestAlbum;
    }

    public void setBestAlbum(Album bestAlbum) {
        this.bestAlbum = bestAlbum;
    }

    @Override
    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", albumsCount=" + albumsCount +
                ", genre=" + genre +
                ", bestAlbum=" + bestAlbum +
                '}';
    }

    public void saveToFile() throws IOException {

        PrintWriter csvWriter = new PrintWriter("C:\\Users\\Ecler\\IdeaProjects\\musicband\\src\\text.csv");
        TreeMap<Integer, MusicBand> map = new TreeMap<>();
        for(Map.Entry<Integer, MusicBand> e : map.entrySet()){

            System.out.println(String.valueOf(e.getValue()));

            csvWriter.write(String.valueOf(e.getValue()));

        }

        csvWriter.flush();
        csvWriter.close();

    }
}
