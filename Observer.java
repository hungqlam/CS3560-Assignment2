public interface Observer {
    void update(Subject subject);
    void update(Subject subject, String message); // Overloaded update method to receive a message
}
