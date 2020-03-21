package nstu.javaprog.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

final class ElementsKeeper {
    public static final ElementsKeeper INSTANCE = new ElementsKeeper();
    private final List<CanvasElement> elements = Collections.synchronizedList(new LinkedList<>());

    private ElementsKeeper() {
    }

    public void removeAll() {
        elements.clear();
    }

    public void add(CanvasElement element) {
        elements.add(element);
    }

    public void foreach(Consumer<? super CanvasElement> consumer) {
        elements.forEach(consumer);
    }

    public int getSize() {
        return elements.size();
    }
}
