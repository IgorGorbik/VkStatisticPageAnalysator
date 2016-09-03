package controller;

import comment.Comment;
import post.Post;
import group.Group;
import group.CreateGroupException;
import observer.Updatable;
import observer.Observable1;
import observer.Observer1;
import gui.InputTextArea;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.xml.sax.SAXException;

/**
 *
 * @author Игорь
 */
public class ExecuteController implements Observer1 {

    private ArrayList<Observable1> observableList = new ArrayList<>();
    static public Map<String, Group> arrGroup = new LinkedHashMap<>();
    static public Map<String, DefaultMutableTreeNode> node = new LinkedHashMap<>();
    private JTree tree;

    public ExecuteController(JTree jTree) {
        this.tree = jTree;
    }

    public void addObservable(Observable1 o) {
        o.addObserver(this);
        observableList.add(o);
    }

    private String[] parse(String str) {
        str = str.replace("\n", "");
        String[] arr = str.split(";");
        return arr;
    }

    public static void main(String[] args) {
        String[] strings = new ExecuteController(null).parse("CREATE GROUP football;\n"
                + "SELECT POST FROM football START 0 TO 50 WHERE likes>=10;\n"
                + "DROP GROUP football");
        for (String a : strings) {
            System.out.println(a);
        }

    }

    @Override
    public void update(Observable1 o, Object arg) {
        if (o instanceof InputTextArea) {
            String[] input = parse((String) arg);
            for (String s : input) {
                int code = LexAnalysator.getCode(s);
                for (Observable1 o1 : observableList) {
                    if (o1 instanceof Updatable) {
                        try {
                            execute(code, s, (Updatable) o1);
                        } catch (Exception ex) {
                            Logger.getLogger(ExecuteController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

        }
    }

    private void execute(int code, String expr, Updatable u) throws Exception, CreateGroupException, SAXException {
        switch (code) {
            case 1: {
                Pattern p = Pattern.compile(LexAnalysator.patternCreate);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                String group_id = m.group(2);
                Group group = null;
                try {
                    group = Group.getGroupById(group_id);
                } catch (CreateGroupException e) {
                    u.update("Group id incorrect");
                    return;
                }
                if (arrGroup.put(group_name, group) == null) {
                    addGroupToTree(group_name);
                    u.update("Group created");
                } else {
                    u.update("Group replaced");
                }
                break;
            }
            case 2: {
                Pattern p = Pattern.compile(LexAnalysator.patternDrop);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                if (arrGroup.remove(group_name) == null) {
                    u.update("No group match");
                } else {
                    removeGroupFromTree(group_name);
                    u.update("Group deleted");
                }
                break;
            }
            case 3: {
                Pattern p = Pattern.compile(LexAnalysator.patternShowGroup);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                if (group == null) {
                    u.update("No group match");
                } else {
                    u.update(group.toString());
                }
                break;
            }
            case 4: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectAll);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(0, group.getPostCount());
                    int i = 1;
                    for (Post p1 : list) {
                        if (Integer.valueOf(p1.getLikes()) >= Integer.valueOf(start)) {
                            u.update(i + " " + p1);
                            i++;
                        }
                    }

                }
                break;
            }
            case 5: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByLikes);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        if (Integer.valueOf(p1.getLikes()) >= Integer.valueOf(m.group(4))) {
                            u.update(i + " " + p1);
                            i++;
                        }
                    }

                }
                break;
            }
            case 6: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByText);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        if (p1.getText().toLowerCase().contains(m.group(4).toLowerCase())) {
                            u.update(i + " " + p1);
                            i++;
                        }
                    }

                }
                break;
            }
            case 7: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByComment);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        if (Integer.valueOf(p1.getComments()) >= Integer.valueOf(m.group(4))) {
                            u.update(i + " " + p1);
                            i++;
                        }
                    }

                }
                break;
            }
            case 8: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByDependencyCommentDivLike);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        if (Integer.valueOf(p1.getLikes()) != 0) {
                            Double res = Double.valueOf(p1.getComments()) / Double.valueOf(p1.getLikes());
                            if (res >= Double.valueOf(m.group(4))) {
                                u.update(i + " res=" + res + " ;" + p1);
                                i++;
                            }
                        }
                    }

                }
                break;
            }
            case 9: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByDependencyLikeDivComment);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        if (Integer.valueOf(p1.getComments()) != 0) {
                            if ((Double.valueOf(p1.getLikes()) / Double.valueOf(p1.getComments())) >= Double.valueOf(m.group(4))) {
                                u.update(i + " " + p1);
                                i++;
                            }
                        }
                    }

                }
                break;
            }
            case 10: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByCommentContainsText);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        List<Comment> c = p1.getCommentList();
                        for (Comment c1 : c) {
                            if (c1.getText().toLowerCase().contains(m.group(4).toLowerCase())) {
                                u.update(i + " " + p1);
                                break;
                            }
                        }
                    }

                }
                break;
            }
            case 11: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByLikesWithoutText);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        u.update("Post№" + i + "; " + p1.getLikes());
                        i++;
                    }

                }
                break;
            }
            case 12: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByCommentWithoutText);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        u.update("Post№" + i + "; " + p1.getComments());
                        i++;
                    }

                }
                break;
            }
            case 13: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByLikesDivCommentWithoutText);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        if (Integer.valueOf(p1.getComments()) != 0) {
                            Double res = Double.valueOf(p1.getLikes()) / Double.valueOf(p1.getComments());
                            u.update("Post№" + i + "; " + res);
                            i++;
                        }
                    }

                }
                break;
            }
            case 14: {
                Pattern p = Pattern.compile(LexAnalysator.patternSelectIntervalPostByCommentDivLikesWithoutText);
                Matcher m = p.matcher(expr);
                m.find();
                String group_name = m.group(1);
                Group group = arrGroup.get(group_name);
                String start = m.group(2);
                String finish = m.group(3);
                if ("?".equals(finish)) {
                    finish = (group.getPostCount()) + "";
                }
                if (group == null) {
                    u.update("No group match");
                } else {
                    List<Post> list = group.getPostList(Integer.valueOf(start),
                            Integer.valueOf(finish) - Integer.valueOf(start) + 1);
                    int i = 1;
                    for (Post p1 : list) {
                        if (Integer.valueOf(p1.getLikes()) != 0) {
                            Double res = Double.valueOf(p1.getComments()) / Double.valueOf(p1.getLikes());
                            u.update("Post№" + i + "; " + res);
                            i++;
                        }
                    }

                }
                break;
            }
            default:
                u.update("Неверный запрос");
                break;
        }
    }

    private void addGroupToTree(String str) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(str);
        node.put(str, dmtn);
        root.add(dmtn);
        model.reload(root);
    }

    private void removeGroupFromTree(String str) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.removeNodeFromParent(node.get(str));
    }

}
