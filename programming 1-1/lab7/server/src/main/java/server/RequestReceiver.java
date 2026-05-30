package server;

import common.protocol.CommandRequest;
import common.util.ObjectSerializer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class RequestReceiver {
    private final DatagramChannel channel;

    public RequestReceiver(DatagramChannel channel) {
        this.channel = channel;
    }

    /**
     * Класс для хранения данных запроса + адрес клиента
     */
    public static class RequestData {
        public final CommandRequest request;
        public final InetSocketAddress clientAddress;

        public RequestData(CommandRequest request, InetSocketAddress clientAddress) {
            this.request = request;
            this.clientAddress = clientAddress;
        }
    }

    /**
     * Неблокирующее чтение запроса
     */
    public RequestData receiveNonBlocking() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(65535);
        InetSocketAddress clientAddr = (InetSocketAddress) channel.receive(buffer);

        if (clientAddr == null) {
            return null;
        }

        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        CommandRequest request = (CommandRequest) ObjectSerializer.deserialize(data);

        return new RequestData(request, clientAddr);
    }
}