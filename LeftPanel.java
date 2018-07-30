import javax.swing.*;

/**
 * Date: 08.09.14
 * Time: 20:20
 */
public class LeftPanel extends JTextArea {

    public LeftPanel(){
        this.setEditable(false);
        this.setText("Online: 0" + "       " + "\n");
    }
}
