package nstu.javaprog.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import static nstu.javaprog.util.ImageReader.readImage;

final class Canvas extends JPanel {
    private static final BufferedImage BACKGROUND_IMAGE = readImage("./resources/background.png");
    private final TextArea statistic = new TextArea(4, 30);
    private final TextField currentTime = new TextField(5);
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

        add(currentTime);
        add(statistic);

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
                        if (container.isGenerationActivated())
                            container.activateGeneration();
                        break;
                    case KeyEvent.VK_E:
                        if (!container.isGenerationActivated())
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
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(BACKGROUND_IMAGE, 0, 0, getWidth(), getHeight(), null);
        container.doForEachElement(element -> {
            element.normalize(getWidth(), getHeight());
            element.draw(graphics);
        });
    }

    void activateGeneration() {
        activateUpdater();
        statistic.setVisible(false);
        revalidate();
    }

    void deactivateGeneration() {
        deactivateUpdater();
        statistic.setVisible(true);
        statistic.setText(container.getStatistic());
        currentTime.setText(container.getCurrentTime());
        revalidate();
        repaint();
    }

    void showTime() {
        currentTime.setText(container.getCurrentTime());
        currentTime.setVisible(true);
        revalidate();
    }

    void hideTime() {
        currentTime.setVisible(false);
        revalidate();
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

    private class CanvasUpdater {
        private volatile boolean isSuspended = true;

        void schedule() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended)
                        SwingUtilities.invokeLater(Canvas.this::repaint);
                }
            }, 0, 33);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended)
                        SwingUtilities.invokeLater(() -> {
                            if (currentTime.isVisible())
                                currentTime.setText(container.getCurrentTime());
                        });
                }
            }, 0, 1000);
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
