/**
 * The <code>Router</code> class represents a router in the network, which is ultimately a queue. This
 * queue is represented by the <code>LinkedList</code> class which extends the <code>Router</code> class.
 * This class also uses the <code>Packet</code> class to store the packets it receives.
 *
 *
 * @author Piotr Milewski
 *    email: piotr.milewski@stonybrook.edu
 *    Stony Brook ID: 112291666
 **/

import java.io.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class Router extends LinkedList<Packet>{

    LinkedList<Packet> queue;
    static int maxBuffSize;

    public Router(){
	queue = new LinkedList<Packet>();
    }
    
    public Router(int maxBufferSize){
	queue = new LinkedList<Packet>();
	maxBuffSize = maxBufferSize;
    }

    public void enqueue(Packet p){
	queue.addLast(p);
    }

    public Packet dequeue(){
	if (isEmpty()){
	    System.out.println("\nQueue is empty.\n");
	    return null;
	}
	return queue.removeFirst();
    }

    public Packet peek(){
	if (isEmpty()){
	    System.out.println("\nQueue is empty.\n");
	    return null;
	}
	return queue.peek();
    }

    public int size(){
	return queue.size();
    }

    public boolean isEmpty(){
	return (queue.size() == 0);
    }

    public String toString(){
	String output = "{";
	if (isEmpty()){ //Check if queue is empty
	    output += "}";
	    return output;
	}
	ListIterator<Packet> litr = queue.listIterator();
	while (litr.hasNext()){ //Iterate through queue and add each packet
	    output += litr.next();
	    if (litr.hasNext())
		output += ", ";
	}
	output += "}";
	return output;
    }

    public static int sendPacketTo(LinkedList<Router> routers){
	boolean areRoutersFull = true;
	int index = 0;
	int counter = 1;
	int freeSize = maxBuffSize;
	Router currRouter;
	if (routers.size() == 0){ //No routers
	    System.out.println("\nNo Routers instantiated\n");
	    return 0;
	}
	ListIterator<Router> litr = routers.listIterator(0);
	while (litr.hasNext()){
	    currRouter = litr.next();
	    if (currRouter.size() < freeSize){
		areRoutersFull = false;
		index = counter;
		freeSize = currRouter.size();
	    }
	    counter++;
	}
	if (areRoutersFull) //if all router queues are full, drop the packet (send -1)
	    return -1;
	return index;
    }
}
