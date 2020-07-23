package framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import baseinit.Base;
import config.PropertyFileReader;
import util.PathHelper;
import util.Xls_Reader;

public class GenericActions {
	public static Date firtDatefrmExecl;
	
	public static byte[] filesToByte(String filepath) throws IOException
	{
		File file = new File(filepath);
		//init array with file length
		byte[] bytesArray = new byte[(int) file.length()];

		FileInputStream fis = new FileInputStream(file);
		fis.read(bytesArray); //read file into bytes[]
		fis.close();

		return bytesArray;
	}
	
	public static void issueUpdate(String path) throws IOException
	{
		String JSONString = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    	 JSONString = JSONString.substring(4);
    	 JSONString = JSONString.substring(0, JSONString.length() - 1) + "";
    	JSONObject json = new JSONObject(JSONString);
        JSONArray arr = json.getJSONArray("elements");
        
     for(int j=0;j<arr.length();j++)   
     {
    	 String scenarioname = arr.getJSONObject(j).getString("name");
    	 //System.out.println(scenarioname);
    	 for(int h=0;h<=Base.rowcount;h++)
    	 {
    	 String[] EM = null;
         String stepName = null;
    		 String test = Base.suiteXLS.getCellData("TestSuite", "TestCaseDescription", h);
    	if(test.equalsIgnoreCase(scenarioname))
    	{		
    	 JSONArray d= arr.getJSONObject(j).getJSONArray("steps");
    	 for(int i = 0; i < d.length(); i++){
        	try{
        		JSONObject position = d.getJSONObject(i).getJSONObject("result");
        		String errorMessage =   position.getString("error_message");
        		EM = errorMessage.split(":",2)[1].split("}",2);
        		stepName = d.getJSONObject(i).getString("name");
               // System.out.println(EM[0]+"}");
               }catch(Exception e)
               {
            	   
               }
            
           // System.out.println(stepName);  
        } 
    	 if(EM !=null)
    	 {
    		 String msg =EM[0].replace("\r\n", "").replace("\r", "").replace("\n", "");
    		 msg = msg.replace(",", "");
    	 Base.suiteXLS.setCellData("TestSuite", "Investigations", h, "FailedStep: "+stepName.replace(",", "")+"--->ErrorDescription: " + msg +"}" );
    	 }
    	} 
    }
    }

	}
	
