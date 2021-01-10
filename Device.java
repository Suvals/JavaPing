import javax.swing.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.*;

/**
 * Date: 18.06.14
 * Time: 19:59
 */
public class Device {


    private String ip = null;
    private String deviceName = null;
    private String description = null;
    private int status = 0;
    private boolean state;
    private Thread threadStart;
    private File file;
    private File filePath = new File("src/logs/device/");
    private File dateFile;
    private FileWriter fileWriter;
    private GregorianCalendar calendar;
    private String systemMassage = null;
    private String command = null;
    private boolean isReachableMy;
    private String osName = System.getProperty("os.name");
    private Process process;
    private BufferedReader bufferedReader;
    private String line;
    private ArrayList<Device> deviceArList = new ArrayList<Device>();
    private ArrayList<Thread> threadSet = new ArrayList<Thread>();
    private Object sync;
    private LoadFrame loadFrame;
    private boolean flag;
    public static final int ONLINE = 1;
    public static final int OFFLINE = 2;
    public static final int START = 3;
    public static final int PAUSE = 4;
    public static final int RESUME = 5;
    public static final int STOP = 7;


    public Device(Object sync, LoadFrame loadFrame) {
        String consoleEncoding = System.getProperty("consoleEncoding");
        System.out.println("ConsoleEncoding is " + consoleEncoding);
        if (consoleEncoding != null) {
            try {
                System.setOut(new PrintStream(System.out, true, consoleEncoding));
            } catch (UnsupportedEncodingException ex) {
                System.err.println("Unsupported encoding set for console: " + consoleEncoding);
            }
        }
        this.sync = sync;
        this.loadFrame = loadFrame;
        // loadDataBase();
    }

    public Device(String deviceName, String description, String ip, Object sync) {
        this.ip = ip;
        this.deviceName = deviceName;
        this.description = description;
        this.sync = sync;
        flag = true;
        state = true;
    }

    public int addDevice(String deviceName, String description, String ip, Object sync) {
        if (!deviceArList.isEmpty()) {
            for (Device d : deviceArList) {
                if (d.getDeviceName().equals(deviceName)) {
                    System.out.println("Устройство с именем " + "\"" + deviceName + "\" " + "уже есть в базе");
                    alert("Устройство с именем " + "\"" + deviceName + "\" " + "уже есть в базе");
                    return 0;
                } else if (d.getIp().equals(ip)) {
                    System.out.println("Устройство с IP " + "\"" + ip + "\" " + "уже есть в базе");
                    alert("Устройство с IP " + "\"" + ip + "\" " + "уже есть в базе");
                    return 0;
                }
            }
        }
        System.out.println("Добавлено устройство: " + "\"" + deviceName + "\"" + "||" + "\"" + ip + "\"" + "||" + "\"" + description + "\"");
        deviceArList.add(new Device(deviceName, description, ip, sync));
        Collections.sort(deviceArList, new Comparator<Device>() {
            @Override
            public int compare(Device o1, Device o2) {
                return o1.getDeviceName().compareTo(o2.getDeviceName());
            }
        });
        return 1;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSystemMassage() {
        return systemMassage;
    }

    private void inputIP() {
        System.out.println("Input HostName or IP:");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            try {
                ip = InetAddress.getByName(scanner.next()).getHostAddress();
            } catch (UnknownHostException uhe) {
                System.out.println("You entered WRONG HOST or IP");
                inputIP();
            }
        }
    }

