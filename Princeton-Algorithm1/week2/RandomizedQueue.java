import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private Item[] a;         // array of items
    private int N;            // number of elements on stack
	
   public RandomizedQueue()                 // construct an empty randomized queue
   {
	   a = (Item[]) new Object[2];
   }
   
   public boolean isEmpty()                 // is the queue empty?
   {
	   return N == 0;
   }
   
   public int size()                        // return the number of items on the queue
   {
	   return N;
   }
   
   // resize the underlying array
   private void resize(int capacity) {
	   assert capacity >= N;
       Item[] temp = (Item[]) new Object[capacity];
       for (int i = 0; i < N; i++) {
           temp[i] = a[i];
       }
       a = temp;
   }
   
   public void enqueue(Item item)           // add the item
   {
	   if(item == null) throw new java.lang.NullPointerException();
	   if (N == a.length) resize(2*a.length);    // double size of array if necessary
       a[N++] = item;                            // add item
   }
   
   public Item dequeue()                    // remove and return a random item
   {
	   if (isEmpty()) throw new NoSuchElementException();
	   // choose index uniformly in [0, N-1]
       int r =  (int) (Math.random() * N);
       Item tmp = a[r];
       a[r] = a[N-1];
       a[N-1] = tmp;
       a[N-1] = null;                              // to avoid loitering
       N--;
       // shrink size of array if necessary
       if (N > 0 && N == a.length/4) resize(a.length/2);
       return tmp;
   }
   
   public Item sample()                     // return (but do not remove) a random item
   {
	   if (isEmpty()) throw new NoSuchElementException();
	   // choose index uniformly in [0, N-1]
       int r =  (int) (Math.random() * N);              
       return a[r];
   }
   
   public Iterator<Item> iterator()         // return an independent iterator over items in random order
   {
	   return new ReverseArrayIterator();
   }
   
// an iterator, doesn't implement remove() since it's optional
   private class ReverseArrayIterator implements Iterator<Item> {
       private int i;
       private Item[] dest;

       public ReverseArrayIterator() {
           i = N-1;
           dest = a.clone();
           for (int i = 0; i < N; i++) {
               // choose index uniformly in [i, N-1]
               int r = i + (int) (Math.random() * (N - i));
               Item swap = dest[r];
               dest[r] = dest[i];
               dest[i] = swap;
           }
       }

       public boolean hasNext() {
           return i >= 0;
       }

       public void remove() {
           throw new UnsupportedOperationException();
       }

       public Item next() {
           if (!hasNext()) throw new NoSuchElementException();
           return dest[i--];
       }
   }
   
   public static void main(String[] args)   // unit testing
   {
	   RandomizedQueue<String> rq = new RandomizedQueue<String>();
	   rq.enqueue("1");
	   rq.enqueue("2");
	   rq.enqueue("3");
	   rq.enqueue("4");
	   rq.enqueue("5");
	   System.out.println(rq.dequeue());
	   System.out.println(rq.dequeue());
	   System.out.println(rq.dequeue());
	   System.out.println(rq.dequeue());
	   System.out.println(rq.dequeue());
	   
   }
}