package junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import worker.MstRunner;

public class MyJunit
{
	String	dataPathInput;
	String	dataPathOutput	= "data/output.txt";
	
	@Test
	public void test1() throws IOException, InterruptedException
	{
		executeTest(1);
	}
	
	@Test
	public void test2() throws IOException, InterruptedException
	{
		executeTest(2);
	}
	
	@Test
	public void test3() throws IOException, InterruptedException
	{
		executeTest(3);
	}
	
	@Test
	public void test4() throws IOException, InterruptedException
	{
		executeTest(4);
	}
	
	@Test
	public void test5() throws IOException, InterruptedException
	{
		executeTest(5);
	}
	
	@Test
	public void test6() throws IOException, InterruptedException
	{
		executeTest(6);
	}
	
	@Test
	public void test7() throws IOException, InterruptedException
	{
		executeTest(7);
	}
	
	@Test
	public void test8() throws IOException, InterruptedException
	{
		executeTest(8);
	}
	
	@Test
	public void test9() throws IOException, InterruptedException
	{
		executeTest(9);
	}
	
	private boolean compareOutput(String reqOutputPath) throws IOException
	{
		File outputFile = new File(dataPathOutput);
		File reqOutputFile = new File(reqOutputPath);
		
		FileReader outputFileReader = new FileReader(outputFile);
		FileReader reqOutputFileReader = new FileReader(reqOutputFile);
		
		BufferedReader buffReader = new BufferedReader(outputFileReader);
		BufferedReader reqBuffReader = new BufferedReader(reqOutputFileReader);
		
		String line1 = null;
		String line2 = null;
		boolean flag = true;
		while ((flag == true) && ((line1 = buffReader.readLine()) != null)
				&& ((line2 = reqBuffReader.readLine()) != null))
		{
			if (!line1.equalsIgnoreCase(line2))
			{
				flag = false;
			}
		}
		buffReader.close();
		reqBuffReader.close();
		return flag;
	}
	
	private void executeTest(int testNum) throws IOException, InterruptedException
	{
		System.out.println("Executing testcase " + testNum);
		dataPathInput = "data/testcases/input" + testNum + ".txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output" + testNum + ".txt"));
	}
}
