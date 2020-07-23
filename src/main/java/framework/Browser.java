package framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;

import baseinit.Base;
import config.PropertyFileReader;
import cucumber.api.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser {
	public static int chromeDriverProcessID;
	public static int chromeProcessID;
	public static Logger log = Logger.getLogger(Browser.class);
	
	public static WebDriver startBrowser() {
		String browser = Base.reader.getBrowser().toLowerCase();
		log.info("Selected Browser is: "+browser);
		
		switch (browser) {

		case "chrome":
			WebDriverManager.chromedriver().setup();
			
			System.setProperty("webdriver.chrome.silentOutput", "true");

			String downloadFilePath = System.getProperty("user.dir")+"/DownloadedFiles/";

			HashMap<String, Object> chromePref = new HashMap<String, Object>();

			chromePref.put("profile.default_content_settings.popups", 0);

			chromePref.put("download.default_directory", downloadFilePath);

			ChromeOptions options = new ChromeOptions();

			options.setExperimentalOption("prefs", chromePref);
			//options.addArguments("--headless");
			
			
			//Linux Config below two lines
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			
			options.addArguments("--disable-extensions");
			options.addArguments("--disable-infobars");
			//options.setPageLoadStrategy(PageLoadStrategy.NONE);
			//DesiredCapabilities dc = new DesiredCapabilities();
			options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
			//options.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);

			ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
		    int port = chromeDriverService.getUrl().getPort();
			
			try
			{
			Base.driver = new ChromeDriver(chromeDriverService,options);	
			chromeDriverProcessID = GetChromeDriverProcessID(port);
			chromeProcessID=GetChromeProcesID(chromeDriverProcessID);
			//System.out.println("chrome driver ps id: "+chromeDriverProcessID);
			//System.out.println("chrome ps id: "+chromeProcessID);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			log.info("Chrome Browser is Started " + Base.driver.hashCode());
			return Base.driver;

		case "ie":
			WebDriverManager.iedriver().setup();
			Base.driver = new InternetExplorerDriver();
			log.info("Internet Explorer Browser is Started " + Base.driver.hashCode());
			return Base.driver;

		case "opera":
			WebDriverManager.operadriver().setup();
			Base.driver = new OperaDriver();
			log.info("Opera Browser is Started " + Base.driver.hashCode());
			return Base.driver;

		case "htmlunit":
			Base.driver = new HtmlUnitDriver();
			log.info("HtmlUnit Browser is Started " + Base.driver.hashCode());
			return Base.driver;
		case "firefox":
			if(PropertyFileReader.properties.getProperty("OS").toLowerCase().equalsIgnoreCase("windows"))
			{
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/drivers/Windows/Firefox/geckodriver.exe");
			}else if(PropertyFileReader.properties.getProperty("OS").toLowerCase().equalsIgnoreCase("linux"))
			{
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/drivers/Linux/Firefox/geckodriver");
			}
			
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
			FirefoxOptions firefoxoptions = new FirefoxOptions();


			firefoxoptions.addPreference("browser.download.dir", System.getProperty("user.dir")+"/DownloadedFiles/");
			firefoxoptions.addPreference("browser.download.folderList", 2);

			//Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
			firefoxoptions.addPreference("browser.helperApps.neverAsk.saveToDisk","application/pdf;");

			firefoxoptions.addPreference( "browser.download.manager.showWhenStarting", false );
			firefoxoptions.addPreference( "pdfjs.disabled", true );

			//options.addArguments("--headless");
			
			//Linux conifg below 2 lines
			
			firefoxoptions.addArguments("--no-sandbox");
			firefoxoptions.addArguments("--disable-dev-shm-usage");
			firefoxoptions.addArguments("--disable-extensions");
			firefoxoptions.addArguments("--disable-infobars");
			firefoxoptions.setPageLoadStrategy(PageLoadStrategy.NONE);
			//DesiredCapabilities capabilities=DesiredCapabilities.firefox();
			firefoxoptions.setCapability("marionette", true);

			
			Base.driver = new FirefoxDriver(firefoxoptions);
			log.info("Firefox Browser is Started" + Base.driver.hashCode());
			return Base.driver;


		default:
			
			//WebDriverManager.firefoxdriver().setup();
			if(PropertyFileReader.properties.getProperty("OS").toLowerCase().equalsIgnoreCase("windows"))
			{
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/drivers/Windows/Firefox/geckodriver.exe");
			}else if(PropertyFileReader.properties.getProperty("OS").toLowerCase().equalsIgnoreCase("linux"))
			{
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/drivers/Linux/Firefox/geckodriver");
			}
			
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
			FirefoxOptions firefoxoptions1 = new FirefoxOptions();


			firefoxoptions1.addPreference("browser.download.dir", System.getProperty("user.dir")+"/DownloadedFiles/");
			firefoxoptions1.addPreference("browser.download.folderList", 2);

			//Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
			firefoxoptions1.addPreference("browser.helperApps.neverAsk.saveToDisk","application/pdf;");

			firefoxoptions1.addPreference( "browser.download.manager.showWhenStarting", false );
			firefoxoptions1.addPreference( "pdfjs.disabled", true );

			//options.addArguments("--headless");
			
			//Linux conifg below 2 lines
			
			firefoxoptions1.addArguments("--no-sandbox");
			firefoxoptions1.addArguments("--disable-dev-shm-usage");
			firefoxoptions1.addArguments("--disable-extensions");
			firefoxoptions1.addArguments("--disable-infobars");
			firefoxoptions1.setPageLoadStrategy(PageLoadStrategy.NONE);
			//DesiredCapabilities capabilities=DesiredCapabilities.firefox();
			firefoxoptions1.setCapability("marionette", true);

			
			Base.driver = new FirefoxDriver(firefoxoptions1);
			log.info("Firefox Browser is Started" + Base.driver.hashCode());
			return Base.driver;
		}

	}

	public static void maximize() {
		Base.driver.manage().window().maximize();
	}
	
	private static int GetChromeDriverProcessID(int aPort) throws IOException, InterruptedException
	  {
	    String[] commandArray = new String[3];

	    if (SystemUtils.IS_OS_LINUX)
	    {
	      commandArray[0] = "/bin/sh";
	      commandArray[1] = "-c";
	      commandArray[2] = "netstat -anp | grep LISTEN | grep " + aPort;
	    }
	    else if (SystemUtils.IS_OS_WINDOWS)
	    {
	      commandArray[0] = "cmd";
	      commandArray[1] = "/c";
	      commandArray[2] = "netstat -aon | findstr LISTENING | findstr " + aPort;
	    }
	    else
	    {
	      System.out.println("platform not supported");
	      System.exit(-1);
	    }

	   // System.out.println("running command " + commandArray[2]);

	    Process p = Runtime.getRuntime().exec(commandArray);
	    p.waitFor();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

	    StringBuilder sb = new StringBuilder();
	    String line = "";
	    while ((line = reader.readLine()) != null)
	    {
	      sb.append(line + "\n");
	    }

	    String result = sb.toString().trim();

	   // System.out.println("parse command response line:");
	   // System.out.println(result);

	    return SystemUtils.IS_OS_LINUX ? ParseChromeDriverLinux(result) : ParseChromeDriverWindows(result);
	  }
	
	 private static int GetChromeProcesID(int chromeDriverProcessID) throws IOException, InterruptedException
	  {
	    String[] commandArray = new String[3];

	    if (SystemUtils.IS_OS_LINUX)
	    {
	      commandArray[0] = "/bin/sh";
	      commandArray[1] = "-c";
	      commandArray[2] = "ps -efj | grep google-chrome | grep " + chromeDriverProcessID;
	    }
	    else if (SystemUtils.IS_OS_WINDOWS)
	    {
	      commandArray[0] = "cmd";
	      commandArray[1] = "/c";
	      commandArray[2] = "wmic process get processid,parentprocessid,executablepath | find \"chrome.exe\" |find \"" + chromeDriverProcessID + "\"";
	    }
	    else
	    {
	      System.out.println("platform not supported");
	      System.exit(-1);
	    }

	  //  System.out.println("running command " + commandArray[2]);

	    Process p = Runtime.getRuntime().exec(commandArray);
	    p.waitFor();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

	    StringBuilder sb = new StringBuilder();
	    String line = "";
	    while ((line = reader.readLine()) != null)
	    {
	      if (SystemUtils.IS_OS_LINUX && line.contains("/bin/sh"))
	      {
	        continue;
	      }

	      sb.append(line + "\n");
	    }

	    String result = sb.toString().trim();

	    //System.out.println("parse command response line:");
	   // System.out.println(result);

	    return SystemUtils.IS_OS_LINUX ? ParseChromeLinux(result) : ParseChromeWindows(result);
	  }
	
	 private static int ParseChromeLinux(String result)
	  {
	    String[] pieces = result.split("\\s+");
	    
	    return Integer.parseInt(pieces[1]);
	  }

	  private static int ParseChromeWindows(String result)
	  {
	    String[] pieces = result.split("\\s+");
	    
	    return Integer.parseInt(pieces[pieces.length - 1]);
	  }

	  private static int ParseChromeDriverLinux(String netstatResult)
	  {
	    String[] pieces = netstatResult.split("\\s+");
	    String last = pieces[pieces.length - 1];
	   
	    return Integer.parseInt(last.substring(0, last.indexOf('/')));
	  }

	  private static int ParseChromeDriverWindows(String netstatResult)
	  {
	    String[] pieces = netstatResult.split("\\s+");
	    
	    return Integer.parseInt(pieces[pieces.length - 1]);
	  }


	@SuppressWarnings("deprecation")
	public static byte[] takeScreenshot(Scenario scenario) {
		try {
			return ((TakesScreenshot)Base.driver).getScreenshotAs(OutputType.BYTES);
		}
		catch(Exception e){
			log.info("Exception has Occured while taking screenshot");
			//e.printStackTrace();
			return null;
		}

	}
}
