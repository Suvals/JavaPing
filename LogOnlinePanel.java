import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * Date: 31.08.14
 * Time: 15:43
 */
public class LogOnlinePanel extends JTextArea implements Observer, DisplayElement {

    private String pingText;


    public LogOnlinePanel(Subject pingData) {
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
        if (deviceStatus == Device.ONLINE) {
            this.pingText = pingLog;
            display();
        }
    }
}
