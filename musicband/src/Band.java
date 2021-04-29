import java.io.*;
import java.util.*;

/**
 * @author Ergle Kate R3136
 * This is main class
 */

public class Band{

    private static TreeMap<Integer, MusicBand> map = new TreeMap<>();

    private static Scanner in = new Scanner(System.in);

    public static void band(String[] args) throws IOException {

        while (true) {
            try {
                fillMap(args[0]);
                break;
            }catch (IndexOutOfBoundsException e){
                System.out.println("You didn't enter a input file");
            }
            System.out.println("Enter a file's name for starting");
            fillMap(in.nextLine());
            break;
        }
        /**
         * This is start point of program
         *
         * @param args [0] - name of input file
         */

        boolean working = true;

        while (working) {

            System.out.println("Введите команду: ");

            String command = in.nextLine();

            switch (command) {

                case "help":
                    printHelp();
                case "info":
                    printInfo();
                    break;
                case "show":
                    showAll();
                    break;
                case "insert":

                    int key = 0;
                    String keycheck;
                    try{
                        keycheck = in.nextLine();
                        key = Integer.parseInt(keycheck);
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    MusicBand musicBand = MyReader.getElementFromConsole(in);
                    map.put(key,musicBand);
                    break;
                case "update":
                    try {
                        key = in.nextInt();
                        updateByKey(key);
                    } catch (InputMismatchException e) {
                        System.out.println("The key must be int type.");
                        in.nextLine();
                    }

                    break;
                case "remove_key":
                    key = in.nextInt();
                    map.remove(key);
                    break;
                case "clear":
                    if (map.isEmpty()) {
                        System.out.println("Collection is already empty");
                    } else {
                        map.clear();
                        System.out.println("Collection is empty");
                    }
                    break;
                case "save":
                    saveToFile();
                    break;
                case "execute_script":
                    String fileName = in.next();
                    String script = returnScript(fileName);
                    working = runScript(script);
                    break;
                case "exit":
                    working = false;
                    break;
                case "remove_lower":
                    key = in.nextInt();
                    removeLessKey(key);
                    break;
                case "replace_if_greater null":
                    key = in.nextInt();
                    break;
                case "remove_lower_key null":
                    key = in.nextInt();
                    removeLessKey(key);
                    break;
                case "sum_of_number_of_participants":
                    System.out.println("sum of number of participants = " + sumOfNumberOfParticipants());
                    break;
                case "average_of_albums_count":
                    System.out.println("average of albums count = " + averageOfAlbums());
                    break;
                case "print_unique_number_of_participants":
                    printUniqueParticipants();
                    break;

            }
        }

    }

    /**
     * Method changes element with <i>key</i>
     *
     * @param key
     */
    private static void updateByKey(int key) {

        if (map.keySet().contains(key)) {

            MusicBand musicBand = readMusicBandEntity();

            map.put(key, musicBand);

        }

    }

    private static MusicBand readMusicBandEntity() {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < 9; i++) {

            s.append(in.next()).append(" ");

        }

        return MusicBand.createBand(Collections.singletonList(s.toString()));

    }

    private static boolean runScript(String script) throws IOException {

        boolean working = true;

        Scanner scanner = new Scanner(script);

        String command = scanner.next();

        switch (command) {

            case "help":
                printHelp();
                break;
            case "info":
                printInfo();
                break;
            case "show":
                showAll();
                break;
            case "insert null":
                break;
            case "update id":
                break;
            case "remove_key":
                int key = in.nextInt();
                map.remove(key);
                break;
            case "clear":
                if (map.isEmpty()) {
                    System.out.println("Collection is already empty");
                } else {
                    map.clear();
                    System.out.println("Collection is empty");
                }
                break;
            case "save":
                saveToFile();
                break;
            case "execute_script":
                String fileName = in.next();
                String script1 = returnScript(fileName);
                runScript(script1);
                break;
            case "exit":
                working = false;
                break;
            case "remove_lower":
                key = in.nextInt();
                removeLessKey(key);
                break;
            case "replace_if_greater null":
                break;
            case "remove_lower_key null":
                break;
            case "sum_of_number_of_participants":
                System.out.println("sum of number of participants = " + sumOfNumberOfParticipants());
                break;
            case "average_of_albums_count":
                System.out.println("average of albums count = " + averageOfAlbums());
                break;
            case "print_unique_number_of_participants":
                printUniqueParticipants();
                break;

        }

        return working;

    }

