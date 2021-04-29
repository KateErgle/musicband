import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;

/**
 * @author Ergle Kate R3136
 * This is the main class
 */
public class Client {
    private String host;
    private int port;
    private DatagramChannel clientChannel;
    private SocketAddress address;
    private Selector selector;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
    private static Scanner scanner = new Scanner(System.in);
    private static TreeMap<Integer, MusicBand> map = new TreeMap<>();
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            clientChannel = DatagramChannel.open();
            address = new InetSocketAddress("localhost", this.port);
            clientChannel.connect(address);
            clientChannel.configureBlocking(false);
            selector = Selector.open();
            clientChannel.register(selector, SelectionKey.OP_WRITE);

            System.out.println("Enter the command, please");
            boolean param = true;
            try {
                DataForClient message = null;
                String[] parametrs = new String[]{"",""};
                String task = scanner.nextLine();
                parametrs = checkTask(task, parametrs);
                task = parametrs[0];
                while (param) {
                    switch (task) {
                        case "help":
                            DataForServer<String> help = new DataForServer<>("help", "");
                            send(help);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "info":
                            DataForServer<String> info = new DataForServer<>("info", "");
                            send(info);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "show":
                            DataForServer<String> show = new DataForServer<>("show", "");
                            send(show);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "insert":
                            DataForServer<MusicBand> insert = new DataForServer<>("insert", add(task,0));
                            send(insert);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "update":
                            if (!parametrs[1].equals("")) {
                                try {
                                    int id = Integer.parseInt(parametrs[1]);
                                    DataForServer<Integer> update = new DataForServer<>("update", id);
                                    send(update);
                                    message = receive();
                                    if (message.getMessage().equals("")) {
                                        DataForServer<MusicBand> updateId = new DataForServer<>("update", add(task, id));
                                        send(updateId);
                                        message = receive();
                                        System.out.println(message.getMessage());
                                    } else {
                                        System.out.println(message.getMessage());
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("The id must be int type. Try again");
                                }
                            } else {
                                System.out.println("You didn't enter the id");
                            }
                            break;
                        case "remove_key":
                            if (!parametrs[1].equals("")) {
                                try {
                                    int id = Integer.parseInt(parametrs[1]);
                                    DataForServer<Integer> remove = new DataForServer<>("remove_key", id);
                                    send(remove);
                                    message = receive();
                                    System.out.println(message.getMessage());
                                } catch (NumberFormatException e) {
                                    System.out.println("The id must be int type. Try again");
                                }
                            } else {
                                System.out.println("You didn't enter the id");
                            }
                            break;
                        case "clear":
                            DataForServer<String> clear = new DataForServer<>("clear", "");
                            send(clear);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "execute_script":
                            if (!parametrs[1].equals("")) {
                                String scriptName = parametrs[1];
                                DataForServer<String> executeScript = new DataForServer<>("execute_script", scriptName);
                                send(executeScript);
                                message = receive();
                                System.out.println(message);
                            } else {
                                System.out.println("You didn't enter a file name");
                            }
                            break;
                        case "exit":
                            param = false;
                            break;
                        case "remove_lower":
                            DataForServer<String> removeLessKey = new DataForServer<>("remove_lower", "");
                            send(removeLessKey);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "replace_if_greater null":
                            DataForServer<MusicBand> replacegreater = new DataForServer<>("replace_if_greater null", add(task,0));
                            send(replacegreater);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "sum_of_number_of_participants":
                            DataForServer<String> sumOfNumberOfParticipants = new DataForServer<>("sum_of_number_of_participants", "");
                            send(sumOfNumberOfParticipants);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "average_of_albums_count":
                            DataForServer<String> averageOfAlbums = new DataForServer<>("average_of_albums_count", "");
                            send(averageOfAlbums);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        case "print_unique_number_of_participants":
                            DataForServer<String> uniquenumber = new DataForServer<>("print_unique_number_of_participants", "");
                            send(uniquenumber);
                            message = receive();
                            System.out.println(message.getMessage());
                            break;
                        default:
                            System.out.println("Unknown command. Please try again");
                    }
                    if (param) {
                        System.out.println("Enter a command please");
                        task = scanner.nextLine();
                        parametrs = checkTask(task, parametrs);
                        task = parametrs[0];
                    }
                }
            } catch (NoSuchElementException e) {
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void send(DataForServer<?> command) throws IOException {
        byteBuffer.put(serialize(command));
        byteBuffer.flip();
        DatagramChannel channel = null;
        while (channel == null) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey key : selectionKeys) {
                if (key.isWritable()) {
                    channel = (DatagramChannel) key.channel();
                    channel.write(byteBuffer);
                    channel.register(selector, SelectionKey.OP_READ);
                    break;
                }
            }
        }
        byteBuffer.clear();
    }

