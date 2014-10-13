package worker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import constants.MstGlobals;
import constants.StatusType;

public class MstRunner
{
	private MstRunner()
	{
		// Making constructor private, don't want anyone to make the object
	}
	
	private static void readFromFile(String dataPathInput) throws IOException
	{
		FileInputStream fstream = null;
		DataInputStream in = null;
		try
		{
			fstream = new FileInputStream(dataPathInput);
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			int adjacentNodeInfo[];
			
			if ((strLine = br.readLine()) != null)
			{
				// It is the first Line - Number of Nodes
				MstGlobals.noOfNodes = Integer.parseInt(strLine);
			}
			
			MstGlobals.n = new MstNode[MstGlobals.noOfNodes + 1];
			
			// Make Objects and initialize their weight array
			for (int i = 1; i <= MstGlobals.noOfNodes; i++)
			{
				adjacentNodeInfo = new int[MstGlobals.noOfNodes + 1];
				if ((strLine = br.readLine()) != null)
				{
					String read[] = strLine.split(" ");
					for (int j = 0; j < MstGlobals.noOfNodes; j++)
					{
						adjacentNodeInfo[j + 1] = Integer.parseInt(read[j]);
					}
					MstGlobals.n[i] = new MstNode(adjacentNodeInfo, i);
				}
			}
			
			// Initialize information about adjacent nodes
			for (int i = 1; i <= MstGlobals.noOfNodes; i++)
			{
				MstGlobals.n[i].setAdjNodes(MstGlobals.n);
			}
		}
		finally
		{
			try
			{
				if (fstream != null)
				{
					fstream.close();
				}
				if (in != null)
				{
					in.close();
				}
			}
			catch (IOException e)
			{
				System.out.println("Error closing input stream, continuing");
			}
		}
	}
	
	private static void writeToFile(String dataPathOutput) throws IOException
	{
		FileWriter writer = null;
		try
		{
			File file = new File(dataPathOutput);
			file.createNewFile();
			writer = new FileWriter(file);
			
			for (int i = 1; i <= MstGlobals.noOfNodes; i++)
			{
				for (int j = i + 1; j <= MstGlobals.noOfNodes; j++)
				{
					if ((MstGlobals.n[i].getStatus())[j] == StatusType.BRANCH)
					{
						writer.write(i + " -> " + j + "\n");
					}
				}
			}
		}
		finally
		{
			try
			{
				if (writer != null)
				{
					writer.close();
				}
			}
			catch (IOException e)
			{
				System.out.println("Error closing output stream, continuing");
			}
		}
	}
	
	public static void findMst(String dataPathInput, String dataPathOutput) throws IOException,
			InterruptedException
	{
		readFromFile(dataPathInput);
		
		// Start every Node
		for (int i = 1; i <= MstGlobals.noOfNodes; i++)
		{
			MstGlobals.n[i].initialize();
			MstGlobals.n[i].start();
		}
		
		for (int i = 1; i <= MstGlobals.noOfNodes; i++)
		{
			MstGlobals.n[i].join();
		}
		
		System.out.println("All nodes finished");
		
		writeToFile(dataPathOutput);
	}
}
