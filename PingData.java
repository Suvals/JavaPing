import java.util.*;


/**
 * Date: 08.07.14
 * Time: 21:22
 */
public class PingData implements Subject {

    private ArrayList<Observer> observers;
    private String pingLog;
    private String deviceName;
    private int deviceStatus;

    public PingData() {
        observers = new ArrayList<Observer>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0) observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            Observer observer = (Observer) observers.get(i);
            observer.update(pingLog, deviceName, deviceStatus);
        }
    }

    public void pingChanged() {
        notifyObservers();
    }

    public void setPingLog(String pingLog, String deviceName, int deviceStatus) {
        this.pingLog = pingLog;
        this.deviceName = deviceName;
        this.deviceStatus = deviceStatus;
        pingChanged();
    }


}
