import java.io.IOException;
import java.util.Scanner;

public class ServerInput extends Thread{
    private MusicBand musicBand;

    public ServerInput(MusicBand collection) {
        this.musicBand = collection;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String command = scanner.nextLine();
            if (command.equals("save")) {
                try {
                    musicBand.saveToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Collection is saved to a file");
            } else {
                System.out.println("Command \"" + command + "\" doesn't exists");
            }
        }
    }
}
