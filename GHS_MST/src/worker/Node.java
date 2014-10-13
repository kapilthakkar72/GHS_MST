package worker;

import java.util.Queue;

import org.apache.commons.lang3.StringUtils;

import constants.MessageType;
import constants.MstConstants;
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
	private int				noOfNodes;
	private int				myIndex;
	private String			myName;
	private int				parent;
	
	public Node(int adjacentNodeInfo[], int index)
	{
		// set weight info
		this.adjWeights = adjacentNodeInfo;
		this.status = new StatusType[adjacentNodeInfo.length];
		
		for (int i = 1; i < adjacentNodeInfo.length; i++)
		{
			this.status[i] = StatusType.BASIC;
		}
		this.myIndex = index;
		this.state = StateType.SLEEP;
		this.bestNode = 0; // though this is by default, mentioned to have
							// clarity
	}
	
	public void setAdjNodes(Node adjNodes[])
	{
		this.adjNodes = adjNodes;
		this.noOfNodes = adjNodes.length - 1;
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
		// check all neighbors to find min w(pq)
		
		int minWt = MstConstants.INFINITY;
		Node q;
		int i, index = 0;
		
		for (i = 1; i <= noOfNodes; i++)
		{
			if (adjWeights[i] != 0)
			{
				if (minWt > adjWeights[i])
				{
					minWt = adjWeights[i];
					index = i;
				}
			}
		}
		if (index == 0)
		{
			System.out.println("Neighbor with min weight not found");
		}
		else
		{
			status[index] = StatusType.BRANCH;
			level = 0;
			state = StateType.FOUND;
			rec = 0;
			myName = Integer.toString(adjWeights[index]);
			q = adjNodes[index];
			
			// send <connect,0> to q
			q.message.add("connect " + myIndex + " 0");
		}
	}
	
	public void processMessage(String msg)
	{
		String splitMsgArr[] = msg.split(" ");
		
		// s[0] will be function name and rest arguments
		MessageType msgType = MessageType.getMsgType(splitMsgArr[0]);
		
		switch (msgType)
		{
			case CONNECT:
				processConnect(splitMsgArr);
				break;
			case INITIATE:
				processInitiate(splitMsgArr);
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
	
	private void processConnect(String splitMsgArr[])
	{
		// 1 argument = level
		int L = Integer.parseInt(splitMsgArr[2]);
		int q = Integer.parseInt(splitMsgArr[1]);
		
		if (L < level)
		{
			status[q] = StatusType.BRANCH;
			adjNodes[q].message.add("initiate " + myIndex + " " + level + " " + myName + " "
					+ state);
		}
		else if (status[q] == StatusType.BASIC)
		{
			String msg = StringUtils.join(splitMsgArr, ' ');
			message.add(msg);
		}
		else
		{
			adjNodes[q].message.add("initiate " + myIndex + " " + (level + 1) + " " + myName + " "
					+ state);
		}
	}
	
	private void processInitiate(String splitMsgArr[])
	{
		int q = Integer.parseInt(splitMsgArr[1]);
		int level_dash = Integer.parseInt(splitMsgArr[2]);
		String name_dash = splitMsgArr[3];
		String state_dash = splitMsgArr[4];
		
		this.level = level_dash;
		this.myName = name_dash;
		this.state = StateType.getStateType(state_dash);
		
		this.parent = q;
		
		this.bestNode = 0;
		this.bestWeight = MstConstants.INFINITY;
		this.testNode = 0;
		
		// send initiate message to my neighbors
		
		for (int i = 1; i <= noOfNodes; i++)
		{
			if (adjWeights[i] != 0 && status[i] == StatusType.BRANCH && i != q)
			{
				String msg = StringUtils.join(splitMsgArr, ' ');
				adjNodes[i].message.add(msg);
			}
		}
		
		if (state == StateType.FIND)
		{
			rec = 0;
			findMin();
		}
		
	}
	
	private void processTest()
	{
		// TODO - implement
	}
	
	private void processReject(String splitMsgArr[])
	{
		int q = Integer.parseInt(splitMsgArr[1]);
		System.out.println("Received 'reject' msg from " + q);
		
		if (this.status[q] == StatusType.BASIC)
		{
			this.status[q] = StatusType.REJECT;
		}
		
		findMin();
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
