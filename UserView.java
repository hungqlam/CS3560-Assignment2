import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
public class UserView extends JFrame {
    private User user; // The user this view represents

    // Components for the user view
    private JTextArea tweetArea;
    private JTextArea newsFeedArea;
    private JTextField tweetInput;
    private JTextField followUserInput; // Declaration for the follow user input field
    private JTextArea followedUsersArea; // To display the users that this user is following
    private JLabel followedUsersCountLabel; // To display the count of followed users
    // Declaration of the class field
    private JScrollPane followedUsersScrollPane;

    public UserView(User user) {
        this.user = user;
        initializeComponents();
        updateTweetArea();
        updateNewsFeedArea();
        updateFollowedUsersArea();
    }

    private void initializeComponents() {
        setTitle("User View - " + user.getName());
        setSize(500, 600);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Initialize all components first
        tweetArea = new JTextArea(10, 20);
        tweetArea.setEditable(false);
        JScrollPane tweetScrollPane = new JScrollPane(tweetArea);
        tweetScrollPane.setBorder(BorderFactory.createTitledBorder("Tweets"));

        followUserInput = new JTextField(20);
        JButton followButton = new JButton("Follow User");
        followButton.addActionListener(e -> followUser());

        JPanel followUserPanel = new JPanel();
        followUserPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        followUserPanel.add(followUserInput);
        followUserPanel.add(followButton);

        tweetInput = new JTextField(20);
        JButton tweetButton = new JButton("Post Tweet");
        tweetButton.addActionListener(e -> postTweet());

        JPanel tweetMessagePanel = new JPanel();
        tweetMessagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tweetMessagePanel.add(tweetInput);
        tweetMessagePanel.add(tweetButton);

        followedUsersArea = new JTextArea(5, 20);
        followedUsersArea.setEditable(false);
        JScrollPane followedScrollPane = new JScrollPane(followedUsersArea);
        followedScrollPane.setBorder(BorderFactory.createTitledBorder("Following"));

        newsFeedArea = new JTextArea(10, 20);
        newsFeedArea.setEditable(false);
        JScrollPane newsFeedScrollPane = new JScrollPane(newsFeedArea);
        newsFeedScrollPane.setBorder(BorderFactory.createTitledBorder("News Feed"));

        // Add all the initialized components to the layout
        add(followUserPanel);
        add(followedScrollPane);
        add(tweetMessagePanel);
        add(tweetScrollPane);
        add(newsFeedScrollPane);

        // Call the update methods after all components are initialized and added to the layout
        updateTweetArea();
        updateNewsFeedArea();
        updateFollowedUsersArea();

        // Finalize the layout
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    private void postTweet() {
        String tweet = tweetInput.getText().trim();
        if (!tweet.isEmpty()) {
            user.postTweet(tweet);
            tweetInput.setText("");
            updateTweetArea();
            updateNewsFeedArea(); // If you want to show the user's tweets in their news feed
        }
    }

    // Make sure the update methods check for null before attempting to set text on the JTextAreas
    private void updateTweetArea() {
        if (tweetArea != null && user.getTweets() != null) {
            StringBuilder tweetsContent = new StringBuilder();
            for (String tweet : user.getTweets()) {
                tweetsContent.append(tweet).append("\n");
            }
            tweetArea.setText(tweetsContent.toString());
        }
    }

    private void updateNewsFeedArea() {
        if (newsFeedArea != null && user.getNewsFeed() != null) {
            StringBuilder newsFeedContent = new StringBuilder();
            for (String news : user.getNewsFeed()) {
                newsFeedContent.append(news).append("\n");
            }
            newsFeedArea.setText(newsFeedContent.toString());
        }
    }


    private void followUser() {

        try {
            String userIdToFollow = followUserInput.getText().trim();
            System.out.println("Attempting to follow user with ID: " + userIdToFollow); // Debugging output

            if (!userIdToFollow.isEmpty()) {
                User userToFollow = MiniTwitterAdminPanel.getInstance().getUserById(userIdToFollow);
                if (userToFollow != null && !user.equals(userToFollow)) {
                    System.out.println("User before following: " + user.getFollowings());
                    user.followUser(userToFollow);
                    System.out.println("User after following: " + user.getFollowings());
                    System.out.println("Now following user: " + userToFollow.getName()); // Debugging output

                    SwingUtilities.invokeLater(() -> {
                        followUserInput.setText("");
                        JOptionPane.showMessageDialog(this, "You are now following " + userToFollow.getName(), "Followed", JOptionPane.INFORMATION_MESSAGE);
                        updateFollowedUsersArea(); // This line should update the UI
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "User not found or cannot follow oneself", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print any exception to the console
        }
    }

    private void updateFollowedUsersArea() {
        // Ensure this runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            if (followedUsersArea != null) {
                StringBuilder followedUsersContent = new StringBuilder();
                User[] followedUsers = user.getFollowings().toArray(new User[0]); // Assuming getFollowedUsers returns an array of User.
                if (followedUsers != null) {
                    for (User followedUser : followedUsers) {
                        if (followedUser != null) {
                            followedUsersContent.append(followedUser.getName()).append("\n");
                        }
                    }
                    followedUsersArea.setText(followedUsersContent.toString());
                } else {
                    followedUsersArea.setText("No followed users.");
                }
            }
        });
    }



}
