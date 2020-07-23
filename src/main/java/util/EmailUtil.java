package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import config.PropertyFileReader;

public class EmailUtil {
	
	private static String getfileName() 
	{
		File dir = new File(System.getProperty("user.dir")+"/test-output/Spark");
	    File[] files = dir.listFiles();
	    String fileName = null;
	    
	    if (files == null || files.length == 0) 
	    {
	        return null;
	    }

	    File lastModifiedFile = files[0];
	    for (int i = 0; i <files.length; i++) 
	    {
	    	if (lastModifiedFile.lastModified() <= files[i].lastModified()) 
	    	{
	           lastModifiedFile = files[i];
	           fileName = lastModifiedFile.getName();
	           
	    	}
	    }
	    return fileName.toString();
	}
	
	public static String getExecutionDate()
	{
		Date dNow = new Date();
    	SimpleDateFormat ft = new SimpleDateFormat("dd-MMM-yyyy"); 
    	String strTimeOfExecution = ft.format(dNow);
		return strTimeOfExecution;
	}
	
	public static void sendEmailAfterLogs() throws IOException 
	{     
	  try 
      {
		  
		 // ArrayList<String> status = DriverScript.summary(suiteFile);
		  String exeDate = getExecutionDate();
          Email from = new Email(PropertyFileReader.properties.getProperty("FromEmailId"));
          String toEmailList = PropertyFileReader.properties.getProperty("EmailIdsToBeSent");
          String subject = "Automation Test Report for - " + PropertyFileReader.properties.getProperty("Product") + "_" + exeDate + " - " + "Initial Execution";
          Email to = new Email("praveen.rajendran@exelaonline.com");
          String htmlText = "Hi Team,<br><br><br> Please find the <b>Automation Test Result for " +PropertyFileReader.properties.getProperty("Product") + "</b> Summary Details and Screen shot. ";
  	    
  	    Personalization personalization = new Personalization();
  	    personalization.addTo(new Email("praveen.rajendran@exelaonline.com"));
  	    Content content = new Content("text/html", htmlText);
  	    Mail mail = new Mail(from, subject, to, content);
  	    String[] receipt = toEmailList.split(";");
          for(int toemail =0;toemail <= receipt.length-1;toemail++)
          {
        	  mail.personalization.get(toemail).addTo(new Email(receipt[toemail]));
          }
          
          if(PropertyFileReader.properties.getProperty("HtmlReportAttachment").toLowerCase().equalsIgnoreCase("yes"))
          {
          String reportFilename = getfileName(); 
          Path file = Paths.get(System.getProperty("user.dir")+"/test-output/Spark" + reportFilename);
          Attachments attachments = new Attachments();
          attachments.setFilename(file.getFileName().toString());
          attachments.setType("application/html");
          attachments.setDisposition("attachment");

          byte[] attachmentContentBytes = Files.readAllBytes(file);
          String attachmentContent = Base64.getEncoder().encodeToString(attachmentContentBytes);
          attachments.setContent(attachmentContent);
          mail.addAttachments(attachments);
          }
  	    SendGrid sg = new SendGrid(System.getenv(PropertyFileReader.properties.getProperty("APIKey")));
  	    Request request = new Request();
  	    try {
  	      request.setMethod(Method.POST);
  	      request.setEndpoint("mail/send");
  	      request.setBody(mail.build());
  	      Response response = sg.api(request);
  	      System.out.println("Email Sent message successfully....");
  	    }catch (IOException ex) {
  	    
  	    	System.out.println("Problem in sending the Email....");
  	     // throw ex;
  	    }
          
      } catch (IOException mex) 
	  {
    	  System.out.println("SMTP email has not connected....");
      }
   }



}
