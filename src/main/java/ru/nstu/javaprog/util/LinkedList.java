package ru.nstu.javaprog.util;

import java.util.Collection;

public final class LinkedList<T> {
    private int size;
    private Node head;

    public static <T> LinkedList<T> removeOdd(LinkedList<? extends T> origin) {
        LinkedList<T> withoutOdd = new LinkedList<>();
        for (int i = 0; i < origin.getSize(); ++i) {
            if (i % 2 == 0) {
                withoutOdd.add(origin.get(i));
            }
        }
        return withoutOdd;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(size == 0 ? "EMPTY" : "");
        for (int i = 0; i < size; ++i) {
            builder.append("[").append(get(i).toString()).append("];");
        }
        return builder.toString();
    }

    public void add(T data) {
        add(data, size);
    }

    public void add(T data, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            Node oldHead = head;
            head = new Node(data);
            head.next = oldHead;
        } else {
            Node beforeAdded = head;
            for (int i = 1; i < index; ++i) {
                beforeAdded = beforeAdded.next;
            }
            Node oldAtIndex = beforeAdded.next;
            beforeAdded.next = new Node(data);
            beforeAdded.next.next = oldAtIndex;
        }
        ++size;
    }

    public T remove() {
        return remove(size - 1);
    }

    public int getSize() {
        return size;
    }

    public void addAll(Collection<? extends T> collection) {
        collection.forEach(this::add);
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node required = head;
        for (int i = 0; i < index; ++i) {
            required = required.next;
        }
        return required.data;
    }

    private T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        T removed;
        if (index == 0) {
            removed = head.data;
            head = head.next;
        } else {
            Node beforeAdded = head;
            for (int i = 1; i < index; ++i) {
                beforeAdded = beforeAdded.next;
            }
            removed = beforeAdded.next.data;
            beforeAdded.next = beforeAdded.next.next;
        }
        --size;
        return removed;
    }

    private final class Node {
        private T data;
        private Node next;

        Node(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
