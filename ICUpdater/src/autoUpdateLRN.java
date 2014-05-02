import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class autoUpdateLRN{
	static String OS = System.getProperty("os.name").toLowerCase();
	static int svnVer= 0;
	static String useThisOne = "";
	static String xmlFilePath = "";
	static fileParser parser = new fileParser();
	static String defaultPath = "C:\\InControl-NG2";	// Path of installation.  Example: C:\\InControl-NG2
	static String computerName = "SAGE3CC5";			// Name of computer.  Example: SAGE3CC5
	public static void main(String[] args){
		//Begin Windows
		if(isWindows()){
			System.out.println("Windows system detected");
			
			/*
			 * folderBackup   -- Make a backup of InControl-NG named InControl-NG_ + timestamp
			 * deleteCurrent  -- Delete three main InControl folders in InControl-NG (Data, LocalData, System)
			 * copyNewSVN     -- Copy new SVN version of three main InControl folders into InControl-NG (Data, LocalData, System)
			 */
			
//			folderBackup();
//			deleteCurrent();
//			copyNewSVN();
			checkDomainAndDomainSecurity();


			
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
			//Step 1
			String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
			final Path targetPath = Paths.get("C:\\InControl-NG"+"-"+time);
				    final Path sourcePath = Paths.get("C:\\-NG2");
				    try {
						Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
						    @Override
						    public FileVisitResult preVisitDirectory(final Path dir,
						            final BasicFileAttributes attrs) throws IOException {
						        Files.createDirectories(targetPath.resolve(sourcePath
						                .relativize(dir)));
						        return FileVisitResult.CONTINUE;
						    }

						    @Override
						    public FileVisitResult visitFile(final Path file,
						            final BasicFileAttributes attrs) throws IOException {
						        Files.copy(file,
						                targetPath.resolve(sourcePath.relativize(file)));
						        return FileVisitResult.CONTINUE;
						    }
						});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		
	} // end main()
	
	public static boolean isWindows() {
		 
		return (OS.indexOf("win") >= 0);
 
	} // end isWindows()
	public static boolean isUnix() {
		 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	} // end isUnix()

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
		 String time = new SimpleDateFormat("yyyyMMdd").format(new Date());
		 System.out.println("STEP 1 - Making a backup of InControl-NG folder named InControl-NG_" + time);

		 final Path targetPath = Paths.get(defaultPath +"_"+time);
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
//			 // TODO Auto-generated catch block
//			 e.printStackTrace();
//			 System.out.println("Step 1 encountered a problem");
		 }
	 }// END folderBackup()

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
				// TODO Auto-generated catch block
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
								// TODO Auto-generated catch block
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


}