package nstu.javaprog.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class Menu extends JPanel implements Interdependent {
    private final ViewContainer container;
    private final JButton activate = new JButton("Activate");
    private final JButton deactivate = new JButton("Deactivate");
    private final JButton pause = new JButton("Pause");
    private final JButton resume = new JButton("Resume");
    private final JCheckBox showTime = new JCheckBox("Show the time");
    private final JCheckBox hideTime = new JCheckBox("Hide the time");
    private final JCheckBox showStatisticAsDialog = new JCheckBox("Statistic as dialog");
    private final JButton goldSettings = new JButton("Gold settings");
    private final JButton guppySettings = new JButton("Guppy settings");

    Menu(ViewContainer container) {
        this.container = container;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.BLACK);

        deactivate.setEnabled(false);
        pause.setEnabled(false);
        resume.setVisible(false);
        hideTime.setSelected(true);

        activate.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        deactivate.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        pause.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        resume.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        showTime.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        hideTime.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        showStatisticAsDialog.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        goldSettings.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        guppySettings.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

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
        top.add(pause);
        top.add(resume);
        add(top);

        middle.setBorder(border);
        middle.add(showTime);
        middle.add(hideTime);
        middle.add(new JSeparator(JSeparator.HORIZONTAL));
        middle.add(showStatisticAsDialog);
        add(middle);

        bot.setBorder(border);
        bot.add(goldSettings);
        bot.add(guppySettings);
        add(bot);

        configureListeners();
    }

    private void configureListeners() {
        activate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Menu.this.activate();
                container.activate(Menu.this);
            }
        });

        deactivate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Menu.this.deactivate();
                container.deactivate(Menu.this);
            }
        });

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Menu.this.pause();
                container.pause(Menu.this);
            }
        });

        resume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Menu.this.resume();
                container.resume(Menu.this);
            }
        });

        showTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Menu.this.showTime();
                container.showTime(Menu.this);
            }
        });

        hideTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Menu.this.hideTime();
                container.hideTime(Menu.this);
            }
        });

        showStatisticAsDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                container.changeStatisticView(Menu.this);
            }
        });

        goldSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                container.changeGoldSettings();
            }
        });

        guppySettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                container.changeGuppySettings();
            }
        });
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
