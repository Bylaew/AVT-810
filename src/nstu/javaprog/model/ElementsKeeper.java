package nstu.javaprog.model;

import nstu.javaprog.view.CanvasElement;

import java.util.*;
import java.util.function.Consumer;

final class ElementsKeeper {
    public static final ElementsKeeper INSTANCE = new ElementsKeeper();
    private final List<Fish> elements = Collections.synchronizedList(new LinkedList<>());
    private final Map<Long, Integer> generationTimes = Collections.synchronizedMap(new TreeMap<>());

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Set<Long> ids = new HashSet<>();

    private ElementsKeeper() {
    }

    void add(Fish element, int time) {
        elements.add(element);
        generationTimes.put(element.getId(), time);
        ids.add(element.getId());
    }

    void removeDeadElements(int time) {
        final List<Fish> deadFishes = new LinkedList<>();
        elements.forEach((fish -> {
            if (generationTimes.get(fish.getId()) + fish.getLifetime() <= time)
                deadFishes.add(fish);
        }));
        for (Fish fish : deadFishes) {
            generationTimes.remove(fish.getId());
            ids.remove(fish.getId());
            elements.remove(fish);
        }
    }

    Object[] getAliveElementsInfo() {
        List<String> info = new LinkedList<>();
        generationTimes.forEach((key, value) -> info.add(
                "Element id " + key + " was created at " + value + "\n"
        ));
        return info.toArray();
    }

    void removeAll() {
        elements.clear();
        generationTimes.clear();
        ids.clear();
    }

    void doForEachElement(Consumer<CanvasElement> consumer) {
        elements.forEach(consumer);
    }

    int getSize() {
        return elements.size();
    }
}