    public void loadDataBase() {
        File deviceBase = new File("src/deviceBase.db");
        String encoding = "UTF-8";
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(deviceBase), encoding));
            Scanner lineReader = new Scanner(fileReader);
            Scanner wordReader = new Scanner(line);
            while (lineReader.hasNextLine()) {
                line = lineReader.nextLine();
                try {
                    while (wordReader.hasNext()) {
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("База данных повреждена. Введите устройства вручную");
                    return;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(deviceArList.size());
    }

    public void pingHost(PingData pingData, int offlineTimeOut, int onlineTimeOut) {

        Locale locale = new Locale("ua");
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);

        while (!Thread.currentThread().isInterrupted()) {
            if (flag) {
                calendar = new GregorianCalendar();
                isReachableMy = pingHostByCommandNew(ip);
                if (isReachableMy) {
                    systemMassage = (dateFormat.format(calendar.getTime()) + "  " + deviceName + "(" + description + ")" + " is " + "ONLINE" + " ip: " + ip);
                    status = ONLINE;
                    if (!Thread.currentThread().isInterrupted() && flag) {
                        pingData.setPingLog(systemMassage, deviceName, status);
                        try {
                            Thread.sleep(onlineTimeOut);
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                } else {
                    dateFile = new File(filePath + "/" + calendar.get(Calendar.YEAR) + "/" + calendar.getDisplayName(2, 2, locale) + "/" + calendar.get(Calendar.DATE));
                    dateFile.mkdirs();
                    file = new File(dateFile + "/" + deviceName + "_" + ip + ".txt");
                    systemMassage = (dateFormat.format(calendar.getTime()) + "  " + deviceName + "(" + description + ")" + " is " + "OFFLINE" + " ip: " + ip);
                    status = OFFLINE;
                    try {
                        fileWriter = new FileWriter(file, true);
                        fileWriter.write(systemMassage + "\n");
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (!Thread.currentThread().isInterrupted() && flag) {
                        pingData.setPingLog(systemMassage, deviceName, status);
                        try {
                            Thread.sleep(offlineTimeOut);
                        } catch (InterruptedException e1) {
                            //e1.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } else {
                synchronized (sync) {
                    try {
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean pingHostByCommand(String aIp) {

        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
                command = "ping -n 1 " + aIp;
            } else {
                command = "ping -c 1 " + aIp;
            }

            Process myProcess = Runtime.getRuntime().exec(command);
            myProcess.waitFor();
            if (myProcess.exitValue() == 0) return true;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return false;
    }

    private boolean pingHostByCommandNew(String aIp) {
        try {
            if (osName.toLowerCase().startsWith("windows")) {
                command = "ping -n 2 " + aIp;
                process = Runtime.getRuntime().exec(command);
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "Cp866"));
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("time=") || line.contains("время=") || line.contains("time<") || line.contains("время<"))
                        return true;
                }
                bufferedReader.close();
            } else {
                command = "ping -c 2 " + aIp;
                process = Runtime.getRuntime().exec(command);
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("time=") || line.contains("время=") || line.contains("time<") || line.contains("время<"))
                        return true;
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void startPingAll(final PingData pingData) {
        pingData.setPingLog("RUNNING", null, Device.START);
        loadFrame.setVisible(true);
        loadFrame.setAmount(deviceArList.size());
        threadStart = new Thread(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                threadSet.add(threadStart);
                for (Device d : deviceArList) {
                    if (d.isState() && !threadStart.isInterrupted() && d.isFlag()) {
                        System.out.print("Starting " + "\"" + "PING" + "\" " + "for Device: " + d.toFile() + " ");
                        Runnable runnable = new DeviceRunnable(d, pingData, sync);
                        Thread t = new Thread(runnable);
                        threadSet.add(t);
                        System.out.println("\"" + "Thread:" + "\" " + t.getName());
                        t.start();
                        d.setState(false);
                        i++;
                        loadFrame.showCount(i);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                if (threadStart.isInterrupted()) {
                    System.out.println("Остановка " + i + " потоков произведена...");
                } else
                    System.out.println("Всего запущено " + i + " новых потоков");
            }
        });
        threadStart.start();
         //loadFrame.setVisible(false);
    }


    public void pausePing(PingData pingData) {
        for (Device d : deviceArList) {
            if (d.isFlag()) {
                d.setFlag(false);
                System.out.println("Device: " + d.toFile() + " is waiting...");
            }
        }
        pingData.setPingLog("PAUSING", null, Device.PAUSE);
    }


    public void stopPing(PingData pingData) {
        if (!threadSet.isEmpty()) {
            int i = 1;
            for (Thread t : threadSet) {
                t.interrupt();
            }
            threadSet.clear();
            for (Device d : deviceArList) {
                if (!d.isState()) {
                    d.setState(true);
                    System.out.println("Device:: stopPing: " + i + " " + d.getDeviceName());
                    i++;
                }
            }
            System.out.println("Остановка " + (i - 1) + " потоков произведена...");
            pingData.setPingLog("STOP", null, Device.STOP);
        }
    }

    public void resuming(PingData pingData) {
        for (Device d : deviceArList) {
            if (!d.isFlag()) {
                d.setFlag(true);
                System.out.println("Device: " + d.toFile() + " is resuming...");
            }
        }
        startPingAll(pingData);
        pingData.setPingLog("RUNNING", null, Device.RESUME);
        synchronized (sync) {
            sync.notifyAll();
        }
    }


    public void loadFromFile(Object sync) {
        int i = 0;
        int countDevice = 0;
        File deviceList = new File("src/deviceList.txt");
        File deviceListNew = new File("src/deviceListNew.txt");
        String encoding = "UTF-8";
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(deviceList)));
            Scanner scannerEncoding = new Scanner(fileReader);
            if (scannerEncoding.hasNext()) {
                encoding = scannerEncoding.next();
                System.out.println("Кодировка файла: " + encoding);
            }
            fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(deviceList), encoding));
            Scanner scannerCount = new Scanner(fileReader);
            scannerCount.nextLine();
            while (scannerCount.hasNextLine() && !scannerCount.nextLine().isEmpty()) {
                countDevice++;
            }
            System.out.println("Device::loadFromFile::countDevice " + countDevice);

            fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(deviceList), encoding));
            Scanner scannerDevice = new Scanner(fileReader);
            scannerDevice.nextLine();
            while (scannerDevice.hasNext()) {
                if (addDevice(scannerDevice.next(), scannerDevice.next(), scannerDevice.next(), sync) > 0) {
                    i++;
                }
            }

            System.out.println("Device::loadFromFile::i " + i);
            try {
                fileWriter = new FileWriter(deviceListNew);
                for (Device d : deviceArList) fileWriter.write(d.toFile());
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            System.out.println("Файл содержит неполную информацию! Проверьте, пожалуйста, содержимое файла и перезапустите загрузку из файла!!!");
            return;
        }
        System.out.println(deviceArList.size());
    }


    @Override
    public String toString() {
        return "Device{" +
                "ip='" + ip + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    private String toFile() {
        return deviceName + " " + description + " " + ip + "\n";
    }

    private void alert(String s) {
        JOptionPane.showMessageDialog(new JFrame(), s, "Ошибка добавления устройства", JOptionPane.ERROR_MESSAGE);
    }
}
