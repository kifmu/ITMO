package client;

import common.protocol.CommandRequest;
import common.util.ObjectSerializer;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UdpSender {
    private final DatagramChannel channel;

    public UdpSender(DatagramChannel channel) {
        this.channel = channel;
    }

    public void send(CommandRequest request) throws Exception {
        byte[] data = ObjectSerializer.serialize(request);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.write(buffer);
    }
}