    private DataForClient receive() throws IOException, ClassNotFoundException {
        DatagramChannel channel = null;
        while (channel == null) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey key : selectionKeys) {
                if (key.isReadable()) {
                    channel = (DatagramChannel) key.channel();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    channel.register(selector, SelectionKey.OP_WRITE);
                    break;
                }
            }
        }
        return deserialize();
    }

    private byte[] serialize(DataForServer<?> command) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(command);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        objectOutputStream.flush();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return buffer;
    }

    private DataForClient deserialize() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        DataForClient message = (DataForClient) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        byteBuffer.clear();
        return message;
    }

    /**
     * Method finds a command from the list in the input line
     * @param task
     * @return parametrs[]
     */
    public String[] checkTask(String task, String[] parametrs) {
        String[] command = task.split(" ");
        for (int i = 0; i<command.length; i++) {
            if ((command[i].equals("help")) || (command[i].equals("info")) || (command[i].equals("show")) || (command[i].equals("add")) ||
                    (command[i].equals("clear")) || (command[i].equals("exit")) || (command[i].equals("head")) ||
                    (command[i].equals("remove_head")) || (command[i].equals("add_if_max")) || (command[i].equals("sum_of_age"))) {
                parametrs[0] = command[i];
                break;
            }
            try {
                if ((command[i].equals("update")) || (command[i].equals("remove_by_id")) || (command[i].equals("execute_script")) ||
                        (command[i].equals("filter_contains_name")) || (command[i].equals("filter_less_than_age"))) {
                    parametrs[0] = command[i];
                    parametrs[1] = command[i+1];
                    break;
                } else {
                    parametrs[0] = "";
                }
            } catch (IndexOutOfBoundsException e) {
                parametrs[0] = command[i];
                parametrs[1] = "";

            }
        }
        return parametrs;
    }

    private static boolean chekByZero(String line) {
        boolean check = false;
        char[] chars = line.toCharArray();
        if (chars.length > 1 && !line.equals("-0")) {
            if (chars[0] == '0' && chars[ 1] != '.' || chars[0] == '-' && chars[1] == '0' && chars[2] != '.') {
                check = false;
            } else {
                check = true;
            }
        } else check = true;
        return check;
    }

    private MusicBand add(String task, int id) {
        System.out.println("Enter name");
        String name = scanner.nextLine();

        System.out.println("Enter X coordinate (X is long and > -898):");
        String xCheck;
        long x;
        while (true) {
            xCheck = scanner.nextLine();
            try {
                if (chekByZero(xCheck)) {
                    x = Long.parseLong(xCheck);
                    if (x < 741) {
                        if (x >= Long.MIN_VALUE) {
                            break;
                        } else throw new TooBigNumberException();
                    } else throw new NumberFormatException();
                } else throw new ZeroCheckException();
            } catch (TooBigNumberException e) {
                System.out.println("You tried to enter too big number");
            } catch (ZeroCheckException e) {
                System.out.println("You wrote '000000' or '-000000' or something same. If you want fill field like 0(1/etc),please write just 0(1/etc)");
            } catch (NumberFormatException e) {
                System.out.println("X has to be long type and  < 791.Try again");
            }
        }
        System.out.println("Enter Y coordinate (Y is long):");
        String yCheck;
        long y;
        while (true) {
            yCheck = scanner.nextLine();
            try {
                if (chekByZero(yCheck)) {
                    y = Long.parseLong(yCheck);
                    if (y >= Long.MIN_VALUE && y < 522) {
                        break;
                    } else throw new TooBigNumberException();
                } else throw new ZeroCheckException();
            } catch (TooBigNumberException e) {
                System.out.println("You tried to enter too big or too small number");
            } catch (ZeroCheckException e) {
                System.out.println("You wrote '000000' or '-000000' or something same. If you want fill field like 0(1/etc),please write just 0(1/etc)");
            } catch (NumberFormatException e) {
                System.out.println("Y has to be Long type.Try again");
            }
        }
        Coordinates coordinates = new Coordinates(x, y);

        System.out.println("Enter the x location coordinate. The x location coordinate must be float type");
        while (true) {
            try {
                x = scanner.nextLong();
                break;
            } catch (InputMismatchException e) {
                System.out.println("The x location coordinate must be float type. Try again");
                scanner.nextLine();
            }
        }
        System.out.println("Enter the y location coordinate. The y location coordinate must be int type");
        while (true) {
            try {
                y = scanner.nextLong();
                break;
            } catch (InputMismatchException e) {
                System.out.println("The y location coordinate must be int type. Try again");
                scanner.nextLine();
            }
        }
        System.out.println("Enter Number Of Participants (Number Of Participants is integer and >0):");
        String numberOfParticipantsCheck;
        long numberOfParticipants;
        while (true) {
            numberOfParticipantsCheck = scanner.nextLine();
            try {
                numberOfParticipants = Long.parseLong(numberOfParticipantsCheck);
                if (chekByZero(numberOfParticipantsCheck)) {
                    if (numberOfParticipants > 0) {
                        if (numberOfParticipants <= Integer.MAX_VALUE) {
                            break;
                        } else throw new TooBigNumberException();
                    } else throw new NumberFormatException();
                } else throw new ZeroCheckException();
            } catch (TooBigNumberException e) {
                System.out.println("You tried to enter too big number");
            } catch (ZeroCheckException e) {
                System.out.println("You wrote '000000' or '-000000' or something same. If you want fill field like 0(1/etc),please write just 0(1/etc)");
            } catch (NumberFormatException e) {
                System.out.println("Engine power has to be integer and > 0. Try again");
            }
        }

        System.out.println("Enter albums Count (albums Count is int and >0):");
        String albumsCountCheck;
        long albumsCount;
        while (true) {
            albumsCountCheck = scanner.nextLine();
            try {
                if (chekByZero(albumsCountCheck)) {
                    albumsCount = Long.parseLong(albumsCountCheck);
                    if (albumsCount > 0) {
                        break;
                    } else throw new NumberFormatException();
                } else throw new ZeroCheckException();
            } catch (ZeroCheckException e) {
                System.out.println("You wrote '000000' or '-000000' or something same. If you want fill field like 0(1/etc),please write just 0(1/etc)");
            } catch (NumberFormatException e) {
                System.out.println("Capacity has to be int and > 0. Try again");
            }
        }
        System.out.println("Choose genre of music: " + MusicGenre.showAllValues());
        String vehicleTypeCheck = scanner.nextLine();
        MusicGenre genre;
        try {
            genre = MusicGenre.valueOf(vehicleTypeCheck);
        } catch (IllegalArgumentException e) {
            System.out.println("You tried to enter incorrect data. The field will be null");
            genre = null;
        }
        System.out.println("Choose the best album: ");
        Album bestAlbum;
        String albumName = scanner.nextLine();
        String lenghth = scanner.nextLine();

        bestAlbum = new Album(albumName, lenghth);

        MusicBand musicBand = new MusicBand(name,coordinates, numberOfParticipants,albumsCount,genre, bestAlbum);
        return musicBand;
    }
}
