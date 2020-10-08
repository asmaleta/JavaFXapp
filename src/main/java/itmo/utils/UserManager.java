package itmo.utils;

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
        map.add("show");
        map.add("update_id");
        map.add("exit");
        map.add("registration");
        map.add("remove_driver");
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
            System.out.print("Ошибка при выводе(( ");
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
            name = readString("Введите название маршрута: ", false);
        } while (name.isEmpty());
        Coordinates coordinates = readCoordinates();
        Location to = readLocationTo();
        Location from = readLocationFrom();
        Float distance = parseFloatInputWithParameters("Введите длину маршрута больше 1: ", 1.0f, Float.POSITIVE_INFINITY);
        return new Route(name, coordinates, from, to, distance);
    }

    public String[] inputProcessing(String line) throws NoCorrectInputException{
        return line.trim().indexOf(" ") == -1 ? new String[]{line.trim(), ""} : line.trim().split(" ");
    }

    public Location readLocationTo() throws NoCorrectInputException {
        String name = readString("Введите название места отправления: ", true);
        Long x = parseLongInput("Введите координату места отправления x (Long): ");
        Long y = parseLongInput("Введите координату места отправления y (Long): ");
        return new Location(x, y, name);
    }

    public Location readLocationFrom() throws NoCorrectInputException{
        String name = readString("Введите название места прибытия: ", true);
        Long x = parseLongInput("Введите координату места прибытия x (Long): ");
        Long y = parseLongInput("Введите координату места прибытия y (Long): ");
        return new Location(x, y, name);
    }


    public Coordinates readCoordinates() throws NoCorrectInputException{
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
                if(!manualInput) throw new NoCorrectInputException(input + " - это вам не null");
                writeln(input + " - это вам не null");
                return false;
            }
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            if(!manualInput) throw new NoCorrectInputException(input + " - это вам не Float");
            writeln(input + " - это вам не Float");
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
                if(!manualInput) throw new NoCorrectInputException(input + " - это вам не null");
                writeln(input + " - это вам не null");
                return false;
            }
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            if(!manualInput) throw new NoCorrectInputException(input + " - это вам не Integer");
            writeln(input + " - это вам не Integer");
            return false;
        }
    }
    public boolean checkIntInputWithParameters(String input, int min, int max) {
        if (checkIntInput(input)){
            if (Integer.parseInt(input) >= min && Integer.parseInt(input) <= max){
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
                if(!manualInput) throw new NoCorrectInputException(input + " - это вам не null");
                writeln(input + " - это вам не null");
                return false;
            }
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            if(!manualInput) throw new NoCorrectInputException(input + " - это вам не Long");
            writeln(input + " - это вам не Long");
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

    public int parseIntInputWithParameters(String input, int min, int max) {
        int res;
        do {
            res = parseIntInput(input);
        } while (Integer.sum(res, -min) < -0.00001 || Integer.sum(res, -max) > 0.00001);
        return res;
    }
    /**
     * Метод парсит строку в Int
     * @param input
     */

    public Float parseFloatInput(String input) {
        String res;
        do {
            res = input;
        } while (!checkFloatInput(res));
        return Float.parseFloat(res);
    }

    /**
     * Метод парсит строку в Int
     */

    public Integer parseIntInput(String message){
        String res;
        do {
            res = readString(message, false);
        } while (!checkIntInput(res));
        return Integer.parseInt(res);
    }

    /**
     * Метод проверяет строоку на Long
     */

    public Long parseLongInput(String message){
        String res;
        do {
            res = readString(message, false);
        } while (!checkLongInput(res));
        return Long.parseLong(res);
    }



    public boolean checkString (String message, boolean nullable){
        message = message.trim();
        return !message.equals("") && message != null || nullable;
    }
    public boolean checkStringRegex (String message, String regex){
        return Pattern.matches(regex,message);
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
                writeln("Введите не пустую строку.");
            }
            if (!manualInput && !hasNextLine()) {
                scriptModeOff();
                LOGGER.log(Level.ERROR,"Недостаточно введенных данных");
            }
            if (manualInput) {
                write(message);
            }
            result = scanner.nextLine();
            result = result.isEmpty() ? null : result;
        } while (manualInput && !nullable && result == null);
        if (!manualInput && result == null) {
            LOGGER.log(Level.ERROR,"Это поле не может быть null");
        }
        return result;
    }

    public Driver readDriver() {
        boolean flag = true;
        String line = null;
        Driver driver = new Driver();
        while (flag) {
            write("Введите логин: ");
            line = read();
            line  = line.replaceAll("\\s+", "");
            if (line == null || line.equals("")) {
                writeln("Введие не пустую строку");
            } else {
                driver.setLogin(line);
                flag = false;
            }
        }
        flag = true;
        while (flag) {
            write("Введите пароль: ");
            line = read();
            line = line.replaceAll("\\s+", "");
            if (line == null || line.equals("")) {
                writeln("Введие не пустую строку");
            } else {
                driver.setPassword(line);
                flag = false;
            }
        }
        return driver;
    }
    public String[] readRequest() {
        boolean flag = true;
        String line;
        String[] inputProcessing = new String[2];
        while (flag) {
            if (!manualInput && !hasNextLine()) {
                scriptModeOff();
            }
            if (manualInput) {
                write("Введите команду: ");
            }
            line = read();
            if (line == null) {
                writeln("Введие не пустую строку");
            } else {
                inputProcessing = inputProcessing(line);
                if (haveCommand(inputProcessing[0]) && chekArg(inputProcessing)) {
                    if (!manualInput){
                        writeln("Считана команда : " + inputProcessing[0]);
                    }
                    if (inputProcessing[0].equals("execute_script")) {
                        scriptModeOn(inputProcessing[1]);
                    } else {
                        flag = false;
                    }
                }
            }
        }
        return inputProcessing;
    }
//execute_script clientMod/script.txt
    public void scriptModeOff() {
        setScanner(new Scanner(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))));
        manualInput = true;
        writeln("Скрипт выполнен");
    }

    public void scriptModeOn(String path) {
        try {
            setScanner(new Scanner(new FileReader(path)));
            manualInput = false;
        } catch (NullPointerException e) {
            LOGGER.log(Level.ERROR, "Файл пуст!");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.ERROR, "Файла по указанному пути не существует!");
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Ошибка при рабтое с файлом");
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
            LOGGER.log(Level.INFO,"Неверное имя команды : " + line);
            return false;
        }
    }

    public Package createPackage(String[] line, Driver driver) throws NoCorrectInputException {
            switch (line[0]) {
                case "filter_less_than_distance":
                    return new Package(line[0], Float.parseFloat(line[1]),driver);
                case "remove_by_id":
                    return new Package(line[0], Integer.parseInt(line[1]),driver );
                case "update_id":
                    return new Package(line[0], Integer.parseInt(line[1]), readRoute(),driver);
                case "add":
                    return new Package(line[0], null, readRoute(), driver );
                case "remove_greater":
                    return new Package(line[0], null, readRoute(), driver);
                case "remove_lower":
                    return new Package(line[0], null, readRoute(), driver);
                default:
                    return new Package(line[0], null,driver );
            }
    }

    public boolean chekArg(String [] line){
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
