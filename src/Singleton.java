import java.util.*;

final class Singleton {
    public static final Singleton INSTANCE = new Singleton();
    private final List<Bee> elements = Collections.synchronizedList(new LinkedList<>());

    private Singleton() {
    }

    public void removeAll() {
        elements.clear();
    }

    public void add(Bee element) {
        elements.add(element);
    }

    public List<Bee> get() {
        return elements;
    }

    public int getSize() {
        return elements.size();
    }
}