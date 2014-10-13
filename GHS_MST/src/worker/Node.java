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
	private Node			bestNode;
	private int				bestWeight;
	private Node			testNode;
	private Queue<String>	message;
	
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
		this.bestNode = null;
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
				processReject();
				break;
			case ACCEPT:
				processAccept();
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
	
	private void processReject()
	{
		// TODO - implement
	}
	
	private void processAccept()
	{
		// TODO - implement
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
