package server;

import common.protocol.CommandResponse;
import common.util.ObjectSerializer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;

public class ResponseSender {
    private final DatagramChannel channel;

    public ResponseSender(DatagramChannel channel) {
        this.channel = channel;
    }

    public void send(CommandResponse response, SelectionKey key) throws Exception {
        InetSocketAddress clientAddress = (InetSocketAddress) key.attachment();
        if (clientAddress == null) {
            return;
        }

        byte[] data = ObjectSerializer.serialize(response);

        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.send(buffer, clientAddress);
    }

}