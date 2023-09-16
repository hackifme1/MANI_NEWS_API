# MANI_NewsAPI

A Spring Boot based application for delivering top headlines to users according to their preferences.

## Table of Contents

- [Project Structure](#project-structure)
- [Project Setup](#project-setup)

## Project Structure

The project follows a standard Maven project structure:

- `src/`
- `main/`
- `java/`
- `com.navibootcamp.newswave/`
- `controller/` # Controller classes for handling HTTP requests
- `entity/` # Entity classes for database tables or api responses
- `repository/` # Repository interfaces for database access
- `service/` # Service classes for business logic
- `configuration/` # Configuration classes (e.g like cache configuration)
- `utils/` # Utility classes
- `NewsDigestApplication.java` # Main Spring Boot application class
- `resources/` # Application properties and thymeleaf templates
- `test/`
- `java/` # Test source code
- `pom.xml` # Maven configuration file
- `DockerFile`


## Project Setup

1. **Dependencies**: This project uses JDK 11 and Springboot 2.7. Maven dependencies are present in `pom.xml`

2. **Intellij Plugins**: Lombok.

3. **Database**: [Optional. Ignore if you are using remote database credentials] This project uses PostgreSql 13. Ensure credentials match with those in `src/main/resources/application.yml`
```  
# install postgres using brew  
brew install postgres@15  
  
# add postgres binaries to shell path  
echo 'export PATH="/opt/homebrew/opt/postgresql@15/bin:$PATH"' >> ~/.zshrc  
  
# update zsh  
source ~/.zshrc  
  
# create default postgres username and db  
createdb  
  
# login using default command  
psql  
  
# create new database  
CREATE DATABASE newsApi;  
  
# exit psql  
\q  
  
# login using new user  
psql -h localhost -U bootcamp_user -d bootcamp_database  
  
# add the required tables to the database from db  
\it path/to/dbstarter.sql  
```  

**Note:**  
Make sure to have your valid credentials as value in the application.yml file for the followings:-
- apikey_1: ${NEWSAPI_KEY:###########}
- apikey_2: ${NEWSIO_KEY:########}
- username: ${DATASOURCE_USERNAME:######}
- password: ${DATASOURCE_PASSWORD:#####}
- username: ${GMAIL_USERNAME:###########}
- password: ${GMAIL_PASSWORD:########}
- secret: ${JWT_SECRET:#########}
