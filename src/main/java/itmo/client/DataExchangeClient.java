package itmo.client;

import lab6common.data.Package;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class DataExchangeClient{

    public static final Logger LOGGER = Logger.getLogger(DataExchangeClient.class.getName());
    private ByteBuffer byteBufferGet;
    private ByteBuffer byteBufferSend;
    private SocketChannel socketChannel;

    public DataExchangeClient(){
        this.byteBufferGet =  ByteBuffer.allocate(1024);
    }
    public void connect (SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }

    public void packageSend(Package request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(request);
        oos.flush();
        byteBufferSend = ByteBuffer.wrap(baos.toByteArray());
        while (socketChannel.write(byteBufferSend) > 0);
        LOGGER.log(Level.INFO,"Запрос отпрален");
    }

    public Package packageGet() throws IOException, ClassCastException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(listToByteArray(getBytesFromChannel()));
        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
        Package answer = (Package) objectInputStream.readObject();
        return answer;
    }



    private byte[] listToByteArray(ArrayList<Byte> arrayList) {
        byte[] bytes = new byte[arrayList.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = arrayList.get(i);
        }
        return bytes;
    }

    public ArrayList<Byte> getBytesFromChannel() throws IOException {
        ArrayList<Byte> arrayBuffer = new ArrayList<>();
        int a;
        do {
            byteBufferGet.clear();
            a = socketChannel.read(byteBufferGet);
            byteBufferGet.flip();
            for (int i = 0; i < byteBufferGet.limit(); i++) {
                arrayBuffer.add(byteBufferGet.get());
            }
        }
        while (a > -1);
        return arrayBuffer;
    }

}
