import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class TreeViewObserver implements Observer {
    private JTree tree;

    public TreeViewObserver(JTree tree) {
        this.tree = tree;
    }

    // This method is called when a user is followed/unfollowed, or other non-tweet updates occur
    @Override
    public void update(Subject subject) {
        if (SwingUtilities.isEventDispatchThread()) {
            ((DefaultTreeModel) tree.getModel()).nodeChanged(findTreeNode(subject));
        } else {
            SwingUtilities.invokeLater(() -> ((DefaultTreeModel) tree.getModel()).nodeChanged(findTreeNode(subject)));
        }
    }

    // This method is called when a tweet is posted
    @Override
    public void update(Subject subject, String message) {
        // For tweet updates, if you want to show a notification or refresh part of the tree, implement it here.
        // If you want to update the tree structure as well, you can call the same logic as in the above update method.
        update(subject); // For now, we'll just refresh the tree as with any other update.
    }

    // Helper method to find the tree node associated with a subject
    private TreeNode findTreeNode(Subject subject) {
        // Start with the root node and search recursively
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        return findTreeNodeRecursive(root, subject);
    }

    // Recursive method to search for the node
    private TreeNode findTreeNodeRecursive(DefaultMutableTreeNode currentNode, Subject subject) {
        if (currentNode.getUserObject().equals(subject)) {
            // The user object of the current node matches the subject
            return currentNode;
        } else {
            // Continue the search with child nodes
            for (int i = 0; i < currentNode.getChildCount(); i++) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) currentNode.getChildAt(i);
                TreeNode result = findTreeNodeRecursive(childNode, subject);
                if (result != null) {
                    // Found the matching node in a child
                    return result;
                }
            }
        }
        // No match found in this branch
        return null;
    }

}
