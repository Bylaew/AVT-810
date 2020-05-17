package nstu.javaprog.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import static nstu.javaprog.util.ImageReader.readImage;

final class Canvas extends JPanel {
    private static final BufferedImage BACKGROUND_IMAGE = readImage("./resources/background.png");
    private final TextArea statistic = new TextArea(4, 30);
    private final TextField currentTime = new TextField(5);
    private final JProgressBar progressBar = new JProgressBar();
    private final CanvasUpdater canvasUpdater = new CanvasUpdater();
    private ViewContainer container = null;

    Canvas() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setDoubleBuffered(true);

        statistic.setVisible(false);
        statistic.setEditable(false);
        statistic.setFocusable(false);
        statistic.setFont(new Font("Century Gothic", Font.ITALIC, 14));

        currentTime.setVisible(false);
        currentTime.setEditable(false);
        currentTime.setFocusable(false);
        currentTime.setFont(new Font(null, Font.BOLD, 10));

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        add(currentTime);
        add(statistic);
        add(progressBar);

        configureListeners();
    }

    final void prepare(ViewContainer container) {
        this.container = container;
        canvasUpdater.schedule();
    }

    private void configureListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_B:
                        if (!container.isGenerationActivated())
                            container.activateGeneration();
                        break;
                    case KeyEvent.VK_E:
                        if (container.isGenerationActivated())
                            container.deactivateGeneration();
                        break;
                    case KeyEvent.VK_T:
                        if (currentTime.isVisible()) {
                            hideTime();
                            container.hideTime();
                        } else {
                            showTime();
                            container.showTime();
                        }
                        break;
                    case KeyEvent.VK_C:
                        container.changeConsoleView();
                        break;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(BACKGROUND_IMAGE, 0, 0, getWidth(), getHeight(), null);
        container.doForEachEntity(entity -> {
            entity.normalize(getWidth(), getHeight());
            entity.draw(graphics);
        });
    }

    void activateGeneration() {
        activateUpdater();
    }

    void deactivateGeneration() {
        deactivateUpdater();
        currentTime.setText(container.getCurrentTime());
        revalidate();
        repaint();
    }

    void showStatistic() {
        statistic.setVisible(true);
    }

    void hideStatistic() {
        statistic.setVisible(false);
    }

    void updateStatistic() {
        statistic.setText(container.getStatistic());
    }

    void showTime() {
        currentTime.setVisible(true);
        revalidate();
    }

    void hideTime() {
        currentTime.setVisible(false);
        revalidate();
    }

    void updateTime() {
        currentTime.setText(container.getCurrentTime());
    }

    boolean isUpdaterActivated() {
        return canvasUpdater.isActivated();
    }

    void activateUpdater() {
        canvasUpdater.activate();
    }

    void deactivateUpdater() {
        canvasUpdater.deactivate();
    }

    void activateProgressBar() {
        progressBar.setVisible(true);
        revalidate();
    }

    void deactivateProgressBar() {
        progressBar.setVisible(false);
        revalidate();
    }

    private class CanvasUpdater {
        private volatile boolean isSuspended = true;

        void schedule() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended) {
                        SwingUtilities.invokeLater(() -> {
                            repaint();
                            if (currentTime.isVisible())
                                updateTime();
                        });
                    }
                }
            }, 0, 33);
        }

        void activate() {
            isSuspended = false;
        }

        void deactivate() {
            isSuspended = true;
        }

        boolean isActivated() {
            return !isSuspended;
        }
    }
}
