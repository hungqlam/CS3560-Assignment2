public class Follower implements Observer {
    // Implementing Observer interface
    @Override
    public void update(Subject subject) {
        // General update without a message
    }

    @Override
    public void update(Subject subject, String message) {
        if (subject instanceof User) {
            System.out.println("New tweet from " + ((User) subject).getName() + ": " + message);
            // Additional logic to handle the new tweet, e.g., adding it to the news feed
        }
    }
}
