// Define the Subject interface
public interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
    void notifyObservers(String message);
    void update(Subject subject);
    void update(Subject subject, String message);

}
