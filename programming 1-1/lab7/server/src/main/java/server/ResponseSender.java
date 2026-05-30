package server;

import common.protocol.CommandResponse;
import common.util.ObjectSerializer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ResponseSender {
    private final DatagramChannel channel;

    public ResponseSender(DatagramChannel channel) {
        this.channel = channel;
    }
    public void send(CommandResponse response, InetSocketAddress clientAddress) throws Exception {
        byte[] data = ObjectSerializer.serialize(response);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.send(buffer, clientAddress);
    }
}