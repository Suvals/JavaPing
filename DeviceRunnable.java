import java.awt.*;

/**
 * Date: 18.06.14
 * Time: 19:55
 */
public class DeviceRunnable implements Runnable {

    private Device device;
    private PingData pingData;
    private Object sync;



    public DeviceRunnable(Device device, PingData pingData, Object sync) {
        this.device = device;
        this.pingData = pingData;
        this.sync = sync;
    }

    @Override
    public void run() {
        device.pingHost(pingData, 3000, 60000);
       }
}
