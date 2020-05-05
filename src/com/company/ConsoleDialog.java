package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class ConsoleDialog extends JDialog implements Runnable{
    PipedWriter pw;
    PipedReader pr;
    PipedWriter getStream(){return pw;}
    final JTextArea textArea = new JTextArea();
    void setPr(PipedWriter pw){
        try {
            pr=new PipedReader(pw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ConsoleDialog() {
        pw=new PipedWriter();
        pr=new PipedReader();
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                super.keyPressed(keyEvent);
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    String[] lines = textArea.getText().split("\n");
                    try {
                            pw.write(lines[lines.length - 1].length());
                            pw.write(lines[lines.length - 1]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        JPanel contents = new JPanel();
        contents.add(textArea);
        contents.setLayout(new GridLayout(1, 1));
        setContentPane(contents);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            int ch;
            String result = "";
            int len = 0;
            try {
                len = pr.read();
                for (int i = 0;i < len; i++) {
                    ch = pr.read();
                    result += (char)ch;
                }
                if (result != "")
                {
                        textArea.append(result);
                        textArea.append("\n");
                    textArea.setCaretPosition(textArea.getText().length());
                }
                else
                    textArea.setText(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
