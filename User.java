import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public  class User implements Component, Subject, Observer {
    private String id; // Unique identifier for the user
    private List<User> followers; // List of users who follow this user
    private List<Observer> observers; // Observers for the Observer pattern
    private List<String> newsFeed; // This list will store the tweets for the user's news feed
    private List<String> tweets = new ArrayList<>(); // Initialize the list
    private List<User> followings = new ArrayList<>(); // Initialize the list
    private List<User> followedUsers;
    public User(String id) {
        this.id = id;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.tweets = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.newsFeed = new ArrayList<>(); // Initialize the news feed
        this.newsFeed = new ArrayList<>();
        this.followedUsers = new ArrayList<>();
    }

    public User() {
        // Initialize with default values or leave it empty
        this.id = ""; // You can assign a default value or generate an ID
        // Initialize other fields if necessary
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.tweets = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
    }


    // Implement Component methods
    // Users cannot have children, so these methods can throw an UnsupportedOperationException
    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException("Users cannot add components.");
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException("Users cannot remove components.");
    }

    @Override
    public Component getChild(int i) {
        throw new UnsupportedOperationException("Users do not have children components.");
    }



    // Implement Subject methods to manage observers
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    // Additional methods related to user functionality

    // New method to notify observers with the tweet content
// Overloaded notifyObservers method that includes a message
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(this, message);
        }
    }

    @Override
    public void update(Subject subject) {

    }

    public void postTweet(String tweet) {
        if (tweet == null || tweet.trim().isEmpty()) {
            throw new IllegalArgumentException("Tweet cannot be null or empty.");
        }
        this.tweets.add(tweet);
        // Notify observers with the tweet content
        notifyObservers(tweet);
    }
    // Method for a user to receive a tweet from another user they follow

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id); // Assuming 'id' is the unique identifier
    }
    // Implement Observer methods

    @Override
    public void update(Subject subject, String message) {
        if (subject instanceof User) {
            // Logic for handling the receipt of a tweet from a user being followed
            receiveTweet((User) subject, message);
            // If additional logic is needed to handle different types of updates,
            // it can be added here as well, possibly with more conditional checks.
        }
    }

    // Helper method to handle receiving a tweet and adding it to the news feed
    private void receiveTweet(User user, String tweet) {
        // Assuming 'newsFeed' is a List<String> containing tweets from followed users
        newsFeed.add(user.getName() + ": " + tweet);
        // If there is a UI component for news feed, refresh it here
        // ... other code to handle a received tweet, if necessary ...
    }



    // Helper method to add a tweet to the user's news feed
    private void addTweetToNewsFeed(User user, String tweet) {
        // Assuming 'newsFeed' is a List<String> containing tweets from followed users
        newsFeed.add(user.getName() + ": " + tweet);
        // If there is a UI component for news feed, refresh it here
    }
    public void followUser(User userToFollow) {
        if (userToFollow == null || userToFollow == this || this.followings.contains(userToFollow)) {
            // Handle invalid follow attempts
            return;
        }
        this.followings.add(userToFollow);
        userToFollow.getFollowers().add(this);
        userToFollow.registerObserver(this); // No need to cast this to Observer
        // Notify this user's observers about the new following
        notifyObservers();
        // Optionally, notify the followed user's observers that they have a new follower
        userToFollow.notifyObservers();
    }

    // Getters for followers, followings, and tweets
    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowings() {
        return followings;
    }

    public List<String> getTweets() {
        return tweets;
    }
    @Override
    public String toString() {
        return this.getName(); // or any other appropriate representation of the User
    }
    @Override
    public String getName() {
        return this.id; // Assuming 'id' holds the user's name
    }

    public String[] getNewsFeed() {
        // Convert the newsFeed list to an array and return it
        String[] feedArray = new String[newsFeed.size()];
        return newsFeed.toArray(feedArray);
    }

    // Method to get the followed users in an array form
    public User[] getFollowedUsers() {
        // Convert the followedUsers list to an array and return it
        User[] usersArray = new User[followedUsers.size()];
        return followedUsers.toArray(usersArray);
    }

    // Methods to add data to the lists
    public void addNewsFeedItem(String newsItem) {
        newsFeed.add(newsItem);
    }

    public void addFollowedUser(User user) {
        followedUsers.add(user);
    }
}
