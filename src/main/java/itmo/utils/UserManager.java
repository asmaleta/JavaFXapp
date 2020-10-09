package itmo.utils;

import itmo.client.ClientProviding;
import itmo.gui.AlertMaker;
import javafx.scene.control.TextArea;
import lab6common.data.Driver;
import lab6common.data.Package;
import lab6common.generatedclasses.*;
import itmo.exceptions.NoCorrectInputException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class UserManager {
    public static final Logger LOGGER = Logger.getLogger(UserManager.class.getName());
    private HashSet<String> map = new HashSet<>();
    private Reader reader;
    private Writer writer;
    private Scanner scanner;
    private boolean manualInput;


    private Driver driver;

    public UserManager(Reader reader, Writer writer, boolean manualInput) {
        registerCommands();
        this.reader = reader;
        this.writer = writer;
        this.scanner = new Scanner(reader);
        this.manualInput = manualInput;
    }

    private void registerCommands() {
        map.add("add");
        map.add("clear");
        map.add("execute_script");
        map.add("filter_less_than_distance");
        map.add("help");
        map.add("history");
        map.add("info");
        map.add("print_ascending");
        map.add("remove_by_id");
        map.add("remove_greater");
        map.add("remove_lower");
        map.add("sum_of_distance");
        map.add("update_id");
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public boolean isManualInput() {
        return manualInput;
    }

    /**
     * Метод считывающий из потока строку
     *
     * @return строка
     */
    public String read() {
        return scanner.nextLine();
    }

    /**
     * Выводит в поток вывода строку.
     *
     * @param message строка для вывода.
     */
    public void write(String message) {
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            System.out.print("Error while outputting (( ");
        }
    }

    /**
     * Выводит в поток вывода строку с добавлением символа переноса.
     *
     * @param message строка для вывода.
     */
    public void writeln(String message) {
        write(message + "\n");
    }

    public void writeAnsFromServer(Package pack) {
        System.out.println(pack.getAns());
    }

    /**
     * Метод возрващающий есть ли что считывать из входного потока.
     *
     * @return Есть ли ещё что считывать.
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public Route readRoute() throws NoSuchElementException, NoCorrectInputException {
        String name;
        do {
            name = readString("Enter route name: ", false);
        } while (name.isEmpty());
        Coordinates coordinates = readCoordinates();
        Location to = readLocationTo();
        Location from = readLocationFrom();
        Float distance = parseFloatInputWithParameters("Enter route length greater than 1: ", 1.0f, Float.POSITIVE_INFINITY);
        return new Route(name, coordinates, from, to, distance);
    }

    public String[] inputProcessing(String line) throws NoCorrectInputException {
        return line.trim().indexOf(" ") == -1 ? new String[]{line.trim(), ""} : line.trim().split(" ");
    }

    public Location readLocationTo() throws NoCorrectInputException {
        String name = readString("Введите название места отправления: ", true);
        Long x = parseLongInput("Введите координату места отправления x (Long): ");
        Long y = parseLongInput("Введите координату места отправления y (Long): ");
        return new Location(x, y, name);
    }

    public Location readLocationFrom() throws NoCorrectInputException {
        String name = readString("Введите название места прибытия: ", true);
        Long x = parseLongInput("Введите координату места прибытия x (Long): ");
        Long y = parseLongInput("Введите координату места прибытия y (Long): ");
        return new Location(x, y, name);
    }


    public Coordinates readCoordinates() throws NoCorrectInputException {
        Long x = parseLongInput("Введите текущую координату местанахождения x (Long): ");
        Integer y = parseIntInput("Введите текущую координату местанахождения y (int): ");
        return new Coordinates(x, y);
    }

    /**
     * Метод проверяет строоку на числовое значение
     *
     * @param input строка которую парсим
     * @return true это значние Float
     */

    public boolean checkFloatInput(String input) throws NoCorrectInputException {
        try {
            if (input == null) {
                if (!manualInput) throw new NoCorrectInputException(input + " - this is not null for you");
                writeln(input + " - this is not null for you");
                return false;
            }
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            if (!manualInput) throw new NoCorrectInputException(input + " - this is not Float for you");
            writeln(input + " - this is not Float for you");
            return false;
        }
    }

    /**
     * Метод проверяет строоку на числовое значение
     *
     * @param input строка которую парсим
     * @return true это значние int
     */
    public boolean checkIntInput(String input) throws NoCorrectInputException {
        try {
            if (input == null) {
                if (!manualInput) throw new NoCorrectInputException(input + "- this is not null for you");
                writeln(input + " - this is not null for you");
                return false;
            }
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            if (!manualInput) throw new NoCorrectInputException(input + " - this is not Integer for you");
            writeln(input + " - this is not Integer for you");
            return false;
        }
    }

    public boolean checkIntInputWithParameters(String input, int min, int max) {
        if (checkIntInput(input)) {
            if (Integer.parseInt(input) >= min && Integer.parseInt(input) <= max) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод проверяет строоку на числовое значение
     *
     * @param input строка которую парсим
     * @return true это значние Long
     */
    public boolean checkLongInput(String input) throws NoCorrectInputException {
        try {
            if (input == null) {
                if (!manualInput) throw new NoCorrectInputException(input + " - this is not null for you");
                writeln(input + " - this is not null for you");
                return false;
            }
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            if (!manualInput) throw new NoCorrectInputException(input + " - this is not Long for you");
            writeln(input + " - this is not Long for you");
            return false;
        }
    }

    /**
     * Метод парсит в определенном диапазоне
     */

    public Float parseFloatInputWithParameters(String message, float min, float max) {
        Float res;
        do {
            res = parseFloatInput(message);
        } while (Float.sum(res, -min) < -0.001f || Float.sum(res, -max) > 0.001f);
        return res;
    }

    /**
     * Метод парсит int в определенном диапазоне
     */

    public Integer parseIntInputWithParameters(String input, int min, int max) {
        Integer res;
        do {
            res = parseIntInput(input);
        } while (Integer.sum(res, -min) < -0.00001 || Integer.sum(res, -max) > 0.00001);
        return res;
    }

    /**
     * Метод парсит строку в Float
     *
     * @param message
     */

    public Float parseFloatInput(String message) {
        String res;
        do {
            res = readString(message, false);
        } while (!checkFloatInput(res));
        return Float.parseFloat(res);
    }

    /**
     * Метод парсит строку в Int
     */

    public Integer parseIntInput(String message) {
        String res;
        do {
            res = readString(message, false);
        } while (!checkIntInput(res));
        return Integer.parseInt(res);
    }

    /**
     * Метод проверяет строоку на Long
     */

    public Long parseLongInput(String message) {
        String res;
        do {
            res = readString(message, false);
        } while (!checkLongInput(res));
        return Long.parseLong(res);
    }


    public boolean checkString(String message, boolean nullable) {
        message = message.trim();
        return !message.equals("") && message != null || nullable;
    }

    public boolean checkStringRegex(String message, String regex) {
        return Pattern.matches(regex, message);
    }

    /**
     * Метод проверяет строоку на null
     *
     * @param message  Сообщение для пользователя
     * @param nullable Флаг. True - если мы допускаем пустой ввод от пользователя. False - если нам надо добиться от него не пустого ввода.
     */

    public String readString(String message, boolean nullable) throws NoSuchElementException {
        String result = "";
        do {
            if (result == null) {
                writeln("Please enter a non-empty string.");
            }
            if (!manualInput && !hasNextLine()) {
                scriptModeOff();
                LOGGER.log(Level.ERROR, "Insufficient data entered");
            }
            if (manualInput) {
                write(message);
            }
            result = scanner.nextLine();
            result = result.isEmpty() ? null : result;
        } while (manualInput && !nullable && result == null);
        if (!manualInput && result == null) {
            LOGGER.log(Level.ERROR, "This field cannot be null");
        }
        return result;
    }


    public void readRequests(TextArea output, ClientProviding clientProviding) {
        String line;
        String[] inputProcessing = new String[2];
        while (scanner.hasNext()) {
            line = read();
            if (line == null) {
                output.appendText("\nEntering not an empty string");
            } else {
                inputProcessing = inputProcessing(line);
                if (haveCommand(inputProcessing[0]) && chekArg(inputProcessing)) {
                    output.appendText("\nCommand read : " + inputProcessing[0]);
                    try {
                        output.appendText((String) "\n" + clientProviding.dataExchangeWithServer(inputProcessing[0]
                                , inputProcessing[1], needRoute(inputProcessing[0])).getAns());
                    }catch (NoCorrectInputException e){
                        e.printStackTrace();
                        output.appendText("\n"+e.getMessage());
                    }
                }else{
                    output.appendText("\nInvalid command or argument");
                }
            }
        }
        scriptModeOff();
    }

    //execute_script clientMod/script.txt
    public void scriptModeOff() {
        setScanner(new Scanner(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))));
        manualInput = true;
    }

    public boolean scriptModeOn(String path) {
        try {
            setScanner(new Scanner(new FileReader(path)));
            manualInput = false;
            return true;
        } catch (NullPointerException e) {
            AlertMaker.showErrorMessage("Load file error", "The file is empty!!");
            LOGGER.log(Level.ERROR, "The file is empty!");
            return false;
        } catch (FileNotFoundException e) {
            AlertMaker.showErrorMessage("Load file error", "The file does not exist at the specified path!");
            LOGGER.log(Level.ERROR, "The file does not exist at the specified path!");
            return false;
        } catch (IOException e) {
            AlertMaker.showErrorMessage("Load file error", "Error while working with file");
            LOGGER.log(Level.ERROR, "Error while working with file\n");
            return false;
        }
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public boolean haveCommand(String line) {
        if (map.contains(line)) {
            return true;
        } else {
            LOGGER.log(Level.INFO, "Invalid command name: " + line);
            return false;
        }
    }

    public Package createPackage(String command, String arg, Route route) throws NoCorrectInputException {
        switch (command) {
            case "filter_less_than_distance":
                return new Package(command, Float.parseFloat(arg), driver);
            case "remove_by_id":
                return new Package(command, Integer.parseInt(arg), driver);
            case "update_id":
                return new Package(command, Integer.parseInt(arg), route, driver);
            case "add":
                return new Package(command, null, route, driver);
            case "remove_greater":
                return new Package(command, null, route, driver);
            case "remove_lower":
                return new Package(command, null, route, driver);
            default:
                return new Package(command, null, driver);
        }
    }
    public Route needRoute(String command){
        switch (command) {
            case "update_id":
                return readRoute();
            case "remove_greater":
                return readRoute();
            case "remove_lower":
                return readRoute();
            case "add":
                return readRoute();
            default:
                return null;
        }
    }
    public boolean chekArg(String[] line) {
        switch (line[0]) {
            case "filter_less_than_distance":
                return checkFloatInput(line[1]);
            case "remove_by_id":
                return checkLongInput(line[1]);
            case "update_id":
                return checkLongInput(line[1]);
            default:
                return true;
        }


    }
}
