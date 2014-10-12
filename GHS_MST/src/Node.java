import java.io.*;
import java.util.Queue;

public class Node extends Thread
{
	int adjWeights[];
	Node adjNodes[];
	int level;
	String status[];
	String state;
	int rec;
	Node bestNode;
	int bestWeight;
	Node testNode;
	Queue message;
	
	Node(int adjacentNodeInfo[])
	{
		// set weight info
		adjWeights=adjacentNodeInfo;
		
		status=new String[adjacentNodeInfo.length];
		
		for(int i=1;i<adjacentNodeInfo.length;i++)
		{
			status[i]="basic";	
		}
		state="sleep";
		bestNode=null;
	}
	void setAdjNodes(Node adjNodes[])
	{
		this.adjNodes=adjNodes;
	}
	
	public void run()
	{
		while(true)
			if(!message.isEmpty())
				processMessage((String)message.remove());
	}
	
	public void initialize()
	{
		
	}
	public void processMessage(String msg)
	{
		String s[];
		s=msg.split(" ");
		
		// s[0] will be function name and rest arguments
	}
}