package ru.nstu.javaprog.view;

import ru.nstu.javaprog.exception.IllegalFormatException;
import ru.nstu.javaprog.exception.IllegalPropertiesException;
import ru.nstu.javaprog.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

final class EnvironmentSettings extends JDialog {
    private static final String[] CHANCE_VALUES = {
            "0%",
            "10%",
            "20%",
            "30%",
            "40%",
            "50%",
            "60%",
            "70%",
            "80%",
            "90%",
            "100%"
    };

    private static final String[] PRIORITY_VALUES = {
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10"
    };

    private final JTextField delay = new JTextField(10);
    private final JTextField minSpeed = new JTextField(10);
    private final JTextField maxSpeed = new JTextField(10);
    private final JTextField lifetime = new JTextField(10);
    private final JComboBox<String> chance = new JComboBox<>(CHANCE_VALUES);
    private final JComboBox<String> priority = new JComboBox<>(PRIORITY_VALUES);
    private final JButton accept = new JButton("Accept");

    EnvironmentSettings(Component parent, String title, Properties properties, Consumer<Properties> setter) {
        super((Frame) null, title, true);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new GridLayout(7, 2, 2, 1));
        setSize(new Dimension(300, 240));

        add(new JLabel("Delay"));
        add(delay);
        add(new JLabel("Chance"));
        add(chance);
        add(new JLabel("Minimal speed"));
        add(minSpeed);
        add(new JLabel("Maximal speed"));
        add(maxSpeed);
        add(new JLabel("Lifetime"));
        add(lifetime);
        add(new JLabel("Priority"));
        add(priority);
        add(new JLabel());
        add(accept);

        setPropertiesToForm(properties);
        configureListeners(setter);
    }

    @Override
    public String toString() {
        return getTitle();
    }

    void activate() {
        setVisible(true);
    }

    private void configureListeners(Consumer<Properties> setter) {
        accept.addActionListener(event -> {
            try {
                validateData();
                setter.accept(getPropertiesFromForm());
                dispose();
            } catch (IllegalPropertiesException | IllegalFormatException exception) {
                JOptionPane.showMessageDialog(
                        this,
                        exception.getMessage(),
                        "Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void validateData() throws IllegalFormatException {
        if (!delay.getText().matches("\\d{1,2}"))
            throw new IllegalFormatException(
                    "Invalid delay format\n" +
                            "Valid range from 1 to 99"
            );

        if (!minSpeed.getText().matches("\\d"))
            throw new IllegalFormatException(
                    "Invalid minimal speed format\n" +
                            "Valid range from 1 to 9"
            );

        if (!maxSpeed.getText().matches("\\d"))
            throw new IllegalFormatException(
                    "Invalid maximal speed format\n" +
                            "Valid range from 1 to 9"
            );

        if (!maxSpeed.getText().matches("\\d{1,2}"))
            throw new IllegalFormatException(
                    "Invalid lifetime format\n" +
                            "Valid range from 1 to 99"
            );
    }

    private Properties getPropertiesFromForm() throws IllegalPropertiesException {
        return Properties.buildPropertiesWithCheckedException(
                (float) chance.getSelectedIndex() / 10.f,
                Integer.parseInt(delay.getText()),
                Integer.parseInt(minSpeed.getText()),
                Integer.parseInt(maxSpeed.getText()),
                Integer.parseInt(lifetime.getText()),
                priority.getSelectedIndex() + 1
        );
    }

    private void setPropertiesToForm(Properties properties) {
        delay.setText(Integer.toString(properties.getDelay()));
        chance.setSelectedIndex(Math.round(properties.getChance() * 10.f));
        priority.setSelectedIndex(properties.getPriority() - 1);
        minSpeed.setText(Integer.toString(properties.getMinSpeed()));
        maxSpeed.setText(Integer.toString(properties.getMaxSpeed()));
        lifetime.setText(Integer.toString(properties.getLifetime()));
    }
}
