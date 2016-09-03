package gui.menuparser;

import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Игорь
 */
public class XMLMenuLoader {

    private InputSource source;
    private SAXParser parser;
    private DefaultHandler documentHandler;
    private Map menuStorage = new HashMap();

    public XMLMenuLoader(InputStream is) throws UnsupportedEncodingException {
        try {
            Reader reader = new InputStreamReader(is, "UTF-8");
            source = new InputSource(reader);
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException | SAXException ex) {
            throw new RuntimeException(ex);
        }
        documentHandler = new XMLParser();
    }

    public void parse() throws Exception {
        parser.parse(source, documentHandler);
    }

    public JMenuBar getMenuBar(String name) {
        return (JMenuBar) menuStorage.get(name);
    }

    public JMenu getMenu(String name) {
        return (JMenu) menuStorage.get(name);
    }

    public JMenuItem getMenuItem(String name) {
        return (JMenuItem) menuStorage.get(name);
    }

    public void addActionListener(String name, ActionListener listener) {
        getMenuItem(name).addActionListener(listener);
    }

    private JMenuBar currentMenuBar;

    private LinkedList menus = new LinkedList();

    class XMLParser extends DefaultHandler {

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            // определяем тип узла
            switch (qName) {
                case "menubar":
                    parseMenuBar(attributes);
                    break;
                case "menu":
                    parseMenu(attributes);
                    break;
                case "menuitem":
                    parseMenuItem(attributes);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("menu")) {
                menus.removeFirst();
            }
        }

        protected void parseMenuBar(Attributes attrs) {
            JMenuBar menuBar = new JMenuBar();
            String name = attrs.getValue("name");
            menuStorage.put(name, menuBar);
            currentMenuBar = menuBar;
        }

        protected void parseMenu(Attributes attrs) {
            JMenu menu = new JMenu();
            String name = attrs.getValue("name");
            adjustProperties(menu, attrs);
            menuStorage.put(name, menu);
            if (!menus.isEmpty()) {
                ((JMenu) menus.getFirst()).add(menu);
            } else {
                currentMenuBar.add(menu);
            }
            menus.addFirst(menu);
        }

        protected void parseMenuItem(Attributes attrs) {
            String name = attrs.getValue("name");
            if (name.equals("separator")) {
                ((JMenu) menus.getFirst()).addSeparator();
                return;
            }
            JMenuItem menuItem = new JMenuItem();
            adjustProperties(menuItem, attrs);
            menuStorage.put(name, menuItem);
            ((JMenu) menus.getFirst()).add(menuItem);
        }

        private void adjustProperties(JMenuItem menuItem, Attributes attrs) {
            String text = attrs.getValue("text");
            String mnemonic = attrs.getValue("mnemonic");
            String accelerator = attrs.getValue("accelerator");
            String enabled = attrs.getValue("enabled");
            menuItem.setText(text);
            if (mnemonic != null) {
                menuItem.setMnemonic(mnemonic.charAt(0));
            }
            if (accelerator != null) {
                menuItem.setAccelerator(
                        KeyStroke.getKeyStroke(accelerator));
            }
            if (enabled != null) {
                boolean isEnabled = true;
                if (enabled.equals("false")) {
                    isEnabled = false;
                }
                menuItem.setEnabled(isEnabled);
            }
        }
    }
}
