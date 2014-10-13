package worker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import constants.StatusType;

public class MstRunner
{

	private MstRunner()
	{
		// Making constructor private, don't want anyone to make the object
	}

	public static void findMst(String dataPathInput,String dataPathOutput) throws IOException
	{
		FileInputStream fstream = null;
		DataInputStream in = null;
		FileWriter writer=null;
		try
		{

			fstream = new FileInputStream(dataPathInput);

			in = new DataInputStream(fstream);

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int count = 0;

			int noOfNodes = -1, i;
			int adjacentNodeInfo[];
			Node n[];

			if ((strLine = br.readLine()) != null)
			{
				// It is the first Line - Number of Nodes
				noOfNodes = Integer.parseInt(strLine);
			}

			n = new Node[noOfNodes + 1];
			

			// Make Objects and initialize their weight array
			for (i = 1; i <= noOfNodes; i++)
			{
				adjacentNodeInfo = new int[noOfNodes + 1];
				if ((strLine = br.readLine()) != null)
				{
					String read[] = strLine.split(" ");
					for (int j = 0; j < noOfNodes; j++)
					{
						adjacentNodeInfo[j + 1] = Integer.parseInt(read[j]);
					}
					n[i] = new Node(adjacentNodeInfo,i);
				}
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
			
			// Print the selected edges
			
			File file = new File(dataPathOutput);
		    // creates the file
		    file.createNewFile();
		    // creates a FileWriter Object
		    writer = new FileWriter(file); 
		    // Writes the content to the file
				
			for(i=1;i<=noOfNodes;i++)
			{
				for(int j=i+1;j<=noOfNodes;j++)
				{
					if((n[i].getStatus())[j] ==StatusType.BRANCH)
					{
						writer.write(i+" -> "+j+"\n");
					}
				}
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
				
				if(writer!=null)
				{
					writer.flush();
				    writer.close();
				}
			}
			catch (IOException e)
			{
				System.out.println("Error closing stream, continuing");
			}

		}

	}
}
