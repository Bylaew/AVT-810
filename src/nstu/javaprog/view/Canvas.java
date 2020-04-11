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

final class Canvas extends JPanel implements Interdependent {
    private static final BufferedImage BACKGROUND_IMAGE = readImage("./resources/background.png");

    private final ViewContainer container;
    private final TextArea statistic = new TextArea(4, 30);
    private final TextField currentTime = new TextField(5);
    private final CanvasUpdater canvasUpdater = new CanvasUpdater();
    private boolean isStatisticAsDialog = false;
    private boolean isPaused = false;

    Canvas(ViewContainer container) {
        this.container = container;
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

    private void configureListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_B:
                        if (canvasUpdater.isSuspended && !isPaused) {
                            activate();
                            container.activate(Canvas.this);
                        }
                        break;
                    case KeyEvent.VK_E:
                        if (!canvasUpdater.isSuspended || isPaused) {
                            deactivate();
                            container.deactivate(Canvas.this);
                        }
                        break;
                    case KeyEvent.VK_T:
                        if (currentTime.isVisible()) {
                            hideTime();
                            container.hideTime(Canvas.this);
                        } else {
                            showTime();
                            container.showTime(Canvas.this);
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

    private void setDeactivatedScene() {
        statistic.setVisible(true);
        statistic.setText(container.getStatistic());
        container.reset();
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(BACKGROUND_IMAGE, 0, 0, getWidth(), getHeight(), null);
        container.drawElements(graphics, getWidth(), getHeight());
    }

    @Override
    public void activate() {
        canvasUpdater.activate();
        statistic.setVisible(false);
        revalidate();
    }

    @Override
    public void deactivate() {
        canvasUpdater.deactivate();
        if (isStatisticAsDialog) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    container.getStatistic(),
                    "Deactivate generation",
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (option == JOptionPane.OK_OPTION)
                setDeactivatedScene();
            else {
                activate();
                container.activate(this);
                if (isPaused) {
                    pause();
                    container.pause(this);
                }
            }
        } else
            setDeactivatedScene();
    }

    @Override
    public void pause() {
        isPaused = true;
        canvasUpdater.deactivate();
    }

    @Override
    public void resume() {
        isPaused = false;
        canvasUpdater.activate();
    }

    @Override
    public void showTime() {
        currentTime.setText(container.getCurrentTime());
        currentTime.setVisible(true);
        revalidate();
    }

    @Override
    public void hideTime() {
        currentTime.setVisible(false);
        revalidate();
    }

    @Override
    public void changeStatisticView() {
        isStatisticAsDialog = !isStatisticAsDialog;
    }

    private class CanvasUpdater {
        private volatile boolean isSuspended = true;

        CanvasUpdater() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended)
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                container.moveElements();
                                repaint();
                            }
                        });
                }
            }, 0, 33);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended)
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (currentTime.isVisible())
                                    currentTime.setText(container.getCurrentTime());
                            }
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
    }
}
