package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class InfoTree extends JPanel implements TreeSelectionListener {

    private JEditorPane htmlPane;
    private JTree tree;

    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";

    public InfoTree() {

        super(new GridLayout(1, 0));

        DefaultMutableTreeNode top
                = new DefaultMutableTreeNode("VKQL");
        createNodes(top);

        tree = new JTree(top);
        tree.putClientProperty("JTree.lineStyle", "None");
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);

        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }

        JScrollPane treeView = new JScrollPane(tree);

        htmlPane = new JEditorPane();
        htmlPane.setContentType("text/html; charset=utf-8");
        htmlPane.setEditable(false);

        JScrollPane htmlView = new JScrollPane(htmlPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(210);
        splitPane.setPreferredSize(new Dimension(500, 300));

        add(splitPane);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            BookInfo book = (BookInfo) nodeInfo;
            displayURL(book.path);
        } else {
        }
    }

    private class BookInfo {

        public String bookName;
        URL path;

        public BookInfo(String book, String filename) {
            bookName = book;
            path = getClass().getResource(filename);
            if (path == null) {
                System.err.println("Couldn't find file: " + filename);
            }
        }

        @Override
        public String toString() {
            return bookName;
        }
    }

    private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            } else {
                htmlPane.setText("File Not Found");
            }
        } catch (IOException e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }
    }

    private void createNodes(DefaultMutableTreeNode top) {

        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        category = new DefaultMutableTreeNode("Запросы");
        top.add(category);

        book = new DefaultMutableTreeNode(new BookInfo("Работа с группой", "/resources/123.html"));
        category.add(book);

        book = new DefaultMutableTreeNode(new BookInfo("Текстовые запросы", "/resources/223.html"));
        category.add(book);

        book = new DefaultMutableTreeNode(new BookInfo("Статистические запросы", "/resources/323.html"));
        category.add(book);

    }

    public static void createAndShowGUI() {
        JFrame frame;
        frame = new JFrame("Справка");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new InfoTree());
        frame.setSize(700, 540);
        frame.setVisible(true);
    }

    public void showGUI() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void main(String[] args) {
        new InfoTree().showGUI();
    }

}
