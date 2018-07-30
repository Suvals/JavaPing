import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

/**
 * Date: 30.03.15
 * Time: 15:27
 */
public class LoadFrame extends JFrame {

    private int width = 0;
    private int height = 0;
    private int amount = 0;
    DrawComponent drawComponent;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LoadFrame(final JFrame frame) {

        setTitle("Загрузка устройств из файла");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // System.out.println("LoadFrame::LoadFrame::frame.toString()" + frame.toString());
        // System.out.println("LoadFrame::LoadFrame::frame.getWidth " + frame.getWidth());
        width = frame.getWidth() / 2;
        // System.out.println("LoadFrame::LoadFrame::frame.getHeight " + frame.getHeight());
        height = frame.getHeight() / 4;
        setSize(width, height);
        setAlwaysOnTop(true);
        setLocation(width / 2, (int) (height * 1.8));
        setResizable(false);



        drawComponent = new DrawComponent();
        drawComponent.setWidthActiveMin(amount);
        add(drawComponent, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton button = new JButton("CLOSE");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(button);
        add(panel, BorderLayout.SOUTH);

    }

    public void showCount(int count) {
        drawComponent.setCount(count);
        repaint();
    }

}


class DrawComponent extends JComponent {

    private Rectangle2D rectFill;
    private int count = 0;
    private int widthActiveMin = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWidthActiveMin() {
        return widthActiveMin;
    }

    public void setWidthActiveMin(int widthActiveMin) {
        this.widthActiveMin = widthActiveMin;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        double widthPassive = getWidth() - 20;
        double height = getHeight() / 5;
        double leftX = (getWidth() - widthPassive) / 2;
        double topY = getHeight() / 2;

        double   widthActive = widthPassive / widthActiveMin * count;


        //  widthActive = widthActive * count;

        Rectangle2D rect = new Rectangle2D.Double(leftX, topY, widthPassive, height);
        g2.draw(rect);
        rectFill = new Rectangle2D.Double(leftX, topY, widthActive, height);
        g2.setColor(Color.BLUE);
        g2.fill(rectFill);
    }


}
