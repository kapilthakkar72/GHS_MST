package worker;

import java.util.LinkedList;
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

	/**
	 * @return the status
	 */
	public StatusType[] getStatus()
	{
		return status;
	}

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
		this.message = new LinkedList<>();
	}

	public void setAdjNodes(Node adjNodes[])
	{
		this.adjNodes = adjNodes;
		this.noOfNodes = adjNodes.length - 1;
	}

	public void run()
	{
		while (!this.isInterrupted())
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
			String s = "connect " + myIndex + " 0";
			q.message.add(s);
		}
	}

	public void processMessage(String msg)
	{
		String splitMsgArr[] = msg.split(" ");

		// s[0] will be function name and rest arguments
		MessageType msgType = MessageType.getMsgType(splitMsgArr[0]);
		
		System.out.println("Mssage Type : "+splitMsgArr[0]);

		switch(msgType)
		{
			case CONNECT:
				processConnect(splitMsgArr);
				break;
			case INITIATE:
				processInitiate(splitMsgArr);
				break;
			case TEST:
				processTest(splitMsgArr);
				break;
			case REJECT:
				processReject(splitMsgArr);
				break;
			case ACCEPT:
				processAccept(splitMsgArr);
				break;
			case REPORT:
				processReport(splitMsgArr);
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
			System.out.println("Sending initiate");
			adjNodes[q].message.add("initiate " + myIndex + " " + (level + 1) + " " + myName + " "
					+ StateType.FIND);
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
		
		System.out.println("My state is : "+state);

		if (state == StateType.FIND)
		{
			rec = 0;
			findMin();
		}

	}

	private void processTest(String splitMsgArr[])
	{
		int q = Integer.parseInt(splitMsgArr[1]);
		int level_dash = Integer.parseInt(splitMsgArr[2]);
		String name_dash = splitMsgArr[3];

		if (level_dash > level)
		{
			String msg = StringUtils.join(splitMsgArr, ' ');
			message.add(msg);
		}
		else if (myName.equals(name_dash))
		{
			if (status[q] == StatusType.BASIC)
				status[q] = StatusType.REJECT;
			if (q != testNode)
				adjNodes[q].message.add("reject " + myIndex);
			else
				findMin();
		}
		else
		{
			adjNodes[q].message.add("accept " + myIndex);
		}
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
		int minWt = MstConstants.INFINITY;
		int i, index = 0;
		
		System.out.println("Inside find min");

		for (i = 1; i <= noOfNodes; i++)
		{
			if (adjWeights[i] != 0 && status[i] == StatusType.BASIC)
			{
				if (minWt > adjWeights[i])
				{
					minWt = adjWeights[i];
					index = i;
				}
			}
		}
		
		System.out.println("Index: "+index);

		if (index != 0)
		{
			testNode = index;
			adjNodes[index].message.add("test " + myIndex + " " + level + " " + myName);
		}
		else
		{
			testNode = 0;
			myReport();
		}
	}

	private void myReport()
	{
		int temp = 0;
		for (int i = 1; i < noOfNodes; i++)
		{
			if (adjWeights[i] != 0)
			{
				if (i != parent && status[i] == StatusType.BRANCH)
				{
					temp++;
				}
			}
		}
		if (temp == rec && testNode == 0)
		{
			state = StateType.FOUND;
			adjNodes[parent].message.add("report " + myIndex + " " + bestWeight);
		}
	}

	private void processReport(String splitMsgArr[])
	{
		int q = Integer.parseInt(splitMsgArr[1]);
		int w = Integer.parseInt(splitMsgArr[2]);
		if (q != parent)
		{
			if (w > bestWeight)
			{
				bestWeight = w;
				bestNode = q;
			}
			rec = rec + 1;
			myReport();
		}
		else
		{
			if (state == StateType.FIND)
			{
				String msg = StringUtils.join(splitMsgArr, ' ');
				message.add(msg);
			}
			else if (w > bestWeight)
			{
				processChangeRoot();
			}
			else if (w == bestWeight && w == MstConstants.INFINITY)
			{
				// stop
				System.out.println("This node ended: "+myIndex);
				this.interrupt();
			}
		}
	}

	private void processChangeRoot()
	{
		if (status[bestNode] == StatusType.BRANCH)
		{
			// send change-root to best node
			adjNodes[bestNode].message.add("changeroot " + myIndex);
		}
		else
		{
			status[bestNode] = StatusType.BRANCH;
			adjNodes[bestNode].message.add("connect " + myIndex + " " + level);
		}
	}
}
