package ru.nstu.javaprog.view;

import ru.nstu.javaprog.api.FishType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class RepositoryChooser extends JDialog {
    static final String FILE = "File";
    static final String DATABASE = "Database";

    private static final String[] REPOSITORY_VALUES = {
            FILE,
            DATABASE
    };

    private boolean isClosed = false;
    private final JRadioButton gold = new JRadioButton();
    private final JRadioButton guppy = new JRadioButton();
    private final JComboBox<String> repository = new JComboBox<>(REPOSITORY_VALUES);
    private final JButton accept = new JButton("Accept");

    RepositoryChooser(Component parent) {
        super((Frame) null, "Choose repository", true);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new GridLayout(5, 2, 2, 1));
        setSize(new Dimension(300, 180));

        JRadioButton both = new JRadioButton();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(gold);
        buttonGroup.add(guppy);
        buttonGroup.add(both);
        both.setSelected(true);
        add(new JLabel("Repository name"));
        add(repository);
        add(new JLabel("Golds"));
        add(gold);
        add(new JLabel("Guppies"));
        add(guppy);
        add(new JLabel("Both"));
        add(both);
        add(new JLabel());
        add(accept);

        configureListeners();
    }

    void activate() {
        setVisible(true);
    }

    boolean isClosed() {
        return isClosed;
    }

    FishType getFishType() {
        return gold.isSelected()
                ? FishType.GOLD
                : guppy.isSelected()
                    ? FishType.GUPPY
                    : FishType.GENERAL;
    }

    String getRepositoryName() {
        return (String) repository.getSelectedItem();
    }

    private void configureListeners() {
        accept.addActionListener(event -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                isClosed = true;
            }
        });
    }
}
