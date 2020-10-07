#### Building
 - To build the project simply use maven with a clean build call, or use you preferred IDE
#### Running
 - Simply run the jar file and access http://localhost:8080/login.html
    - You can use the users defined on V2__insertUsers.sql or simply change it to add your own user 
    - You'll have to open two browsers in order to be able to send messages
    - You can access http://localhost:8080/actuator/health to check the status of the application 
    - You can also access http://localhost:8080/actuator/info to get information about the application 
    (i.e. app name, app version and number of connected users) 
#### My testing strategy follows both the classical and mockist approach:
 - For the security config and for the websockets the approach was classical
 - For the services I used a more mockist approach because the idea was to test 
the logic, so it was unnecessary to go to the database for data