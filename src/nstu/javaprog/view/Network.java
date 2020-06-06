package nstu.javaprog.view;

import javafx.util.Pair;
import nstu.javaprog.net.client.ConnectionHandler;
import nstu.javaprog.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public final class Network extends JDialog {
    private final ViewContainer container;
    private final DefaultListModel<Long> membersModel = new DefaultListModel<>();
    private final JList<Long> members = new JList<>(membersModel);
    private ConnectionHandler connectionHandler;

    Network(ViewContainer container) {
        super(container, false);
        this.container = container;

        setLocationRelativeTo(container);
        setBounds(600, 200, 150, 130);
        setResizable(false);
        setLayout(new BorderLayout());

        members.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        members.setSelectedIndex(0);
        add(
                new JScrollPane(
                        members,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                ),
                BorderLayout.CENTER
        );

        configureListeners();

        pack();
    }

    private void configureListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deactivate();
            }
        });

        members.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2)
                    connectionHandler.getProperties(members.getSelectedValue());
            }
        });
    }

    void activate() {
        new ConnectionSettings(container, "Connection").activate();
    }

    void deactivate() {
        container.deactivateNetwork();
        if (connectionHandler != null)
            new Thread(() -> connectionHandler.interrupt()).start();
        dispose();
    }

    public void showConnections() {
        SwingUtilities.invokeLater(() -> {
            setTitle("Network, " + connectionHandler.toString());
            setVisible(true);
            revalidate();
        });
    }

    public void crash(String reason) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    this,
                    reason,
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE
            );
            deactivate();
        });
    }

    public void updateMembersList(List<Long> list) {
        SwingUtilities.invokeLater(() -> {
            membersModel.clear();
            list.forEach(memberSessionId -> {
                if (memberSessionId != connectionHandler.getSessionId())
                    membersModel.addElement(memberSessionId);
            });
            revalidate();
        });
    }

    public void showRequestedProperties(long whose, Pair<Properties, Properties> properties) {
        SwingUtilities.invokeLater(() -> {
            container.commitAndDeactivate();
            new EnvironmentSettings(
                    container,
                    "Gold properties from client " + whose,
                    properties.getKey(),
                    container::setGoldProperties
            ).activate();
            new EnvironmentSettings(
                    container,
                    "Guppy properties from client " + whose,
                    properties.getValue(),
                    container::setGuppyProperties
            ).activate();
            container.rollback();
        });
    }

    public Properties getGoldProperties() {
        return container.getGoldProperties();
    }

    public Properties getGuppyProperties() {
        return container.getGuppyProperties();
    }

    private class ConnectionSettings extends JDialog {
        final JTextField ip = new JTextField(10);
        final JTextField port = new JTextField(10);
        final JButton accept = new JButton("Connect");

        ConnectionSettings(Component parent, String title) {
            super((Frame) null, title, true);
            setLocationRelativeTo(parent);
            setResizable(false);
            setLayout(new GridLayout(3, 2, 2, 1));
            setSize(new Dimension(250, 150));

            add(new JLabel("IP"));
            add(ip);
            add(new JLabel("Port"));
            add(port);
            add(accept);

            configureListeners();
        }

        void configureListeners() {
            accept.addActionListener(event ->
                    new Thread(() -> {
                        try {
                            connectionHandler = new ConnectionHandler(
                                    ip.getText(),
                                    Integer.parseInt(port.getText()),
                                    Network.this
                            );
                            connectionHandler.start();
                            dispose();
                        } catch (IOException exception) {
                            crash("Server is not available");
                        } catch (NumberFormatException exception) {
                            crash("Invalid port");
                        }
                    }).start()
            );

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dispose();
                }
            });
        }

        void activate() {
            setVisible(true);
        }
    }
}
