package worker;

import java.util.Queue;

import constants.MessageType;
import constants.StateType;
import constants.StatusType;

public class Node extends Thread
{
	private int				adjWeights[];
	private Node			adjNodes[];
	private int				level;
	private StatusType		status[];
	private StateType		state;
	private int				rec;
	private int				bestNode;
	private int				bestWeight;
	private int				testNode;
	private Queue<String>	message;
	
	// Node '0' means none/null, since the program have all nodes starting with 1
	
	public Node(int adjacentNodeInfo[])
	{
		// set weight info
		this.adjWeights = adjacentNodeInfo;
		this.status = new StatusType[adjacentNodeInfo.length];
		
		for (int i = 1; i < adjacentNodeInfo.length; i++)
		{
			this.status[i] = StatusType.BASIC;
		}
		
		this.state = StateType.SLEEP;
		this.bestNode = 0; // though this is by default, mentioned to have clarity
	}
	
	public void setAdjNodes(Node adjNodes[])
	{
		this.adjNodes = adjNodes;
	}
	
	public void run()
	{
		while (true)
		{
			if (!message.isEmpty())
			{
				processMessage((String) message.remove());
			}
		}
	}
	
	public void initialize()
	{
		
	}
	
	public void processMessage(String msg)
	{
		String splitMsgArr[] = msg.split(" ");
		
		// s[0] will be function name and rest arguments
		MessageType msgType = MessageType.getMsgType(splitMsgArr[0]);
		
		switch (msgType)
		{
			case CONNECT:
				processConnect();
				break;
			case INITIATE:
				processInitiate();
				break;
			case TEST:
				processTest();
				break;
			case REJECT:
				processReject(splitMsgArr);
				break;
			case ACCEPT:
				processAccept(splitMsgArr);
				break;
			case REPORT:
				processReport();
				break;
			case CHANGEROOT:
				processChangeRoot();
				break;
			default:
				break;
		}
	}
	
	private void processConnect()
	{
		// TODO - implement
	}
	
	private void processInitiate()
	{
		// TODO - implement
	}
	
	private void processTest()
	{
		// TODO - implement
	}
	
	private void processReject(String splitMsgArr[])
	{
		// TODO - implement
		int q = Integer.parseInt(splitMsgArr[1]);
		System.out.println("Received 'reject' msg from " + q);
		
		if (this.status[q] == StatusType.BASIC)
		{
			this.status[q] = StatusType.REJECT;
		}
		
		findMin();
		
		/*
		 * if status [q] = basic then status [q] ← reject end findMin()
		 */
	}
	
	private void processAccept(String splitMsgArr[])
	{
		int q = Integer.parseInt(splitMsgArr[1]);
		System.out.println("Received 'accept' msg from " + q);
		
		this.testNode = 0;
		if (this.adjWeights[q] < this.bestWeight)
		{
			this.bestWeight = this.adjWeights[q];
			this.bestNode = q;
		}
		myReport();
	}
	
	private void findMin()
	{
		
	}
	
	private void myReport()
	{
		
	}
	
	private void processReport()
	{
		// TODO - implement
	}
	
	private void processChangeRoot()
	{
		// TODO - implement
	}
}
