package ru.nstu.javaprog.view;

import ru.nstu.javaprog.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class Console extends JDialog {
    private final JTextArea history = new JTextArea(10, 50);
    private final JTextField command = new JTextField(40);
    private final JButton send = new JButton("Send");
    private final ConsoleService consoleService;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final ViewContainer container;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    Console(ViewContainer container) throws IOException {
        super((Frame) null, "Console", false);
        this.container = container;

        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new PrintWriter(outputStream, true);
        consoleService = new ConsoleService(inputStream, outputStream);

        setLocationRelativeTo(container);
        setBounds(600, 200, 150, 130);
        setResizable(false);

        history.setEditable(false);

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(command);
        panel.add(send);

        add(
                new JScrollPane(
                        history,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                ),
                BorderLayout.CENTER
        );
        add(panel, BorderLayout.SOUTH);

        configureListeners();
        pack();

        command.requestFocus();
    }

    private void configureListeners() {
        send.addActionListener(event -> {
            writer.println(command.getText());
            executorService.submit(() -> {
                try {
                    String reply = reader.readLine();
                    SwingUtilities.invokeLater(() -> {
                        history.append("> " + reply + '\n');
                        command.setText("");
                    });
                } catch (IOException exception) {
                    System.err.println("Console send request error: " + exception.getMessage());
                    SwingUtilities.invokeLater(() ->
                            history.append("> " + "Console send request error: " + exception.getMessage() + '\n')
                    );
                }

            });
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                changeVisibleState();
            }
        });
    }

    void activate() {
        consoleService.start();
        setVisible(true);
    }

    void changeVisibleState() {
        setVisible(!isVisible());
    }

    void deactivate() {
        consoleService.interrupt();
        try {
            reader.close();
            writer.close();
        } catch (IOException exception) {
            System.err.println("Couldn't to close console pipes");
        }
        dispose();
    }

    private final class ConsoleService {
        private final BufferedReader reader;
        private final PrintWriter writer;
        private final Thread thread;

        ConsoleService(PipedInputStream to, PipedOutputStream from)
                throws IOException {

            reader = new BufferedReader(new InputStreamReader(new PipedInputStream(from)));
            writer = new PrintWriter(new PipedOutputStream(to), true);
            thread = new Thread(new ParsingTask());
        }

        void start() {
            thread.start();
        }

        void interrupt() {
            thread.interrupt();
            try {
                reader.close();
                writer.close();
            } catch (IOException exception) {
                System.err.println("Couldn't to close console service pipes");
            }
        }

        private class ParsingTask implements Runnable {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        writer.println(parse(reader.readLine()));
                    } catch (IOException exception) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            String parse(String request) {
                String reply;
                String[] split = request.split(" ");
                try {
                    if (split.length == 0 || split.length > 2 || !split[0].equals("gprob")) {
                        throw new IllegalArgumentException(
                                "Invalid command " + request + ". Expected gprob [value]"
                        );
                    } else {
                        if (split.length == 2) {
                            try {
                                Properties old = container.getGoldProperties();
                                container.setGoldProperties(
                                        Properties.buildPropertiesWithCheckedException(
                                                Float.parseFloat(split[1]),
                                                old.getDelay(),
                                                old.getMinSpeed(),
                                                old.getMaxSpeed(),
                                                old.getLifetime(),
                                                old.getPriority()
                                        )
                                );
                            } catch (NumberFormatException exception) {
                                throw new IllegalArgumentException(
                                        "2nd parameter must be a numeric value"
                                );
                            }
                            reply = "New goldfish spawn probability was set";
                        } else
                            reply = "Goldfish spawn probability = " + container.getGoldProperties().getChance();
                    }
                } catch (Exception exception) {
                    reply = exception.getMessage();
                }
                return reply;
            }
        }
    }
}
