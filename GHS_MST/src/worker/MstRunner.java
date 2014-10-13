package worker;

import java.io.IOException;

import util.Utils;

public class MstRunner
{
	
	private MstRunner()
	{
		// Making constructor private, don't want anyone to make the object
	}
	
	public static void findMst(String dataPath) throws IOException
	{
		Utils u = new Utils(dataPath);
		
		int noOfNodes, i;
		int adjacentNodeInfo[];
		Node n[];
		
		noOfNodes = u.getNumberOfNodes();
		n = new Node[noOfNodes + 1];
		adjacentNodeInfo = new int[noOfNodes + 1];
		
		if (noOfNodes != -1)
		{
			// Make Objects and initialize their weight array
			for (i = 1; i <= noOfNodes; i++)
			{
				adjacentNodeInfo = u.getAdjacentNodeInfo(i, noOfNodes);
				n[i] = new Node(adjacentNodeInfo);
			}
			
			// Initialize information about adjacent nodes
			for (i = 1; i <= noOfNodes; i++)
			{
				n[i].setAdjNodes(n);
			}
			
			// Start every Node
			for (i = 1; i <= noOfNodes; i++)
			{
				n[i].initialize();
				n[i].start();
			}
		}
	}
}
