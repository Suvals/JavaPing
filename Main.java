import javax.swing.*;
import java.awt.*;

/**
 * Date: 27.05.14
 * Time: 23:56
 */
public class Main {


    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JavaPingFrame();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}



