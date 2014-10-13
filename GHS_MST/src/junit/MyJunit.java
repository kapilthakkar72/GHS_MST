package junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import worker.MstRunner;

public class MyJunit
{
	String	dataPathInput;
	String	dataPathOutput	= "data/output.txt";
	
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void test1() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 1");
		dataPathInput = "data/testcases/input1.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output1.txt"));
	}
	
	@Test
	public void test2() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 2");
		dataPathInput = "data/testcases/input2.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output2.txt"));
	}
	
	@Test
	public void test3() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 3");
		dataPathInput = "data/testcases/input3.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output3.txt"));
	}
	
	@Test
	public void test4() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 4");
		dataPathInput = "data/testcases/input4.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output4.txt"));
	}
	
	@Test
	public void test5() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 5");
		dataPathInput = "data/testcases/input5.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output5.txt"));
	}
	
	@Test
	public void test6() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 6");
		dataPathInput = "data/testcases/input6.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output6.txt"));
	}
	
	@Test
	public void test7() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 7");
		dataPathInput = "data/testcases/input7.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output7.txt"));
	}
	
	@Test
	public void test8() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 8");
		dataPathInput = "data/testcases/input8.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output8.txt"));
	}
	
	@Test
	public void test9() throws IOException, InterruptedException
	{
		System.out.println("Executing testcase 9");
		dataPathInput = "data/testcases/input9.txt";
		MstRunner.findMst(dataPathInput, dataPathOutput);
		Assert.assertTrue(compareOutput("data/testcases/output9.txt"));
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
				flag = false;
		}
		buffReader.close();
		reqBuffReader.close();
		return flag;
	}
}
