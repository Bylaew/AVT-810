package nstu.javaprog.view;

import nstu.javaprog.controller.WindowController;
import nstu.javaprog.util.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class ViewContainer extends JFrame {
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    private final ArrayList<Interdependent> components = new ArrayList<>();
    private final WindowController windowController;

    public ViewContainer(String title, WindowController windowController) {
        super(title);
        this.windowController = windowController;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        nstu.javaprog.view.Canvas canvas = new nstu.javaprog.view.Canvas(this);
        nstu.javaprog.view.Menu menu = new nstu.javaprog.view.Menu(this);
        nstu.javaprog.view.MenuBar menuBar = new nstu.javaprog.view.MenuBar(this);

        components.add(canvas);
        components.add(menu);
        components.add(menuBar);

        add(canvas, BorderLayout.CENTER);
        add(menu, BorderLayout.EAST);
        setJMenuBar(menuBar);

        pack();
        setVisible(true);
    }

    void activate(Interdependent initiator) {
        windowController.activate();
        components.forEach(component -> {
            if (component != initiator)
                component.activate();
        });
    }

    void deactivate(Interdependent initiator) {
        windowController.deactivate();
        components.forEach(component -> {
            if (component != initiator)
                component.deactivate();
        });
    }

    void pause(Interdependent initiator) {
        windowController.deactivate();
        components.forEach(component -> {
            if (component != initiator)
                component.pause();
        });
    }

    void resume(Interdependent initiator) {
        windowController.activate();
        components.forEach(component -> {
            if (component != initiator)
                component.resume();
        });
    }

    void showTime(Interdependent initiator) {
        components.forEach(component -> {
            if (component != initiator)
                component.showTime();
        });
    }

    void hideTime(Interdependent initiator) {
        components.forEach(component -> {
            if (component != initiator)
                component.hideTime();
        });
    }

    void changeStatisticView(Interdependent initiator) {
        components.forEach(component -> {
            if (component != initiator)
                component.changeStatisticView();
        });
    }

    void drawElements(Graphics graphics, int width, int height) {
        windowController.drawElements(graphics, width, height);
    }

    void moveElements() {
        windowController.moveElements();
    }

    void reset() {
        windowController.reset();
    }

    void changeGoldSettings() {
        if (windowController.isLaunched())
            pause(null);
        new EnvironmentSettings(
                this,
                windowController.getGoldProperties(),
                windowController::setGoldProperties
        ).setVisible(true);
    }

    void changeGuppySettings() {
        if (windowController.isLaunched())
            pause(null);
        new EnvironmentSettings(
                this,
                windowController.getGuppyProperties(),
                windowController::setGuppyProperties
        ).setVisible(true);
    }

    void showAliveElements() {
        if (windowController.isLaunched())
            pause(null);
        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(new JList<>(windowController.getAliveElementsInfo())),
                "Alive elements",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    String getCurrentTime() {
        return Integer.toString(windowController.getCurrentTime());
    }

    String getStatistic() {
        return windowController.getStatistic();
    }

    public static Coordinates getRandomCoordinates() {
        return new Coordinates(
                (int) (Math.random() * WIDTH),
                (int) (Math.random() * HEIGHT)
        );
    }
}
