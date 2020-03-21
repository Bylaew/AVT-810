package nstu.javaprog.lab1.view.canvas;

import nstu.javaprog.lab1.controller.WindowController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import static nstu.javaprog.lab1.util.ImageReader.readImage;

public class MainWindow extends JFrame {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    private static final BufferedImage BACKGROUND_IMAGE = readImage("./resources/background.png");
    private TextArea statistic = new TextArea(4, 30);
    private TextField currentTime = new TextField(5);
    private JPanel canvas;
    private CanvasUpdater canvasUpdater;
    private WindowController windowController;

    public MainWindow(int width, int height, String title, WindowController windowController) {
        super(title);
        this.windowController = windowController;
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                graphics.drawImage(BACKGROUND_IMAGE, 0, 0, null);
                windowController.updateCanvas(graphics, width, height);
            }
        };
        statistic.setVisible(false);
        statistic.setEditable(false);
        statistic.setFocusable(false);
        statistic.setFont(new Font("Century Gothic", Font.ITALIC, 14));
        currentTime.setVisible(false);
        currentTime.setEditable(false);
        currentTime.setFocusable(false);
        currentTime.setFont(new Font(null, Font.BOLD, 10));
        canvas.setBackground(Color.WHITE);
        canvas.setLayout(new FlowLayout(FlowLayout.RIGHT));
        canvas.setDoubleBuffered(true);
        canvas.add(statistic);
        canvas.add(currentTime);
        add(canvas);
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        configureListeners();
        setResizable(false);
        setVisible(true);
        canvasUpdater = new CanvasUpdater();
    }

    private void configureListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_B:
                        windowController.activate();
                        canvasUpdater.activate();
                        statistic.setVisible(false);
                        currentTime.setVisible(false);
                        break;
                    case KeyEvent.VK_E:
                        windowController.deactivate();
                        canvasUpdater.deactivate();
                        statistic.setVisible(true);
                        currentTime.setVisible(false);
                        statistic.setText(windowController.getStatistic());
                        canvas.revalidate();
                        break;
                    case KeyEvent.VK_T:
                        currentTime.setVisible(!currentTime.isVisible());
                        canvas.revalidate();
                        break;
                }
            }
        });
    }

    private class CanvasUpdater {
        private volatile boolean isSuspended = true;

        public CanvasUpdater() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSuspended)
                        SwingUtilities.invokeLater(() -> {
                            if (currentTime.isVisible())
                                currentTime.setText(Long.toString(windowController.getCurrentTime()));
                            canvas.repaint();
                        });
                }
            }, 0, 33);
        }

        public void activate() {
            isSuspended = false;
        }

        public void deactivate() {
            isSuspended = true;
        }
    }

}
