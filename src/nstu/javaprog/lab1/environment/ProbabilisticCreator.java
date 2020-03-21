package nstu.javaprog.lab1.environment;

import nstu.javaprog.lab1.view.element.CanvasElement;

public interface ProbabilisticCreator {
    CanvasElement createCanvasElement();

    int getObjectCounter();

    void resetCounter();
}
