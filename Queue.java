import java.util.Iterator;

public class Queue<T extends Miner> implements Iterable<T> {
    private Node<T> first;    // beginning of queue
    private Node<T> last;     // end of queue
    private int N;               // number of elements on queue
	
    private static class Node<T> {
        private T item;
        private Node<T> next;
    }

    /**
     * Initializes an empty queue.
     */
    public Queue() {
        first = null;
        last  = null;
        N = 0;
    }

    /**
     * Returns true if this queue is empty.
     *
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Returns the number of items in this queue.
     *
     * @return the number of items in this queue
     */
    public int size() {
        return N;     
    }

    /**
     * Returns the item least recently added to this queue.
     *
     * @return the item least recently added to this queue, or null if the queue is empty
     */
    public T peek() {
        if (isEmpty()) return null;
        return first.item;
    }

    /**
     * Adds the item to this queue.
     *
     * @param item the item to add
     */
    public void enqueue(T item) {
        Node<T> oldlast = last;
        last = new Node<>();
        last.item = item;
        last.next = null;
        if (isEmpty()) first = last;
        else           oldlast.next = last;
        N++;
    }

    /**
     * Removes and returns the item on this queue that was least recently added. Only removes if that item is connected to 5 or more other items, otherwise it gets placed at the end of the queue.
     *
     * @return the item on this queue that was least recently added or null if the queue is empty
     */
    public T dequeue() {
        if (isEmpty()) return null;
        T item = first.item;
        if(item.getConnectionCount() < 5) {
            first = first.next;
            this.enqueue(item);
        }
        else {
            first = first.next;
            N--;
        }
        if (isEmpty()) last = null;   // to avoid loitering
        return item;
    }

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    @Override
    public Iterator<T> iterator()  {
        return new ListIterator<>(first);  
    }
    private class ListIterator<T> implements Iterator<T> {
        private Node<T> current;

        public ListIterator(Node<T> first) {
            current = first;
        }

        @Override
        public boolean hasNext()  { return current != null;                     }
        @Override
        public void remove()      { throw new UnsupportedOperationException();  }

        @Override
        public T next() {
            if (!hasNext()) return null;
            T item = current.item;
            current = current.next; 
            return item;
        }
    }
}