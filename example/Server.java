import com.alxan.noteefy.publish.broadcaster.Broadcaster;
import com.alxan.noteefy.web.NoteefyWeb;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.exception.NotConnectedException;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

class Server {
    public static UUID publisherId;

    public static void main(String[] args) throws InterruptedException, NotConnectedException {
        NoteefyWeb noteefyWeb = new NoteefyWeb();
        RemoteAddress remoteAddress = new RemoteAddress(8088, "0.0.0.0");
        noteefyWeb.createServer(TcpDataSourceInfo.getInstance(), remoteAddress);

        Broadcaster publisher = createPublisher();
        publishWithInterval(publisher);

        publisherId = publisher.getUUID();
        noteefyWeb.registerWebPublisher(publisher);

        Client.start();
    }

    private static Broadcaster createPublisher() {
        return new Broadcaster();
    }

    private static void publishWithInterval(Broadcaster publisher) {
        AtomicInteger counter = new AtomicInteger();
        new Thread(() -> {
            while (true) {
                publisher.publish(counter.getAndIncrement());
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}