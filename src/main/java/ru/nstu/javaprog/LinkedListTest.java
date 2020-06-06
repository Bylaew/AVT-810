package ru.nstu.javaprog;

import ru.nstu.javaprog.util.LinkedList;

import java.util.Arrays;

public class LinkedListTest {
    public void test() {
        LinkedList<Integer> integerLinkedList = new LinkedList<>();
        integerLinkedList.add(1);
        integerLinkedList.add(2);
        integerLinkedList.addAll(Arrays.asList(1, 2, 3));
        System.out.println("After adding");
        for (int i = 0; i < integerLinkedList.getSize(); i++) {
            System.out.println(integerLinkedList.get(i));
        }
        System.out.println("Removed: " + integerLinkedList.remove());
        System.out.println("Removed: " + integerLinkedList.remove());
        System.out.println();
        System.out.println();
        System.out.println("After removing");
        for (int i = 0; i < integerLinkedList.getSize(); i++) {
            System.out.println(integerLinkedList.get(i));
        }

        // noOdd - список Number, integerLinkedList - список Integer, и все работает
        // за счет использования конструкции ? extends
        LinkedList<Number> noOdd = LinkedList.removeOdd(integerLinkedList);
        System.out.println();
        System.out.println();
        System.out.println("Even elements only");
        for (int i = 0; i < noOdd.getSize(); i++) {
            System.out.println(noOdd.get(i));
        }

        LinkedList<String> stringLinkedList = new LinkedList<>();

        // здесь будет ошибка компиляции, поскольку метод removeOdd здесь ждет String или какой-либо производый от String класс
        //LinkedList<Integer> anotherIntegerLinkedList = LinkedList.removeOdd(stringLinkedList);
    }
}
