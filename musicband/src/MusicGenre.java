/**
 * Enum of Music Genres
 */
public enum MusicGenre {

    PROGRESSIVE_ROCK("PROGRESSIVE_ROCK"),
    PSYCHEDELIC_CLOUD_RAP("PSYCHEDELIC_CLOUD_RAP"),
    SOUL("SOUL"),
    POST_ROCK("POST_ROCK"),
    POST_PUNK("POST_PUNK");

    private String name;

    MusicGenre(String name) {
        this.name = name;
    }
    /**
     * Show all values in enum
     * @return String with all Values
     */
    public static String showAllValues() {
        StringBuilder s = new StringBuilder();
        for (MusicGenre env : MusicGenre.values()) {
            s.append(env.getName()).append(" ");
        }
        return s.toString();
    }
    private String getName() {
        return name;
    }
}
