package test;

import java.io.IOException;

import worker.MstRunner;

public class TestMain
{
	public static void main(String[] args)
	{
		String dataPath = "data/input.txt";
		System.out.println("Started");
		try
		{
			MstRunner.findMst(dataPath);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
