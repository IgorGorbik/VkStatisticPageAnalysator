package notifier;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import token.VKToken;

public class VkFXApp extends Application {

    static final TextArea title = new TextArea();

    private TextField poem;

    private String client_id = "5251743";
    private String scope = "messages";

    private String email = "+380631413921";
    private String pass = "italia777";

    private static String access_token;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        access_token = VKToken.getToken(client_id, scope, email, pass);

        title.setEditable(false);

        poem = new TextField();
        poem.setPromptText("Введите ваш ответ");
        poem.setPrefColumnCount(20);
        poem.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getCode() == KeyCode.ENTER) {

                    String str = poem.getText();
                    print(str);

                    try {
                        sendMessage(str);
                    } catch (Exception ex) {
                        Logger.getLogger(VkFXApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        });

        Button printBtn = new Button("Отправить");
        printBtn.setStyle("-fx-font:  bold italic 9pt Arial;"
                + "-fx-text-fill: #0000FF;"
                + "-fx-background-color: #E0EEEE; "
                + "-fx-border-width: 1px; "
                + "-fx-border-radius: 20;"
                + "-fx-background-radius: 20;"
                + "-fx-border-color: #836FFF;");
        printBtn.setBlendMode(BlendMode.MULTIPLY);
        printBtn.setAlignment(Pos.CENTER);
        printBtn.setContentDisplay(ContentDisplay.RIGHT);

        DropShadow effect = new DropShadow();
        effect.setOffsetX(5);
        effect.setOffsetY(5);

        printBtn.setEffect(effect);

        printBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String str = poem.getText();
                print(str);
                try {
                    sendMessage(str);
                } catch (Exception ex) {
                    Logger.getLogger(VkFXApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        VBox root = new VBox(new Label("Получено:"), title, new Label("Ваш ответ:"), poem);
        root.setSpacing(5);
        root.getChildren().add(printBtn);
        root.setStyle("-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 3;"
                + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: gray;");

        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Сообщения");
        stage.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                
                String newMessage = null;

                String oldMessage = null;

                try {
                    oldMessage = getNewMessage();
                } catch (Exception ex) {
                    Logger.getLogger(VkFXApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                while (true) {

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(VkFXApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        newMessage = getNewMessage();
                    } catch (Exception ex) {
                        Logger.getLogger(VkFXApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (!newMessage.equals(oldMessage)) {

                        oldMessage = newMessage;

                        String str = null;
                        try {
                            str = parseString(oldMessage);
                        } catch (IOException ex) {
                            Logger.getLogger(VkFXApp.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        updateText(str);

                    }

                }
            }
        }).start();

    }

    private void updateText(String str) {
        
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                title.appendText(str + "\n");
            }

        });
        
    }

    public static String getNewMessage() throws Exception {

        String url = "https://api.vk.com/method/"
                + "messages.get"
                + "?out=0&count=1"
                + "&access_token=" + access_token;

        String line = "";

        try {

            URL url2 = new URL(url);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()))) {
                line = reader.readLine();
            }

        } catch (Exception e) {
            System.out.println("gfgh");
        }

        return line;

    }

    private void sendMessage(String str) throws Exception {

        String url1 = "https://api.vk.com/method/"
                + "messages.send"
                + "?user_id=199742880&message=" + str.trim()
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

    public void print(String str) {

        String send = str;

        if (send.length() > 0) {
            title.appendText(send + "\n");

        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                poem.positionCaret(0);
            }
        });

        poem.setText("");

    }

    public static void main(String[] args) {

        launch(args);

    }

    private String parseString(String str) throws IOException {

        StringTokenizer st = new StringTokenizer(str, "body\":\"");

        LinkedList l = new LinkedList();

        while (st.hasMoreTokens()) {

            String s = st.nextToken("\"}]}");
            l.add(0, s);

        }

        int a = str.indexOf("uid\":");

        int b = str.indexOf(",\"read_state");

        String id = str.substring(a + 5, b);

        String user = Jsoup.connect("http://vk.com/id" + id).get().title();

        str = user + " : " + (String) l.get(0);

        return str;

    }

}
