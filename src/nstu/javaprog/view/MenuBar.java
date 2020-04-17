package nstu.javaprog.view;

import javax.swing.*;

final class MenuBar extends JMenuBar implements Interdependent {
    private final ViewContainer container;
    private final JMenuItem activate = new JMenuItem("Activate");
    private final JMenuItem deactivate = new JMenuItem("Deactivate");
    private final JMenuItem pause = new JMenuItem("Pause");
    private final JMenuItem resume = new JMenuItem("Resume");
    private final JCheckBoxMenuItem showTime = new JCheckBoxMenuItem("Show the time");
    private final JCheckBoxMenuItem hideTime = new JCheckBoxMenuItem("Hide the time");
    private final JCheckBoxMenuItem showStatisticAsDialog = new JCheckBoxMenuItem("Statistic as dialog");

    MenuBar(ViewContainer container) {
        this.container = container;
        deactivate.setEnabled(false);
        pause.setEnabled(false);
        resume.setVisible(false);

        JMenu control = new JMenu("Control");
        JMenu time = new JMenu("Time");
        JMenu statistic = new JMenu("Statistic");

        control.add(activate);
        control.add(deactivate);
        control.add(pause);
        control.add(resume);
        add(control);

        hideTime.setSelected(true);
        time.add(showTime);
        time.add(hideTime);
        add(time);

        statistic.add(showStatisticAsDialog);
        add(statistic);

        configureListeners();
    }

    private void configureListeners() {
        activate.addActionListener(event -> {
            activate();
            container.activate(this);
        });

        deactivate.addActionListener(event -> {
            deactivate();
            container.deactivate(this);
        });

        pause.addActionListener(event -> {
            pause();
            container.pause(this);
        });

        resume.addActionListener(event -> {
            resume();
            container.resume(this);
        });

        showTime.addActionListener(event -> {
            showTime();
            container.showTime(this);
        });

        hideTime.addActionListener(event -> {
            hideTime();
            container.hideTime(this);
        });

        showStatisticAsDialog.addActionListener(event -> container.changeStatisticView(this));
    }

    @Override
    public void activate() {
        activate.setEnabled(false);
        deactivate.setEnabled(true);
        pause.setEnabled(true);
        revalidate();
    }

    @Override
    public void deactivate() {
        activate.setEnabled(true);
        deactivate.setEnabled(false);
        pause.setEnabled(false);
        resume();
        revalidate();
    }

    @Override
    public void pause() {
        pause.setVisible(false);
        resume.setVisible(true);
        revalidate();
    }

    @Override
    public void resume() {
        pause.setVisible(true);
        resume.setVisible(false);
        revalidate();
    }

    @Override
    public void showTime() {
        showTime.setSelected(true);
        hideTime.setSelected(false);
        revalidate();
    }

    @Override
    public void hideTime() {
        showTime.setSelected(false);
        hideTime.setSelected(true);
        revalidate();
    }

    @Override
    public void changeStatisticView() {
        showStatisticAsDialog.setSelected(!showStatisticAsDialog.isSelected());
        revalidate();
    }
}
