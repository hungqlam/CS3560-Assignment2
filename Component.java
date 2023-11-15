// Define the Component interface for Composite Pattern
public interface Component {
    void add(Component component);

    void remove(Component component);

    Component getChild(int i);

    String getName();
}
