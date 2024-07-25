This app allows to calculate the distribution of work in our team between maintenance work, technical improvements and 
adding business capabilities. We decided to measure this distribution during a workshop in July 2024. The results are 
documented in Confluence: https://confluence.gls-group.eu/display/TH/30.6+Improvement+Metrics

The app fetches the data for a Sprint from Jira and then adds up the time that has been logged for each ticket. The 
category is determined by the Epic Link.  
The Epics are mapped to the categories in the file "epic-links.list". All Epics that are not listed there are 
considered to be business Epics. 

**Instructions**
* start the app via gradle composeApp:run
* enter the name of the Sprint
* enter a valid username for Jira
* enter the password for Jira
* after clicking the button for the calculation, the sum of the logged hours is displayed for each category