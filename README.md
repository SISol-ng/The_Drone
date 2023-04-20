# The_Drone

**The_Drone** is a major new technology that is destined to be a disruptive force in the field of
transportation. Just as the mobile phone allowed developing countries to leapfrog older technologies 
for personal communication, the drone has the potential to leapfrog traditional transportation infrastructure.
Built with Java Spring Boot The_Drone functions include the delivery of small items that are (urgently) needed in
locations with difficult access.

## Task description

```bash
A drone is capable of carrying devices, other than cameras, and capable of delivering small loads. 
For our use case **the load is medications**.

# A **Drone** has:
- serial number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

# Each **Medication** has:
- name (allowed only letters, numbers, '-', '_');
- weight;
- code (allowed only upper case letters, underscore and numbers);
- image (picture of the medication case).

# A clients should be able to: 
- Communicate with the drones (i.e. **dispatch controller**). 
*The specific communicaiton with the drone is outside the scope of this task.* 
```

## Requirements

```bash
While implementing your solution **please take care of the following requirements**:

# Functional requirements
- Registering a drone;
- Loading a drone with medication items;
- Checking loaded medication items for a given drone;
- Checking available drones for loading;
- Check drone battery level for a given drone;
- Prevent the drone from being loaded with more weight that it can carry;
- Prevent the drone from being in LOADING state if the battery level is **below 25%**;
- Introduce a periodic task to check drones battery levels and create history/audit event log for this.

#### Non-functional requirements
- Input/output data must be in JSON format;
- Your project must be build-able and runnable;
- Your project must have a README file with build/run/test instructions (use DB that can
be run locally, e.g. in-memory, via container);
- Required data must be preloaded in the database.
- Unit tests are optional but advisable (if you have time);
- Advice: Show us how you work through your commit history.
- Programming Language: (Node.js Typescript optional) or Java
```

## Technologies

Spring boot
Java 17
H2 Database

## Building and Running

The project can by executed locally by executing the `main` method in the `org.assessment.the_drone.TheDroneApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
1. Build the project using
  `mvn clean install`
2. Run using 
  `mvn spring-boot:run`
3. The service is accessible via localhost:8080
```

To run the project in a container, you can do so by:

```shell
1. Add [Configuration](https://docs.docker.com/get-started/08_using_compose/) to dockerize the Spring Boot application.
2. Build the project/Generate a .jar file using
  `mvn clean install`
  The jar file can be found in your local maven repository Example - `C:\Users\Your_Username\.m2\repository\org\assessment\the_drone\0.0.1\the_drone-0.0.1.jar`
3. Move the jar file to the directory containing your Docker file
4. Run the Spring Boot Docker image in a container by:
  `docker-compose up -d --build service_name`
```


## Testing

```Java
## Dispatch Medication

```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#web)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
