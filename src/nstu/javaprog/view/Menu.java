package nstu.javaprog.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

final class Menu extends JPanel {
    private final JButton activate = new JButton("Activate");
    private final JButton deactivate = new JButton("Deactivate");
    private final JButton deactivateGolds = new JButton("Pause golds");
    private final JButton activateGolds = new JButton("Resume golds");
    private final JButton deactivateGuppies = new JButton("Pause guppies");
    private final JButton activateGuppies = new JButton("Resume guppies");
    private final JCheckBox showTime = new JCheckBox("Show the time");
    private final JCheckBox hideTime = new JCheckBox("Hide the time");
    private final JCheckBox statisticAsDialog = new JCheckBox("Statistic as dialog");
    private final JButton aliveEntities = new JButton("Show alive entities");
    private final JButton goldSettings = new JButton("Gold settings");
    private final JButton guppySettings = new JButton("Guppy settings");
    private ViewContainer container = null;

    Menu() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.BLACK);

        deactivate.setEnabled(false);
        deactivateGolds.setEnabled(false);
        activateGolds.setVisible(false);
        deactivateGuppies.setEnabled(false);
        activateGuppies.setVisible(false);
        hideTime.setSelected(true);

        activate.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        deactivate.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        deactivateGolds.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        activateGolds.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        deactivateGuppies.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        activateGuppies.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        showTime.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        hideTime.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        statisticAsDialog.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        aliveEntities.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        goldSettings.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        guppySettings.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        activate.setFocusable(false);
        deactivate.setFocusable(false);
        deactivateGolds.setFocusable(false);
        activateGolds.setFocusable(false);
        deactivateGuppies.setFocusable(false);
        activateGuppies.setFocusable(false);
        showTime.setFocusable(false);
        hideTime.setFocusable(false);
        statisticAsDialog.setFocusable(false);
        aliveEntities.setFocusable(false);
        goldSettings.setFocusable(false);
        guppySettings.setFocusable(false);

        LineBorder border = new LineBorder(Color.BLACK);
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bot = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
        middle.setLayout(new BoxLayout(middle, BoxLayout.PAGE_AXIS));
        bot.setLayout(new BoxLayout(bot, BoxLayout.PAGE_AXIS));

        setBorder(border);

        top.setBorder(border);
        top.add(activate);
        top.add(deactivate);
        top.add(deactivateGolds);
        top.add(activateGolds);
        top.add(deactivateGuppies);
        top.add(activateGuppies);
        add(top);

        middle.setBorder(border);
        middle.add(showTime);
        middle.add(hideTime);
        middle.add(new JSeparator(JSeparator.HORIZONTAL));
        middle.add(statisticAsDialog);
        middle.add(new JSeparator(JSeparator.HORIZONTAL));
        middle.add(aliveEntities);
        add(middle);

        bot.setBorder(border);
        bot.add(goldSettings);
        bot.add(guppySettings);
        add(bot);

        configureListeners();
    }

    final void prepare(ViewContainer container) {
        this.container = container;
    }

    private void configureListeners() {
        activate.addActionListener(event -> container.activateGeneration());

        deactivate.addActionListener(event -> container.deactivateGeneration());

        deactivateGolds.addActionListener(event -> container.deactivateGolds());

        activateGolds.addActionListener(event -> container.activateGolds());

        deactivateGuppies.addActionListener(event -> container.deactivateGuppies());

        activateGuppies.addActionListener(event -> container.activateGuppies());

        showTime.addActionListener(event -> container.showTime());

        hideTime.addActionListener(event -> container.hideTime());

        statisticAsDialog.addActionListener(event -> container.changeStatisticView(this));

        aliveEntities.addActionListener(event -> container.showAliveEntities());

        goldSettings.addActionListener(event -> container.changeGoldSettings());

        guppySettings.addActionListener(event -> container.changeGuppySettings());
    }

    void activateGeneration() {
        activate.setEnabled(false);
        deactivate.setEnabled(true);
        deactivateGolds.setEnabled(true);
        activateGolds.setEnabled(true);
        deactivateGuppies.setEnabled(true);
        activateGuppies.setEnabled(true);
        revalidate();
    }

    void deactivateGeneration() {
        activate.setEnabled(true);
        deactivate.setEnabled(false);
        deactivateGolds.setEnabled(false);
        activateGolds.setEnabled(false);
        deactivateGuppies.setEnabled(false);
        activateGuppies.setEnabled(false);
        activateGolds();
        activateGuppies();
        revalidate();
    }

    void activateGolds() {
        deactivateGolds.setVisible(true);
        activateGolds.setVisible(false);
        revalidate();
    }

    void deactivateGolds() {
        deactivateGolds.setVisible(false);
        activateGolds.setVisible(true);
        revalidate();
    }

    void activateGuppies() {
        deactivateGuppies.setVisible(true);
        activateGuppies.setVisible(false);
        revalidate();
    }

    void deactivateGuppies() {
        deactivateGuppies.setVisible(false);
        activateGuppies.setVisible(true);
        revalidate();
    }

    void showTime() {
        showTime.setSelected(true);
        hideTime.setSelected(false);
        revalidate();
    }

    void hideTime() {
        showTime.setSelected(false);
        hideTime.setSelected(true);
        revalidate();
    }

    void changeStatisticView() {
        statisticAsDialog.setSelected(!statisticAsDialog.isSelected());
        revalidate();
    }
}
