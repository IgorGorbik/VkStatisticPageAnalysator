package notifier;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jsoup.Jsoup;
import token.VKToken;

/**
 *
 * @author Игорь
 */
class SwingVK {

    JFrame frame;

    JLabel label1;

    JTextArea textArea1;

    JLabel label2;

    JTextField field;

    JButton button;

    private String client_id = "5251743";
    private String scope = "messages";

    private String email = "+380631413921";
    private String pass = "italia777";

    private String access_token;

    private UIManager.LookAndFeelInfo l[];

    private void setView() {
        ArrayDeque arr;

        try {
            UIManager.setLookAndFeel(l[3].getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

    }

    public String getNewMessage() throws Exception {

        String url = "https://api.vk.com/method/"
                + "messages.get.xml"
                + "?out=0&count=1"
                + "&access_token=" + access_token;

        String line = "";

        try {

            URL url2 = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
            line = reader.readLine();
            int a = 0;
            while ((a = reader.read()) != -1) {
                System.out.print((char) a);
            }
            reader.close();

        } catch (Exception e) {
            System.out.println("gfgh");
        }

        return line;

    }

    public SwingVK() throws IOException, Exception {

        l = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(l[1].getClassName());
            //SwingUtilities.updateComponentTreeUI(frame);
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        //setView();
        //access_token = VKToken.getToken(client_id, scope, email, pass);
        String url1 = "https://api.vk.com/method/"
                + "friends.get.xml"
                + "?user_id=";

        URL url = new URL(url1);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        try (Reader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            int a = 0;

            while ((a = buff.read()) != -1) {
                System.out.print((char) a);
            }

        }

        initComponents();

        JPanel panel1 = initPanel1();

        JPanel panel2 = initPanel2();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(BorderLayout.CENTER, panel1);
        panel.add(BorderLayout.SOUTH, panel2);
        panel.add(BorderLayout.NORTH, new JComboBox());

        frame.add(BorderLayout.CENTER, panel);
//
//        //getFriends();
//        Thread t = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                String oldMessage = null;
//
//                try {
//                    oldMessage = getNewMessage();
//                } catch (Exception ex) {
//                    Logger.getLogger(SwingVK.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                String newMessage = null;
//
//                while (true) {
//
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(SwingVK.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    try {
//                        newMessage = getNewMessage();
//                    } catch (Exception ex) {
//                        Logger.getLogger(SwingVK.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    if (!newMessage.equals(oldMessage)) {
//
//                        oldMessage = newMessage;
//
//                        String str = null;
//                        try {
//                            str = parseString(oldMessage);
//                        } catch (IOException ex) {
//                            Logger.getLogger(SwingVK.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                        textArea1.append(str + "\n");
//                    }
//
//                }
//            }
//
//            private String parseString(String str) throws IOException {
//
//                StringTokenizer st = new StringTokenizer(str, "body\":\"");
//
//                LinkedList l = new LinkedList();
//
//                while (st.hasMoreTokens()) {
//
//                    String s = st.nextToken("\"}]}");
//                    l.add(0, s);
//
//                }
//
//                int a = str.indexOf("uid\":");
//
//                int b = str.indexOf(",\"read_state");
//
//                String id = str.substring(a + 5, b);
//
//                String user = Jsoup.connect("http://vk.com/id" + id).get().title();
//
//                str = user + " : " + (String) l.get(0);
//
//                return str;
//
//            }
//        });
//
//        t.start();
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel initPanel2() {
        JPanel panel2 = new JPanel(new BorderLayout(0, 5));
        panel2.add(BorderLayout.CENTER, field);
        panel2.add(BorderLayout.NORTH, label2);
        JPanel panel3 = initPanel3();
        panel2.add(BorderLayout.SOUTH, panel3);
        return panel2;
    }

    private JPanel initPanel3() {
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
        panel3.add(button);
        return panel3;
    }

    private JPanel initPanel1() {
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(BorderLayout.NORTH, label1);
        panel1.add(BorderLayout.CENTER, new JScrollPane(textArea1));       
        return panel1;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        new SwingVK();
                    } catch (Exception ex) {
                        Logger.getLogger(SwingVK.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SwingVK.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    private void initComponents() throws HeadlessException {
        initFrame();
        initLabels();
        initTextComponents();
        initButtons();
    }

    private void initButtons() {

        button = new JButton("Отправить");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String str = field.getText();
                print(str);
                try {
                    sendMessage(str);
                } catch (Exception ex) {
                    Logger.getLogger(SwingVK.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

    }

    private void sendMessage(String str) throws Exception {

        String url1 = "https://api.vk.com/method/"
                + "messages.send.xml"
                + "?user_id=199742880&message=" + str
                + "&access_token=" + access_token;

        URL url = new URL(url1);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        try (Reader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            int a = 0;

            while ((a = buff.read()) != -1) {
                System.out.print((char) a);
            }

        }

    }

    private void getFriends() throws Exception {

        String url1 = "https://api.vk.com/method/"
                + "friends.get"
                + "?user_id=199742880&count=" + "50"
                + "&access_token=" + access_token;

        URL url = new URL(url1);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        StringBuilder sb = new StringBuilder();

        try (Reader buff = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            int a = 0;

            while ((a = buff.read()) != -1) {
                sb.append((char) a);
            }

        }

        StringTokenizer st = new StringTokenizer(sb.substring(12 + 1).toString(), ",");

        String s = st.nextToken("]}");

        //System.out.println(s);
        st = new StringTokenizer(s, ",");

        Map<String, String> map = new HashMap();

        while (st.hasMoreTokens()) {

            String id = st.nextToken();
            String user = Jsoup.connect("http://vk.com/id" + id).get().title();

            map.put(id, user);

        }

        for (Map.Entry<String, String> set : map.entrySet()) {
            System.out.println(set.getKey() + " " + set.getValue());
        }

    }

    public void print(String str) {

        String send = str;

        if (send.length() > 0) {
            textArea1.append(send + "\n");

        }

        field.setText("");

    }

    private void initTextComponents() {
        textArea1 = new JTextArea(10, 30);
        textArea1.setEnabled(false);
        field = new JTextField(30);
    }

    private void initFrame() throws HeadlessException {
        frame = new JFrame("Сообщения");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

    }

    private void initLabels() {
        label1 = new JLabel("Получено:");
        label2 = new JLabel("Ответ:");
    }

}
