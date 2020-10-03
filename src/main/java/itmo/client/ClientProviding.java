package itmo.client;

import itmo.exceptions.NoCorrectInputException;
import itmo.utils.UserManager;
import lab6common.data.Driver;
import lab6common.data.Package;

import java.io.*;
import java.net.*;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ClientProviding {
    public static final Logger LOGGER = Logger.getLogger(ClientProviding.class.getName());
    private DataExchangeClient dataExchangeClient;
    private final Charset charset = StandardCharsets.UTF_8;
    private UserManager userManager;
    private SocketAddress socketAddress;
    private SocketChannel chanell;
    private Selector selector;
    private int PORT;
    private String ADDRESS;
    private Driver driver;
    public ClientProviding() {
        this.userManager = new UserManager(
                new BufferedReader(new InputStreamReader(System.in, charset)),
                new BufferedWriter(new OutputStreamWriter(System.out, charset)), true);
        dataExchangeClient = new DataExchangeClient();
    }

    /**
     * Устанавливает активное соединение с сервером.
     */
    public void work() throws IOException {
        try {
            setSocketAddress();
            this.driver = userManager.readDriver();
            selector = Selector.open();
            clientApp();
            selector.close();
        }catch (NoSuchElementException e) {
            LOGGER.log(Level.INFO, "Завершение программы");
        }
    }

    private void setSocketAddress() {
       ADDRESS = userManager.readString("Введите адрес : ", false);
       PORT = userManager.parseIntInputWithParameters("Введите порт: ",1,65535);
       socketAddress = new InetSocketAddress(ADDRESS, PORT);
    }

    public void clientApp() {
        boolean connect = false;
        boolean wassend = true;
        Package apackage = null;
        String[] request;
        while (true) {
            try {
                if (wassend) {
                    request = userManager.readRequest();
                    if (request[0].equals("exit")) {
                        break;
                    } else {
                        apackage = userManager.createPackage(request,driver);
                        connect = true;
                        wassend = false;

                    }

                }
            } catch (NoCorrectInputException e) {
                LOGGER.log(Level.ERROR, e.getMessage());
            }
            try {
                if (connect) {
                    wassend = dataExchangeWithServer(connect, apackage);
                }
            } catch (ConnectException e) {
                wassend = reconnecting();
            } catch (EOFException e) {
                LOGGER.log(Level.ERROR, "Ошибка при чтении потока");
            } catch (ClassNotFoundException | ClassCastException e) {
                LOGGER.log(Level.ERROR, "Ошибка при создании класса");
                e.printStackTrace();
            } catch (NoRouteToHostException e) {
                LOGGER.log(Level.ERROR, "Неверный адрес");
                ADDRESS = userManager.readString("Соберись и введи норм адрес : ", false);
                socketAddress = new InetSocketAddress(ADDRESS, PORT);
            } catch (UnresolvedAddressException e) {
                LOGGER.log(Level.ERROR, "Ловушка Афанаса, FFFFFFFF");
                ADDRESS = userManager.readString("Соберись и введи норм адрес : ", false);
                socketAddress = new InetSocketAddress(ADDRESS, PORT);
            } catch (IOException e) {
                LOGGER.log(Level.ERROR, "Потоковая ошиб очка");
            } catch (NoSuchElementException e) {
                LOGGER.log(Level.ERROR, "Ошибка при чтении ввода");
            }
        }
    }

    public boolean reconnecting() {
        LOGGER.log(Level.ERROR, "Нет связи с сервером. Подключться ещё раз (введите {yes} или {no})?");
        String answer;
        do{
                answer = userManager.readString("Ответ : ", false);
            if (answer.equals("no")) {
                return true;
            } else if (!answer.equals("yes")) {
                userManager.writeln("Введите корректный ответ.");
            }
        } while (!answer.equals("yes"));
        return false;
    }

    public boolean dataExchangeWithServer(boolean connect, Package aPackage) throws IOException, EOFException, ClassNotFoundException {
        connect();
        LOGGER.log(Level.INFO, "Подключение к серверу");
        boolean successSend = false;
        while (connect) {
            selector.selectNow();
            Iterator selectedKeys = this.selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = (SelectionKey) selectedKeys.next();
                selectedKeys.remove();
                if (!key.isValid()) {
                    connect = false;
                    break;
                }
                if (key.isConnectable()) {
                    chanell.finishConnect();
                    key.interestOps(SelectionKey.OP_WRITE);
                } else if (key.isReadable()) {
                    userManager.writeAnsFromServer(dataExchangeClient.packageGet());
                    connect = false;
                    break;
                } else if (key.isWritable()) {
                    chanell.finishConnect();
                    dataExchangeClient.packageSend(aPackage);
                    key.interestOps(SelectionKey.OP_READ);
                    successSend = true;
                }
            }
        }
        close();
        return successSend;
    }


    public void connect() throws IOException {
        chanell = SocketChannel.open();
        chanell.configureBlocking(false);
        chanell.connect(socketAddress);
        dataExchangeClient.connect(chanell);
        chanell.register(selector, SelectionKey.OP_WRITE);
    }

    public void close() throws IOException {
        chanell.close();
    }


}