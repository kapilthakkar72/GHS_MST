package test;

import java.io.IOException;

import worker.MstRunner;

public class TestMain
{
	public static void main(String[] args)
	{
		String dataPathInput = "data/input.txt";
		String dataPathOutput = "data/output.txt";
		System.out.println("Started");
		try
		{
			MstRunner.findMst(dataPathInput,dataPathOutput);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
