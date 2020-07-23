package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import baseinit.Base;

public class pageObjects {
	
	public pageObjects(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath="//a[text()='Sign In']")
	public static WebElement signup;
	
	@FindBy(xpath="//input[@name='username']")
	public static WebElement username;
	
	@FindBy(xpath="//input[@name='password']")
	public static WebElement password;
	
	@FindBy(xpath="//button[@type='submit']")
	public static WebElement singninbtn; 
	
	public static WebElement OTP(String data)
	{
		String otp = "//input[@name='digit-$']";
		WebElement OTP = Base.driver.findElement(By.xpath(otp.replace("$", data)));
		return OTP;
	}
	
	@FindBy(xpath="//button[@type='button']")
	public static WebElement OTP_Submit_btn;
	
	@FindBy(xpath="(//div[@class='col-xs-5 text-right']/a)[1]")
	public static WebElement Completed_num;
	
	@FindBy(xpath="(//td[@class=' details-control'])[1]")
	public static WebElement plus_icon;
	
	@FindBy(xpath="//button[@type='button' and text()='Download']")
	public static WebElement DownLoad_btn;
	
	@FindBy(xpath="//a[@class='dropdown-toggle']/i")
	public static WebElement DropDown_Toggle;
	
	@FindBy(xpath="//a[text()='Logout']")
	public static WebElement LogOut;
	
	@FindBy(xpath="//button[text()='Confirm']")
	public static WebElement Confirm_btn;
	
	
	
	
}
