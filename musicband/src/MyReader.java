import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
/**
 * Class for checking
 */
public class MyReader {
    /**
     * Check file on big amounts of zero
     *
     * @param line - line where could be some problem with a lot of zeros
     * @return
     */
    private static boolean chekByZero(String line) {
        boolean check = false;
        char[] chars = line.toCharArray();
        if (chars.length > 1 && !line.equals("-0")) {
            if (chars[0] == '0' && chars[1] != '.' || chars[0] == '-' && chars[1] == '0' && chars[2] != '.') {
                check = false;
            } else {
                check = true;
            }
        } else check = true;
        return check;
    }

    /**
     * Method gets new <i>MusicBand</i> from User
     *
     * @param scanner
     * @return MusicBand
     */
    public static MusicBand getElementFromConsole(Scanner scanner) {

        String name;
        while (true) {
            System.out.println("Enter the name:");
            name = scanner.nextLine();
            if (name != null) {
                break;
            }
        }
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


        MusicBand musicBand = new MusicBand(name, coordinates, numberOfParticipants, albumsCount, genre, bestAlbum);
        return musicBand;
    }

    static File fileWay;
    static boolean checkEnvironmentVariable = true;

    public void setFileWay(String fileName) {
        fileWay = new File(fileName);
        checkEnvironmentVariable = false;
    }

    public void readFile(Band musicBand) throws FileNotFoundException {
        if (checkEnvironmentVariable) {
            fileWay = new File(System.getenv("enV"));
        }
        if (!fileWay.exists()) {
            System.out.println("File doesn't exist");
            throw new FileNotFoundException();
        }
        if (!fileWay.canWrite()) {
            System.out.println("Permission denied");
            throw new FileNotFoundException();
        }
    }
}


