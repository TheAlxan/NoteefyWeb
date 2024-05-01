import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.event.EventHandler;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.web.NoteefyWeb;
import com.alxan.noteefy.web.bridge.datasource.TcpDataSourceInfo;
import com.alxan.noteefy.web.common.PublisherAddress;
import com.alxan.noteefy.web.common.RemoteAddress;
import com.alxan.noteefy.web.exception.NotConnectedException;

public class Client {
    private static final RemoteAddress remoteAddress = new RemoteAddress(8088, "127.0.0.1");

    public static void start() throws NotConnectedException, InterruptedException {
        NoteefyWeb noteefyWeb = new NoteefyWeb();
        noteefyWeb.connectToRemoteBridge(TcpDataSourceInfo.getInstance(), remoteAddress);
        Thread.sleep(1000); // Wait for client to connect
        Listener listener = createListener();
        noteefyWeb.registerToRemotePublisher(createPublisherAddress(), listener);
    }

    public static Listener createListener() {
        Listener listener = new Listener();
        listener.doOnEvent(Integer.class, new EventCounter());
        return listener;
    }

    public static PublisherAddress createPublisherAddress() {
        return new PublisherAddress(remoteAddress, Server.publisherId);
    }

    private static class EventCounter implements EventHandler<Integer> {
        int previous = -1;

        @Override
        public void handle(Event<Integer> event) {
            int current = event.getContent();
            if (previous == -1) {
                previous = current;
            } else if (current - previous == 1) {
                System.out.println("OK -> Prev: " + previous + ", Current: " + current);
            } else {
                System.out.println("ERROR -> Prev: " + previous + ", Current: " + current);
            }
            previous = current;
        }
    }
}
