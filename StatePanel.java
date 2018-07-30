import javax.swing.*;

/**
 * Date: 26.03.15
 * Time: 16:39
 */
public class StatePanel extends JButton implements Observer, DisplayElement{

    private String pingText;

    public StatePanel(Subject pingData){
        this.setEnabled(false);
        pingData.registerObserver(this);
    }

    @Override
    public void display() {
          this.setText(pingText);
    }

    @Override
    public void update(String pingLog, String deviceName, int deviceStatus) {
        if (deviceStatus == Device.STOP) {
            this.setText(" ");
            this.pingText = pingLog;
            display();
        } else if (deviceStatus == Device.PAUSE) {
            this.setText(" ");
            this.pingText = pingLog;
            display();
        } else if (deviceStatus == Device.RESUME) {
            this.setText(" ");
            this.pingText = pingLog;
            display();
        } else if (deviceStatus == Device.START){
            this.setText(" ");
            this.pingText = pingLog;
            display();
        }
    }
}
