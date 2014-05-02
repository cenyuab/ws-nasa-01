

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.StandardCopyOption.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class autoUpdate02{
	static String OS = System.getProperty("os.name").toLowerCase();
	static String initials = "";
	static String hostname = "";
	static int svnVer= 0;
	static String svnTag = "";
	static String timeStr = "";
	static String useThisOne = "";
	static String xmlFilePath = "";
	static fileParser parser = new fileParser();
	static int osSwitch = 0;//0 or 1 Linux or Windows
	static int pcType = 0;// 0 or 1 CR & Spread or IAM & SCS
	static String defaultPath = "";	// Path of installation.  Example: C:\\InControl-NG2
	static String computerName = "SAGE3CC5";			// Name of computer.  Example: SAGE3CC5
	static Scanner name = new Scanner(System.in);
	public static void main(String[] args){
		
		System.out.println("Enter your initials: ");
		initials = name.next();
		//Begin Windows
		if(isWindows()){
			System.out.println("Windows system detected");
			getHost();
			defaultPath = "C:\\InControl-NG2";
			osSwitch = 1;		
			
			/*
			 * folderBackup   -- Make a backup of InControl-NG named InControl-NG_ + timestamp
			 * deleteCurrent  -- Delete three main InControl folders in InControl-NG (Data, LocalData, System)
			 * copyNewSVN     -- Copy new SVN version of three main InControl folders into InControl-NG (Data, LocalData, System)
			 */
			
//			folderBackup();
//			deleteCurrent();
//			copyNewSVN();
//			checkDomainAndDomainSecurity();
//			incInstructions();
//			modComENV();
//			modDirTXT();
//			modSystemTopology();
			copyIAMCommands();
//STEP 4
			/////////////////////////////////////////////////////////////////////////////////////////////////////Step 4
//			System.out.println("Step 4:	If the file ingest gateway will be used, copy: data, badFiles, and Files2Ingest folders");
//			
//			if(!new File("C:\\InControl-NG2\\badFiles").exists())
//			{
//				new File("C:\\InControl-NG2\\badFiles").mkdir();
//				System.out.println("Created C:\\InControl-NG2\\badFiles");
//			}
//			if(!new File("C:\\InControl-NG2\\data").exists())
//			{
//				new File("C:\\InControl-NG2\\data").mkdir();
//				System.out.println("Created C:\\InControl-NG2\\badFiles");
//			}
//			if(!new File("C:\\InControl-NG2\\Files2Ingest").exists())
//			{
//				new File("C:\\InControl-NG2\\Files2Ingest").mkdir();
//				System.out.println("Created C:\\InControl-NG2\\badFiles");
//			}
//			System.out.println("Step 4 completed successfully");
//			/////////////////////////////////////////////////////////////////////////////////////////////////////Step 5
//			System.out.println("Step 5 does not apply to Windows machines");
//			/////////////////////////////////////////////////////////////////////////////////////////////////////Step 6
//			System.out.println("Step 6: Make appropriate changes based on the computer/location/hardware configuration");
//			System.out.println("Step 6A: File changes for all computers");
//			xmlFilePath = "C:\\InControl-NG\\InControl-NG-Data\\SAGE3-Data\\scopedData\\SPOC\\Domain\\properties\\Domain.xml";
//			
			
			
		}//end Windows
		
		
		
		if(isUnix()){
			System.out.println("Linux system detected");
			getHost();
			defaultPath = "\\sage3\\home\\InControl-NG2\\";
			osSwitch = 0;
			
		}
		
	} // end main()
	
	public static boolean isWindows() {
		 
		return (OS.indexOf("win") >= 0);
 
	} // end isWindows()
	public static boolean isUnix() {
		 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	} // end isUnix()

	@SuppressWarnings("unused")
	public static void checkDomainAndDomainSecurity()
	{
		String queryText = "";
		boolean domainIsGood = false;
		boolean domainSecurityIsGood = false;
		
		//Check Domain.xml
		parser.setFile(defaultPath + "\\Domain.xml");														// Set File
		queryText = "        <Host value=\"" + computerName + "\"/>";										// Set Query Text
		System.out.println("isInFile - Domain: " + Boolean.toString(parser.isInFile(queryText)));			// Run Query
		domainIsGood = parser.isInFile(queryText);															// Run Query

//		if (!domainIsGood)
		if (domainIsGood)   // THIS WILL NEED CHANGED TO !domainIsGood ONCE IT IS WORKING CORRECTLY!!!
		{
			System.out.println("ADDING LINE TO domain.xml");
			addToFile(queryText, "domain.xml", "    <!-- InControl Servers -->");
		}
		
		//Check DomainSecurity.xml
		parser.setFile(defaultPath + "\\DomainSecurity.xml");												// Set File
		queryText = "        <Host name=\"" + computerName + "\"/>";										// Set Query Text
		System.out.println("isInFile - DomainSecurity: " + Boolean.toString(parser.isInFile(queryText)));	// Run Query
		domainSecurityIsGood = parser.isInFile(queryText);													// Run Query
	
	} // END checkDomainAndDomainSecurity()
		
	/*
	 * Adds line found in 'addThis' directly after 'indexLine' in file 'filename'
	 */
	 public static void addToFile(String addThis, String filename, String indexLine)
	{
		boolean goAhead = false;
		int addToLine = -1;
		
		goAhead = parser.isInFile(indexLine);
		System.out.println("CHECK " + Boolean.toString(parser.isInFile(indexLine)));
		
		if (goAhead)
		{
			addToLine = parser.getLastLineNumber();
			addToLine++;
			parser.setFile(defaultPath + "\\Domain.xml");
			parser.addLine(addThis, addToLine);
		}
		
		
	}
	
	 public static void delete(File file)
		    	throws IOException{
		    	if(file.isDirectory()){	 
		    		//directory is empty, then delete it
		    		if(file.list().length==0){
		    		   file.delete();
		    		   //System.out.println("Directory is deleted : " + file.getAbsolutePath());
		    		}else{
		    		   //list all the directory contents
		        	   String files[] = file.list();
		        	   for (String temp : files) {
		        	      //construct the file structure
		        	      File fileDelete = new File(file, temp);
		        	      //recursive delete
		        	     delete(fileDelete);
		        	   }
		        	   //check the directory again, if empty then delete it
		        	   if(file.list().length==0){
		           	     file.delete();
		        	     //System.out.println("Directory is deleted : " + file.getAbsolutePath());
		        	   }
		    		}
		    	}else{
		    		//if file, then delete it
		    		file.delete();
		    		//System.out.println("File is deleted : " + file.getAbsolutePath());
		    	}
	 } // end delete()

	 public static void folderBackup()
	 {
		 timeStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
		 System.out.println("STEP 1 - Making a backup of InControl-NG folder named InControl-NG_" + timeStr);

		 final Path targetPath = Paths.get(defaultPath +"_"+timeStr);
		 final Path sourcePath = Paths.get(defaultPath);
		 System.out.println("**** Attempting to copy from "+sourcePath.toString()+" to "+targetPath.toString());
		 try {
			 Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
				 @Override
				 public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					 System.out.println("Backing up directory: "+targetPath.resolve(sourcePath.relativize(dir)).toString());
					 Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
					 return FileVisitResult.CONTINUE;
				 }
				 @Override
				 public FileVisitResult visitFile(final Path file,
						 final BasicFileAttributes attrs) throws FileAlreadyExistsException, IOException  {
					 System.out.println("Backing up file: "+targetPath.resolve(sourcePath.relativize(file)).toString());
					 Files.copy(file,targetPath.resolve(sourcePath.relativize(file)));
					 return FileVisitResult.CONTINUE;
				 }
			 });
			 System.out.println("STEP 1 - COMPLETED Successfully");
		 } catch (IOException e) {
			 e.printStackTrace();
			 System.out.println("Step 1 encountered a problem");
		 }
	 }// END folderBackup()

	 public static void archiveCopy2Current()
	 {
		 final Path targetPath = Paths.get(defaultPath +"\\InControl-NG-LocalData\\SAGE3-LocalData\\archives\\");
		 final Path sourcePath = Paths.get(defaultPath +"_"+timeStr+"\\InControl-NG-LocalData\\SAGE3-LocalData\\archives\\");
		 System.out.println("**** Attempting to copy from "+sourcePath.toString()+" to "+targetPath.toString());
		 try {
			 Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
				 @Override
				 public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					 System.out.println("Copying archive directory: "+targetPath.resolve(sourcePath.relativize(dir)).toString());
					 Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
					 return FileVisitResult.CONTINUE;
				 }
				 @Override
				 public FileVisitResult visitFile(final Path file,
						 final BasicFileAttributes attrs) throws FileAlreadyExistsException, IOException  {
					 System.out.println("Copying archive file: "+targetPath.resolve(sourcePath.relativize(file)).toString());
					 Files.copy(file,targetPath.resolve(sourcePath.relativize(file)));
					 return FileVisitResult.CONTINUE;
				 }
			 });
			 System.out.println("Archives copied from backup - COMPLETED Successfully");
		 } catch (IOException e) {
			 e.printStackTrace();
			 System.out.println("Archives were not copied successfully");
		 }
	 }// END archiveCopy2Current()
	 
	 public static void archiveCopy2Portable()
	 {
		 Path archiveRoot = Paths.get("");
		 for(Path p:FileSystems.getDefault().getRootDirectories())
		    {
		    	if(!(p.toString().contains("D"))&&!(p.toString().contains("C")))
		    	{
		    		File folder = new File(p.toString());
		    		File[] listOfFiles = folder.listFiles();
		    		    for (int i = 0; i < listOfFiles.length; i++) 
		    		    {
		    		    	if (listOfFiles[i].isDirectory()) 
			    		    {
		    		    		if(listOfFiles[i].toString().contains("archives"))
		    		    		{
		    		    			archiveRoot = Paths.get(listOfFiles[i].toString());
		    		    		}
			    		    }
		    		    }
		    	}
		    }
		 final Path targetPath = archiveRoot;
		 final Path sourcePath = Paths.get(defaultPath +"_"+timeStr+"\\InControl-NG-LocalData\\SAGE3-LocalData\\archives\\");
		 System.out.println("**** Attempting to copy from "+sourcePath.toString()+" to "+targetPath.toString());
		 try {
			 Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
				 @Override
				 public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					 System.out.println("Copying archive directory: "+targetPath.resolve(sourcePath.relativize(dir)).toString());
					 Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
					 return FileVisitResult.CONTINUE;
				 }
				 @Override
				 public FileVisitResult visitFile(final Path file,
						 final BasicFileAttributes attrs) throws FileAlreadyExistsException, IOException  {
					 System.out.println("Copying archive file: "+targetPath.resolve(sourcePath.relativize(file)).toString());
					 Files.copy(file,targetPath.resolve(sourcePath.relativize(file)));
					 return FileVisitResult.CONTINUE;
				 }
			 });
			 System.out.println("Archives copied to portable - COMPLETED Successfully");
		 } catch (IOException e) {
			 e.printStackTrace();
			 System.out.println("Archives were not copied successfully");
		 }
	 }// END archiveCopy2Portable()
	 
	 public static void reportsCopy2Current()
	 {
		 final Path targetPath = Paths.get(defaultPath +"\\InControl-NG-LocalData\\SAGE3-LocalData\\reports\\");
		 final Path sourcePath = Paths.get(defaultPath +"_"+timeStr+"\\InControl-NG-LocalData\\SAGE3-LocalData\\reports\\");
		 System.out.println("**** Attempting to copy from "+sourcePath.toString()+" to "+targetPath.toString());
		 try {
			 Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
				 @Override
				 public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					 System.out.println("Copying reports directory: "+targetPath.resolve(sourcePath.relativize(dir)).toString());
					 Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
					 return FileVisitResult.CONTINUE;
				 }
				 @Override
				 public FileVisitResult visitFile(final Path file,
						 final BasicFileAttributes attrs) throws FileAlreadyExistsException, IOException  {
					 System.out.println("Copying reports file: "+targetPath.resolve(sourcePath.relativize(file)).toString());
					 Files.copy(file,targetPath.resolve(sourcePath.relativize(file)));
					 return FileVisitResult.CONTINUE;
				 }
			 });
			 System.out.println("Reports copied from backup - COMPLETED Successfully");
		 } catch (IOException e) {
			 e.printStackTrace();
			 System.out.println("Reports were not copied successfully");
		 }
	 }// END reportsCopy2Current()
	 
	 public static void reportsCopy2Portable()
	 {
		 Path reportsRoot = Paths.get("");
		 for(Path p:FileSystems.getDefault().getRootDirectories())
		    {
		    	if(!(p.toString().contains("D"))&&!(p.toString().contains("C")))
		    	{
		    		File folder = new File(p.toString());
		    		File[] listOfFiles = folder.listFiles();
		    		    for (int i = 0; i < listOfFiles.length; i++) 
		    		    {
		    		    	if (listOfFiles[i].isDirectory()) 
			    		    {
		    		    		if(listOfFiles[i].toString().contains("reports"))
		    		    		{
		    		    			reportsRoot = Paths.get(listOfFiles[i].toString());
		    		    		}
			    		    }
		    		    }
		    	}
		    }
		 final Path targetPath = reportsRoot;
		 final Path sourcePath = Paths.get(defaultPath +"_"+timeStr+"\\InControl-NG-LocalData\\SAGE3-LocalData\\archives\\");
		 System.out.println("**** Attempting to copy from "+sourcePath.toString()+" to "+targetPath.toString());
		 try {
			 Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
				 @Override
				 public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					 System.out.println("Copying reports directory: "+targetPath.resolve(sourcePath.relativize(dir)).toString());
					 Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
					 return FileVisitResult.CONTINUE;
				 }
				 @Override
				 public FileVisitResult visitFile(final Path file,
						 final BasicFileAttributes attrs) throws FileAlreadyExistsException, IOException  {
					 System.out.println("Copying reports file: "+targetPath.resolve(sourcePath.relativize(file)).toString());
					 Files.copy(file,targetPath.resolve(sourcePath.relativize(file)));
					 return FileVisitResult.CONTINUE;
				 }
			 });
			 System.out.println("Reports copied to portable - COMPLETED Successfully");
		 } catch (IOException e) {
			 e.printStackTrace();
			 System.out.println("Reports were not copied successfully");
		 }
	 }// END reportsCopy2Portable()
	 
	 public static void deleteCurrent()
	 {
		    System.out.println("STEP 2 - Delete three main InControl-NG folders in InControl: Data, LocalData, and System");
			File dataDir = new File(defaultPath + "\\InControl-NG-Data");
		    File locDataDir = new File(defaultPath + "\\InControl-NG-LocalData");
		    File systemDir = new File(defaultPath + "\\InControl-NG-System");
		    try {
				delete(dataDir);
				delete(locDataDir);
				delete(systemDir);
				System.out.println("STEP 2 - COMPLETED Successfully");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("STEP 2 - Encountered a PROBLEM");
			}
	 }// END deleteCurrent()

	 public static void copyNewSVN()
	 {
		    System.out.println("STEP 3 - Copy new SVN version of three main InControl folders into InControl-NG: Data, LocalData, System");
		    for(Path p:FileSystems.getDefault().getRootDirectories())
		    {
		    	if(!(p.toString().contains("D"))&&!(p.toString().contains("C")))
		    	{
		    		File folder = new File(p.toString());
		    		File[] listOfFiles = folder.listFiles();
		    		    for (int i = 0; i < listOfFiles.length; i++) 
		    		    {
		    		    	if (listOfFiles[i].isDirectory()) 
			    		    {
		    		    		if(listOfFiles[i].toString().contains("SVNv"))
		    		    		{
		    		    			  String currentIndex = listOfFiles[i].toString();
		    		    			  if(svnVer==0)
		    		    			  {
		    		    				  useThisOne = currentIndex;
		    		    				  int startA = 0;
		    		    				  while (!Character.isDigit(useThisOne.charAt(startA))) startA++;
		    		    				  if(useThisOne.contains("-"))
		    		    				  {
		    		    					  svnVer = Integer.parseInt(useThisOne.substring(startA, useThisOne.indexOf('-')));
		    		    					  svnTag = useThisOne.substring(useThisOne.indexOf('-'), useThisOne.length()-1);
		    		    				  }
		    		    				  else
		    		    				  {
		    		    					  svnVer = Integer.parseInt(useThisOne.substring(startA, useThisOne.length()));
		    		    				  }
		    		    			  }
		    		    			  else
		    		    			  {
		    		    				  int svnVerCurrent = 0;
		    		    				  int startA = 0;
		    		    				  while (!Character.isDigit(currentIndex.charAt(startA))) startA++;
		    		    				  if(currentIndex.contains("-"))
		    		    				  {
		    		    					  svnVerCurrent = Integer.parseInt(currentIndex.substring(startA, currentIndex.indexOf('-')));
		    		    				  }
		    		    				  else
		    		    				  {
		    		    					  svnVerCurrent = Integer.parseInt(currentIndex.substring(startA, currentIndex.length()));
		    		    				  }
		    		    				  if(svnVerCurrent>svnVer)
		    		    				  {
		    		    					  useThisOne = currentIndex;
		    		    					  svnVer = svnVerCurrent;
		    		    					  if(useThisOne.contains("-"))
		    		    					  {
		    		    						  svnTag = useThisOne.substring(useThisOne.indexOf('-'), useThisOne.length()-1);
		    		    					  }
		    		    				  }
		    		    			  }
		    		    			  System.out.println("Found " + listOfFiles[i].getName());
		    		    		  }	    		  
			    		      }
		    		   }
		    		   if(!useThisOne.equals(""))
		    		   {
		    		    	System.out.println(useThisOne+" will be used");
		    		    	final Path targetPath2 = Paths.get(defaultPath);
						    final Path sourcePath2 = Paths.get(useThisOne);
						    System.out.println("Attempting to copy from "+sourcePath2.toString()+" to "+targetPath2.toString());
						    try {
								Files.walkFileTree(sourcePath2, new SimpleFileVisitor<Path>() {
								    @Override
								    public FileVisitResult preVisitDirectory(final Path dir,
								            final BasicFileAttributes attrs) throws IOException {
								        System.out.println("Copying directory: "+targetPath2.resolve(sourcePath2.relativize(dir)).toString());
								    	Files.createDirectories(targetPath2.resolve(sourcePath2.relativize(dir)));
								        return FileVisitResult.CONTINUE;
								    }
								    @Override
								    public FileVisitResult visitFile(final Path file,
								            final BasicFileAttributes attrs) throws IOException {
								    	System.out.println("Copying file: "+targetPath2.resolve(sourcePath2.relativize(file)).toString());
								        Files.copy(file,targetPath2.resolve(sourcePath2.relativize(file)));
								        return FileVisitResult.CONTINUE;
								    }
								});
								System.out.println("Step 3 completed successfully");
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println("Step 3 encountered a problem");
							}
		    		   }
		    		   else
		    		   {
		    			   System.out.println(p+" was skipped");
		    		   }
		    	 }  
		    }
	 } // END copyNewSVN()

	 public static void incInstructions()
	 {
		 System.out.println("Automated update of InControl has finished. Control being returned to user.\n");
		 System.out.println(">>>Follow the instructions below to finish InControl Configuration<<<");
		 Scanner keyIn = new Scanner(System.in);
		 System.out.println("For the following, Press \"Enter\" to continue when ready.\n");
		 
		 System.out.println("1. Run ActDaemon.");
		 keyIn.nextLine();
		 
		 System.out.println("2. Run InControl-NG. Login and wait for startup to complete.");
		 keyIn.nextLine();
		 
		 System.out.println("3. Run runFlite. Login and wait for startup to complete.");
		 keyIn.nextLine();
		 
		 System.out.println("4. In the top menu, click \"System\" >> \"Translator\".");
		 keyIn.nextLine();
		 
		 System.out.println("Scope: f_SAGE\nComponent:XTCE\nInput Version: 1.0\nOverride Version: 1.0\nOutput Version: 1.0\nCheck \"Make Shared\" and then \"Translate\"\n--Repeat this for Component:XTCESingle!");
		 keyIn.nextLine();
		 
		 System.out.println("5. In the top menu, click \"System\" >> \"Move Database\".");
		 keyIn.nextLine();
		 
		 System.out.println("Input Domain: SPOC\nInput Scope: f_SAGE\nCheck \"SPOC\"\nOutput Scope: f_SAGE\nCheck \"Make Shared\"\nCheck \"Translated\"\nDB Component: XTCE\nLeave the remaining parameters as their defaults and then \"Move Files\"\n--Repeat this for Component:XTCESingle!");
		 keyIn.nextLine();
		 
		 System.out.println("6. In the top menu, click \"Users\" >> \"Control Token\"");
		 keyIn.nextLine();
		 
		 System.out.println("Enter the admin password and click \"Acquire\".\n--Repeat this for each Mission being run!");
		 keyIn.nextLine();
		 
		 
		 System.out.println("7. In the top menu, click \"Execute\" >> \"Applications\" >> \"Switch Database\" >> Click \"OK\"\n   In the new window, click \"Execute\" >> \"Generate Map Files\" --Wait for the mission to restart.");
		 keyIn.nextLine();

		 System.out.println("InControl Update and Configuration Completed. Do dry runs appropriate for the configuration (e.g. check telemetry and commanding with IAM, Spread System, or IA). Press \"Enter\" to exit.");
		 keyIn.nextLine();
		 keyIn.close();
		 System.exit(0);
	 }

	 public static void modComENV()
	 {
		    BufferedReader br = null;
			try 
			{
				String osDeterminantPath="";
				if(osSwitch==1)
				{
					osDeterminantPath = defaultPath+"\\InControl-NG-System\\SAGE3\\shortcuts.Windows\\common_env";
				}
				else
				{
					osDeterminantPath = defaultPath+"\\InControl-NG-System\\SAGE3\\shortcuts.Linux\\common_env";
				}
				System.out.println(osDeterminantPath);
				br = new BufferedReader(new FileReader(osDeterminantPath));
				BufferedWriter bw = null;
				String line;
				ArrayList <String> strArray = new ArrayList<String>();
				while ((line = br.readLine()) != null)
				{
					strArray.add(line);					
				}
				//updated copyNewSVN to grab the tag if it exists
				if(svnTag.equals("")){
					svnTag = "null";
				}
				strArray.set(5,"set.INC_SVN_IDENTIFIER="+svnTag);
				strArray.set(6, "set.INC_SVN_VERSION="+svnVer);
				bw = new BufferedWriter(new FileWriter(osDeterminantPath));
				for(int i =0; i<strArray.size(); i++)
				{
					//System.out.println(strArray.get(i));
					bw.write(strArray.get(i));
					bw.newLine();	
				}
				br.close();
				bw.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		 
	 }

	 public static void modDirTXT()
	 {
		 	String dirTXT = null;
		 	File folder = new File(defaultPath);
 			File[] listOfFiles = folder.listFiles();
 		    for (int i = 0; i < listOfFiles.length; i++) 
 		    {
 		    	if (listOfFiles[i].isFile()) 
	    		{
 		    		if(listOfFiles[i].toString().contains("_"))
 		    		{
 		    			dirTXT = listOfFiles[i].toString();
 		    		}
 		    			
	    		}
 		    }
 		    System.out.println(dirTXT);
			File dirDIE = new File(dirTXT);
			try {
				delete(dirDIE);
				BufferedWriter bw = null;
				//timeStr = new SimpleDateFormat("yyyyMMdd").format(new Date()); //Borrowed from skipped Folder Backup step
				bw = new BufferedWriter(new FileWriter(defaultPath+"\\"+svnVer+"_"+timeStr));
				bw.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
	 }

	 public static void linux90nprocCheck()
	 {
		String nProc = "/etc/security/limits.d/90-nproc.conf";
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(nProc)));
			String line;
			while ((line = br.readLine()) != null)
			{
				if(line.startsWith("*"))
				{
					if(line.equals("*          soft    nproc     10240")){
						System.out.println("90-nproc.conf was verified to be correct.");
						br.close();
					}
					else
					{
						System.out.println("90-proc.conf failed its automated check!\nExpected: \"*          soft    nproc     10240\"\nActual: \""+line+"\"");
						System.out.println("For security reasons, editing this file must be done manually with root priveleges. Follow the instructions below.");
						System.out.println("Launching terminal...");
						Runtime rt = Runtime.getRuntime(); 	
				    	Process pr = rt.exec("gnome-terminal");
				    	pr.waitFor();
				    	System.out.println("In the terminal window, type \"su-\" and hit enter. *Do not type quotes!");
				    	System.out.println("Enter the root password and hit enter.");
				    	System.out.println("Type \"cd etc/security/limits.d/\" and hit enter");
				    	System.out.println("Type \"vi 90-nproc.conf\" and hit enter");
				    	System.out.println("Press \"A\" on your keyboard to switch to INSERT mode. Use the arrow keys to navigate. Modify nproc to be 10240.");
				    	System.out.println("Press \"esc\" to exit INSERT mode. Hold \"SHIFT\" and type \"ZZ\" to save and exit the file.");
				    	br.close();
				    	linux90nprocCheck();	
					}
				}
			}	
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		 
	 }

	 public static void linuxIsoCheck()
	 {
		String iso = "/etc/sysconfig/i18n";
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(iso)));
			String line;
			line = br.readLine();
			if(line.equals("LANG=\"en_US.iso8859-1\""))
			{
				System.out.println("i18n was verified to be correct.");
				br.close();
			}
			else
			{
				br.close();
				System.out.println("i18n failed its automated check!\nExpected: \"LANG=\"en_US.iso8859-1\\\"\nActual: \""+line+"\"");
				System.out.println("For security reasons, editing this file must be done manually with root priveleges. Follow the instructions below.");
				System.out.println("Launching terminal...");
				Runtime rt = Runtime.getRuntime(); 	
		    	Process pr = rt.exec("gnome-terminal");
		    	pr.waitFor();
		    	System.out.println("In the terminal window, type \"su-\" and hit enter. *Do not type quotes!");
		    	System.out.println("Enter the root password and hit enter.");
		    	System.out.println("Type \"cd /etc/sysconfig/\" and hit enter");
		    	System.out.println("Type \"vi i18n\" and hit enter");
		    	System.out.println("Press \"A\" on your keyboard to switch to INSERT mode. Use the arrow keys to navigate. Modify language to be en_US.iso8859-1.");
		    	System.out.println("Press \"esc\" to exit INSERT mode. Hold \"SHIFT\" and type \"ZZ\" to save and exit the file.");
		    	linuxIsoCheck();	
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}	 
	 }

	 public static void updateDesktopLog()
	 {
		 String osDeterminantPath = "";
		 BufferedReader br = null;
			try 
			{
				if(osSwitch==1)
				{
					osDeterminantPath = System.getProperty("user.home") + "\\Desktop\\"+hostname+"_Log.txt";
				}
				else
				{
					osDeterminantPath = "/home/sage3/desktop/"+hostname+"_Log.txt";
				}
				System.out.println(osDeterminantPath);
				br = new BufferedReader(new FileReader(osDeterminantPath));
				BufferedWriter bw = null;
				String line;
				ArrayList <String> strArray = new ArrayList<String>();
				while ((line = br.readLine()) != null)
				{
					strArray.add(line);					
				}
				//5 is arbitrary, w/e line this is supposed to be, I'm not sure without looking at it.
				strArray.add(5, svnVer+"  "+initials+"  "+timeStr+"   Routine automated update of InControl to SVN version "+svnVer+" "+svnTag);
				bw = new BufferedWriter(new FileWriter(osDeterminantPath));
				for(int i =0; i<strArray.size(); i++)
				{
					//System.out.println(strArray.get(i));
					bw.write(strArray.get(i));
					bw.newLine();	
				}
				br.close();
				bw.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	 
	 }

	 public static void copyIAMCommands()
	 {
		 File com2Dir = new File(defaultPath +"\\InControl-NG-Data\\SAGE3-Data\\scopedData\\Shared\\f_SAGE\\command\\IAM\\");
		 if(!com2Dir.exists()){
			 com2Dir.mkdirs();
		 }
		 timeStr = new SimpleDateFormat("yyyyMMdd").format(new Date());//for testing
		 final Path targetPath = Paths.get(defaultPath +"\\InControl-NG-Data\\SAGE3-Data\\scopedData\\Shared\\f_SAGE\\command\\IAM\\");
		 final Path sourcePath = Paths.get(defaultPath +"_"+timeStr+"\\InControl-NG-Data\\SAGE3-Data\\scopedData\\Shared\\f_SAGE\\command\\IAM\\");
		 System.out.println("**** Attempting to copy from "+sourcePath.toString()+" to "+targetPath.toString());
		 try {
			 Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
				 @Override
				 public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws FileAlreadyExistsException, IOException {
					 System.out.println("Copying Command directory: "+targetPath.resolve(sourcePath.relativize(dir)).toString());
					 Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
					 return FileVisitResult.CONTINUE;
				 }
				 @Override
				 public FileVisitResult visitFile(final Path file,
						 final BasicFileAttributes attrs) throws FileAlreadyExistsException, IOException  {
					 System.out.println("Copying Command file: "+targetPath.resolve(sourcePath.relativize(file)).toString());
					 Files.copy(file,targetPath.resolve(sourcePath.relativize(file)),REPLACE_EXISTING);
					 return FileVisitResult.CONTINUE;
				 }
			 });
			 System.out.println("IAM Commands copied from backup - COMPLETED Successfully");
		 } catch (IOException e) {
			 e.printStackTrace();
			 System.out.println("IAM Commands was not copied successfully");
		 }
	 }// END reportsCopy2Current()

	 public static void getHost()
	 {
		 try {
				hostname = java.net.InetAddress.getLocalHost().getHostName();
				System.out.println("Hostname captured: "+hostname);
			} catch (UnknownHostException e) {
				System.out.println("Hostname could not be determined.");
				if(osSwitch==1)
				{
					System.out.println("Open command prompt and type \"hostname\" and hit enter.");
				}
				if(osSwitch==0)
				{
					System.out.println("Open Terminal and type \"hostname\" and hit enter.");
				}
				System.out.println("Please type the hostname: ");
				hostname = name.next();
				name.close();
				System.out.println("Hostname captured: "+hostname);
			}
		 name.close();
	 }

	 public static void modSystemTopology()
	 {
		String sTopology = defaultPath+"\\InControl-NG-Data\\SAGE3-Data\\unscopedData\\SystemTopology.xml";
	    BufferedReader br = null;
		try 
		{
			br = new BufferedReader(new FileReader(sTopology));
			BufferedWriter bw = null;
			String line;
			ArrayList <String> strArray = new ArrayList<String>();
			while ((line = br.readLine()) != null)
			{
				strArray.add(line);	
			}		
			strArray.set(5,"        <StartHost name = \""+hostname+"\"/>");
			bw = new BufferedWriter(new FileWriter(sTopology));
			for(int i =0; i<strArray.size(); i++)
			{
				bw.write(strArray.get(i));
				bw.newLine();	
			}
			br.close();
			bw.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	 }
}