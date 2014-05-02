import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class fileParser {
	String filename = "";
	int lastLineNumber = -1;
	
	public fileParser()
	{
		
	}
	public fileParser(String file)
	{
		
	}
	
	
	public void setFile(String file)
	{
		filename = file;
	}
	public String getFile()
	{
		String file = filename;
		return file;
	}
	public void printFile()
	{
		boolean isIn = false;
		BufferedReader br;
		
		try
		{
			br = new BufferedReader(new FileReader(filename));
		
			String line;
			while ((line = br.readLine()) != null)
			{
				// process the line.
				System.out.println("** " + line);
			}
			br.close();
		
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getLastLineNumber()
	{
		return lastLineNumber;
	}
	
	public boolean isInFile(String queryText)
	{
		BufferedReader br;
		boolean found = false;
		int foundAtLocation = -1;
		
		try
		{
			br = new BufferedReader(new FileReader(filename));
			
			String line;
			int i = 0;
			while ((line = br.readLine()) != null)
			{
				// process the line.
				i++;
				if (line.equals(queryText))
				{
					found = true;
//					isIn = true;
					foundAtLocation = i;
					lastLineNumber = i;
				}
				else
				{
				}
			}
//			System.out.println("The item was found: " + found);
//			System.out.println("It was found at line: " + Integer.toString(foundAtLocation));
			br.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		return isIn;		
		return found;
	}

	public void addLine(String addThis, int addToLine)
	{
		BufferedReader br;
//		FileWriter outputFile;
		BufferedWriter bw;
		boolean found = false;
		int foundAtLocation = -1;
		String output = "";
		

		System.out.println("ENTERED addLine");
		
		try
		{
			br = new BufferedReader(new FileReader(filename));
			bw = new BufferedWriter(new FileWriter("C:\\InControl-NG2\\output.xml"));
//			outputFile = new FileWriter("C:\\InControl-NG2\\output.xml");
			
			
			String line;
			int i = 0;
			while ((line = br.readLine()) != null)
			{
				// process the line.
				
//				if (i == addToLine)
//				{
//					output = addThis;
//					outputFile.write(output);
//				}
				
				System.out.println(line);
				output = line;
//				outputFile.write(output);
				bw.write(output);
				bw.newLine();
				
				i++;
			}
			br.close();
//			outputFile.close();
			bw.write(output);
			bw.newLine();
			bw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("EXITING addLine");
	}
	
}
