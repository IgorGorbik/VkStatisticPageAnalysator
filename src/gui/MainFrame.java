package gui;

import gui.menuparser.ActionListenerInstaller;
import gui.menuparser.ActionListenerFor;
import controller.ExecuteController;
import gui.menuparser.XMLMenuLoader;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Игорь
 */
public class MainFrame {

    private JFrame frame;
    private JPanel mainPanel;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JMenuBar jMenuBar;
    private JSplitPane jMainSplitPane;
    private JSplitPane jSplitPane;
    private JScrollPane treeView;
    private InputTextArea ita;
    private OutputTextArea ota;
    private UIManager.LookAndFeelInfo l[];

    public MainFrame() throws FileNotFoundException, Exception {
        initComponents();
        initFrame();
    }

    public static void main(String[] args) throws Exception {
        new MainFrame().showGUI();
    }

    private void initFrame() throws HeadlessException {
        frame = new JFrame("VKStatistics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(jMenuBar);
        frame.add(mainPanel);
        frame.setResizable(false);
        frame.setSize(800, 600);
        centerFrame();
    }

    private void centerFrame() throws HeadlessException {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int) (dimension.getWidth() / 2 - frame.getWidth() / 2),
                (int) (dimension.getHeight() / 2 - frame.getHeight() / 2));
    }

    private void initComponents() throws UnsupportedEncodingException, Exception {
        l = UIManager.getInstalledLookAndFeels();
        initTreeView();
        initWorkSplitPane();
        initTreePanel();
        initInputOutputPanel();
        initMainSplitPane();
        initMainPanel();
        createMenuBar();
    }

    private void initMainSplitPane() {
        jMainSplitPane = new JSplitPane();
        jMainSplitPane.setLeftComponent(jPanel2);
        jMainSplitPane.setRightComponent(jPanel1);
    }

    private void initWorkSplitPane() {
        jSplitPane = new JSplitPane();
        jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        ita = new InputTextArea();
        ota = new OutputTextArea();
        ExecuteController controller = new ExecuteController(tree);
        controller.addObservable(ota);
        controller.addObservable(ita);
        jSplitPane.setLeftComponent(ita);
        jSplitPane.setRightComponent(new JScrollPane(ota));
    }

    private void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        mainPanel.add(jMainSplitPane);
    }

    private DefaultMutableTreeNode top;
    private JTree tree;

    private void initTreeView() {
        tree = new GroupTree();
        tree.putClientProperty("JTree.lineStyle", "None");
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeView = new JScrollPane(tree);
    }

    private void initInputOutputPanel() {
        jPanel1 = new JPanel();
        jPanel1.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(jSplitPane);
    }

    private void initTreePanel() {
        jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(treeView);
    }

    private void createMenuBar() throws UnsupportedEncodingException, Exception {
        XMLMenuLoader loader = parseMenuXML();
        jMenuBar = loader.getMenuBar("mainMenu");
        ActionListenerInstaller.proccessAnnotations(this, loader);
    }

    @ActionListenerFor(source = "exit")
    public void exit() {
        System.exit(0);
    }

    @ActionListenerFor(source = "execute")
    public void execute() {
        ita.run();
    }

    @ActionListenerFor(source = "clean")
    public void clean() {
        ota.setText("");
    }

    @ActionListenerFor(source = "look1")
    public void setView1() {
        change(0);
    }

    @ActionListenerFor(source = "look2")
    public void setView2() {
        change(1);
    }

    @ActionListenerFor(source = "look3")
    public void setView3() {
        change(2);
    }

    @ActionListenerFor(source = "look4")
    public void setView4() {
        change(3);
    }

    @ActionListenerFor(source = "info")
    public void showInfo() {
        new InfoTree().showGUI();
    }

    private void change(int i) {
        try {
            UIManager.setLookAndFeel(l[i].getClassName());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private XMLMenuLoader parseMenuXML() throws UnsupportedEncodingException {
        String str = "/resources/menu.xml";
        InputStream stream = getClass().getResourceAsStream(str);
        XMLMenuLoader loader = new XMLMenuLoader(stream);
        try {
            loader.parse();
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        JLabel icon = new JLabel(new ImageIcon(getClass().getResource("/resources/vk.gif")));
        loader.getMenuBar("mainMenu").add(Box.createHorizontalGlue());
        loader.getMenuBar("mainMenu").add(icon);
        return loader;
    }

    public void showGUI() {
        frame.setVisible(true);
    }

}
