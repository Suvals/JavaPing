import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Date: 21.08.14
 * Time: 12:31
 */
public class JavaPingFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 450;
    private PingData pingData;
    private Object sync = new Object();
    private LoadFrame loadFrame;
    private  Device device;

    public JavaPingFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("JavaPing");
        pingData = new PingData();
        loadFrame = new LoadFrame(this);
        device =  new Device(sync, loadFrame);



        JPanel northPanel = new JPanel(new GridLayout(3, 1));
        JPanel labels = new JPanel(new GridLayout(1, 3));
        JPanel text = new JPanel(new GridLayout(1, 3));
        final TextField ip = new TextField();
        final TextField deviceName = new TextField();
        final TextField description = new TextField();
        labels.add(new JLabel("IP", SwingConstants.CENTER));
        labels.add(new JLabel("Device Name", SwingConstants.CENTER));
        labels.add(new JLabel("Description", SwingConstants.CENTER));
        text.add(ip);
        text.add(deviceName);
        text.add(description);
        northPanel.add(labels);
        northPanel.add(text);
        JPanel buttonPanel1 = new JPanel(new GridLayout(1, 3));
        JButton infoPanel = new StatePanel(pingData);
        buttonPanel1.add(infoPanel);
        addButton(buttonPanel1, "Add Device", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device.addDevice(deviceName.getText(), description.getText(), ip.getText(), sync);
                ip.setText("");
                deviceName.setText("");
                description.setText("");
            }
        });
        addButton(buttonPanel1, "LoadFromFile", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device.loadFromFile(sync);
            }
        });


        northPanel.add(buttonPanel1);
        add(northPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        LogOfflinePanel logOfflinePanel = new LogOfflinePanel(pingData);
        JScrollPane scrollPaneOffline = new JScrollPane(logOfflinePanel);
        scrollPaneOffline.createVerticalScrollBar();

        LogOnlinePanel logOnlinePanel = new LogOnlinePanel(pingData);
        JScrollPane scrollPaneOnline = new JScrollPane(logOnlinePanel);
        scrollPaneOnline.createVerticalScrollBar();

        centerPanel.add(scrollPaneOnline);
        centerPanel.add(scrollPaneOffline);
        add(centerPanel, BorderLayout.CENTER);


        JPanel leftPanel = new JPanel(new GridLayout(1, 1));
        LeftPanel lP = new LeftPanel();
        JScrollPane leftScrollPanel = new JScrollPane(lP);
        leftScrollPanel.createVerticalScrollBar();
        leftScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        leftScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        leftPanel.add(leftScrollPanel);
        add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new GridLayout(1, 1));
        RightPanel rP = new RightPanel();
        JScrollPane rightScrollPanel = new JScrollPane(rP);
        rightScrollPanel.createVerticalScrollBar();
        rightScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        rightScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rightPanel.add(rightScrollPanel);
        add(rightPanel, BorderLayout.EAST);

        DeviceDisplay deviceDisplay = new DeviceDisplay(pingData, lP, rP);


        JPanel buttonPanel = new JPanel();

        addButton(buttonPanel, "Start", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device.startPingAll(pingData);
            }
        });
        addButton(buttonPanel, "Pause", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device.pausePing(pingData);
            }
        });
        addButton(buttonPanel, "Resume", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                   device.resuming(pingData);
            }
        });
        addButton(buttonPanel, "Stop", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device.stopPing(pingData);
            }
        });
        addButton(buttonPanel, "Close", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addButton(Container c, String title, ActionListener listener) {
        JButton button = new JButton(title);
        c.add(button);
        button.addActionListener(listener);
    }
}
