package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
	private Item[] a;
	private int N;
		
	public RandomizedQueue()
	{
		a = (Item[]) new Object[1];
		N = 0;
	}
	
	public boolean isEmpty()
	{
		return N == 0;
	}
	
	public int size()
	{
		return N;
	}
	
    private void resize(int capacity) 
    {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) 
        {
            temp[i] = a[i];
        }
        a = temp;
    }
	
	public void enqueue(Item item)
	{
        if (item == null) throw new NullPointerException("Tried to add null item");
		
		if (N == a.length) resize(2 * a.length);    // double size of array if necessary
        a[N++] = item; 
	}
	
	public Item dequeue()
	{
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        int randInt = StdRandom.uniform(N);
        
        Item item = a[randInt];
        a[randInt] = a[N-1];
        a[N-1] = null;
        N--;
        
        // shrink size of array if necessary
        if (N > 0 && N == a.length/4) resize(a.length/2);
        return item;
	}
	
	public Item sample()
	{
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		
		int randInt = StdRandom.uniform(N);
		return a[randInt];
	}
	
    public Iterator<Item> iterator() 
    {
        return new ReverseArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ReverseArrayIterator implements Iterator<Item> 
    {
        private int i;
        private int[] order;

        public ReverseArrayIterator() 
        {
            i = N-1;
            order = new int[N];
            for (int j = 0; j < N; j++)
            {
            	order[j] = j;
            }
            StdRandom.shuffle(order);
        }

        public boolean hasNext() 
        {
            return i >= 0;
        }

        public void remove() 
        {
            throw new UnsupportedOperationException();
        }

        public Item next() 
        {
            if (!hasNext()) throw new NoSuchElementException();
            
            return a[order[i--]];
        }
    }
	
	public static void main(String[] args) 
	{
		RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
		
		rq.enqueue(1);
		rq.enqueue(2);
		rq.enqueue(3);
		rq.enqueue(4);
		rq.enqueue(5);
		rq.enqueue(6);
		
		Iterator<Integer> it = rq.iterator();
		while (it.hasNext()) System.out.println(it.next());
			

	}

}
