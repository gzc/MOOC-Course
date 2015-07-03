import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
	
	private int N;         // number of elements on queue
    private Node first;    // beginning of queue
    private Node last;     // end of queue
	
	// helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
	
   public Deque()                           // construct an empty deque
   {
	   first = null;
       last  = null;
       N = 0;
   }
   
   public boolean isEmpty()                 // is the deque empty?
   {
	   return N == 0;
   }
   
   public int size()                        // return the number of items on the deque
   {
	   return N;
   }
   
   public void addFirst(Item item)          // add the item to the front
   {
	   if(item == null) throw new java.lang.NullPointerException();
	   if(N == 0)
	   {
		   first = new Node();
		   first.item = item;
		   first.previous = null;
		   first.next = null;
		   last = first;
	   } else {
		   Node oldfirst = first;
		   first = new Node();
		   first.item = item;
		   first.next = oldfirst;
		   first.previous = null;
		   oldfirst.previous = first;
	   }
	   N++;
   }
   
   public void addLast(Item item)           // add the item to the end
   {
	   if(item == null) throw new java.lang.NullPointerException();
	   if(N == 0)
	   {
		   first = new Node();
		   first.item = item;
		   first.previous = null;
		   first.next = null;
		   last = first;
	   } else {
		   Node oldlast = last;
		   last = new Node();
		   last.item = item;
		   last.previous = oldlast;
		   last.next = null;
		   oldlast.next = last;
	   }
	   N++;
   }
   
   public Item removeFirst()                // remove and return the item from the front
   {
	   if(isEmpty()) throw new java.util.NoSuchElementException();
	   Item v = first.item;
	   
	   first = first.next;
	   N--;
	   if (isEmpty()) first = null;
	   else first.previous = null;
	   return v;
   }
   
   public Item removeLast()                 // remove and return the item from the end
   {
	   if(isEmpty()) throw new java.util.NoSuchElementException();
	   Item v = last.item;
	   
	   last = last.previous;
	   N--;
	   if (isEmpty()) last = null;
	   else last.next = null;
	   return v;
   }
   
   public Iterator<Item> iterator()         // return an iterator over items in order from front to end
   {
	   return new ListIterator();
   }
   
   private class ListIterator implements Iterator<Item> {
       private Node current = first;

       public boolean hasNext()  { return current != null; }
       public void remove()      { throw new UnsupportedOperationException();  }

       public Item next() {
           if (!hasNext()) throw new NoSuchElementException();
           Item item = current.item;
           current = current.next; 
           return item;
       }
   }
   
   public static void main(String[] args)   // unit testing
   {
	   Deque<Integer> deque = new Deque<Integer>();
	   deque.addFirst(0);
       deque.removeFirst();
   }
}