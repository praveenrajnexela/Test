package stepDefinitions;

import static org.junit.Assert.assertEquals;

import baseinit.Base;
import config.PropertyFileReader;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import framework.Elements;
import junit.framework.Assert;
import pageObjects.pageObjects;

public class Steps {
	
	@Given("navigate to given URL")
	public void navigate_to_given_URL() {
	    
		Base.driver.navigate().to(PropertyFileReader.properties.getProperty("url"));
	}
	
	@Then("title of the page should be {string}")
	public void titile_of_the_page_should_be(String title) {
	
		Assert.assertEquals(Elements.getTitle(), title);
	}

	@Then("click on signin button")
	public void click_on_signin_button() {
		Elements.click(pageObjects.singninbtn);
	}

	@Then("click on signin link")
	public void click_on_signin_link() {
		pageObjects loginpage = new pageObjects(Base.driver);
	    Elements.click(pageObjects.signup);
	}

	@Then("enter username and password")
	public void enter_username_as_and_password_as() {
		Elements.TypeText(pageObjects.username, PropertyFileReader.properties.getProperty("username"));
	    Elements.TypeText(pageObjects.password, PropertyFileReader.properties.getProperty("Password"));
	}

	@Then("enter OTP {string}")
	public void enter_OTP(String OTP) {
		char[] array = OTP.toCharArray();
		for(int j =0;j<=array.length-1;j++)
		{
			Elements.TypeText(pageObjects.OTP(String.valueOf(j+1)), String.valueOf(array[j]));
		}	
	}

	@Then("click submit button")
	public void click_submit_button() {
		Elements.click(pageObjects.OTP_Submit_btn);
	}

	@Then("logout")
	public void logout() throws InterruptedException {
		Elements.click(pageObjects.DropDown_Toggle);
		Thread.sleep(2000);
		Elements.click(pageObjects.LogOut);
		Thread.sleep(2000);
		Elements.click(pageObjects.Confirm_btn);
	}

	@Then("click on completed documents overview option")
	public void click_on_completed_documents_overview_option() {
		Elements.click(pageObjects.Completed_num);
	}

	@Then("click on plus icon")
	public void click_on_plus_icon() {
		Elements.click(pageObjects.plus_icon);
	}

	@Then("download the document")
	public void download_the_document() {
		Elements.scrollIntoViewAndClick(pageObjects.DownLoad_btn);
	}



}
