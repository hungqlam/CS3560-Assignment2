import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserGroup extends User implements Component, Subject, Observer {
    private String id;
    private List<Component> members;
    private List<Observer> observers;

    private long creationTime; // Add creationTime attribute
    public UserGroup(String id) {

        this.id = id;
        this.members = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
    }

    // Subject methods for managing observers
    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
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

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(this, message);
        }
    }

    // Observer methods for receiving updates
    @Override
    public void update(Subject subject) {
        // Handle the update for changes in subjects this group is observing
    }

    @Override
    public void update(Subject subject, String message) {
        // Handle the update for changes in subjects this group is observing, including messages
    }

    // Component methods
    @Override
    public void add(Component component) {
        if (component != null && !members.contains(component)) {
            members.add(component);
            if (component instanceof Subject) {
                ((Subject) component).registerObserver(this); // Now we can register this UserGroup as an Observer
            }
            notifyObservers();
        }
    }

    @Override
    public void remove(Component component) {
        if (members.contains(component)) {
            members.remove(component);
            if (component instanceof Subject) {
                ((Subject) component).removeObserver(this);
            }
            notifyObservers();
        }
    }
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public Component getChild(int i) {
        return members.get(i);
    }

    @Override
    public String getName() {
        return id;
    }

    // ... Any additional UserGroup-specific methods ...
}
