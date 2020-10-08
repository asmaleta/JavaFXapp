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
    public ClientProviding(UserManager userManager) {
        this.userManager = userManager;
        dataExchangeClient = new DataExchangeClient();
    }

    public void setSocketAddress(String address,int port) {
       ADDRESS = address;
       PORT = port;
       socketAddress = new InetSocketAddress(ADDRESS, PORT);
    }

    public void clientApp() {
            /*try {

            } catch (ConnectException e) {

            } catch (EOFException e) {
                LOGGER.log(Level.ERROR, "Ошибка при чтении потока");
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
            }*/
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

    public Package dataExchangeWithServer(Package aPackage) throws IOException, ClassNotFoundException {
        connect();
        LOGGER.log(Level.INFO, "Подключение к серверу");
        boolean successSend = false;
        boolean connect = true;
        Package ans = new Package("Данные не были получены");
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
                    ans = dataExchangeClient.packageGet();
                    userManager.writeAnsFromServer(ans);
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
        return ans;
    }
    public boolean testConnect() throws IOException {
        chanell = SocketChannel.open();
        chanell.configureBlocking(false);
        chanell.connect(socketAddress);
        chanell.finishConnect();
        return chanell.isConnectionPending() || chanell.isConnected();
    }
    public void connect() throws IOException {
        selector = Selector.open();
        chanell = SocketChannel.open();
        chanell.configureBlocking(false);
        chanell.connect(socketAddress);
        dataExchangeClient.connect(chanell);
        chanell.register(selector, SelectionKey.OP_CONNECT);
    }

    public void close() throws IOException {
        chanell.close();
    }


}