package ru.nstu.javaprog.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static ru.nstu.javaprog.util.ImageReader.readImage;

final class Canvas extends JPanel {
    private static final BufferedImage BACKGROUND_IMAGE = readImage("./resources/background.png");
    private final JTextArea statistic = new JTextArea(4, 30);
    private final JTextField currentTime = new JTextField(5);
    private final JProgressBar progressBar = new JProgressBar();
    private ViewContainer container;

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
                    case KeyEvent.VK_N:
                        container.activateNetwork();
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

    void deactivateGeneration() {
        currentTime.setText(container.getCurrentTime());
        statistic.setText(container.getStatistic());
        revalidate();
        repaint();
    }

    void showStatistic() {
        statistic.setVisible(true);
        revalidate();
    }

    void hideStatistic() {
        statistic.setVisible(false);
        revalidate();
    }

    void showTime() {
        currentTime.setVisible(true);
        revalidate();
    }

    void hideTime() {
        currentTime.setVisible(false);
        revalidate();
    }

    void activateProgressBar() {
        progressBar.setVisible(true);
        revalidate();
    }

    void deactivateProgressBar() {
        progressBar.setVisible(false);
        revalidate();
    }

    void updateTime() {
        currentTime.setText(container.getCurrentTime());
    }
}
