package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.NullPointerException;

public class Deque<Item> implements Iterable<Item> 
{
	private int N;
	private Node first;
	private Node last;
	
    private class Node 
    {
        private Item item;
        private Node oneToBack;
        private Node oneToFront;
    }
	
	public Deque()
	{
		first = null;
		last  = null;
		N = 0;
	}
	
	private void checkNotNull(Item item)
	{
		if (item == null) throw new NullPointerException("Item reference is null.");
	}
	
	public boolean isEmpty()
	{
		return N == 0;
	}
	
	public int size()
	{
		return N;
	}
	

	
//    private void push(Item item) 
//    {
//        Node oldfirst = first;
//        first = new Node();
//        first.item = item;
//        first.next = oldfirst;
//        N++;
//        assert check();
//    }
	
	public void addFirst(Item item)
	{
		checkNotNull(item);
		
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.oneToBack = oldFirst;
		first.oneToFront = null;
		if (isEmpty()) last = first;
		else  		   oldFirst.oneToFront = first;
		N++;		
	}
//  private void enqueue(Item item) {
//  Node oldlast = last;
//  last = new Node();
//  last.item = item;
//  last.next = null;
//  if (isEmpty()) first = last;
//  else           oldlast.next = last;
//  N++;
//  assert check();
//}
	public void addLast(Item item)
	{
		checkNotNull(item);
		
		Node oldLast = last;
		last = new Node();
		last.item = item;
		last.oneToBack = null;
		last.oneToFront = oldLast;
		if (isEmpty()) first = last;
		else		   oldLast.oneToBack = last;
		N++;
	}
	
//    public Item pop() {
//        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
//        Item item = first.item;        // save item to return
//        first = first.next;            // delete first node
//        N--;
//        assert check();
//        return item;                   // return the saved item
//    }
	
	public Item removeFirst()
	{
		if (isEmpty()) throw new NoSuchElementException("The Deque is empty");
		
		Item item = first.item;
		first = first.oneToBack;
		if (first != null) first.oneToFront = null;
		N--;
		if (isEmpty()) last = first;
		
		return item;
	}
//    public Item dequeue() {
//        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
//        Item item = first.item;
//        first = first.next;
//        N--;
//        if (isEmpty()) last = null;   // to avoid loitering
//        assert check();
//        return item;
//    }
	public Item removeLast()
	{
		if (isEmpty()) throw new NoSuchElementException("The Deque is empty");
		
		Item item = last.item;
		last = last.oneToFront;
		if (last != null) last.oneToBack = null;
		N--;
		if (isEmpty()) first = last;
		
		return item;	
	}
	
    public Iterator<Item> iterator()  
    {
        return new ListIterator();  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> 
    {
        private Node current = first;

        public boolean hasNext()  
        { 
        	return current != null;
        }
        public void remove()      
        { 
        	throw new UnsupportedOperationException();  
        }

        public Item next() 
        {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            if (current.oneToBack != null) current = current.oneToBack; 
            else current = null;
            return item;
        }
    }
	   
	public static void main(String[] args) 
	{		

		
		
	}

}
