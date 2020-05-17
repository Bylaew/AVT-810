package nstu.javaprog.view;

import javax.swing.*;

final class MenuBar extends JMenuBar {
    private final JMenuItem open = new JMenuItem("Open");
    private final JMenuItem save = new JMenuItem("Save");
    private final JMenuItem activate = new JMenuItem("Activate");
    private final JMenuItem deactivate = new JMenuItem("Deactivate");
    private final JMenuItem deactivateGolds = new JMenuItem("Pause golds");
    private final JMenuItem activateGolds = new JMenuItem("Resume golds");
    private final JMenuItem deactivateGuppies = new JMenuItem("Pause guppies");
    private final JMenuItem activateGuppies = new JMenuItem("Resume guppies");
    private final JCheckBoxMenuItem showTime = new JCheckBoxMenuItem("Show the time");
    private final JCheckBoxMenuItem hideTime = new JCheckBoxMenuItem("Hide the time");
    private final JCheckBoxMenuItem statisticAsDialog = new JCheckBoxMenuItem("Statistic as dialog");
    private ViewContainer container = null;

    MenuBar() {
        deactivate.setEnabled(false);
        deactivateGolds.setEnabled(false);
        activateGolds.setVisible(false);
        deactivateGuppies.setEnabled(false);
        activateGuppies.setVisible(false);

        JMenu file = new JMenu("File");
        JMenu control = new JMenu("Control");
        JMenu time = new JMenu("Time");
        JMenu statistic = new JMenu("Statistic");

        file.add(open);
        file.add(save);
        add(file);

        control.add(activate);
        control.add(deactivate);
        control.add(deactivateGolds);
        control.add(activateGolds);
        control.add(deactivateGuppies);
        control.add(activateGuppies);
        add(control);

        hideTime.setSelected(true);
        time.add(showTime);
        time.add(hideTime);
        add(time);

        statistic.add(statisticAsDialog);
        add(statistic);

        configureListeners();
    }

    final void prepare(ViewContainer container) {
        this.container = container;
    }

    private void configureListeners() {
        open.addActionListener(event -> container.open());

        save.addActionListener(event -> container.save());

        activate.addActionListener(event -> container.activateGeneration());

        deactivate.addActionListener(event -> container.deactivateGeneration());

        deactivateGolds.addActionListener(event -> container.deactivateGolds());

        activateGolds.addActionListener(event -> container.activateGolds());

        deactivateGuppies.addActionListener(event -> container.deactivateGuppies());

        activateGuppies.addActionListener(event -> container.activateGuppies());

        showTime.addActionListener(event -> container.showTime());

        hideTime.addActionListener(event -> container.hideTime());

        statisticAsDialog.addActionListener(event -> container.changeStatisticView(this));
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
