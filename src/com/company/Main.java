/*Вариант 10
        Картотека налоговой инспекции состоит из записей двух типов: физические и юридические лица.
        Физические лица генерируются каждые N1 секунд с вероятностью P1.
        Юридические лица генерируются каждые N2 секунд с вероятностью P2.*/

package com.company;
import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        Habitat habitat = new Habitat();
        habitat.init(4,3,0.5f,1);
    }
}
