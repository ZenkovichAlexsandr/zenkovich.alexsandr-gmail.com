# REST API presenting aggregated information from different services

Rest API, that return details information by `userId`.

### Pre-requisites

Following tools and packages should be installed before run application:

| Tool              | How to install           | Version  |
|-------------------|--------------------------|----------|
| JDK               | http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html | >= 1.8.0_121 |
| Docker            | https://www.docker.com/                                                             | >= 18        |
| Docker Compose    | https://www.docker.com/     

### Run
To run app, go to the folder with project and type in console:
```
./run.sh
```

### Example of call
You can use following curl command to call API:
```
curl http://localhost:8081/account-details/${userid}

```
Where `userId` can be: 
1. `Fellowship%20of%20the%20ring`
2. `Super%20duper%20company`
3. `Super%20duper%20employee`