package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

 /*
 * @Author Jay Patel
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 * 
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
   /*
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
   /*
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
   */
  public Node head;
  
  /**
   * Dummy node for tail.
   */
  private Node tail;
  
  /**
   * Number of elements in the list.
   */
  private int size;
  
  /**
   * Constructs an empty list with the default node size.
   */
  public StoutList()
  {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize)
  {
    if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
   * @param head
   * @param tail
   * @param nodeSize
   * @param size
   */
  public StoutList(Node head, Node tail, int nodeSize, int size)
  {
	  this.head = head; 
	  this.tail = tail; 
	  this.nodeSize = nodeSize; 
	  this.size = size; 
  }

  @Override
  public int size()
  {
    // TODO Auto-generated method stub
    return size;
  }
  
  @Override
  public boolean add(E item)
  {
	  if (item == null) {
			throw new NullPointerException("Item is null.");
		}
		
		if(contains(item)==true) {
			return false;
		}
		if (size == 0) { // case where there are no nodes, creates a new Node
			Node n = new Node();
			n.addItem(item);
			head.next = n;
			n.previous = head;
			n.next = tail;
			tail.previous = n;
		} else {
			
			if (tail.previous.count < nodeSize) { //checks to see if space in previous node , if so add to previous node
				tail.previous.addItem(item);
			}
			else {
				Node n = new Node(); //creates new node at the end of the list
				n.addItem(item);
				Node temp = tail.previous;
				temp.next = n;
				n.previous = temp;
				n.next = tail;
				tail.previous = n;
			}
		}
		size++;
		return true;
	  		  
  		}

  @Override
  public void add(int pos, E item){
	  if (item == null) {
			throw new NullPointerException("Item is null.");
		}
	  if(size==0) //same as add() checks to see if list has no nodes, creates new node.
	  {
		  Node n = new Node();
			n.addItem(item);
			head.next = n;
			n.previous = head;
			n.next = tail;
			tail.previous = n;
	  }else {
	  
	  NodeInfo temp = new NodeInfo(head, 0);
	 
	  temp = temp.find(pos);
	  
	  Node currentNode = temp.node;
	  
	  int offset = temp.offset;
	  
	  if(offset==0) 
	  {
		  if(currentNode.previous.count != nodeSize && currentNode.previous!=head)
		  {
			  currentNode = currentNode.previous;
			  currentNode.addItem(item);
			  size++;
			  return;
		  }
	  if(currentNode==tail&&currentNode.previous.count==nodeSize)
	  {
		  Node tempNode = new Node();
		  Node prevNode = currentNode.previous;
		  tempNode.previous = prevNode;
		  tempNode.next = currentNode;
		  currentNode.previous = tempNode;
		  prevNode.next = tempNode;
		  tempNode.addItem(item);
		  size++;
		  return;
	  }
	 }
	  if(currentNode.count<nodeSize)
	  {
		  currentNode.addItem(offset, item);
		  size++;
		  return;
	  }
	  
	  if(currentNode.count>=nodeSize)
	  {
		  Node tempNode = new Node();
		  Node tempNext = currentNode.next;
		  tempNode.next=tempNext;
		  currentNode.next=tempNode;
		  tempNode.previous=currentNode;
		  tempNext.previous=tempNode;
		  int halfSize = nodeSize/2;
		  while(tempNode.count!=halfSize)
	  {
			  tempNode.addItem(currentNode.data[halfSize]);
			  currentNode.removeItem(halfSize);
	  }
		  if(offset<= halfSize)
	  {
			  currentNode.addItem(offset, item);
			  size++;
			  return;
	  }
	  if(offset>(halfSize))
	  {
	  tempNode.addItem(offset-halfSize, item);
	  size++;
	  return;
	  }
	}
	  }
		
		
		
  }
	  
  private NodeInfo currentInfo(int pos) {
	// TODO Auto-generated method stub
	  Node temp = head.next;
		int currPos = 0;
		while (temp != tail) {
		if (currPos + temp.count <= pos) {
				currPos = currPos + temp.count;
				temp = temp.next;
			}

			NodeInfo nodeInfo = new NodeInfo(temp, (pos - currPos));
			return nodeInfo;

		}
		return new NodeInfo(temp, currPos);

}

@Override
  public E remove(int pos)
  {
	  if(pos > size  ||  pos<0) { 
		throw new IndexOutOfBoundsException(); 
	  }
	  
      Node currentNode = head.next;
      int count = currentNode.count;
      int offset  = 0;
      
      
      while (count < pos){ //iterates through nodes until the specified position
          currentNode = currentNode.next;
          count = count +  currentNode.count;
      	}
      
      if(currentNode.count == 0) {
          currentNode = currentNode.previous;
          offset = currentNode.count;
      }
      else {
    	  int countNext =(count - currentNode.count);
          offset = pos - countNext;
      }

      if(offset > currentNode.count - 1) {
          currentNode = currentNode.next;
      }

      NodeInfo nodeInfo = new NodeInfo(currentNode, offset);

      return nodeInfo.remove();
  	}

 

/**
   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
   * Traverse the list and copy its elements into an array, deleting every visited node along 
   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
   * (possibly) the last one must be full of elements.  
   *  
   * Comparator<E> must have been implemented for calling insertionSort().    
   */
  public void sort()
  {
	  Iterator<E> iter = iterator();
	  E[] dataList = (E[]) new Comparable[size()];
	
	for (int i = 0; i < size; i++) 
		dataList[i] = iter.next();
	
	head.next = tail;
	tail.previous = head;
	size = 0;
	InsertionSortComparator i = new InsertionSortComparator();
	
	insertionSort(dataList, i);
	
  }
  
  /**
   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  public void sortReverse() 
  {
	  // TODO 
	  Iterator<E> iterator = iterator();
		E[] dataList = (E[]) new Comparable[size];
		
		for (int i = 0; i < size; i++) 
		dataList[i] = iterator.next();
		head.next = tail;
		tail.previous = head;
		size = 0;
		bubbleSort(dataList);
		
		for(int i = 0 ; i < dataList.length; i++)
			this.add(dataList[i]);
  }
  
  @Override
  public Iterator<E> iterator()
  {
    // TODO Auto-generated method stub
    return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator()
  {
    // TODO Auto-generated method stub
    return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index)
  {
    // TODO Auto-generated method stub
    return new StoutListIterator(index);
  }
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal()
  {
    return toStringInternal(null);
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }


  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node
  {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable.
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;

    /**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      //useful for debugging
      //      System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
  
    /**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item)
    {
      if (count >= nodeSize)
      {
    	  return;
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      E item = data[offset];
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      --count;
    }    
  }
  private class StoutListIterator implements ListIterator<E> 
	{
		private int index;
		private NodeInfo prevState;
		private boolean canRemove;

		/**
		 * Default constructor
		 */
		public StoutListIterator() 
		{
			index = 0;
			prevState = null;
			canRemove = false;
		}

		/**
	     * Constructor finds node at a given position.
	     * @param pos
	     */
		public StoutListIterator(int pos) 
		{
			if (pos > size||pos < 0) {
				throw new IndexOutOfBoundsException();
			}
			index = pos;
			prevState = null;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			
			if(index < size) {
				return true;
			}else {
				return false;
			}
		}

		@Override
		public E next() 
		{
			if (hasNext()==true) 
			{
				NodeInfo n = currentInfo(index++);
				
				prevState = n;
				canRemove = true;
				int  nOffset = n.offset;
				
				return (n.node.data[nOffset]);
			}else {
			throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() 
		{
			if (canRemove=false) {
				throw new IllegalStateException();
			}
			
			NodeInfo cursorPos = currentInfo(index);
			
			if (prevState.node != cursorPos.node || prevState.node == cursorPos.node && prevState.offset < cursorPos.offset ) {
				
			index=index-1;
			canRemove = false;
			StoutList.this.remove(prevState);
			}
		}
		
		@Override
		public boolean hasPrevious() {
			if (index>0) {
			return true;
			}else {
				return false;
			}
		}

		//checks for previous available value
		@Override
		public E previous() 
		{
			if (hasPrevious()==true) 
			{
				NodeInfo n = currentInfo((index-1));
				prevState = n;
				canRemove = true;
				
				return n.node.data[n.offset];
			}else {
			
			throw new NoSuchElementException();
			}
		}

		//Returns index of next open element
		@Override
		public int nextIndex() 
		{
			return index;
		}

		//Returns index of previous element
		@Override
		public int previousIndex() {
			int prevIndex = index - 1;
			return prevIndex;
		}

		//Returns previous open element 
		@Override
		public void set(E e) 
		{
			if (e == null|| canRemove == false) {
				throw new NullPointerException();
		}
			int prevOffset = prevState.offset;
			prevState.node.data[prevOffset] = e;
		}

		@Override
		public void add(E e) 
		{
			if (e == null) {
				throw new NullPointerException();
			}
			
			canRemove = false;
			StoutList.this.add((index+1), e);
		}
	}
  //helper method for the insertion sort method. Uses generic item comparator to compare two elements
  class InsertionSortComparator<E extends Comparable<E>> implements Comparator<E>{
	  @Override
		public int compare(E item1, E item2) {
			// TODO Auto-generated method stub
			return item1.compareTo(item2);
		}
  }

	/**
	   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
	   * @param arr   array storing elements from the list 
	   * @param comp  comparator used in sorting 
	   */
	private void insertionSort(E[] arr, Comparator<? super E> comp) 
	{
		for (int i = 1; i < arr.length; i++) 
		{
			E temp = arr[i];
			int j = i - 1;
			
			while(j > -1 && comp.compare(arr[j], temp) > 0) 
			{
				arr[j + 1] = arr[j];
				j--;
			}
			
			arr[j + 1] = temp;
		}
		
		for(int i = 0 ; i < arr.length; i++)
			this.add(arr[i]);
	}

	/**
	   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
	   * description of bubble sort please refer to Section 6.1 in the project description. 
	   * You must use the compareTo() method from an implementation of the Comparable 
	   * interface by the class E or ? super E. 
	   * @param arr  array holding elements from the list
	   */
	private void bubbleSort(E[] arr) 
	{
		boolean swapPos = false;
		
		for (int i = 1; i < arr.length; i++) 
		{
			if (arr[i - 1].compareTo(arr[i]) < 0) 
			{
				E temp = arr[i - 1];
				arr[i - 1] = arr[i];
				arr[i] = temp;
				swapPos = true;
			}
		}
		
		if (swapPos==false) 
			return;
		
		else 
			bubbleSort(arr);
	}
  
	//helper class used throughout to hold offset and node information 
  private class NodeInfo{
	  public Node node;
	  public int offset;
	public NodeInfo(Node n, int offset) {
		  this.node = n;
		  this.offset = offset;
	  }
	public E remove() {// remove method for checking remove rules and updating the list
		// TODO Auto-generated method stub
		E remove;
		
		int midPoint = nodeSize/2;
        if(node.next.count == 0 || node.count > midPoint) {
            if(node.count == 1) {
                remove = node.data[0];
                node.removeItem(0);
                node.previous.next = tail;
                node.next.previous= node.previous;
                node.next = null;
                node.previous = null;
            }
            else {
                remove = node.data[offset];
                node.removeItem(offset);
            }

        }
        else {
            if(node.next.count > midPoint) {
            	  node.removeItem(offset);
                remove = node.data[offset];
                node.addItem(node.count, node.next.data[0]);
                node.next.removeItem(0);
            }
            else {
                node.removeItem(offset);
                remove = node.data[offset];

                for(int i = 0; i < node.next.count; ++i) {
                    node.addItem(node.count, node.next.data[i]);
                }
                node.next = node.next.next;
                node.next.previous = null;
                node.next.previous.next = null;
                node.next.previous = node;
            }
        }
        size = size-1; // Bookkeeping
        return remove;
    }
	NodeInfo find(int pos){
		Node current = head.next;
		int nextPos = current.count;
		int previous = 0;
		while((pos >= nextPos && current.next != null)){
			previous = previous + current.count;
			current = current.next;
			nextPos = nextPos +current.count;
		}
		return new NodeInfo(current, (pos-previous));
	}

}
  
}