	public static void deleteFiles(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	                f.delete();
	        }
	    }
	}
	
	public static void filesDeletion()
    {
		deleteFiles(new File(System.getProperty("user.dir")+"/"+"DownloadedFiles"));
    	deleteFiles(new File(System.getProperty("user.dir")+"/"+"ScreenShots"));
    	deleteFiles(new File(System.getProperty("user.dir")+"/"+"Uploadfiles/FilesToInput"));
    	deleteFiles(new File(System.getProperty("user.dir")+"/"+"Uploadfiles/FilesToArchive"));
    }
	
	public static void date(Xls_Reader suite,String testCasedate,int rownum)
	{
		Date dNow = new Date();
    	//SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa"); 
    	
    	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
    	String ExeTime = ft.format(dNow);
    	
    	SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
    	String ExeTime1 = ft1.format(dNow);
    	suite.setCellData("TestSuite", testCasedate, rownum, ExeTime);
    	//suite.setCellDataasDate("TestSuite", testCasedate, rownum, ExeTime);
    	//suite.setCellData("RoughSheet", testCasedate, 2, ExeTime1);
	}
		public static void datediff(String testcasestart,String testcaseend,Xls_Reader suite,int rownum) throws ParseException
		{
			DateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
	    	Date startdate=ft.parse(testcasestart);
	    	Date enddate=ft.parse(testcaseend);
			long diff = enddate.getTime() - startdate.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000);
			
			String diffdate = String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
			
			//String diffdate =diffHours+":"+diffMinutes+":"+diffSeconds;
			suite.setCellData("TestSuite", "Timetakentofinish", rownum, diffdate);
		}

		public static void CopyContentOneWorkbookToOther(String inputSheet,String archiveSheet) throws IOException, ParseException 
		{
					  Xls_Reader insuite = new Xls_Reader(inputSheet);
					  
	                  Xls_Reader outsuite = new Xls_Reader(archiveSheet);
	                  
	                  //Xls_Reader archivedatesuite = new Xls_Reader(archiveDateSheet);
	                  
	                  XSSFSheet inputSheetName=insuite.getSheet("TestSuite");
	                  
	                  XSSFSheet outputSheet=outsuite.getSheet("TestSuite");
	                	                                  
	                 copySheet(inputSheetName,outputSheet,insuite,outsuite,archiveSheet); 
	                 util.FileConversionXLSXToCSV.conversionStart(archiveSheet,System.getProperty("user.dir")+"/Uploadfiles/FilesToInput/"+"Selenium_"+PropertyFileReader.properties.getProperty("Product")+"_"+PropertyFileReader.properties.getProperty("Environment")+".csv");
	          }

		
		   public static void copySheet(XSSFSheet inputSheet,XSSFSheet outputSheet,Xls_Reader insuite,Xls_Reader outsuite,String archiveSheet) throws ParseException, IOException 
           { 
			   LocalDateTime dNow =  LocalDateTime.now();
              	
               LocalDateTime dthen = dNow.minusDays(6);
               
               DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd");
               
               DateFormat ft2 = new SimpleDateFormat("yyyy-MM-dd");
               
               Date dthen2 = ft2.parse(ft.format(dthen));
               
               //Date firtDatefrmExec1 = new Date(outsuite.getCellData("TestSuite", "TestStartedTime", 2));
               int outputSheetrowCount=outputSheet.getLastRowNum();
               
               if(outputSheetrowCount!=0)
               {
            	   firtDatefrmExecl = ft2.parse(outsuite.getCellData("TestSuite", "TestStartedTime", 2));
               }
        	   
              int inputSheetrowCount=inputSheet.getLastRowNum();
              
              int inputSheetcoulmnCount=inputSheet.getRow(0).getLastCellNum();
              
              String outputSheetName = outputSheet.getSheetName();
              
              String inputSheetName = inputSheet.getSheetName();
               try 
               {     
                if(inputSheetrowCount>0)
                {
                	if(outputSheetrowCount==0)
                	{
                		for(int i=1;i<=inputSheetrowCount;i++)
                		{
                			for(int celliteration =0;celliteration<=inputSheetcoulmnCount-1;celliteration++)
                			{
                				String colname =insuite.getCellData(inputSheetName, celliteration , 1);
                				
                				String data = insuite.getCellData(inputSheetName, celliteration , i+1);
                				
                				outsuite.setCellData(outputSheetName, colname, i+1, data);
                			}
                		}
                	}else if(firtDatefrmExecl.before(dthen2))
                	{
                	
                	List<String> rowCount = new ArrayList<String>();
                 	
                	for(int j=2;j<=outputSheetrowCount;j++)
                	{
                		 Date ExecutionStartDate = ft2.parse(outsuite.getCellData("TestSuite", "TestStartedTime", 2));
               		
                		if(ExecutionStartDate.before(dthen2))
                		{
                			rowCount.add(String.valueOf(j));
                		}else
                		{
                			break;
                		}
                		
                	}
                	
                    //specify the row index from where to delete
                    int rowIndex =1;
                	//deleting the rows
                    if (rowIndex >= 0 && rowIndex < rowCount.size()) {
                    	
                    	for(int i=2;i<=rowCount.size();i++)
                    	{
                    	outputSheet.shiftRows(rowIndex+1, outputSheetrowCount, -1, true,true);
                    	}
                    }        
                    
                    FileOutputStream outFile = new FileOutputStream(new File(PathHelper.getBasePath()+"/Uploadfiles/ArchiveFileStartDate/"+"Input"+".xlsx"));
                    outsuite.workbook.write(outFile);
                    outFile.close();
                	
                    Xls_Reader changedoutsuite = new Xls_Reader(archiveSheet);
                    
                    int changedoutputSheetrowCount= changedoutsuite.getSheet("TestSuite").getLastRowNum();
                    
                    for(int k = 1;k<=changedoutputSheetrowCount;k++)
                    {
                    	String coln =insuite.getCellData(inputSheetName, 0 , 1);
            			
                    	changedoutsuite.setCellData(outputSheetName,coln , k+1, String.valueOf(k));
                    }
                    
                    int rowcount =2;
            		
            		for(int i=changedoutputSheetrowCount;i<=(changedoutputSheetrowCount+inputSheetrowCount)-1;i++)
            		{
            			String coln =insuite.getCellData(inputSheetName, 0 , 1);
            			
            			changedoutsuite.setCellData(outputSheetName,coln , i+2, String.valueOf(i+1));
            			
            			for(int celliteration =1;celliteration<=inputSheetcoulmnCount-1;celliteration++)
            			{
            				String colname =insuite.getCellData(inputSheetName, celliteration , 1);
            				
            				String data1 = insuite.getCellData(inputSheetName, celliteration , rowcount);
            				
            				changedoutsuite.setCellData(outputSheetName, colname, i+2, data1);
            			}
            			rowcount++;
            		}
                    
                	}else
                	{
                		int rowcount =2;
                		
                		for(int i=outputSheetrowCount;i<=(outputSheetrowCount+inputSheetrowCount)-1;i++)
                		{
                			String coln =insuite.getCellData(inputSheetName, 0 , 1);
                			
                			outsuite.setCellData(outputSheetName,coln , i+2, String.valueOf(i+1));
                			
                			for(int celliteration =1;celliteration<=inputSheetcoulmnCount-1;celliteration++)
                			{
                				String colname =insuite.getCellData(inputSheetName, celliteration , 1);
                				
                				String data = insuite.getCellData(inputSheetName, celliteration , rowcount);
                				
                				outsuite.setCellData(outputSheetName, colname, i+2, data);
                			}
                			rowcount++;
                		}
                		
                	}
                	
                }}catch(Exception e)
               {
                	
               }
	}

    
           public static void OneWorkbookToOtherArchive(String inputSheet,String archiveSheet) throws IOException, ParseException 
   		{
   					  Xls_Reader insuite = new Xls_Reader(inputSheet);
   					  
   	                  Xls_Reader outsuite = new Xls_Reader(archiveSheet);
   	                  
   	                  //Xls_Reader archivedatesuite = new Xls_Reader(archiveDateSheet);
   	                  
   	                  XSSFSheet inputSheetName=insuite.getSheet("TestSuite");
   	                  
   	                  XSSFSheet outputSheet=outsuite.getSheet("TestSuite");
   	                  
   	                                    
   	             copySheetArchive(inputSheetName,outputSheet,insuite,outsuite,archiveSheet);
   	             util.FileConversionXLSXToCSV.conversionStart(archiveSheet,System.getProperty("user.dir")+"/Uploadfiles/FilesToArchive/"+"Selenium_"+PropertyFileReader.properties.getProperty("Product")+"_"+PropertyFileReader.properties.getProperty("Environment")+"_Archive"+".csv");
   	          }

   	           public static void copySheetArchive(XSSFSheet inputSheet,XSSFSheet outputSheet,Xls_Reader insuite,Xls_Reader outsuite,String archiveSheet) throws ParseException, IOException 
   	           { 
   	        	   
   	              int inputSheetrowCount=inputSheet.getLastRowNum();
   	              
   	              int outputSheetrowCount=outputSheet.getLastRowNum();
   	              
   	              int inputSheetcoulmnCount=inputSheet.getRow(0).getLastCellNum();
   	              
   	              String outputSheetName = outputSheet.getSheetName();
   	              
   	              String inputSheetName = inputSheet.getSheetName();
   	                    
   	                if(inputSheetrowCount>0)
   	                {
   	                	if(outputSheetrowCount==0)
   	                	{
   	                		for(int i=1;i<=inputSheetrowCount;i++)
   	                		{
   	                			for(int celliteration =0;celliteration<=inputSheetcoulmnCount-1;celliteration++)
   	                			{
   	                				String colname =insuite.getCellData(inputSheetName, celliteration , 1);
   	                				
   	                				String data = insuite.getCellData(inputSheetName, celliteration , i+1);
   	                				
   	                				outsuite.setCellData(outputSheetName, colname, i+1, data);
   	                			}
   	                		}
   	                	}else
   	                	{
   	                		int rowcount =2;
   	                		
   	                		for(int i=outputSheetrowCount;i<=(outputSheetrowCount+inputSheetrowCount)-1;i++)
   	                		{
   	                			String coln =insuite.getCellData(inputSheetName, 0 , 1);
   	                			
   	                			outsuite.setCellData(outputSheetName,coln , i+2, String.valueOf(i+1));
   	                			
   	                			for(int celliteration =1;celliteration<=inputSheetcoulmnCount-1;celliteration++)
   	                			{
   	                				String colname =insuite.getCellData(inputSheetName, celliteration , 1);
   	                				
   	                				String data = insuite.getCellData(inputSheetName, celliteration , rowcount);
   	                				
   	                				outsuite.setCellData(outputSheetName, colname, i+2, data);
   	                			}
   	                			rowcount++;
   	                		}
   	                		
   	                	}
   	                	
   	                }
   		}   	     

}
