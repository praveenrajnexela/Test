Feature: Lynx Sanity Suite

Background:
Given navigate to given URL
Then click on signin link

Scenario: Tomcat service (URL)
Then title of the page should be "Login | DrySign"

Scenario: ExelaAuth service (Login)
Then enter username and password
And click on signin button
And enter OTP "123456"
And click submit button
Then title of the page should be "Dashboard | DrySign"
And logout

Scenario: eFA service (Download)
Then enter username and password
And click on signin button
And enter OTP "123456"
And click submit button
Then click on completed documents overview option
And click on plus icon
Then download the document
And logout
