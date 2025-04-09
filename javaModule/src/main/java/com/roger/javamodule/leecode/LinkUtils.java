package com.roger.javamodule.leecode;

public class LinkUtils {
    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        ListNode headNode = null, cur = null;
        int carry = 0;//进位
        while (l1 != null || l2 != null) {
            ListNode temp = new ListNode();
            int sum1 = l1 != null ? l1.val : 0;
            int sum2 = l2 != null ? l2.val : 0;
            int value = sum1 + sum2 + carry;
            if (value >= 10) {
                temp.val = value % 10;
                carry = 1;
            } else {
                carry = 0;
                temp.val = value;
            }
            if (headNode == null) {
                headNode = cur = new ListNode(value % 10);
            } else {
                cur.next = new ListNode(value % 10);
                cur = cur.next;
            }
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            cur.next = new ListNode(carry);
        }
        return headNode;
    }
}
