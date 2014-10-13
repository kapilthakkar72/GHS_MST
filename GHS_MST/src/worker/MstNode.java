package worker;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import constants.MessageType;
import constants.MstConstants;
import constants.StateType;
import constants.StatusType;

public class MstNode extends Thread
{
	private static int		branchCount;
	private int				adjWeights[];
	private MstNode			adjNodes[];
	private int				level;
	private StatusType		status[];
	private StateType		state;
	private int				rec;
	private int				bestNode;
	private int				bestWeight;
	private int				testNode;
	private List<String>	message;
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
	
	public MstNode(int adjacentNodeInfo[], int index)
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
		this.message = Collections.synchronizedList(new LinkedList<>());
		this.parent = 0;
	}
	
	public void setAdjNodes(MstNode adjNodes[])
	{
		this.adjNodes = adjNodes;
		this.noOfNodes = adjNodes.length - 1;
		branchCount = (noOfNodes - 1) * 2;
	}
	
	public void run()
	{
		while (!this.isInterrupted())
		{
			if (branchCount == 0)
			{
				// here
				terminateAll();
			}
			if (!message.isEmpty())
			{
				String msg = (String) this.message.get(0);
				this.message.remove(0);
				processMessage(msg);
			}
		}
	}
	
	public void initialize()
	{
		// check all neighbors to find min w(pq)
		
		int minWt = MstConstants.INFINITY;
		MstNode q;
		
		int i, minNodeIndex = 0;
		
		for (i = 1; i <= noOfNodes; i++)
		{
			if (adjWeights[i] != 0)
			{
				if (minWt > adjWeights[i])
				{
					minWt = adjWeights[i];
					minNodeIndex = i;
				}
			}
		}
		if (minNodeIndex == 0)
		{
			System.out.println("Neighbor with min weight not found");
		}
		else
		{
			status[minNodeIndex] = StatusType.BRANCH;
			branchCount--;
			System.out.println(myIndex + " set " + minNodeIndex + " to Branch");
			level = 0;
			state = StateType.FOUND;
			rec = 0;
			
			myName = Integer.toString(adjWeights[minNodeIndex]);
			q = adjNodes[minNodeIndex];
			
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
		int q = Integer.parseInt(splitMsgArr[1]);
		
		switch (msgType)
		{
			case CONNECT:
				processConnect(splitMsgArr, q);
				break;
			case INITIATE:
				processInitiate(splitMsgArr, q);
				break;
			case TEST:
				processTest(splitMsgArr, q);
				break;
			case REJECT:
				processReject(splitMsgArr, q);
				break;
			case ACCEPT:
				processAccept(splitMsgArr, q);
				break;
			case REPORT:
				processReport(splitMsgArr, q);
				break;
			case CHANGEROOT:
				processChangeRoot();
				break;
			default:
				break;
		}
	}
	
	private void processConnect(String splitMsgArr[], int q)
	{
		// 1 argument = level
		int L = Integer.parseInt(splitMsgArr[2]);
		
		if (L < level)
		{
			status[q] = StatusType.BRANCH;
			branchCount--;
			System.out.println(myIndex + " set " + q + " to Branch");
			adjNodes[q].message.add("initiate " + myIndex + " " + level + " " + myName + " "
					+ state.getStateStr());
			if (state == StateType.FIND)
				rec = rec + 1;
		}
		else if (status[q] == StatusType.BASIC)
		{
			String msg = StringUtils.join(splitMsgArr, ' ');
			message.add(msg);
		}
		else
		{
			adjNodes[q].message.add("initiate " + myIndex + " " + (level + 1) + " " + myName + " "
					+ StateType.FIND.getStateStr());
		}
	}
	
	private void processInitiate(String splitMsgArr[], int q)
	{
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
				if (state == StateType.FIND)
					rec = rec + 1;
			}
		}
		
		if (state == StateType.FIND)
		{
			// rec = 0;
			findMin();
		}
		
	}
	
	private void processTest(String splitMsgArr[], int q)
	{
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
	
	private void processReject(String splitMsgArr[], int q)
	{
		if (this.status[q] == StatusType.BASIC)
		{
			this.status[q] = StatusType.REJECT;
		}
		findMin();
	}
	
	private void processAccept(String splitMsgArr[], int q)
	{
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
		if (0 == rec && testNode == 0)
		{
			state = StateType.FOUND;
			adjNodes[parent].message.add("report " + myIndex + " " + bestWeight);
		}
	}
	
	private void processReport(String splitMsgArr[], int q)
	{
		int w = Integer.parseInt(splitMsgArr[2]);
		if (q != parent)
		{
			if (w > bestWeight)
			{
				bestWeight = w;
				bestNode = q;
			}
			
			rec = rec - 1;
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
				terminateAll();
			}
		}
	}
	
	private synchronized void terminateAll()
	{
		for (int i = 1; i <= noOfNodes; i++)
		{
			if (i != myIndex)
				adjNodes[i].interrupt();
		}
		this.interrupt();
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
			System.out.println(myIndex + " set " + bestNode + " to Branch");
			status[bestNode] = StatusType.BRANCH;
			branchCount--;
			adjNodes[bestNode].message.add("connect " + myIndex + " " + level);
		}
	}
}
