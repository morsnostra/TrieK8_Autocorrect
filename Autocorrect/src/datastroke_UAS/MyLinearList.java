package datastroke_UAS;
//MyLinearList
class Node<T> {
   private T data;
   private Node<T> next;

   Node(T value) {
      data = value;
      next = null;
   }

   public void setData(T data) {
      this.data = data;
      next = null;
   }

   public void setNext(Node<T> next) {
      this.next = next;
   }

   public T getData() {
      return data;
   }

   public Node<T> getNext() {
      return next;
   }
}

public class MyLinearList<T> {
   Node<T> head;
   Node<T> tail;

   public MyLinearList() {
      head = null;
      tail = null;
   }

   public void pushQ(T value) {
      Node<T> newNode = new Node<T>(value);
      if (head == null) {
         head = newNode;
         tail = newNode;
      } else {
         tail.setNext(newNode);
         tail = newNode;
      }
   }

   public void pushS(T value) {
      Node<T> newNode = new Node<T>(value);
      if (head == null) {
         head = newNode;
         tail = newNode;
      } else {
         newNode.setNext(head);
         head = newNode;
      }
   }

   public void cetakList() {
      Node<T> curr = head;
      if (curr == null) System.out.println("List kosong!");
      else {
         System.out.print("[ ");
         while (curr != null) {
            System.out.print(curr.getData().toString() + " ");
            curr = curr.getNext();
         }
         System.out.println("]");
      }
   }

   private Node<T> pop() {
      Node<T> n;
      if (head == null) n = null;
      else {
         n = head;
         head = head.getNext();
         if (head == null) {
            tail = null;
         }
      }
      return n;
   }

   public T remove() {
      Node<T> n = pop();
      return (n == null) ? null : n.getData();
   }

   public boolean remove(T data) {
      Node<T> curr = head;
      Node<T> prev = head;
      boolean deleted = false;

      while (curr != null && !deleted) {
         if (curr.getData().equals(data)) {
            deleted = true;
            prev.setNext(curr.getNext());
            if (curr == head) head = head.getNext();
            if (head == null) tail = null;
         } else {
            prev = curr;
            curr = curr.getNext();
         }
      }
      return deleted;
   }

   public boolean isEmpty() {
      return head == null;
   }
}
