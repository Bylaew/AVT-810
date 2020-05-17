package nstu.javaprog.view;

import nstu.javaprog.controller.WindowController;
import nstu.javaprog.model.Fish;
import nstu.javaprog.util.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public final class ViewContainer extends JFrame {
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    private final Canvas canvas = new Canvas();
    private final Menu menu = new Menu();
    private final MenuBar menuBar = new MenuBar();
    private WindowController windowController = null;
    private boolean isTwoStepDeactivated = false;

    public ViewContainer(String title) {
        super(title);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        add(canvas, BorderLayout.CENTER);
        add(menu, BorderLayout.EAST);
        setJMenuBar(menuBar);

        pack();
        setVisible(true);
    }

    public static Coordinates getRandomCoordinates() {
        return new Coordinates(
                (int) (Math.random() * WIDTH),
                (int) (Math.random() * HEIGHT)
        );
    }

    public final void prepare(WindowController windowController) {
        this.windowController = windowController;
        canvas.prepare(this);
        menu.prepare(this);
        menuBar.prepare(this);
    }

    boolean isGenerationActivated() {
        return windowController.isGenerationActivated();
    }

    void activateGeneration() {
        windowController.activateGeneration();
        windowController.activateGolds();
        windowController.activateGuppies();
        canvas.activateGeneration();
        menu.activateGeneration();
        menuBar.activateGeneration();
    }

    private void deactivate() {
        windowController.deactivateGeneration();
        windowController.deactivateGolds();
        windowController.deactivateGuppies();
        canvas.deactivateGeneration();
        menu.deactivateGeneration();
        menuBar.deactivateGeneration();
        windowController.reset();
    }

    void deactivateGeneration() {
        if (isTwoStepDeactivated) {
            ControllerStatement statement = new ControllerStatement();
            windowController.deactivateGeneration();
            windowController.deactivateGolds();
            windowController.deactivateGuppies();
            int option = JOptionPane.showConfirmDialog(
                    this,
                    getStatistic(),
                    "Deactivate generation",
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.OK_OPTION)
                deactivate();
            else
                statement.rollback();
        } else
            deactivate();
    }

    void activateGolds() {
        windowController.activateGolds();
        menu.activateGolds();
        menuBar.activateGolds();
    }

    void deactivateGolds() {
        windowController.deactivateGolds();
        menu.deactivateGolds();
        menuBar.deactivateGolds();
    }

    void activateGuppies() {
        windowController.activateGuppies();
        menu.activateGuppies();
        menuBar.activateGuppies();
    }

    void deactivateGuppies() {
        windowController.deactivateGuppies();
        menu.deactivateGuppies();
        menuBar.deactivateGuppies();
    }

    void showTime() {
        canvas.showTime();
        menu.showTime();
        menuBar.showTime();
    }

    void hideTime() {
        canvas.hideTime();
        menu.hideTime();
        menuBar.hideTime();
    }

    private void deactivateAllExecutors() {
        canvas.deactivateUpdater();
        windowController.deactivateGeneration();
        windowController.deactivateGolds();
        windowController.deactivateGuppies();
    }

    void changeStatisticView(JComponent component) {
        isTwoStepDeactivated = !isTwoStepDeactivated;
        if (menu != component)
            menu.changeStatisticView();
        if (menuBar != component)
            menuBar.changeStatisticView();
    }

    void doForEachElement(Consumer<? super Fish> consumer) {
        windowController.doForEachElement(consumer);
    }

    void changeGoldSettings() {
        ControllerStatement statement = new ControllerStatement();
        deactivateAllExecutors();
        new EnvironmentSettings(
                this,
                windowController.getGoldProperties(),
                windowController::setGoldProperties
        ).setVisible(true);
        statement.rollback();
    }

    void changeGuppySettings() {
        ControllerStatement statement = new ControllerStatement();
        deactivateAllExecutors();
        new EnvironmentSettings(
                this,
                windowController.getGuppyProperties(),
                windowController::setGuppyProperties
        ).setVisible(true);
        statement.rollback();
    }

    void showAliveElements() {
        ControllerStatement statement = new ControllerStatement();
        deactivateAllExecutors();
        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(new JList<>(windowController.getAliveElements()
                        .stream()
                        .map(pair -> "Element id " + pair.getKey() + ", created at " + pair.getValue())
                        .toArray()
                )),
                "Alive elements",
                JOptionPane.INFORMATION_MESSAGE
        );
        statement.rollback();
    }

    String getCurrentTime() {
        return Integer.toString(windowController.getCurrentTime());
    }

    String getStatistic() {
        return windowController.getStatistic();
    }

    private final class ControllerStatement {
        final boolean isUpdaterActivated = canvas.isUpdaterActivated();
        final boolean isGenerationActivated = windowController.isGenerationActivated();
        final boolean isGoldsActivated = windowController.isGoldsActivated();
        final boolean isGuppiesActivated = windowController.isGuppiesActivated();

        void rollback() {
            if (isUpdaterActivated)
                canvas.activateUpdater();
            if (isGenerationActivated)
                windowController.activateGeneration();
            if (isGoldsActivated)
                windowController.activateGolds();
            if (isGuppiesActivated)
                windowController.activateGuppies();
        }
    }
}
