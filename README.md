# Mars-Rover
Coding exercise to download images from NASA's API and store them locally. This project uses Java, Spring Boot, JPA/Hibernate, Asynchronous Programming, Maven, JUnit, MySQL, and RESTful Services

## How it Works
This application processes dates provided in a text file. Those dates will be used to call NASA's API to retrieve photo details for that date.

The process works in this order: 

1) Reads in the dates from a text file
2) For each date, calls NASA's API to retrieve image details (photo id and image URL)
3) After image details are retrieved, process each image by using each image's URL to download the image, create local URL, and put into list to be saved
4) After all images for the date, the images are saved to the database

This process is executed automatically after Spring initialization. This process can also be called manually via web service.

## How to Use
There is a scripts folder with a sql script used in this project. I used a MySQL docker container and ran my scripts on there.
There is a folder called webpage that conatins a basic html file that can be used to see images retireve, process dates again, or clear Image table (note: if you change any of your configurations, the host and port will need to be updated in this html file as well).

In order to use this application, you'll need to use my default configurations or change them for the following in application.properties:
1) Database Configurations
2) server.port=8021
3) domain.url=http://localhost (used for creating image urls)
4) nasa.api.key=YOUR_NASA_API_KEY

## Web Service Details
### Process Dates Text File Webservice
URL: {hostname}/marsrover/process/images
Method: GET
Request Body: None
Request Parameters: None
Path Variable: None
Return: Map<String, String>
Description: Calls the process dates text file manually

### Get Dates / Image URL Map Webservice
URL: {hostname}/marsrover/get/map
Method: GET
Request Body: None
Request Parameters: None
Path Variable: None
Return: Map<Date, List<String>>
Description: Returns a map with all image URLs for all dates in database
  
### Get Image Webservice
URL: {hostname}/marsrover/get/image/{photoId}
Method: GET
Request Body: None
Request Parameters: None
Path Variable: photoId
Return: JPEG Image
Description: Returns JPEG image given for given photo id

### Clear Image Table
URL: {hostname}/marsrover/delete/images
Method: GET
Request Body: None
Request Parameters: None
Path Variable: photoId
Return: Map<String, String>
Description: Deletes all records in Image table

## Improvments & Notes
* I created a very simple html file just to demonstrate the ability to retrieve images my from web service.
* With a more complex front end, I could give an id or some indication the user could refer to check if dates have been processed.
* Based on requirements and end-user usage, I would change the way this web service works. Currently, this web service automatically calls the read dates process once, requiring user interaction for following calls. This could be configured to consistantly run every X amount of time in the background.
