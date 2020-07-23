 package runner;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;
import io.cucumber.junit.CucumberOptions.SnippetType;

@SuppressWarnings("deprecation")
@RunWith(Cucumber.class)

@io.cucumber.junit.CucumberOptions(
		//features = "FeatureFiles",
		 glue= {"stepDefinitions","baseinit"},
		plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:","json:test-output/cucumber-JSON-report/JSONReport.json","junit:test-output/cucumber-XML-report/XMLReport.xml"},
		 monochrome = true,
		 dryRun = false
		 
		 //,tags = {"@tes"}
 )

public class Runner {
	
	
	}
