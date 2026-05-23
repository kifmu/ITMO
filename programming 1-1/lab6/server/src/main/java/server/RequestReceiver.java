package server;

import common.protocol.CommandRequest;
import common.util.ObjectSerializer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class RequestReceiver {
    private final DatagramChannel channel;
    private final ByteBuffer buffer = ByteBuffer.allocate(65535);

    public RequestReceiver(DatagramChannel channel) {
        this.channel = channel;
    }

    public CommandRequest receive(SelectionKey key) throws Exception {
        buffer.clear();

        InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
        if (clientAddress == null) {
            return null;
        }

        key.attach(clientAddress);

        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        return (CommandRequest) ObjectSerializer.deserialize(data);
    }
}