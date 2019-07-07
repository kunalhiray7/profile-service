## Profile Service

A simple microservice that provides RESTful APIs to manage the profile of a user.

### Prerequisites
The microservice needs mongodb 3.4+ to be running on `localhost(127.0.0.1:27017)`. It also expects a database with name `coffeewithme`.

#### Step to setup the database (if you have docker an docker-compose)
1. In the current directory `docker-compose up`. This will start mongodb docker container.
2. You will be able to run `mongo` from terminal. Create a database with name `coffeewithme`.  

### How to build?
`./gradlew clean build`

### How to run?
`./gradlew bootrun`

### API Documentation
`http://localhost:8080/swagger-ui.html`