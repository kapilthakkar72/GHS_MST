package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils
{
	FileInputStream	fstream;
	DataInputStream	in;
	
	public Utils(String dataPath) throws FileNotFoundException
	{
		try
		{
			// Open the file that is the first command line parameter
			fstream = new FileInputStream(dataPath);
			// Get the object of DataInputStream
			in = new DataInputStream(fstream);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Error: " + e.getMessage());
			// throwing the exception above, not my responsibility to catch
			throw e;
		}
		// TODO : can't close the stream
	}
	
	public int getNumberOfNodes() throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int count = 0;
		int noOfNodes = -1;
		// Read File Line By Line
		while ((strLine = br.readLine()) != null)
		{
			if (count == 0)
			{
				// It is the first Line - Number of Nodes
				noOfNodes = Integer.parseInt(strLine);
				return noOfNodes;
			}
		}
		return noOfNodes;
	}
	
	public int[] getAdjacentNodeInfo(int i, int n) throws IOException
	{
		int row[] = new int[n + 1];
		String read[] = new String[n];
		
		int count = 0;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		
		// Read File Line By Line
		while ((strLine = br.readLine()) != null)
		{
			if (count == i)
			{
				// It is the first Line - Number of Nodes
				read = strLine.split(" ");
				for (int j = 0; j < n; j++)
				{
					row[j + 1] = Integer.parseInt(read[j]);
				}
				return row;
			}
			count++;
		}
		return row;
	}
}
