package nstu.javaprog.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    public List<CanvasElement> get() {
        return elements;
    }

    public int getSize() {
        return elements.size();
    }
}
