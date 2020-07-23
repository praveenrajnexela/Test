package runner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.runner.JUnitCore;

import com.google.common.io.Files;

import baseinit.Base;
import config.PropertyFileReader;
import framework.Browser;
import framework.GenericActions;
import util.EmailUtil;
import util.PathHelper;

public class externalCucumberOptions {

	public static void loadPropertiesFile() throws IOException{
		PropertyConfigurator.configure(PathHelper.getResourcePath("/src/main/resources/ConfigurationFile/log4j.properties"));
		PropertyFileReader prop = new PropertyFileReader();
	    }
	public static String createAndGetCucumberOption(){       
	     StringBuilder sb = new StringBuilder();
	     String featureFilesPath = 
	    PropertyFileReader.properties.getProperty("cucumber.options.feature");
	     Base.log.info(" featureFilesPath: " +featureFilesPath);
	     sb.append(featureFilesPath);
	     return sb.toString();
	    }

	 public static void setOptions(){
	   String value = createAndGetCucumberOption();
	   Base.log.info(" Value: " +value);
	   System.setProperty("cucumber.options", value);
	   }

	public static void main(String[] args) throws IOException, ParseException {
			
			System.out.println("Execution process Started....");
			GenericActions.filesDeletion();
			JUnitCore junitRunner = new JUnitCore();
	        loadPropertiesFile();
	        setOptions();
	        
	        junitRunner.run(runner.Runner.class);
	        
	        GenericActions.issueUpdate(PathHelper.getBasePath()+"/test-output/cucumber-JSON-report/JSONReport.json");
	      //Creating the input file to SFTP 
			GenericActions.CopyContentOneWorkbookToOther(PathHelper.getBasePath()+"/test-output/ExcelReport/ExcelReport.xlsx",PathHelper.getBasePath()+"/Uploadfiles/ArchiveFileStartDate/"+"Input"+".xlsx");
			//Creating the Archive file to SFTP
			GenericActions.OneWorkbookToOtherArchive(PathHelper.getBasePath()+"/test-output/ExcelReport/ExcelReport.xlsx",PathHelper.getBasePath()+"/Uploadfiles/ArchiveFileStartDate/"+"Archive"+".xlsx");
			
			String inputfilename = "Selenium_"+PropertyFileReader.properties.getProperty("Product")+"_"+PropertyFileReader.properties.getProperty("Environment")+".csv";
			
			String outfilename = "Selenium_"+PropertyFileReader.properties.getProperty("Product")+"_"+PropertyFileReader.properties.getProperty("Environment")+"_Archive"+".csv";
			
			 FileUtils.copyFile(new File(System.getProperty("user.dir")+"/Uploadfiles/FilesToInput/"+inputfilename), Paths.get(PropertyFileReader.properties.getProperty("InputFilePathToR14")+inputfilename).toFile());
			 
			 FileUtils.copyFile(new File(System.getProperty("user.dir")+"/Uploadfiles/FilesToArchive/"+outfilename), Paths.get(PropertyFileReader.properties.getProperty("OutPutFilePathToR14")+outfilename).toFile());
			
			//EmailUtil.sendEmailAfterLogs();
			  
			System.out.println("Execution process Completed");

	}

}
