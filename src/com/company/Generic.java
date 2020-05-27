package com.company;

import java.util.*;

public class Generic<T> {
    private Node first;
    private Node last;
    private int size;

    public void add(T object) {
        if (this.first == null) {
            this.first = new Node(object);
            this.first.prev = this.first.next = this.first;
            this.last = this.first;
            this.size++;
        } else {
            Node obj = new Node(this.first, this.last, object);
            this.first.prev = this.last.next = obj;
            this.last = obj;
            this.size++;
        }
    }

    public void addCollection(Collection<? extends T> list){
        for(T rabbit:list){
          add(rabbit);
        }
    }

    public void addCollection(HashMap<Integer,? extends T> list){
        this.addCollection(list.values());
    }

    public static<T> List mix(List<? extends T> list){
        List<? extends T> vector= list;
        Random rd=new Random();
        Generic<AbstractRabbit>rabbitGeneric=new Generic<>();
        Collections.shuffle(vector);
        return vector;
    }

    public void clear(){
        this.first=null;
        this.last=null;
        this.size=0;
    }

    public Vector show()
    {
        Vector<T> list=new Vector<>();
        Node value=first;
        System.out.println(size);
        while (value!=null) {
            System.out.println("value "+value);
            value = value.next;
            list.add(value.object);
            System.out.println("list "+list.toString());
            if (last.next == value)
                break;
        }
        return list;
    }

    public T delete(){

        if(this.last==null)
        {
            System.out.println("List empty");
        }
        T oldValue = null;
        if (this.size==1){
            oldValue=this.first.object;
            clear();
        }else if(size>1){
            oldValue=this.last.object;
            Node obj=this.last.prev;
            this.first.prev=obj;
            this.last=obj;
            this.size--;
        }
        return oldValue;
    }

    public void removeAll()
    {
        Node value=first;
        System.out.println(size);
        while (value.next!=null) {
            delete();
            if (size==0)
                break;
        }
    }

    public class Node{
        private Node next;
        private Node prev;
        public T object;
        Node(Node next,Node prev,T obj)
        {
            this.next=next;
            this.prev=prev;
            object=obj;
        }

        Node(T object)
        {
            this(null,null,object);
        }
    }
}
