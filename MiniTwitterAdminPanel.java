import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MiniTwitterAdminPanel {

    private static MiniTwitterAdminPanel instance;
    private JFrame frame;
    private JTree tree;
    private DefaultTreeModel treeModel;
    private Map<String, Component> components;
    // Observer instance as a member variable
    private TreeViewObserver treeViewObserver;
    Map<String, User> allUsers; // A map to store all users by their ID
    // Private constructor for Singleton
    private MiniTwitterAdminPanel() {
        components = new HashMap<>();
        allUsers = new HashMap<>();
        // Initialize the tree and other components first
        initializeComponents();
        // After the tree is initialized, create the observer
        treeViewObserver = new TreeViewObserver(tree);
        // Now, the treeViewObserver can be passed to subjects
    }
    // Method to add a user to the system (and the map)
    public void addUserToSystem(User user) {
        allUsers.put(user.getName(), user);
        System.out.println("Added user to system: " + user.getName());
        System.out.println("All users: " + allUsers);

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        DefaultMutableTreeNode parent = null;
        if (selectedNode != null && selectedNode.getUserObject() instanceof UserGroup) {
            parent = selectedNode;
        }
        // Call the method that adds the user to the tree with the determined parent
        addUserToTree(user, parent);
        // Convert timestamp from milliseconds to a Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(user.getCreationTime());
        String formattedDate = dateFormat.format(date);
        System.out.println("Time creation of user: " + formattedDate);
    }







    // Method to retrieve a User by their ID
    public User getUserById(String id) {
        System.out.println("All users: " + allUsers);
        return allUsers.get(id);
    }
    // Example method that adds a user to the tree and the components map
// Overloaded method to add a user to the tree with a specified parent node
// Overloaded method to add a user to the tree with a specified parent node
    public void addUserToTree(User user, DefaultMutableTreeNode parent) {
        System.out.println("Attempting to add user to tree: " + user.getName());
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
        DefaultMutableTreeNode parentNode = parent != null ? parent : (DefaultMutableTreeNode) treeModel.getRoot();
        treeModel.insertNodeInto(userNode, parentNode, parentNode.getChildCount());
        components.put(user.getName(), user);
        // Make sure the tree structure is reloaded and the UI is updated
        treeModel.reload(parentNode);
        tree.expandPath(new TreePath(parentNode.getPath()));
        System.out.println("User added to tree: " + user.getName());
    }


    // Method to add a user group to the system (and the map)
    public void addUserGroupToSystem(UserGroup group) {
        allUsers.put(group.getName(), group); // Add the group to the allUsers map
        addUserGroupToTree(group); // Add the group to the JTree without specifying a parent node
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(group.getCreationTime());
        String formattedDate = dateFormat.format(date);
        System.out.println("Time creation of Group: " + formattedDate);
    }


    // Overloaded method to add a user group to the tree with a specified parent node
    public void addUserGroupToTree(UserGroup group, DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group);
        DefaultMutableTreeNode parentNode = parent != null ? parent : (DefaultMutableTreeNode) treeModel.getRoot();
        treeModel.insertNodeInto(groupNode, parentNode, parentNode.getChildCount());
        components.put(group.getName(), group);
        // Update the tree
        tree.expandPath(new TreePath(parentNode.getPath()));
    }

    // Overloaded method to add a user group to the tree without a specified parent node
    public void addUserGroupToTree(UserGroup group) {
        addUserGroupToTree(group, null); // Call the overloaded method with 'null' as the parent node
    }

    private void initializeComponents() {
        // Define the list of positive words
        List<String> positiveWords = Arrays.asList("good", "fine", "smile", "lol", "lmao", "looking forward", "see", "waiting for you");
        // Frame Initialization
        frame = new JFrame("Mini Twitter Admin Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Tree Initialization
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setCellRenderer(new BoldTreeCellRenderer());
        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(200, 400));

        // Right Panel Initialization with GridLayout
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(5, 5, 5, 5)); // Rows, Columns, Horizontal gap, Vertical gap

        // Text Fields for user and group IDs
        JTextField userIdField = new JTextField();
        JTextField groupIdField = new JTextField();

        // Buttons for adding users and groups, and other functionalities
        JButton addUserButton = new JButton("Add User");
        JButton addGroupButton = new JButton("Add Group");
        JButton openUserViewButton = new JButton("Open User View");
        JButton showUserTotalButton = new JButton("Show User Total");
        JButton showGroupTotalButton = new JButton("Show Group Total");
        JButton showMessagesTotalButton = new JButton("Show Messages Total");
        JButton showPositivePercentageButton = new JButton("Show Positive Percentage");
        // Now that tree is initialized, you can create the observer
        treeViewObserver = new TreeViewObserver(tree); // This should be after tree initialization
        // Add User Button ActionListener

