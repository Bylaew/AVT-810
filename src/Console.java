import javax.swing.*;
import java.awt.*;

public class Console extends JFrame {
    TextArea text = new TextArea();

    Console(Habitat h){
        text.setBackground(Color.BLACK);
        text.setForeground(Color.GREEN);


        text.addTextListener(textEvent -> {
            if (text.getText().endsWith("\n")) {

                String[] array =  text.getText().split("\n");

                String[] command = array[array.length-1].split(" ");

                for (String lex : command) System.out.println(lex);

                int percent;
                try{
                if (command[0].equals("reduce") & command[1].equals("moto")){
                    System.out.println(command[2]);
                    percent = Integer.parseInt(command[2]);
                    h.reduceMoto(percent);
                }}
                catch (Exception ignored) {

                }
            }
        });

        add(text);
        setSize(new Dimension(600, 400));
        text.setPreferredSize(new Dimension(400, 300));
        setVisible(true);
    }
}
