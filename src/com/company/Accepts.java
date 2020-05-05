package com.company;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Accepts implements Runnable {
    private PipedReader pr;
    PipedWriter pw;
    Habitat h;
    Accepts(Habitat habitat){
        h=habitat;
        pw=new PipedWriter();
    }

    PipedWriter getStream(){return pw;}

    void setPr(PipedWriter pw){
        try {
            pr=new PipedReader(pw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            int ch;
            String com = "";

            try {
                int len = pr.read();
                for (int i = 0;i < len; i++) {
                    ch = pr.read();
                    com += (char)ch;
                }
                String result=h.Execute(com);
                pw.write(result.length());
                pw.write(result);
            } catch (IOException e) {
                System.out.println("finished");
            }
        }
    }
}
