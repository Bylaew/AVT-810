package nstu.javaprog.view;

import nstu.javaprog.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

    private final TextField delay = new TextField(10);
    private final TextField minSpeed = new TextField(10);
    private final TextField maxSpeed = new TextField(10);
    private final JComboBox<String> chance = new JComboBox<>(CHANCE_VALUES);
    private final JButton accept = new JButton("Accept");

    EnvironmentSettings(ViewContainer container, Properties properties) {
        super(container, "Settings", true);
        setLocationRelativeTo(container);
        setBackground(Color.WHITE);
        setResizable(false);
        setLayout(new GridLayout(5, 2, 2, 1));

        add(new JLabel("Delay"));
        add(delay);
        add(new JLabel("Chance"));
        add(chance);
        add(new JLabel("Minimal speed"));
        add(minSpeed);
        add(new JLabel("Maximal speed"));
        add(maxSpeed);
        add(accept);

        setProperties(properties);
        configureListeners();
        pack();
    }

    private void configureListeners() {
        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    EnvironmentSettings.this.validateData();
                    EnvironmentSettings.this.dispose();
                } catch (IllegalArgumentException exception) {
                    JOptionPane.showMessageDialog(
                            EnvironmentSettings.this,
                            exception.getMessage(),
                            "Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void validateData() {
        if (!delay.getText().matches("\\d{1,5}"))
            throw new IllegalArgumentException(
                    "Invalid delay format\n" +
                            "Valid range from 1 to 99999"
            );
        if (!minSpeed.getText().matches("\\d"))
            throw new IllegalArgumentException(
                    "Invalid minimal speed format\n" +
                            "Valid range from 1 to 9"
            );
        if (!maxSpeed.getText().matches("\\d"))
            throw new IllegalArgumentException(
                    "Invalid maximal speed format\n" +
                            "Valid range from 1 to 9"
            );
    }

    Properties getProperties() {
        return new Properties(
                (float) chance.getSelectedIndex() / 10.f,
                Integer.parseInt(delay.getText()),
                Integer.parseInt(minSpeed.getText()),
                Integer.parseInt(maxSpeed.getText())
        );
    }

    private void setProperties(Properties properties) {
        delay.setText(Integer.toString(properties.getDelay()));
        chance.setSelectedIndex((int) (properties.getChance() * 10.f) % chance.getItemCount());
        minSpeed.setText(Integer.toString(properties.getMinSpeed()));
        maxSpeed.setText(Integer.toString(properties.getMaxSpeed()));
    }
}
