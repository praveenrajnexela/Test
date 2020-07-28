package baseinit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import config.ConfigurationReader;
import config.PropertyFileReader;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;
import framework.Browser;
import framework.GenericActions;
import framework.Waits;
import util.PathHelper;
import util.Xls_Reader;


public class Base {
	
	public static Logger log=Logger.getLogger(Base.class);
	public static String mainWindowHandle;
	public static WebDriver driver;
	public static ConfigurationReader reader;

	public static Xls_Reader suiteXLS = new Xls_Reader(PathHelper.getBasePath()+"/test-output/ExcelReport/ExcelReport.xlsx");
	public static int rowcount = suiteXLS.getRowCount("TestSuite");
	
	@Before
	public void BrowsersetUp(Scenario browser){
		log.info("Scenario Started: "+browser.getName());
		Base.reader=new PropertyFileReader();
		Browser.startBrowser();
		Browser.maximize();
		mainWindowHandle=driver.getWindowHandle();
		for(int i=1;i<=rowcount;i++)
		{
			if(suiteXLS.getCellData("TestSuite", "TestCaseDescription", i).equalsIgnoreCase(browser.getName()))
			{
				GenericActions.date(suiteXLS, "TestStartedTime", i);
				suiteXLS.setCellData("TestSuite", "Investigations", i, "");
				suiteXLS.setCellData("TestSuite", "Product", i, PropertyFileReader.properties.getProperty("Product"));
				suiteXLS.setCellData("TestSuite", "Browser", i, PropertyFileReader.properties.getProperty("browser"));
				suiteXLS.setCellData("TestSuite", "URL", i, PropertyFileReader.properties.getProperty("url").replace("https://", "").replace("http://", ""));
				suiteXLS.setCellData("TestSuite", "Environment", i, PropertyFileReader.properties.getProperty("Environment"));
				GenericActions.date(suiteXLS, "TestStartedTime", i);
				break;
			}
		}
	}
	
	@AfterStep
	public void waitToPageRender(Scenario scenario) throws InterruptedException
	{
		//((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete|loaded|interactive");
		
		for(int i=1;i<=5;i++)
		{
			if(Waits.isPageLoaded())
			{
				break;
			}
			
			Thread.sleep(5000);
		}
		
		Thread.sleep(3000);
		
		if(PropertyFileReader.properties.getProperty("ScreenShotforEachSteps").toLowerCase().equalsIgnoreCase("yes"))
		{
			scenario.embed(Browser.takeScreenshot(scenario), "image/png");
		}
	}
	
	@After
	
	public void closeBrowser(Scenario scenario) throws ParseException{
		
		if(scenario.isFailed()){
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmSSSss");
			LocalDateTime now = LocalDateTime.now();
			String dummyy = dtf.format(now).toString();
			String dummy = dummyy.substring(0, 15);
			scenario.embed(Browser.takeScreenshot(scenario), "image/png");
			/*for(int i=1;i<=rowcount;i++)
			{
				if(suiteXLS.getCellData("TestSuite", "TestCaseDescription", i).equalsIgnoreCase(scenario.getName()))
				{
					suiteXLS.setCellData("TestSuite", "Investigations", i, "Unable to locate the element");
					break;
				}
			}*/
			//Browser.takeScreenshot(scenario);
		}
		
		for(int i=1;i<=rowcount;i++)
		{
			if(suiteXLS.getCellData("TestSuite", "TestCaseDescription", i).equalsIgnoreCase(scenario.getName()))
			{
				suiteXLS.setCellData("TestSuite", "Result", i, String.valueOf(scenario.getStatus()));
				GenericActions.date(suiteXLS, "TestEndedTime", i);
				String startdate= suiteXLS.getCellData("TestSuite", "TestStartedTime", i);
				String Enddate= suiteXLS.getCellData("TestSuite", "TestEndedTime", i);
				GenericActions.datediff(startdate, Enddate, suiteXLS, i);
				break;
			}
		}
		
		log.info("Scenario Completed: "+scenario.getName());
		log.info("Scenario Status is: "+scenario.getStatus());
		Base.driver.quit();
		
		 try
		 {
			 if (SystemUtils.IS_OS_WINDOWS)
			    { 
				 	System.out.println("Chrome Process id: "+Browser.chromeProcessID);
				 	System.out.println("Chrome driver Process id: "+Browser.chromeDriverProcessID);
				 	Runtime.getRuntime().exec("taskkill /F /PID "+Browser.chromeProcessID);
				 	Runtime.getRuntime().exec("taskkill /F /PID "+Browser.chromeDriverProcessID);
				 	
				 	
			    }else if(SystemUtils.IS_OS_LINUX)
			    {
			    	Runtime.getRuntime().exec("sudo kill "+Browser.chromeDriverProcessID);
			    	Runtime.getRuntime().exec("sudo kill "+Browser.chromeProcessID);
			    	
			    }
		 }catch(Exception e)
		 {
			 
		 }
		
		
		//scenario.write("Lanched URL");
		}
	 
	}


