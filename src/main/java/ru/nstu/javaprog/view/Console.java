package ru.nstu.javaprog.view;

import ru.nstu.javaprog.api.FishType;
import ru.nstu.javaprog.exception.IllegalPropertiesException;
import ru.nstu.javaprog.util.LinkedList;
import ru.nstu.javaprog.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.ParseException;
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
        executorService.shutdown();
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
                String reply = "";
                String[] split = request.split(" ");
                try {
                    if (split.length < 1) {
                        throw new ParseException("Invalid command " + request, 0);
                    }
                    switch (split[0]) {
                        case "getGoldProb":
                            reply = "Goldfish spawn probability = " + container.getGoldProperties().getChance();
                            break;
                        case "setGoldProb":
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
                                throw new ParseException("gprob error: 2nd parameter must be a numeric value", 0);
                            } catch (ArrayIndexOutOfBoundsException exception) {
                                throw new ParseException("gprob error: 2nd parameter was not found", 0);
                            }
                            reply = "New goldfish spawn probability was set";
                            break;
                        case "listAdd":
                            try {
                                container.addFishToLinkedList(FishType.getTypeByName(split[1]), Long.parseLong(split[2]));
                            } catch (NumberFormatException exception) {
                                throw new ParseException("list add error: 3rd parameter must be a numeric value", 0);
                            } catch (ArrayIndexOutOfBoundsException exception) {
                                throw new ParseException("list add error: fish type or id was not found", 0);
                            } catch (IllegalArgumentException exception) {
                                throw new ParseException("list add error: unknown fish type (must be GOLD or GUPPY)", 0);
                            }
                            reply = "Added";
                            break;
                        case "listGet":
                            reply = container.getLinkedList().toString();
                            break;
                        case "listRemoveOdd":
                            reply = LinkedList.removeOdd(container.getLinkedList()).toString();
                            break;
                    }
                } catch (ParseException | IllegalPropertiesException exception) {
                    reply = exception.getMessage();
                }
                return reply;
            }
        }
    }
}
