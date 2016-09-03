/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Игорь
 */
public class GroupTree extends JTree {

    private static DefaultMutableTreeNode top = new DefaultMutableTreeNode("Перечень групп");

    public GroupTree() {
        super(top);
    }

}