    /**
     * Method saves to file
     * @throws IOException
     */
    private static void saveToFile() throws IOException {

        PrintWriter csvWriter = new PrintWriter("C:\\Users\\Ecler\\IdeaProjects\\musicband\\src\\text.csv");

        for(Map.Entry<Integer, MusicBand> e : map.entrySet()){

            System.out.println(String.valueOf(e.getValue()));

            csvWriter.write(String.valueOf(e.getValue()));

        }

        csvWriter.flush();
        csvWriter.close();

    }

    /**
     * Method return Script
     * @param fileName
     * @return record
     */
    private static String returnScript(String fileName) {

        List<List<String>> records = ListOfListsFromFile(fileName);

        return records.get(0).get(0);

    }

    /**
     * Method make a list from file
     * @param fileName
     * @return records
     */
    private static List<List<String>> ListOfListsFromFile(String fileName) {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("C:\\Users\\Ecler\\IdeaProjects\\musicband\\src\\" + fileName))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return records;
    }
    /**
     * Removes element less then <i>key</i> from collection
     * @param key
     */
    private static void removeLessKey(int key) {

        HashSet<Integer> set = new HashSet<>();

        for(Map.Entry<Integer, MusicBand> e : map.entrySet()){

            if (e.getKey() < key)
                set.add(e.getKey());

        }

        for(int i : set){
            map.remove(i);
        }

    }
    /**
     * Method fills the TreeMap
    */
    private static void fillMap(String fileName) {

        List<List<String>> records = ListOfListsFromFile(fileName);

        convertFromListToMap(records);

    }

    private static void convertFromListToMap(List<List<String>> records) {

        for (List<String> list : records) {
            MusicBand musicBand = MusicBand.createBand(list);
            map.put(musicBand.getId(), musicBand);
        }

    }
/**
 * Method record from list
 * @param line
 * return values
 */
    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    /**
     * MEthod print Unique Participants
     */
    private static void printUniqueParticipants() {

        HashSet<Long> set = new HashSet<>();
        if (map.isEmpty()) {
            System.out.println("Collection is empty");
        }else {
            for(Map.Entry<Integer, MusicBand> e : map.entrySet()){

                set.add(e.getValue().getNumberOfParticipants());

            }

            System.out.println("Unique number of participants are:");

            for (Long l : set) {

                System.out.println(l);

            }
        }


    }

    /**
     * Method count the average of Albums
     * @return map.sixe
     */
    private static float averageOfAlbums() {

        float average = 0;
        if (map.isEmpty()) {
            System.out.println("Collection is empty");
        }else {
            for(Map.Entry<Integer, MusicBand> e : map.entrySet()){

                average += e.getValue().getAlbumsCount();

            }
        }


        return average / map.size();

    }

    /**
     * Method count the sum of number of participants
     * @return sum
     */
    private static int sumOfNumberOfParticipants() {

        int sum = 0;
        if (map.isEmpty()) {
            System.out.println("Collection is empty");
        }else {
            for(Map.Entry<Integer, MusicBand> e : map.entrySet()) {

                sum += e.getValue().getNumberOfParticipants();
            }
        }

        return sum;

    }
    /**
     * Show all elements from collection
     */
    private static void showAll() {

        for(Map.Entry<Integer, MusicBand> e : map.entrySet()){

            System.out.println(e.getValue());

        }

    }
    /**
     * Method prints the legend
     */
    private static void printHelp(){
        System.out.println("List of available commands: \n"+
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                        "insert null {element} : добавить новый элемент с заданным ключом\n" +
                        "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                        "remove_key null : удалить элемент из коллекции по его ключу\n" +
                        "clear : очистить коллекцию\n" +
                        "save : сохранить коллекцию в файл\n" +
                        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                        "exit : завершить программу (без сохранения в файл)\n" +
                        "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                        "replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого\n" +
                        "remove_lower_key null : удалить из коллекции все элементы, ключ которых меньше, чем заданный\n" +
                        "sum_of_number_of_participants : вывести сумму значений поля numberOfParticipants для всех элементов коллекции\n" +
                        "average_of_albums_count : вывести среднее значение поля albumsCount для всех элементов коллекции\n" +
                        "print_unique_number_of_participants : вывести уникальные значения поля numberOfParticipants всех элементов в коллекции");
    }
    /**
     * Show info about collection(Type and size)
     */
    private static void printInfo() {
        System.out.println(map.getClass().toString() + ", size: " + map.size());
    }

}
