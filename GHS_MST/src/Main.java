import java.io.IOException;


public class Main 
{
	public static void main(String args[]) throws IOException
	{
		Utils u=new Utils();
		
		int noOfNodes,i;
		int adjacentNodeInfo[];
		Node adjNodes[];
		Node n[];
		
		noOfNodes=u.getNumberOfNodes();
		n= new Node[noOfNodes+1];
		adjacentNodeInfo=new int[noOfNodes+1];
		
		if(noOfNodes!=-1)
		{
			// Make Objects and initialise their weight array
			for(i=1;i<=noOfNodes;i++)
			{
				adjacentNodeInfo = u.getAdjacentNodeInfo(i, noOfNodes);
				n[i]=new Node(adjacentNodeInfo);
			}
			
			// Initialize information about adjacent nodes
			for(i=1;i<=noOfNodes;i++)
			{
				n[i].setAdjNodes(n);
			}
			
			// Start every Node
			for(i=1;i<=noOfNodes;i++)
			{
				n[i].initialize();
				n[i].start();
			}
		}
	}
}
