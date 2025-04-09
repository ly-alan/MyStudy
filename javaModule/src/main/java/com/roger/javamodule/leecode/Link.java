package com.roger.javamodule.leecode;

public class Link {

    private class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    Node head;


    public Link() {
        head = null;
    }

    public int get(int index) {
        if (head == null) {
            return -1;
        }
        Node temp = head;
        for (int i = 0; temp.next != null; i++) {
            if (i < index) {
                temp = temp.next;
                continue;
            }
            if (i == index) {
                return temp.value;
            }

        }
        return -1;
    }

    public void addAtHead(int val) {
        Node node = new Node(val);
        node.next = head;
        head = node;
    }

    public void addAtTail(int val) {
        Node tail = new Node(val);
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = tail;
    }

    public void addAtIndex(int index, int val) {

    }

    public void deleteAtIndex(int index) {

    }
}
