# Security Test App
This little program allows a user to register, login and retrieve his information

## Build
First, we need to navigate to the project root folder, 
and run the build command as shown below:

Win:
```console
gradlew build
```

Linux/OS
```console
./gradlew build
```

This will generate the next JAR: `/build/libs/AuthTestApp-1.0.jar`

## Run


Go to project build project (or where jar is located) and execute the corresponding command according to your OS
```console
gradle bootRun
```

Terminal
```console
java -jar build/libs/AuthTestApp-1.0.jar
```
## DB
For practical purposes the DB for persistence used in this app is H2 (embedded).
So everytime the app is restarted, all data will be lost.

We have `UserProfileDTO` which should be sent as request body to `/register` endpoint
```console
{
    "username":"uservalue",
    "password":"password",
    "firstname": "toto",
    "lastname" : "dupont",
    "email":"tes@test.com"
}

```
First and last names are optional, constrains have been added to DTO to ensure valid values

## REST / Usage
The workflow would be:
 - Register a user
 - Login with registered user
 - Fetch user details


### /register 
It allows to register a user, with JSON format in request body
```console
curl -H "Content-Type: application/json" -d '{"username":"test_user","password":"password_1","email":"tes@test.com"}' -X POST http://localhost:8080/rest-api/user/register
```

### /profile
Shows the information of current authenticated user
```console
curl -H "Authorization: Bearer jwttoken" -X GET http://localhost:8080/rest-api/user/profile
```

### /update
Allows to update current user data
```console
curl -H "Content-Type: application/json" -H "Authorization: Bearer jwttoken" -d '{"username":"test_user","firstname":"toto", "lastname":"dupont"}' -X PUT http://localhost:8080/rest-api/user/update
```

### /~~login~~
Handle as basic authentication by spring boot.
By default app creates a user:
Username: administrator
Password: password

## JWT token
### /jwt
A JWT token is generated and added to response for reuse at each new call as a way to keep user authenticated after first login

## Swagger
Project implements swagger to allow testing REST calls
`http://localhost:8080/swagger-ui/index.html`


## Docker
Commands for dockerize 
```console
docker build -t authtestapp:1.0 .
docker run -p 8080:8080 --name authtestapp authtestapp:1.0
```
### Resources
- [Spring Doc](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/jdbc.html#servlet-authentication-jdbc)
- [Stack Overflow](https://stackoverflow.com/questions/49325692/how-to-implement-jwt-based-authentication-and-authorization-in-spring-security)
- [Auth0](https://auth0.com/blog/spring-boot-authorization-tutorial-secure-an-api-java/)
