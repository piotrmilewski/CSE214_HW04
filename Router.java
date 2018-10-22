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
	return queue.getFirst();
    }

    public int size(){
	return queue.size();
    }

    public boolean isEmpty(){
	return (queue.size() == 0);
    }

    public String toString(){
	String output = "\n{";
	if (isEmpty()){ //Check if queue is empty
	    output += "}\n";
	    return output;
	}
	else{ //Add first packet if not empty
	    output += "[" + queue.getFirst() + "]";
	}
	ListIterator<Packet> litr = queue.listIterator(0);
	while (litr.hasNext()){ //Iterate through queue and add each packet
	    output += ", [" + litr.next() + "]";
	}
	output += "}\n";
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
	else if (routers.getFirst().size() < freeSize){ //set index to first router
	    areRoutersFull = false;
	    index = 1;
	    freeSize = routers.getFirst().size();
	}
	ListIterator<Router> litr = routers.listIterator(0);
	while (litr.hasNext()){
	    counter++;
	    currRouter = litr.next();
	    if (currRouter.size() < freeSize){
		areRoutersFull = false;
		index = counter;
		freeSize = currRouter.size();
	    }
	}
	if (areRoutersFull) //if all router queues are full, drop the packet (send -1)
	    return -1;
	return index;
    }
}