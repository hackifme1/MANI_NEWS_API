FROM registry.cmd.navi-tech.in/common/openjdk:11.0.16

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean;

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml .

# Build the project dependencies
RUN mvn dependency:go-offline -B

# Copy the project source code to the container
COPY src ./src

# Build the application JAR, skipping tests
RUN mvn package -DskipTests

#Set the working port inside the container
EXPOSE 8080
EXPOSE 587
EXPOSE 4003

# Entry point command to run the application when the container starts
CMD ["java", "-jar", "target/newswaveBuild.jar"]