// ActionListener for adding a group
        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupId = groupIdField.getText().trim();
                if (!groupId.isEmpty() && !components.containsKey(groupId)) {
                    UserGroup newGroup = new UserGroup(groupId);
                    newGroup.registerObserver(treeViewObserver);
                    // If the selected node is a group, add this new group as a subgroup.
                    // Otherwise, add it as a top-level group under the root.
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (selectedNode != null && selectedNode.getUserObject() instanceof UserGroup) {
                        addUserGroupToTree(newGroup, selectedNode);
                    } else {
                        addUserGroupToTree(newGroup, null); // null indicates adding to the root
                    }
                    groupIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Group ID or Group ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

// ActionListener for adding a user
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText().trim();
                if (!userId.isEmpty() && !allUsers.containsKey(userId)) {
                    User newUser = new User(userId);
                    newUser.registerObserver(treeViewObserver);
                    // Call addUserToSystem to add the user to the allUsers map and update the tree
                    addUserToSystem(newUser);
                    userIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid User ID or User ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });


        openUserViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = getSelectedNode();
                if (selectedNode != null) {
                    Object userObject = selectedNode.getUserObject();
                    if (userObject instanceof User) {
                        User selectedUser = (User) userObject;
                        User userToOpen = allUsers.get(selectedUser.getName()); // Retrieve from allUsers
                        if (userToOpen != null) {
                            SwingUtilities.invokeLater(() -> {
                                UserView userView = new UserView(userToOpen); // Pass the instance from allUsers
                                userView.setVisible(true);
                            });
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "The selected node is not a user.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "No node is currently selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }


        });
        // In your initializeComponents method or wherever you set up your buttons

        showUserTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userCount = countUsers();
                JOptionPane.showMessageDialog(frame,
                        "Total number of users: " + userCount,
                        "User Total",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        showGroupTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int groupCount = countGroups();
                JOptionPane.showMessageDialog(frame,
                        "Total number of groups: " + groupCount,
                        "Group Total",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        showMessagesTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalMessages = 0;
                for (User user : allUsers.values()) {
                    String[] tweets = user.getTweets().toArray(new String[0]); // Assuming getTweets returns an array of tweets
                    if (tweets != null) {
                        totalMessages += tweets.length;
                    }
                }
                JOptionPane.showMessageDialog(frame,
                        "Total number of tweets: " + totalMessages,
                        "Message Total",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        showPositivePercentageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalTweets = 0;
                int positiveTweets = 0;

                // Iterate over all users and their tweets to count positive tweets
                for (User user : allUsers.values()) {
                    String[] tweets = user.getTweets().toArray(new String[0]); // Assuming getTweets returns an array of tweet strings
                    if (tweets != null) {
                        for (String tweet : tweets) {
                            totalTweets++;
                            // Check if the tweet contains any of the positive words
                            for (String positiveWord : positiveWords) {
                                if (tweet.toLowerCase().contains(positiveWord.toLowerCase())) {
                                    positiveTweets++;
                                    break; // Break after finding the first positive word
                                }
                            }
                        }
                    }
                }

                // Calculate and display the positive percentage
                double percentage = (totalTweets > 0) ? (double) positiveTweets / totalTweets * 100 : 0;
                JOptionPane.showMessageDialog(frame,
                        String.format("Positive tweets percentage: %.2f%%", percentage),
                        "Positive Tweets Percentage",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });



        // Add components to right panel
        //rightPanel.add(new JLabel("User ID:"));
        rightPanel.add(userIdField);
        rightPanel.add(addUserButton);
        //rightPanel.add(new JLabel("Group ID:"));
        rightPanel.add(groupIdField);
        rightPanel.add(addGroupButton);
        rightPanel.add(openUserViewButton);
        rightPanel.add(new JLabel()); // Placeholder for alignment
        rightPanel.add(showUserTotalButton);
        rightPanel.add(showGroupTotalButton);
        rightPanel.add(showMessagesTotalButton);
        rightPanel.add(showPositivePercentageButton);

        // Add the treeScrollPane and rightPanel to the frame
        frame.add(treeScrollPane, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        // Frame Packing and Visibility
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private DefaultMutableTreeNode getSelectedNode() {
        DefaultMutableTreeNode selectedNode;
        TreePath selectionPath = tree.getSelectionPath();
        if (selectionPath == null) {
            selectedNode = (DefaultMutableTreeNode) treeModel.getRoot();
        } else {
            selectedNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
        }
        return selectedNode;
    }

    private void insertNodeIntoTree(Component component, DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(component.getName());
        treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());

        // Assuming that User and UserGroup objects are properly registered with the observer
        // There is no need to register them here again

        // Notify observers about the change if the component is a subject
        if (component instanceof Subject) {
            ((Subject) component).notifyObservers();
        }

        tree.expandPath(new TreePath(parentNode.getPath()));
    }



    public static MiniTwitterAdminPanel getInstance() {
        if (instance == null) {
            instance = new MiniTwitterAdminPanel();
        }
        return instance;
    }
    private int countUsers() {
        int count = 0;
        for (Component component : components.values()) {
            if (component instanceof User && !(component instanceof UserGroup)) {
                count++;
            }
        }
        return count;
    }

    private int countGroups() {
        int count = 0;
        for (Component component : components.values()) {
            if (component instanceof UserGroup) {
                count++;
            }
        }
        return count;
    }


    //public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> getInstance());
    //}
}
