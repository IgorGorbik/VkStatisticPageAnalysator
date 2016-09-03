package notifier;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import token.VKToken;

/**
 *
 * @author Игорь
 */
public class VKapi {

    private String client_id = "5251743";
    private String scope = "messages";
    private String redirect_uri = "http://oauth.vk.com/blank.html";
    private String display = "page";
    private String response_type = "token";
    private String access_token;

    public VKapi() throws IOException {
        access_token = VKToken.getToken(client_id, scope, email, pass);
    }
    //"4239ba4ba068b985dfefe94ae19ad053a5cf20bb63edb9b580825b80c704b066535f45033ed86e57aca35";
    private String email = "+380631413921";//тут должен быть прописан email
    private String pass = "italia777";//тут должен быть прописан пароль

    //String str = "https://oauth.vk.com/authorize?client_id=5251743&display=page&redirect_uri=http://oauth.vk.com/blank.html&scope=messages&response_type=token&v=5.45";
    public String getNewMessage() throws Exception {

        String url = "https://api.vk.com/method/"
                + "messages.get"
                + "?out=0"
                + "&access_token=" + access_token;

        String line = "";

        try {

            URL url2 = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
            line = reader.readLine();
            reader.close();

        } catch (Exception e) {
            System.out.println("gfgh");
        }

        return line;

    }

    public static void main(String[] args) throws Exception {
        //Создадим раскрывающееся меню
        PopupMenu popup = new PopupMenu();
        //Создадим элемент меню
        MenuItem exitItem = new MenuItem("Выход");
        //Добавим для него обработчик
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //Добавим пункт в меню
        popup.add(exitItem);
        SystemTray systemTray = SystemTray.getSystemTray();
        //получим картинку
        Image image = Toolkit.getDefaultToolkit().getImage("vk_icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "VKNotifer", popup);
        trayIcon.setImageAutoSize(true);
        //добавим иконку в трей
        systemTray.add(trayIcon);
        trayIcon.displayMessage("VKNotifer", "Соединяемся с сервером", TrayIcon.MessageType.INFO);
        //Создадим экземпляр класса ВКапи
        VKapi vkAPI = new VKapi();
        //Получим токен
        //vkAPI.setConnection();
        trayIcon.displayMessage("VKNotifer", "Соединение установлено", TrayIcon.MessageType.INFO);
        //Бескоечный цикл
        String oldMessage = vkAPI.getNewMessage();
        String newMessage;
        int i = 0;
        for (;;) {
            // Запросы на сервер можно подавать раз в 3 секунды
            Thread.sleep(3000); // ждем три секунды
            if (i == 15000) {  // Если прошло 45 000 сек (Время взято с запасом, токен дается на день )
                //vkAPI.setConnection(); // Обновляем токен
                Thread.sleep(3000);    // Запросы шлем только раз в три секунды
                i = 0;
            }
            //Здесь отработка 
            newMessage = vkAPI.getNewMessage();
            if (!newMessage.equals(oldMessage)) {
                oldMessage = newMessage;
                trayIcon.displayMessage("VKNotifer", "Получено новое сообщение", TrayIcon.MessageType.INFO);
                //Tools.playDrum(Drum.d53_Ride_Bell, 127, 0);
            }
            i++;
        }
    }

}

