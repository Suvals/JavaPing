import java.util.concurrent.locks.*;

import java.util.*;

/**
 * Date: 31.08.14
 * Time: 19:20
 */
public class DeviceDisplay implements Observer, DisplayElement {

    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private Set<String> onlineList = Collections.synchronizedSortedSet(new TreeSet<String>());
    private Set<String> offlineList = Collections.synchronizedSortedSet(new TreeSet<String>());
    private Lock deviceDisplayLock = new ReentrantLock();

    public DeviceDisplay(Subject pingData, LeftPanel leftPanel, RightPanel rightPanel) {
        pingData.registerObserver(this);
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
    }

    @Override
    public void display() {
        leftPanel.setText("Online: " + onlineList.size() + "       " + "\n");
        for (String s : onlineList) {
            leftPanel.append(s + "\n");
        }
        rightPanel.setText("Offline: " + offlineList.size() + "       " + "\n");
        for (String s : offlineList) {
            rightPanel.append(s + "\n");
        }
    }

    @Override
    public void update(String pingLog, String deviceName, int deviceStatus) {
        deviceDisplayLock.lock();
        try {
            if (deviceStatus == Device.ONLINE) {
                onlineList.add(deviceName);
                offlineList.remove(deviceName);
            } else if (deviceStatus == Device.OFFLINE) {
                offlineList.add(deviceName);
                onlineList.remove(deviceName);
            } else if (deviceStatus == Device.STOP || deviceStatus == Device.PAUSE) {
                offlineList.clear();
                onlineList.clear();
            }
            display();
        } finally {
            deviceDisplayLock.unlock();
        }
    }
}
