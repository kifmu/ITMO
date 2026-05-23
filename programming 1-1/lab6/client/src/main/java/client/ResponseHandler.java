package client;

import common.protocol.CommandResponse;
import common.util.ObjectSerializer;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ResponseHandler {
    private final DatagramChannel channel;
    private final long timeout;
    private final ByteBuffer buffer = ByteBuffer.allocate(65535);

    public ResponseHandler(DatagramChannel channel, long timeout) {
        this.channel = channel;
        this.timeout = timeout;
    }

    public CommandResponse receive() throws Exception {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeout) {
            buffer.clear();
            channel.configureBlocking(false);

            if (channel.read(buffer) > 0) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                return (CommandResponse) ObjectSerializer.deserialize(data);
            }
            Thread.sleep(100);
        }
        return null;
    }
}