import javax.swing.*;

/**
 * Date: 08.09.14
 * Time: 20:21
 */
public class RightPanel extends JTextArea {

    public RightPanel(){
        this.setEditable(false);
        this.setText("Offline: 0" + "       " + "\n");
    }
}
