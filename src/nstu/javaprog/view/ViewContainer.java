package nstu.javaprog.view;

import javafx.util.Pair;
import nstu.javaprog.controller.WindowController;
import nstu.javaprog.model.Fish;
import nstu.javaprog.util.Coordinates;
import nstu.javaprog.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public final class ViewContainer extends JFrame {
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    private final Canvas canvas = new Canvas();
    private final Menu menu = new Menu();
    private final MenuBar menuBar = new MenuBar();
    private ControllerStatement statement = new ControllerStatement();
    private WindowController windowController;
    private Console console;
    private Network network;
    private boolean isTwoStepDeactivated;

    public ViewContainer(String title) {
        super(title);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        add(canvas, BorderLayout.CENTER);
        add(menu, BorderLayout.EAST);
        setJMenuBar(menuBar);

        configureListeners();
        pack();

        canvas.requestFocus();
    }

    public static Coordinates getRandomCoordinates() {
        return new Coordinates(
                (int) (Math.random() * WIDTH),
                (int) (Math.random() * HEIGHT)
        );
    }

    private void configureListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowController.saveGenerationSettings();
                if (console != null)
                    console.deactivate();
                if (network != null)
                    network.deactivate();
            }
        });
    }

    public final void prepare(WindowController windowController) {
        this.windowController = windowController;
        canvas.prepare(this);
        menu.prepare(this);
        menuBar.prepare(this);
    }

    public void drawEntities() {
        SwingUtilities.invokeLater(canvas::repaint);
    }

    public void updateTime() {
        canvas.updateTime();
    }

    boolean isGenerationActivated() {
        return windowController.isGenerationActivated();
    }

    void activateGeneration() {
        canvas.hideStatistic();
        menu.activateGeneration();
        menuBar.activateGeneration();
        windowController.activateGeneration();
        windowController.activateGolds();
        windowController.activateGuppies();
    }

    private void deactivateAllExecutors() {
        windowController.deactivateGeneration();
        windowController.deactivateGolds();
        windowController.deactivateGuppies();
    }

    void strongDeactivateGeneration() {
        canvas.deactivateGeneration();
        menu.deactivateGeneration();
        menuBar.deactivateGeneration();
        deactivateAllExecutors();
        windowController.reset();
    }

    void deactivateGeneration() {
        if (isTwoStepDeactivated) {
            statement.commitAndDeactivate();
            int option = JOptionPane.showConfirmDialog(
                    this,
                    getStatistic(),
                    "Deactivate generation",
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.OK_OPTION) {
                strongDeactivateGeneration();
                canvas.showStatistic();
            } else
                statement.rollback();
        } else {
            strongDeactivateGeneration();
            canvas.showStatistic();
        }
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

    List<Pair<Long, Integer>> getAliveEntities() {
        return windowController.getAliveEntities();
    }

    void changeStatisticView(JComponent component) {
        isTwoStepDeactivated = !isTwoStepDeactivated;
        if (menu != component)
            menu.changeStatisticView();
        if (menuBar != component)
            menuBar.changeStatisticView();
    }

    void doForEachEntity(Consumer<Fish> consumer) {
        windowController.doForEachEntity(consumer);
    }

    Properties getGoldProperties() {
        return windowController.getGoldProperties();
    }

    void setGoldProperties(Properties properties) {
        windowController.setGoldProperties(properties);
    }

    Properties getGuppyProperties() {
        return windowController.getGuppyProperties();
    }

    void setGuppyProperties(Properties properties) {
        windowController.setGuppyProperties(properties);
    }

    String getCurrentTime() {
        return Integer.toString(windowController.getCurrentTime());
    }

    String getStatistic() {
        return windowController.getStatistic();
    }

    void commitAndDeactivate() {
        statement.commitAndDeactivate();
    }

    void rollback() {
        statement.rollback();
    }

    void changeConsoleView() {
        if (console == null) {
            try {
                console = new Console(this);
                console.activate();
            } catch (IOException exception) {
                statement.commitAndDeactivate();
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Console error",
                        JOptionPane.ERROR_MESSAGE
                );
                statement.rollback();
            }
        } else
            console.changeVisibleState();
    }

    void activateNetwork() {
        if (network == null) {
            network = new Network(this);
            network.activate();
        }
    }

    void deactivateNetwork() {
        if (network != null)
            network = null;
    }

    void activateProgressBar() {
        canvas.activateProgressBar();
    }

    void deactivateProgressBar() {
        canvas.deactivateProgressBar();
    }

    void updateCanvas() {
        canvas.repaint();
        canvas.revalidate();
    }

    void open(File file, Consumer<Exception> callback) {
        windowController.open(file, callback);
    }

    void save(File file, Consumer<Exception> callback) {
        windowController.save(file, callback);
    }

    private class ControllerStatement {
        boolean isGenerationActivated;
        boolean isGoldsActivated;
        boolean isGuppiesActivated;

        void commitAndDeactivate() {
            isGenerationActivated = windowController.isGenerationActivated();
            isGoldsActivated = windowController.isGoldsActivated();
            isGuppiesActivated = windowController.isGuppiesActivated();
            deactivateAllExecutors();
        }

        void rollback() {
            if (isGenerationActivated)
                windowController.activateGeneration();
            if (isGoldsActivated)
                windowController.activateGolds();
            if (isGuppiesActivated)
                windowController.activateGuppies();
        }
    }
}
