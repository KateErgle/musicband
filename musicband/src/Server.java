import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.TreeMap;

public class Server {
    private int port;
    private DatagramSocket socket;
    private InetAddress address;

    TreeMap<Integer, MusicBand> map = new TreeMap<>();

    public Server(int port) {
        this.port = port;
    }
    public void run(MusicBand musicBand) {

        try {
            ServerInput input = new ServerInput(musicBand);
            input.start();
            socket = new DatagramSocket(5000);
            DataForServer command = null;
            DataForClient message = null;
            Band musicBand1;

            while (true) {
                command =  getCommand();
                switch (command.getCommandName()) {
                    case "help":
                        message = new DataForClient(musicBand.help());
                        sendMessage(message);
                        break;
                    case "info":
                        message = new DataForClient(musicBand.printInfo());
                        sendMessage(message);
                        break;
                    case "show":
                        message = new DataForClient(musicBand.showAll());
                        sendMessage(message);
                        break;
                    case "insert":
                        musicBand1 = (MusicBand) command.getArgument();
                        message = new DataForClient(MusicBand.add(musicBand1));
                        sendMessage(message);
                        break;
                    case "update":
                        if (map.size > 0) {
                            int updateId = (int) command.getArgument();
                            if (musicBand.checkIdForExistence(updateId)) {
                                musicBand.removeById(updateId);
                                message = new DataForClient("");
                                sendMessage(message);
                                command = getCommand();
                                musicBand = (MusicBand) command.getArgument();
                                message = new DataForClient(musicBand.updateByKey(updateId));
                                sendMessage(message);
                                break;
                            } else {
                                message = new DataForClient("There is no music band with this id in the collection");
                                sendMessage(message);
                                break;
                            }
                        } else {
                            System.out.println("Collection is empty");
                        }
                        break;
                    case "remove_by_id":
                        if (MusicBand.() > 0) {
                            int id = (int) command.getArgument();
                            message = new DataForClient(musicBand.remove(id));
                            sendMessage(message);
                        } else {
                            message = new DataForClient("Collection is already empty");
                            sendMessage(message);
                        }
                        break;
                    case "clear":
                        message = new DataForClient(musicBand.clear());
                        sendMessage(message);
                        break;
                    case "add_if_max":
                        musicBand = (MusicBand) command.getArgument();
                        message = new DataForClient(musicBand.addIfMax(musicBand));
                        sendMessage(message);
                        break;
                    case "sum_of_age":
                        message = new DataForClient(dragonCollection.sumOfAge());
                        sendMessage(message);
                        break;
                    case "filter_contains_name":
                        String name = (String) command.getArgument();
                        message = new DataForClient(dragonCollection.filterContainsName(name));
                        sendMessage(message);
                        break;
                    case "filter_less_than_age":
                        long age = (long) command.getArgument();
                        message = new DataForClient(dragonCollection.filterLessThanAge(age));
                        sendMessage(message);
                        break;
                    default:
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DataForServer getCommand() throws IOException, ClassNotFoundException {
        byte[] getBuffer = new byte[socket.getReceiveBufferSize()];
        DatagramPacket getPacket = new DatagramPacket(getBuffer, getBuffer.length);
        socket.receive(getPacket);
        address = getPacket.getAddress();
        port = getPacket.getPort();
        return deserialize(getPacket, getBuffer);
    }

    private void sendMessage(DataForClient message) throws IOException {
        byte[] sendBuffer = serialize(message);
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        socket.send(sendPacket);
    }

    private DataForServer deserialize(DatagramPacket getPacket, byte[] buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getPacket.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        DataForServer command = (DataForServer) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return command;
    }

    private byte[] serialize(DataForClient message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(message);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        objectOutputStream.flush();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return buffer;
    }
}
