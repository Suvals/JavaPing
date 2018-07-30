import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.util.*;

/**
 * Date: 23.06.14
 * Time: 15:53
 */
public class LogOfflinePanel extends JTextArea implements Observer, DisplayElement {

    private String pingText;

    public LogOfflinePanel(Subject pingData) {
        this.setEditable(false);
        this.setAutoscrolls(false);
        DefaultCaret caret = (DefaultCaret) this.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        pingData.registerObserver(this);
    }

    @Override
    public void display() {
        this.append(pingText + "\n");
    }

    @Override
    public void update(String pingLog, String deviceName, int deviceStatus) {

        if (deviceStatus == Device.OFFLINE) {
            this.pingText = pingLog;
            display();
        }
    }
}
