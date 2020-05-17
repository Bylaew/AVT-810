package nstu.javaprog.view;

import nstu.javaprog.controller.WindowController;
import nstu.javaprog.exception.IllegalPropertiesException;
import nstu.javaprog.model.Fish;
import nstu.javaprog.util.Coordinates;
import nstu.javaprog.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public final class ViewContainer extends JFrame {
    private static final int WIDTH = 1080;
    private static final int HEIGHT = 720;
    private final Canvas canvas = new Canvas();
    private final Menu menu = new Menu();
    private final MenuBar menuBar = new MenuBar();
    private WindowController windowController = null;
    private Console console = null;
    private ControllerStatement statement = new ControllerStatement();
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
            }
        });
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
        canvas.activateGeneration();
        canvas.hideStatistic();
        menu.activateGeneration();
        menuBar.activateGeneration();
        windowController.activateGeneration();
        windowController.activateGolds();
        windowController.activateGuppies();
    }

    private void deactivateAll() {
        canvas.deactivateGeneration();
        canvas.showStatistic();
        canvas.updateStatistic();
        menu.deactivateGeneration();
        menuBar.deactivateGeneration();
        windowController.deactivateGeneration();
        windowController.deactivateGolds();
        windowController.deactivateGuppies();
        windowController.reset();
    }

    void deactivateGeneration() {
        if (isTwoStepDeactivated) {
            statement.commit();
            deactivateAllExecutors();
            int option = JOptionPane.showConfirmDialog(
                    this,
                    getStatistic(),
                    "Deactivate generation",
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.OK_OPTION)
                deactivateAll();
            else
                statement.rollback();
        } else
            deactivateAll();
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

    void doForEachEntity(Consumer<Fish> consumer) {
        windowController.doForEachEntity(consumer);
    }

    void changeGoldSettings() {
        statement.commit();
        deactivateAllExecutors();
        new EnvironmentSettings(
                this,
                windowController.getGoldProperties(),
                windowController::setGoldProperties
        ).activate();
        statement.rollback();
    }

    void changeGuppySettings() {
        statement.commit();
        deactivateAllExecutors();
        new EnvironmentSettings(
                this,
                windowController.getGuppyProperties(),
                windowController::setGuppyProperties
        ).activate();
        statement.rollback();
    }

    void showAliveEntities() {
        statement.commit();
        deactivateAllExecutors();
        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(
                        new JList<>(windowController.getAliveEntities()
                                .stream()
                                .map(pair -> "Entity id " + pair.getKey() + ", created at " + pair.getValue())
                                .toArray()
                        ),
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                ),
                "Alive entities",
                JOptionPane.INFORMATION_MESSAGE
        );
        statement.rollback();
    }

    float getGoldProbability() {
        return windowController.getGoldProperties().getChance();
    }

    void setGoldProbability(float probability) throws IllegalPropertiesException {
        Properties old = windowController.getGoldProperties();
        windowController.setGoldProperties(Properties.buildPropertiesWithCheckedException(
                probability,
                old.getDelay(),
                old.getMinSpeed(),
                old.getMaxSpeed(),
                old.getLifetime(),
                old.getPriority()
        ));
    }

    void changeConsoleView() {
        if (console == null) {
            try {
                console = new Console(this, this);
                console.activate();
            } catch (IOException exception) {
                statement.commit();
                deactivateAllExecutors();
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

    String getCurrentTime() {
        return Integer.toString(windowController.getCurrentTime());
    }

    String getStatistic() {
        return windowController.getStatistic();
    }

    void open() {
        statement.commit();
        deactivateAllExecutors();
        JFileChooser fileChooser = new JFileChooser(new File("./persistence"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            deactivateAll();
            canvas.hideStatistic();
            canvas.activateProgressBar();
            windowController.open(fileChooser.getSelectedFile(), (exception) ->
                    SwingUtilities.invokeLater(() -> {
                        if (exception == null) {
                            canvas.updateTime();
                            canvas.deactivateProgressBar();
                            canvas.repaint();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "File was successfully opened",
                                    "Open",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    exception.getMessage(),
                                    "Open error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            statement.rollback();
                        }
                    })
            );
        } else
            statement.rollback();
    }

    void save() {
        statement.commit();
        deactivateAllExecutors();
        JFileChooser fileChooser = new JFileChooser(new File("./persistence"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            canvas.activateProgressBar();
            windowController.save(fileChooser.getSelectedFile(), (exception) ->
                    SwingUtilities.invokeLater(() -> {
                        if (exception == null) {
                            canvas.deactivateProgressBar();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "File was successfully saved",
                                    "Save",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    exception.getMessage(),
                                    "Save error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                        statement.rollback();
                    })
            );
        } else
            statement.rollback();
    }

    private final class ControllerStatement {
        boolean isUpdaterActivated;
        boolean isGenerationActivated;
        boolean isGoldsActivated;
        boolean isGuppiesActivated;

        void commit() {
            isUpdaterActivated = canvas.isUpdaterActivated();
            isGenerationActivated = windowController.isGenerationActivated();
            isGoldsActivated = windowController.isGoldsActivated();
            isGuppiesActivated = windowController.isGuppiesActivated();
        }

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
